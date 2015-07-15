package com.baidu.beidou.navi.server;

/**
 * ClassName: NaviRpcExporter <br/>
 * Function: 服务端暴露的NaviRpc服务封装
 * 
 * @author Zhang Xu
 */
public class NaviRpcExporter implements ServiceNameAware {

    /**
     * 服务接口名称
     */
    private String serviceInterfaceName;

    /**
     * 服务实现类
     */
    private Object serviceBean;

    /**
     * Creates a new instance of NaviRpcExporter.
     */
    public NaviRpcExporter() {

    }

    /**
     * Creates a new instance of NaviRpcExporter.
     * 
     * @param serviceInterfaceName
     * @param serviceBean
     */
    public NaviRpcExporter(String serviceInterfaceName, Object serviceBean) {
        this.serviceInterfaceName = serviceInterfaceName;
        this.serviceBean = serviceBean;
    }

    /**
     * 通过反射获取接口class
     * 
     * @return 接口的class
     */
    public Class<?> getServiceInterface() {
        try {
            return Class.forName(serviceInterfaceName);
        } catch (Exception e) {
            throw new RuntimeException("Class not found for " + serviceInterfaceName);
        }
    }

    /**
     * 获取服务名称
     * 
     * @return 名称
     * @see com.baidu.beidou.navi.server.ServiceNameAware#getName()
     */
    @Override
    public String getName() {
        return this.getServiceInterface().getSimpleName();
    }

    public String getServiceInterfaceName() {
        return serviceInterfaceName;
    }

    public void setServiceInterfaceName(String serviceInterface) {
        this.serviceInterfaceName = serviceInterface;
    }

    public Object getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(Object serviceBean) {
        this.serviceBean = serviceBean;
    }

}
