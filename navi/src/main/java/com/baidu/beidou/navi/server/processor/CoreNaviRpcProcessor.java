package com.baidu.beidou.navi.server.processor;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.constant.NaviStatus;
import com.baidu.beidou.navi.exception.rpc.AuthAccessDeniedException;
import com.baidu.beidou.navi.exception.rpc.InvalidAccessException;
import com.baidu.beidou.navi.exception.rpc.InvalidParamException;
import com.baidu.beidou.navi.exception.rpc.MethodNotFoundException;
import com.baidu.beidou.navi.exception.rpc.RpcException;
import com.baidu.beidou.navi.exception.rpc.ServerErrorException;
import com.baidu.beidou.navi.server.NaviRpcExporter;
import com.baidu.beidou.navi.server.callback.Callback;
import com.baidu.beidou.navi.server.locator.MethodDescriptor;
import com.baidu.beidou.navi.server.locator.ServiceLocator;
import com.baidu.beidou.navi.server.locator.impl.MethodSignatureKeyServiceLocator;
import com.baidu.beidou.navi.server.vo.AppIdToken;
import com.baidu.beidou.navi.server.vo.NaviRpcRequest;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;
import com.baidu.beidou.navi.server.vo.RequestDTO;
import com.baidu.beidou.navi.server.vo.ResponseDTO;
import com.baidu.beidou.navi.util.CollectionUtil;
import com.baidu.beidou.navi.util.MethodWrapper;
import com.baidu.beidou.navi.util.Preconditions;
import com.baidu.beidou.navi.util.StringUtil;

/**
 * ClassName: CoreNaviRpcProcessor <br/>
 * Function: Navi Rpc核心处理业务逻辑单元
 * 
 * @author Zhang Xu
 */
public class CoreNaviRpcProcessor implements NaviRpcProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(CoreNaviRpcProcessor.class);

    /**
     * 服务描述方法定位器
     */
    private ServiceLocator<String, NaviRpcExporter> serviceLocator = new MethodSignatureKeyServiceLocator();

    /**
     * Creates a new instance of CoreNaviRpcProcessor.
     */
    public CoreNaviRpcProcessor() {

    }

    /**
     * 处理请求并且响应结果
     * 
     * @param request
     *            请求
     * @param callback
     *            结果Callback
     * @see com.baidu.beidou.navi.server.processor.NaviRpcProcessor#service(com.baidu.beidou.navi.server.vo.NaviRpcRequest,
     *      com.baidu.beidou.navi.server.callback.Callback)
     */
    @Override
    public void service(NaviRpcRequest request, Callback<NaviRpcResponse> callback) {
        Object result = null;
        long traceId = 0L;
        try {
            RequestDTO requestDTO = request.getRequestDTO();
            Preconditions.checkNotNull(requestDTO,
                    "Please ensure serialize filter works before the core processor!");

            traceId = requestDTO.getTraceId();

            result = execute(request.getExporter().getServiceInterface(), request.getExporter()
                    .getServiceBean(), requestDTO);

            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Invoke ok. traceId=%d, service=%s, request=[%s(%s)]",
                        requestDTO.getTraceId(), request.getExporter().getName(),
                        requestDTO.getMethod(), Arrays.toString(requestDTO.getParameters())));
            }

            ResponseDTO responseDTO = buildResponse(result, NaviStatus.RPC_OK, null, traceId);
            callback.handleResult(new NaviRpcResponse(responseDTO));
        } catch (RpcException e) {
            LOG.error(e.getMessage(), e);
            ResponseDTO responseDTO = buildResponse(result, NaviStatus.RPC_FAIL, e, traceId);
            callback.handleResult(new NaviRpcResponse(responseDTO));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ResponseDTO responseDTO = buildResponse(result, NaviStatus.SYS_ERROR, e, traceId);
            callback.handleResult(new NaviRpcResponse(responseDTO));
        }
    }

    /**
     * 执行本地方法
     * 
     * @param service
     *            服务接口class
     * @param actor
     *            服务实现bean
     * @param requestDTO
     *            请求对象DTO
     * @return 结果
     */
    private Object execute(Class<?> service, Object actor, RequestDTO requestDTO) {
        return invoke(service, actor, requestDTO.getMethod(), requestDTO.getParamterTypes(),
                requestDTO.getParameters(), requestDTO.getAppId(), requestDTO.getToken());
    }

    /**
     * 通过{@link #serviceLocator}路由到缓存的方法，并且执行
     * 
     * @param objClass
     *            服务接口class
     * @param obj
     *            服务实现bean
     * @param methodName
     *            方法名称
     * @param argTypes
     *            方法参数类型数组
     * @param args
     *            方法参数数组
     * @param appId
     *            appId
     * @param token
     *            token
     * @return 调用结果
     * @throws RpcException
     */
    private Object invoke(Class<?> objClass, Object obj, String methodName, String[] argTypes,
            Object[] args, String appId, String token) throws RpcException {
        Object res;
        try {
            MethodDescriptor<String> desc = serviceLocator
                    .getServiceDescriptor(new MethodWrapper(objClass.getName(), methodName,
                            StringUtil.toString4Array(argTypes)).toString());
            if (desc == null) {
                throw new MethodNotFoundException(String.format(
                        "Service method not found. class=%s, method=%s, argTypes=%s", objClass,
                        methodName, StringUtil.toString4Array(argTypes)));
            }

            if (!isAuthoriedAccess(desc, appId, token)) {
                throw new AuthAccessDeniedException("AppId or token is not valid to access");
            }

            res = desc.getMethod().invoke(desc.getTarget(), args);
        } catch (MethodNotFoundException e1) {
            throw new InvalidParamException(e1);
        } catch (IllegalArgumentException e1) {
            throw new InvalidParamException(e1);
        } catch (IllegalAccessException e1) {
            throw new InvalidAccessException(e1);
        } catch (AuthAccessDeniedException e1) {
            throw new RpcException(e1);
        } catch (InvocationTargetException e1) {
            Throwable e2 = e1.getTargetException();
            if (e2 instanceof RpcException) {
                throw (RpcException) e2;
            } else {
                throw new ServerErrorException(e2);
            }
        }
        if (res == void.class) {
            return null;
        } else {
            return res;
        }
    }

    /**
     * 构造返回对象的DTO
     * 
     * @param result
     *            返回对象结果
     * @param status
     *            返回状态码
     * @param error
     *            错误或者异常信息
     * @param traceId
     *            追踪id
     * @return ResponseDTO
     */
    private ResponseDTO buildResponse(Object result, int status, Exception error, long traceId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTraceId(traceId);
        responseDTO.setStatus(status);
        if (result != null) {
            responseDTO.setResult(result);
        }
        if (error != null) {
            responseDTO.setError(error);
        }
        return responseDTO;
    }

    /**
     * 验证appId和token是否有权限访问方法
     * 
     * @param desc
     *            方法描述
     * @param appId
     *            appId
     * @param token
     *            token
     * @return 是否有权限访问
     */
    private boolean isAuthoriedAccess(MethodDescriptor<String> desc, String appId, String token) {
        if (CollectionUtil.isNotEmpty(desc.getAppIdTokens())) {
            for (AppIdToken appIdToken : desc.getAppIdTokens()) {
                if (AppIdToken.isValid(appIdToken, appId, token)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
