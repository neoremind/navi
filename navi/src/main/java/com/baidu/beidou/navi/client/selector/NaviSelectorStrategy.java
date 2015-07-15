package com.baidu.beidou.navi.client.selector;

/**
 * ClassName: NaviSelectorStrategy <br/>
 * Function: 负载均衡策略枚举
 * 
 * @author zhangxu04
 */
public enum NaviSelectorStrategy {

    /**
     * 随机
     */
    RANDOM(1),

    /**
     * 轮训
     */
    ROUNDROBIN(2),

    /**
     * 自然定义顺序
     */
    NATURALORDER(3);

    private int strategy;

    private NaviSelectorStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getStrategy() {
        return strategy;
    }

}
