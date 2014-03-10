package net.neoremind.navi.client.invoker;

import net.neoremind.navi.exception.rpc.RpcException;

public interface NaviServiceInvoker {
	
	public Object getInvoker() throws RpcException;
	
}
