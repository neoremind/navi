package net.neoremind.navi.client.selector;

import java.lang.reflect.Method;
import java.util.List;

import net.neoremind.navi.client.invoker.NaviServiceInvoker;
import net.neoremind.navi.exception.InvalidStrategyException;

public class SelectorFactory {

	public static NaviServiceSelector getSelector(NaviSelectorStrategy strategy, List<NaviServiceInvoker> serviceList, int retryTimes, Method method, Object[] args) throws InvalidStrategyException {
		if (strategy.equals(NaviSelectorStrategy.RANDOM)) {
			return new NaviRandomServiceSelector(serviceList, retryTimes, method, args);
		} else if (strategy.equals(NaviSelectorStrategy.ROUNDROBIN)) {
			return new NaviRoundRobinServiceSelector(serviceList, retryTimes, method, args);
		} 
		throw new InvalidStrategyException("RPC selector strategt not supported");
	}

}
