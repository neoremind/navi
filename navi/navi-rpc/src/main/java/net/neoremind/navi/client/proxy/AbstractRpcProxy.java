package net.neoremind.navi.client.proxy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoremind.navi.exception.rpc.InvocationProblemOccurredException;
import net.neoremind.navi.exception.rpc.ParseErrorException;
import net.neoremind.navi.exception.rpc.RpcException;
import net.neoremind.navi.server.vo.RequestDTO;
import net.neoremind.navi.server.vo.ResponseDTO;

/**
 * Rpc proxy base abstract class.
 * 
 * Subclass should extends this class and provide ser/deser overrided method to 
 * make a Rpc dynamic invocation through HTTP channel.
 * 
 * @author Zhang Xu
 */
public abstract class AbstractRpcProxy implements InvocationHandler, Cloneable {
	
	private final static Logger log = LoggerFactory.getLogger(AbstractRpcProxy.class);
	private final static String encoding = "UTF-8";
	
	protected AtomicInteger counter = new AtomicInteger();
	protected String url;
	protected int connectTimeout = -1;
	protected int readTimeout = -1;

	/**
	 * Deserialize binary data to build ReponseDTO
	 * 
	 * @return ResponseDTO
	 * @throws ParseErrorException
	 */
	protected abstract ResponseDTO deserialize(byte[] req) throws ParseErrorException;
	
	/**
	 * When deserialize to responseDTO, if need to parse result to generic type, use this method
	 * 
	 * Note: Usually Json need a <code>fromJson</code> invocation to form generic return type
	 * 
	 * @return ResponseDTO
	 * @throws ParseErrorException
	 */
	//protected abstract ResponseDTO parseGenericeResult(ResponseDTO req) throws ParseErrorException;

	/**
	 * Serialize RequestDTO to binary data
	 * 
	 * @return binary data
	 * @throws ParseErrorException
	 */
	protected abstract byte[] serialize(RequestDTO res) throws ParseErrorException;

	/**
	 * Return HTTP process contentType(), subclass should override this 
	 * 
	 * @return MIME type
	 * @see net.neoremind.navi.server.protocol.NaviProtocol
	 */
	protected abstract String contentType();

	public AbstractRpcProxy(String url) {
		this.url = url;
		this.counter.set(new Random().nextInt());
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		int id = counter.getAndIncrement();
		ResponseDTO response;
		try {
			RequestDTO request = makeRequest(id, method, args);
			byte[] reqBytes = serialize(request);
			log.info("request id=" + id + ", url=" + url + " ,protocol=" + contentType());
			log.debug("request bytes size is " + reqBytes.length);
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			if (connectTimeout > 0) {
				connection.setConnectTimeout(connectTimeout);
			}
			if (readTimeout > 0) {
				connection.setReadTimeout(readTimeout);
			}
			sendRequest(reqBytes, connection);
			byte[] resBytes = null;
			resBytes = readResponse(connection);
			response = deserialize(resBytes);
		} catch (IOException e) {
			throw new RuntimeException("Rpc invocation occurred IO failed - " + e.getMessage(), e);
		} catch (ParseErrorException e) {
			throw new RuntimeException("Rpc invocation ser/deser failed - " + e.getMessage(), e);
		} 
		
		checkResponse(response);
		
		return response.getResult();
	}

	/**
	 * Read response from server
	 * 
	 * @param connection
	 * @return binary data
	 * @throws RpcException
	 */
	protected byte[] readResponse(URLConnection connection) throws RpcException {
		byte[] resBytes;
		InputStream in = null;
		HttpURLConnection httpconnection = (HttpURLConnection) connection;
		try {
			if (httpconnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				in = httpconnection.getInputStream();
			} else {
				if (httpconnection.getContentType().equals(contentType()) && httpconnection.getErrorStream() != null) {
					in = httpconnection.getErrorStream();
				} else {
					in = httpconnection.getInputStream();
				}
			}
			int len = httpconnection.getContentLength();
			if (len <= 0) {
				throw new RuntimeException("No response to get");
			}
			resBytes = new byte[len];
			int offset = 0;
			while (offset < resBytes.length) {
				int bytesRead = in.read(resBytes, offset, resBytes.length - offset);
				if (bytesRead == -1)
					break; // end of stream
				offset += bytesRead;
			}
			if (offset <= 0) {
				throw new RuntimeException("There is no service to " + url);
			}
			log.debug("response bytes size is " + offset);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Connect service instance failed for " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("Rpc invoke occurred IO exception - " + e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("Close read response connection error");
				}
			}
		}
		return resBytes;
	}

	/**
	 * Send request through HTTP to server
	 * 
	 * @param reqBytes
	 * @param connection
	 * @throws IOException
	 */
	protected void sendRequest(byte[] reqBytes, URLConnection connection) {
		HttpURLConnection httpconnection = (HttpURLConnection) connection;
		OutputStream out = null;
		try {
			httpconnection.setRequestMethod("POST");
			httpconnection.setUseCaches(false);
			httpconnection.setDoInput(true);
			httpconnection.setDoOutput(true);
			httpconnection.setRequestProperty("Content-Type", contentType() + ";charset=" + encoding);
			httpconnection.setRequestProperty("Content-Length", "" + reqBytes.length);
			httpconnection.connect();
			out = httpconnection.getOutputStream();
			out.write(reqBytes);
		} catch (Exception e) {
			throw new RuntimeException("Rpc send request failed - " + e.getMessage(), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("Close send request connection error");
				}
			}
		}
	}

	/**
	 * Make request by using rpc framework inner DTO
	 * 
	 * @param method
	 * @param args
	 * @return RequestDTO
	 * @throws ParseErrorException
	 */
	protected RequestDTO makeRequest(int id, Method method, Object[] args) throws ParseErrorException {
		RequestDTO request = new RequestDTO();
		request.setId(id);
		request.setMethod(method.getName());
		request.setParameters(args);
		return request;
	}
	
	protected void checkResponse(ResponseDTO response) throws Exception {
		if (response == null) {
			throw new InvocationProblemOccurredException("Response null");
		}
		
		if (response.getId() != response.getId()) {
			throw new InvocationProblemOccurredException("Id not the same");
		}
		
		if (response.getError() != null) {
			throw new InvocationProblemOccurredException(response.getError().getMessage(), response.getError().getCause());
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

}
