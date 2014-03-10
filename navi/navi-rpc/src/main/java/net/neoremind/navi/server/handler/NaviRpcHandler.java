package net.neoremind.navi.server.handler;

import net.neoremind.navi.exception.rpc.RpcException;
import net.neoremind.navi.server.vo.NaviRpcRequest;
import net.neoremind.navi.server.vo.NaviRpcResponse;

/**
 * Rpc handler interface
 * 
 * @author Zhang Xu
 */
public interface NaviRpcHandler {
	
	/**
	 * Process and handle rpc request to return response
	 * 
	 * @param request
	 * @return rpc response
	 * @throws RpcException
	 */
	public NaviRpcResponse service(NaviRpcRequest request) throws RpcException;

}
