package com.baidu.beidou.navi.server.filter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.server.callback.Callback;
import com.baidu.beidou.navi.server.processor.NaviRpcProcessor;
import com.baidu.beidou.navi.server.vo.NaviRpcRequest;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;
import com.baidu.beidou.navi.util.CollectionUtil;
import com.baidu.beidou.navi.util.ExtensionLocator;

/**
 * ClassName: FilterBuilder <br/>
 * Function: 调用链构造器 <br/>
 * 过滤器<tt>filter</tt>利用SPI技术从META-INF/services/*中指定的顺序加载初始化
 * 
 * @author Zhang Xu
 */
public class FilterBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(FilterBuilder.class);

    /**
     * 构造调用链
     * 
     * @param processor
     * @return
     */
    public static NaviRpcProcessor buildFilterChain(final NaviRpcProcessor processor) {
        NaviRpcProcessor last = processor;
        List<Filter> filterList = ExtensionLocator.getInstanceList(Filter.class);
        if (CollectionUtil.isNotEmpty(filterList)) {
            for (int i = filterList.size() - 1; i >= 0; i--) {
                final Filter filter = filterList.get(i);
                final NaviRpcProcessor next = last;
                last = new NaviRpcProcessor() {
                    @Override
                    public void service(NaviRpcRequest request, Callback<NaviRpcResponse> callback) {
                        filter.doChain(next, request, new FilterCallback(callback, filter));
                    }
                };
                LOG.info("Place " + filter.getClass().getName() + " filter to core processor");
            }
        }
        return last;
    }

}
