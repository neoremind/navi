package com.baidu.beidou.navi.client.ha;

import com.baidu.beidou.navi.client.selector.NaviFailStrategy;
import com.baidu.beidou.navi.client.selector.NaviSelectorStrategy;
import com.baidu.beidou.navi.conf.RpcClientConf;

/**
 * ClassName: LbFactory <br/>
 * Function: 负载均衡器工厂
 * 
 * @author Zhang Xu
 */
public class LbFactory {

    /**
     * 构造负载均衡封装
     * 
     * @param selectorStg
     * @param failStg
     * @return
     */
    public static LoadBalanceStrategy build(NaviSelectorStrategy selectorStg,
            NaviFailStrategy failStg) {
        if (NaviSelectorStrategy.RANDOM.equals(selectorStg)) {
            return new RandomLoadBalanceStrategy(howToDealFailure(failStg));
        } else if (NaviSelectorStrategy.ROUNDROBIN.equals(selectorStg)) {
            return new RRLoadBalanceStrategy(howToDealFailure(failStg));
        } else if (NaviSelectorStrategy.NATURALORDER.equals(selectorStg)) {
            return new NatureOrderLoadBalanceStrategy(howToDealFailure(failStg));
        }
        return new RandomLoadBalanceStrategy(howToDealFailure(failStg));
    }

    /**
     * 构造容错处理策略
     * 
     * @param failStg
     * @return
     */
    private static FailStrategy howToDealFailure(NaviFailStrategy failStg) {
        if (NaviFailStrategy.FAILOVER.equals(failStg)) {
            return new FailOverStrategy(RpcClientConf.RPC_RETRY_TIMES);
        } else if (NaviFailStrategy.FAILFAST.equals(failStg)) {
            return new FailFastStrategy();
        }
        return new FailOverStrategy();
    }

}
