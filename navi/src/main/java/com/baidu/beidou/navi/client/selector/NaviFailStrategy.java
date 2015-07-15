package com.baidu.beidou.navi.client.selector;

/**
 * ClassName: NaviFailStrategy <br/>
 * Function: 错误信息枚举
 * 
 * @author zhangxu04
 */
public enum NaviFailStrategy {

    /**
     * 失败重试
     */
    FAILOVER(1),

    /**
     * 失败直接退出
     */
    FAILFAST(2);

    private int strategy;

    private NaviFailStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getStrategy() {
        return strategy;
    }

}
