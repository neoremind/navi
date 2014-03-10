package net.neoremind.navi.client.selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoremind.navi.client.invoker.NaviServiceInvoker;
import net.neoremind.navi.exception.rpc.RpcInvocationException;

/**
 * Random selector
 * 
 * @author Zhang Xu
 */
public class NaviRandomServiceSelector implements NaviServiceSelector {

	private final static Logger log = LoggerFactory.getLogger(NaviRandomServiceSelector.class);

	private List<NaviServiceInvoker> random = null;
	private int retryTime = 0;

	private Method method;
	private Object[] args;

	private Random randomer = new Random();

	public NaviRandomServiceSelector(List<NaviServiceInvoker> serviceList, int retryTimes, Method method, Object[] args) {
		if (CollectionUtils.isEmpty(serviceList)) {
			throw new IllegalArgumentException("ServiceList should not be empty!");
		}
		random = serviceList;
		if (retryTimes < 1) {
			this.retryTime = 1;
		} else {
			this.retryTime = retryTimes;
		}
		this.method = method;
		this.args = args;
	}

	public Object execute(NaviFailStrategy failStrategy) throws RpcInvocationException {
		if (random == null) {
			return null;
		}
		for (int retry = 0; retry < retryTime && retry < random.size();) {
			int index = randomer.nextInt(random.size());
			NaviServiceInvoker service = random.get(index);
			try {
				return method.invoke(service.getInvoker(), args);
			} catch (InvocationTargetException e) {
				// Exception usually throws throwable here when using JDK dynamic proxy invocation 
				printErrMessage(e);
				if (failStrategy == NaviFailStrategy.FAILFAST || (retry + 1 == retryTime || retry + 1 == random.size())) {
					throw new RpcInvocationException(e);
				}
				continue;
			} catch (Exception e) {
				printErrMessage(e);
				throw new RpcInvocationException(e);
			} finally {
				retry++;
			}
		}
		return null;
	}

	/**
	 * Print error message.
	 * 
	 * "o"=origin, "d"=direct, "e"=exception, "m"=message
	 * 
	 * @param e
	 */
	private void printErrMessage(Throwable e) {
		if (e == null)
			return;
		Throwable baseCause = e;
		while (baseCause.getCause() != null) {
			baseCause = baseCause.getCause();
		}
		String errMsg = "RPC invoke error,Target=[" + method.getDeclaringClass().getCanonicalName() + "#" + method.getName() + "()], oe=[" + baseCause.getClass().getCanonicalName() + "], om=[" + baseCause.getMessage() + "], de=[" + e.getClass().getCanonicalName() + "], dm=[" + e.getMessage() + "]";
		log.error(errMsg, e.getCause());
	}

}
