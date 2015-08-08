package com.baidu.beidou.navimgr.zoo.constant;

/**
 * ClassName: ZooConstant <br/>
 * Function: zookeeper相关常量
 * 
 * @author Zhang Xu
 */
public class ZooConstant {

    /**
     * zookeeper环境常量
     * <ul>
     * <li>online:线上生产环境</li>
     * <li>offline:开发测试环境</li>
     * </ul>
     */
    public static enum ZooEnv {
        /**
         * 线上生产环境
         */
        ONLINE("online"),

        /**
         * 开发测试环境
         */
        OFFLINE("offline");

        private ZooEnv(final String value) {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
