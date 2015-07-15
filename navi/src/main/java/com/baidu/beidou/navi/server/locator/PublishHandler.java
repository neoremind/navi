package com.baidu.beidou.navi.server.locator;

import java.util.Collection;

/**
 * ClassName: PublishHandler <br/>
 * Function: 发布服务handler，配合{@link ServiceLocator}来对加载定位到的所有服务进行发布工作
 * 
 * @author Zhang Xu
 */
public interface PublishHandler {

    /**
     * 发布服务
     * 
     * @param descs
     *            所有的服务描述
     * @return 发布成功与否
     */
    <KEY> boolean publish(Collection<MethodDescriptor<KEY>> descs);

}
