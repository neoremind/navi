package com.baidu.beidou.navi.server.filter;

import com.baidu.beidou.navi.server.callback.Callback;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;

/**
 * Filter callback that works with filter chain. </br> 
 * 
 * Every time result is properly handled by server, a cascade callback will
 * be invoked to the filter callback too.
 * 
 * @author Zhang Xu
 */
public class FilterCallback implements Callback<NaviRpcResponse> {

	/**
	 * Usually refer to the next filter, or at the end refer to 
	 * the callback which constructed by user 
	 */
	private final Callback<NaviRpcResponse> callback;

	/**
	 * Refer to the filter
	 */
	private final Filter filter;

	public FilterCallback(Callback<NaviRpcResponse> callback, Filter filter) {
		this.callback = callback;
		this.filter = filter;
	}

	public void handleResult(NaviRpcResponse response) {
		// first handle filter callback
		filter.doOnCallbackHandleResultTrigger(response);
		
		// then process the next filter callback
		if (callback != null) {
			callback.handleResult(response);
		}
	}

}
