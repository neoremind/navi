package com.baidu.beidou.navi.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.client.attachment.Attachment;
import com.baidu.beidou.navi.client.attachment.AttachmentHandler;
import com.baidu.beidou.navi.client.attachment.AuthAttachmentHandler;
import com.baidu.beidou.navi.client.constant.NaviRpcClientConstant;
import com.baidu.beidou.navi.exception.rpc.CodecException;
import com.baidu.beidou.navi.exception.rpc.RpcException;
import com.baidu.beidou.navi.protocol.NaviProtocol;
import com.baidu.beidou.navi.protocol.SerializeHandler;
import com.baidu.beidou.navi.server.vo.RequestDTO;
import com.baidu.beidou.navi.server.vo.ResponseDTO;
import com.baidu.beidou.navi.util.ByteUtil;
import com.baidu.beidou.navi.util.IdGenerator;
import com.baidu.beidou.navi.util.MethodUtil;
import com.baidu.beidou.navi.util.StringUtil;

/**
 * ClassName: SimpleNaviRpcClient <br/>
 * Function: 简单客户端client
 * <p/>
 * 使用{@link HttpURLConnection}来进行服务端与客户端端的通信，采用 blocking IO方式，半双工通信方式，即一问一答，流程如下图所示：
 * 
 * <pre>
 *          1.request ------------------------->
 * client --------single TCP connection-------- server
 *          <-------------------------2.response
 * </pre>
 * 
 * TCP connection底层的使用HTTP Persistent Connections，可以参考oracle官方<a
 * href="http://docs.oracle.com/javase/6/docs/technotes/guides/net/http-keepalive.html">相关章节</a>
 * 
 * @author zhangxu04
 */
