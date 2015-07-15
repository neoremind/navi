package com.baidu.beidou.navi.it;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static int PORT;

    public static int ZOO_SERVER_PORT;

    public static String ZOO_LIST;

    public static String ZOO_REGISTRY_NAMESPACE;

    public Config() {

    }

    private static Properties props = new Properties();

    static {
        try {
            props.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PORT = Integer.parseInt(getValue("server.port"));
        ZOO_SERVER_PORT = Integer.parseInt(getValue("zoo.server.port"));
        ZOO_LIST = getValue("zoo.zk_list");
        ZOO_REGISTRY_NAMESPACE = getValue("zoo.zk_registry_namespace");
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    public static void updateProperties(String key, String value) {
        props.setProperty(key, value);
    }

}
