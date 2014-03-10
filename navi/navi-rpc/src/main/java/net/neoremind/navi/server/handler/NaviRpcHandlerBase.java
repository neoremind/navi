package net.neoremind.navi.server.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoremind.navi.constant.NaviStatus;
import net.neoremind.navi.exception.rpc.DeserilizeNullException;
import net.neoremind.navi.exception.rpc.InvalidAccessException;
import net.neoremind.navi.exception.rpc.InvalidParamException;
import net.neoremind.navi.exception.rpc.MethodNotFoundException;
import net.neoremind.navi.exception.rpc.ParseErrorException;
import net.neoremind.navi.exception.rpc.RpcException;
import net.neoremind.navi.exception.rpc.ServerErrorException;
import net.neoremind.navi.server.vo.NaviRpcRequest;
import net.neoremind.navi.server.vo.NaviRpcResponse;
import net.neoremind.navi.server.vo.RequestDTO;
import net.neoremind.navi.server.vo.ResponseDTO;

/**
 * Rpc handler base parent class
 * 
 * @author Zhang Xu
 */
public abstract class NaviRpcHandlerBase implements NaviRpcHandler {

	private final static Logger log = LoggerFactory.getLogger(NaviRpcHandlerBase.class);

	/**
	 * Deserialize binary data from request to contruct RequestDTO <br>
	 * Deserialization protocol determined how to override the method
	 * 
	 * @param req
	 * @return RequestDTO
	 * @throws ParseErrorException
	 */
	protected abstract RequestDTO deserialize(byte[] req) throws ParseErrorException;

	/**
	 * Serialize ResponseDTO to build binary data <br>
	 * Serialization protocol determined how to override the method
	 * 
	 * @param req
	 * @return RequestDTO
	 * @throws ParseErrorException
	 */
	protected abstract byte[] serialize(ResponseDTO res) throws ParseErrorException;

	/**
	 * Process and handle rpc request to return response
	 * 
	 * @param request
	 * @return rpc response
	 * @throws RpcException
	 */
	public NaviRpcResponse service(NaviRpcRequest request) throws RpcException {
		NaviRpcResponse response = new NaviRpcResponse();
		Object result = null;
		long id = 0L;
		try {
			RequestDTO requestDTO = deserialize(request.getRequest());
			log.debug("Deserialize request successfully");
			if (requestDTO == null) {
				throw new DeserilizeNullException();
			}
			
			id = requestDTO.getId();
			
			if (log.isDebugEnabled()) {
				StringBuffer logInfo = new StringBuffer();
				logInfo.append("Req id=");
				logInfo.append(id);
				logInfo.append(", Req method=");
				logInfo.append(requestDTO.getMethod());
				logInfo.append(", Req param=");
				logInfo.append(Arrays.toString(requestDTO.getParameters()));
				log.debug(logInfo.toString());
			}
			
			result = execute(request.getService(), request.getActor(), requestDTO);
			log.debug("Invocatoin successfully for req id=" + id + " , service=" + request.getService().getSimpleName() + ", method=" + requestDTO.getMethod());
			
			response.setResponse(serialize(makeResponse(result, NaviStatus.RPC_OK, null, id)));
			//log.debug("Serialize response successfully for req id=" + id);
			
		} catch (RpcException e) {
			log.error(e.getMessage(), e);
			response.setResponse(serialize(makeResponse(result, NaviStatus.RPC_FAIL, e, id)));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponse(serialize(makeResponse(result, NaviStatus.SYS_ERROR, e, id)));
		}
		return response;
	}

	protected Object execute(Class<?> service, Object actor, RequestDTO requestDTO) {
		return invoke(service, actor, requestDTO.getMethod(), requestDTO.getParameters());
	}

	/**
	 * 
	 * JDK Dynamic Proxy Invocation
	 * 
	 * @param objClass Service Interface
	 * @param obj Service Implementation
	 * @param methodname Method Name
	 * @param args Arguments
	 * @return Execution result
	 * @throws RpcException
	 */
	protected Object invoke(Class<?> objClass, Object obj, String methodname, Object[] args) throws RpcException {
		for (Method ms : objClass.getMethods()) {
			if (ms.getName().equals(methodname)) {
				Type[] argtype = ms.getGenericParameterTypes();
				if (args != null && argtype.length != args.length) {
					throw new InvalidParamException("Expected argument number is " + args.length + ", but actual number is " + argtype.length);
				}
				Object res;
				try {
					res = ms.invoke(obj, args);
				} catch (IllegalArgumentException e1) {
					throw new InvalidParamException(e1);
				} catch (IllegalAccessException e1) {
					throw new InvalidAccessException(e1);
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
		}
		throw new MethodNotFoundException();
	}

	/**
	 * Build ResponseDTO
	 * 
	 * @param result
	 * @param status
	 * @param error
	 * @param id request id
	 * @return ResponseDTO
	 */
	protected ResponseDTO makeResponse(Object result, int status, Exception error, long id) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setId(id);
		responseDTO.setStatus(status);
		if (result != null) {
			responseDTO.setResult(result);
		}
		if (error != null) {
			responseDTO.setError(error);
		}
		return responseDTO;
	}

}
