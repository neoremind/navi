package com.baidu.beidou.navi.server.locator;

import java.util.Collection;

import com.baidu.beidou.navi.server.ServiceNameAware;

/**
 * ClassName: ServiceLocator <br/>
 * Function: 服务定位器，用于注入服务到运行容器，可辅助入口端路由到指定方法，另外还可以发布服务，例如到zookeeper中。
 * <p>
 * 泛型&lt;KEY&gt;标示服务的唯一标示，例如可以是一个<tt>int</tt>的数字id，可以是一个<tt>String</tt>字符串；
 * 泛型&lt;V&gt;标示服务bean的一个封装，在上层作为基础路由定位到bean粒度级别的类型
 * 
 * @author Zhang Xu
 */
public interface ServiceLocator<KEY, V extends ServiceNameAware> {

    /**
     * 获取所有服务（通常是接口名称）
     * 
     * @return 服务集合
     */
    Collection<V> getAllServices();

    /**
     * 根据服务名称（通常是接口名称）获取服务
     * 
     * @param 服务名称
     * @return 服务的一个封装
     */
    V getService(String serviceName);

    /**
     * 根据服务标示获取服务方法的描述
     * 
     * @param key
     * @return 服务方法的描述
     */
    MethodDescriptor<KEY> getServiceDescriptor(KEY key);

    /**
     * 注入服务
     * 
     * @param v
     *            服务的实例或者封装类
     * @return 是否注册成功
     */
    boolean regiserService(V v);

    /**
     * 整体发布服务
     * 
     * @param handler
     *            发布处理handler
     * @return 是否发布成功
     */
    boolean publishService(PublishHandler handler);

}