public class SimpleNaviRpcClient implements NaviRpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleNaviRpcClient.class);

    /**
     * 远程服务端地址
     */
    private String url;

    /**
     * 连接超时，单位毫秒
     */
    private int connectTimeout = NaviRpcClientConstant.DEFAULT_CLIENT_CONN_TIMEOUT;

    /**
     * 读超时，单位毫秒
     */
    private int readTimeout = NaviRpcClientConstant.DEFAULT_CLIENT_READ_TIMEOUT;

    /**
     * 序列化反序列化编码处理handler
     */
    private SerializeHandler serializeHandler;

    /**
     * 附加属性信息处理器
     */
    private AttachmentHandler attachmentHandler;

    /**
     * Creates a new instance of SimpleNaviRpcClient.
     * 
     * @param url
     * @param serializeHandler
     */
    public SimpleNaviRpcClient(String url, SerializeHandler serializeHandler) {
        this(url, NaviRpcClientConstant.DEFAULT_CLIENT_CONN_TIMEOUT,
                NaviRpcClientConstant.DEFAULT_CLIENT_READ_TIMEOUT, serializeHandler);
    }

    /**
     * Creates a new instance of SimpleNaviRpcClient.
     * 
     * @param url
     * @param connectTimeout
     * @param readTimeout
     * @param serializeHandler
     */
    public SimpleNaviRpcClient(String url, int connectTimeout, int readTimeout,
            SerializeHandler serializeHandler) {
        this.url = url;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.serializeHandler = serializeHandler;
        attachmentHandler = new AuthAttachmentHandler();
    }

    /**
     * 初始化HTTP Persistent Connections参数
     */
    static {
        System.setProperty("http.keepAlive", NaviRpcClientConstant.DEFAULT_HTTP_KEEPALIVE);
        System.setProperty("http.maxConnections", NaviRpcClientConstant.DEFAULT_MAX_CONNECTIONS);
    }

    /**
     * @see com.baidu.beidou.navi.client.NaviRpcClient#transport(java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object transport(Method method, Object[] args) throws Throwable {
        return transport(method.getName(), args, method.getParameterTypes(),
                method.getGenericReturnType(), null);
    }

    /**
     * NOTE:该方法默认不传参数类型，以及返回泛型，对于Json调用有影响不能成功。
     * 
     * @see com.baidu.beidou.navi.client.NaviRpcClient#transport(java.lang.String, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T transport(String methodName, Object[] args) throws Throwable {
        if (NaviProtocol.JSON.equals(serializeHandler.getProtocol())) {
            throw new CodecException(
                    "Json protocol not supported through transport(java.lang.String, java.lang.Object[]) method");
        }
        return (T) transport(methodName, args, null, null, null);
    }

    /**
     * @see com.baidu.beidou.navi.client.NaviRpcClient#transport(java.lang.reflect.Method, java.lang.Object[],
     *      com.baidu.beidou.navi.client.attachment.Attachment)
     */
    @Override
    public Object transport(Method method, Object[] args, Attachment attachment) throws Throwable {
        return transport(method.getName(), args, method.getParameterTypes(),
                method.getGenericReturnType(), attachment);
    }

    /**
     * NOTE:该方法默认不传参数类型，以及返回泛型，对于Json调用有影响不能成功。
     * 
     * @see com.baidu.beidou.navi.client.NaviRpcClient#transport(java.lang.String, java.lang.Object[],
     *      com.baidu.beidou.navi.client.attachment.Attachment)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T transport(String methodName, Object[] args, Attachment attachment)
            throws Throwable {
        if (NaviProtocol.JSON.equals(serializeHandler.getProtocol())) {
            throw new CodecException(
                    "Json protocol not supported through transport(java.lang.String, java.lang.Object[]) method");
        }
        return (T) transport(methodName, args, null, null, attachment);
    }

    /**
     * 调用通信
     * 
     * @param method
     *            方法
     * @param args
     *            参数
     * @param parameterTypes
     *            参数类型
     * @param genericReturnType
     *            泛型返回结果类型
     * @param attachment附加信息
     * @return 结果对象
     * @throws Throwable
     *             异常信息
     */
    public Object transport(String methodName, Object[] args, Class<?>[] parameterTypes,
            Type genericReturnType, Attachment attachment) throws Throwable {
        ResponseDTO response;
        long start = System.currentTimeMillis();
        try {
            RequestDTO request = makeRequestDTO(methodName, args, parameterTypes);
            if (attachment != null) {
                attachmentHandler.handle(request, attachment);
            }
            byte[] reqBytes = serializeHandler.serialize(request, RequestDTO.class);
            preLog(request, reqBytes);
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            sendRequest(reqBytes, connection);
            byte[] resBytes = readResponse(connection);
            response = serializeHandler.deserialize(resBytes, ResponseDTO.class, null,
                    genericReturnType);
            postLog(request, reqBytes, resBytes, start);
        } catch (IOException e) {
            throw new RpcException("Rpc transport has IO problems - " + e.getMessage(), e);
        } catch (CodecException e) {
            throw new RpcException("Rpc transport has serialization problems - " + e.getMessage(),
                    e);
        } catch (RpcException e) {
            throw e;
        }
        checkResponse(response);
        return response.getResult();
    }

    /**
     * 发送请求字节码
     * 
     * @param reqBytes
     * @param connection
     * @throws IOException
     */
    private void sendRequest(byte[] reqBytes, URLConnection connection) {
        HttpURLConnection httpconnection = (HttpURLConnection) connection;
        OutputStream out = null;
        try {
            if (connectTimeout > 0) {
                httpconnection.setConnectTimeout(connectTimeout);
            }
            if (readTimeout > 0) {
                httpconnection.setReadTimeout(readTimeout);
            }
            httpconnection.setRequestMethod("POST");
            httpconnection.setUseCaches(false);
            httpconnection.setDoInput(true);
            httpconnection.setDoOutput(true);
            httpconnection.setRequestProperty("Content-Type", serializeHandler.getProtocol()
                    .getName() + ";charset=" + NaviRpcClientConstant.ENDODING);
            httpconnection.setRequestProperty("Content-Length", StringUtil.EMPTY + reqBytes.length);
            httpconnection.connect();
            out = httpconnection.getOutputStream();
            out.write(reqBytes);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Send " + reqBytes.length + " bytes done");
            }
        } catch (Exception e) {
            throw new RpcException("Rpc send request failed - " + e.getMessage() + " on "
                    + connection.getURL(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.error("Close send request connection error");
                }
            }
        }
    }

    /**
     * 读服务端返回数据
     * 
     * @param connection
     * @return 字节码
     * @throws RpcException
     */
    private byte[] readResponse(URLConnection connection) throws RpcException {
        byte[] resBytes;
        InputStream in = null;
        HttpURLConnection httpconnection = (HttpURLConnection) connection;
        try {
            if (httpconnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = httpconnection.getInputStream();
            } else {
                if (httpconnection.getContentType()
                        .equals(serializeHandler.getProtocol().getName())
                        && httpconnection.getErrorStream() != null) {
                    in = httpconnection.getErrorStream();
                } else {
                    in = httpconnection.getInputStream();
                }
            }
            int len = httpconnection.getContentLength();
            if (len <= 0) {
                throw new RpcException("Response data length should not be zero");
            }
            resBytes = ByteUtil.readStream(in, len);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Read " + resBytes.length + " bytes response done");
            }
        } catch (IOException e) {
            throw new RpcException("Rpc read response from server has IO problems - "
                    + e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error("Close read response connection error");
                }
            }
        }
        return resBytes;
    }

    /**
     * 通过反射出来的本地方法以及参数构造<tt>RequestDTO</tt>
     * 
     * @param methodName
     * @param args
     * @param prarameterTypes
     * @return RequestDTO
     */
    private RequestDTO makeRequestDTO(String methodName, Object[] args, Class<?>[] prarameterTypes) {
        RequestDTO request = new RequestDTO();
        request.setTraceId(IdGenerator.genUUID());
        request.setMethod(methodName);
        request.setParamterTypes(MethodUtil.getArgsTypeNameArray(prarameterTypes));
        request.setParameters(args);
        return request;
    }

    /**
     * 执行前打印日志
     * 
     * @param request
     * @param reqBytes
     */
    private void preLog(RequestDTO request, byte[] reqBytes) {
        LOG.info(String.format("Navi rpc call start - %d\t%s\t%s\t%s\t%d", request.getTraceId(),
                url, request.getMethod(), serializeHandler.getProtocol().getName(),
                reqBytes == null ? 0 : reqBytes.length));
    }

    /**
     * 执行后打印日志
     * 
     * @param request
     * @param reqBytes
     * @param resBytes
     * @param start
     */
    private void postLog(RequestDTO request, byte[] reqBytes, byte[] resBytes, long start) {
        LOG.info(String.format("Navi rpc call end - %d\t%s\t%s\t%s\t%d\t%d\t%d", request
                .getTraceId(), url, request.getMethod(), serializeHandler.getProtocol().getName(),
                reqBytes == null ? 0 : reqBytes.length, resBytes == null ? 0 : resBytes.length,
                (System.currentTimeMillis() - start)));
    }

    /**
     * 检查reponse是否有异常情况，有则抛出异常
     * 
     * @param response
     * @throws Throwable
     */
    private void checkResponse(ResponseDTO response) throws Throwable {
        if (response == null) {
            throw new RpcException("Response should not be null");
        }

        if (response.getError() != null) {
            throw response.getError().getCause();
        }
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SerializeHandler getSerializeHandler() {
        return serializeHandler;
    }

    public void setSerializeHandler(SerializeHandler serializeHandler) {
        this.serializeHandler = serializeHandler;
    }

    @Override
    public String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("[url:");
        sb.append(url);
        sb.append(", connTimeout:");
        sb.append(connectTimeout);
        sb.append(", readTimeout:");
        sb.append(readTimeout);
        sb.append(", protocol:");
        sb.append(serializeHandler.getProtocol().getName());
        sb.append("]");
        return sb.toString();
    }

}
