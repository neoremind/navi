package com.baidu.beidou.navi.server.locator;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.util.Preconditions;

/**
 * ClassName: ServiceRegistry <br/>
 * Function: 服务注册信息保存者，常驻内存的缓存，使用单例访问，缓存了服务的描述 <br/>
 * 泛型&lt;KEY&gt;标示服务的唯一标示，例如可以是一个<tt>int</tt>的数字id，可以是一个<tt>String</tt>字符串
 * 
 * @author Zhang Xu
 */
@SuppressWarnings("rawtypes")
public class ServiceRegistry<KEY> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRegistry.class);

    /**
     * 服务注册的字典 <br/>
     * 键为服务标示<code>KEY</code>，值为服务具体描述
     * 
     * @see MethodDescriptor
     */
    private ConcurrentHashMap<KEY, MethodDescriptor<KEY>> serviceDescriptors;

    /**
     * 注册单例引用
     */
    private static ServiceRegistry instance;

    /**
     * 获取服务注册对象
     * 
     * @return 服务注册信息保存者
     */
    @SuppressWarnings("unchecked")
    public static <E> ServiceRegistry<E> getInstance() {
        if (instance == null) {
            synchronized (ServiceRegistry.class) {
                if (instance == null) {
                    instance = new ServiceRegistry<E>();
                }
            }
        }
        return instance;
    }

    /**
     * Creates a new instance of ServiceRegistry.
     */
    private ServiceRegistry() {
        serviceDescriptors = new ConcurrentHashMap<KEY, MethodDescriptor<KEY>>();
    }

    /**
     * 获取所有服务描述
     * 
     * @return 所有注册的服务的方法描述
     * @throws IllegalStateException
     */
    public Collection<MethodDescriptor<KEY>> getAllServiceDescriptors()
            throws IllegalStateException {
        Preconditions.checkNotNull(serviceDescriptors, "serviceDescriptors cannot be null");
        return serviceDescriptors.values();
    }

    /**
     * 根据标示<code>KEY</code>获取服务描述
     * 
     * @param key
     *            服务的唯一标示
     * @return 服务的方法描述
     * @throws IllegalStateException
     */
    public MethodDescriptor<KEY> getServiceDescriptorByKey(KEY key) throws IllegalStateException {
        Preconditions.checkNotNull(key, "Key cannot be null");
        Preconditions.checkState(instance != null, "ServiceRegistry not init yet");
        return serviceDescriptors.get(key);
    }

    /**
     * 加入服务描述
     * 
     * @param key
     *            服务的唯一标示
     * @param serviceDescriptor
     *            服务的方法描述
     */
    public void addServiceDescriptor(KEY key, MethodDescriptor<KEY> serviceDescriptor) {
        Preconditions.checkNotNull(key, "Key cannot be null");
        Preconditions.checkState(instance != null, "ServiceRegistry not init yet");
        if (serviceDescriptors.containsKey(key)) {
            LOG.warn("Ignore key=" + key + " since it is already registered with "
                    + serviceDescriptor);
        }
        this.serviceDescriptors.putIfAbsent(key, serviceDescriptor);
    }

}
