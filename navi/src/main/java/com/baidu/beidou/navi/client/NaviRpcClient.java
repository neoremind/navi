package com.baidu.beidou.navi.client;

import java.lang.reflect.Method;

import com.baidu.beidou.navi.client.attachment.Attachment;

/**
 * ClassName: NaviRpcClient <br/>
 * Function: 通用的Navi rpc客户端接口
 * 
 * @author Zhang Xu
 */
public interface NaviRpcClient {

    /**
     * 调用发起远程通信
     * 
     * @param method
     *            方法
     * @param args
     *            参数
     * @return 返回结果
     * @throws Throwable
     *             异常信息
     */
    Object transport(Method method, Object[] args) throws Throwable;

    /**
     * 调用发起远程通信，一般用于结合{@link SimpleNaviRpcClientBuilder}使用用于简单的客户端调用，<br/>
     * 对于动态代理方式使用{@link #transport(Method, Object[])}
     * 
     * @param methodName
     *            方法名称
     * @param args
     *            参数
     * @return 带有泛型T的返回结果
     * @throws Throwable
     *             异常信息
     */
    <T> T transport(String methodName, Object[] args) throws Throwable;

    /**
     * 调用发起远程通信
     * 
     * @param method
     *            方法
     * @param args
     *            参数
     * @param attachment
     *            附加属性信息
     * @return 返回结果
     * @throws Throwable
     *             异常信息
     */
    Object transport(Method method, Object[] args, Attachment attachment) throws Throwable;

    /**
     * 调用发起远程通信，一般用于结合{@link SimpleNaviRpcClientBuilder}使用用于简单的客户端调用，<br/>
     * 对于动态代理方式使用{@link #transport(Method, Object[])}
     * 
     * @param methodName
     *            方法名称
     * @param args
     *            参数
     * @param 附加属性信息
     * @return 带有泛型T的返回结果
     * @throws Throwable
     *             异常信息
     */
    <T> T transport(String methodName, Object[] args, Attachment attachment) throws Throwable;

    /**
     * 获取客户端相关信息
     * 
     * @return 客户端信息
     */
    String getInfo();

}
