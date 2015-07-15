package com.baidu.beidou.navi.util;

/**
 * ClassName: SystemPropertiesUtils <br/>
 * Function: 系统properties工具类
 * 
 * @author Zhang Xu
 */
public class SystemPropertiesUtils {

    public static String getSystemProperty(String key, String defautValue) {
        String value = System.getProperty(key);
        if (value == null || value.length() == 0) {
            value = System.getenv(key);
            if (value == null || value.length() == 0) {
                value = defautValue;
            }
        }
        return value;
    }

    /**
     * Get system property
     * 
     * @param dKey
     *            -D parameter
     * @param shellKey
     *            shell defined system environment property
     * @param defautValue
     * @return system property
     */
    public static String getSystemProperty(String dKey, String shellKey, String defautValue) {
        String value = System.getProperty(dKey);
        if (value == null || value.length() == 0) {
            value = System.getenv(shellKey);
            if (value == null || value.length() == 0) {
                value = defautValue;
            }
        }
        return value;
    }

    public static boolean isDebug() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments()
                .toString().indexOf("-agentlib:jdwp") > 0;
    }

}
