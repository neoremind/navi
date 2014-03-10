package net.neoremind.navi.client.selector;

import net.neoremind.navi.exception.rpc.RpcInvocationException;

/**
 * Rpc execution selector
 * 
 * @author Zhang Xu
 */
public interface NaviServiceSelector {

	/**
	 * Execute Rpc by specific selector(like round robin, rondom, LRU, etc)
	 * 
	 * @param failStrategy Fail handle stragety, like failover, failfast, failsafe, etc
	 * @return Result
	 * @throws RpcInvocationException
	 */
	public Object execute(NaviFailStrategy failStrategy) throws RpcInvocationException;

}
