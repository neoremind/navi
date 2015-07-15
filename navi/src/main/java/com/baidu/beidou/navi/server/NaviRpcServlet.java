package com.baidu.beidou.navi.server;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baidu.beidou.navi.constant.HttpStatus;
import com.baidu.beidou.navi.constant.NaviCommonConstant;
import com.baidu.beidou.navi.exception.InvalidProtocolException;
import com.baidu.beidou.navi.exception.InvalidRequestException;
import com.baidu.beidou.navi.exception.ServiceNotFoundException;
import com.baidu.beidou.navi.exception.rpc.CodecException;
import com.baidu.beidou.navi.exception.rpc.DeserilizeNullException;
import com.baidu.beidou.navi.exception.rpc.MethodNotFoundException;
import com.baidu.beidou.navi.exception.rpc.ServerErrorException;
import com.baidu.beidou.navi.server.annotation.NaviRpcService;
import com.baidu.beidou.navi.server.callback.CallFuture;
import com.baidu.beidou.navi.server.context.LocalContext;
import com.baidu.beidou.navi.server.filter.FilterBuilder;
import com.baidu.beidou.navi.server.processor.CoreNaviRpcProcessor;
import com.baidu.beidou.navi.server.vo.NaviRpcRequest;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;
import com.baidu.beidou.navi.util.ByteUtil;
import com.baidu.beidou.navi.util.IPUtils;

/**
 * ClassName: NaviRpcServlet <br/>
 * Function: Navi rpc处理的servlet，利用jetty、tomcat等container作为IO处理模型、线程管理容器，主要工作聚焦在处理请求响应上。
 * 
 * @author Zhang Xu
 */
public class NaviRpcServlet extends BaseRpcServlet {

    private static final long serialVersionUID = 3953082371382281560L;

    private static final Logger LOG = LoggerFactory.getLogger(NaviRpcServlet.class);

    /**
     * Creates a new instance of NaviRpcServlet.
     */
    public NaviRpcServlet() {
        // 以调用链方式构造processor
        processor = FilterBuilder.buildFilterChain(new CoreNaviRpcProcessor());
    }

    /**
     * 初始化Navi rpc工作，主要步骤包括
     * <ul>
     * <li>1)检查配置。</li>
     * <li>2)从环境中加载Spring bean factory获取以XML方式暴露的<code>NaviRpcExporter</code>，注册该服务bean下的所有可暴露方法。</li>
     * <li>3)从环境中加载Spring bean factory获取以注解方式暴露的<code>@NaviRpcService</code>，注册该服务bean下的所有可暴露方法。</li>
     * <li>4)将上两步暴露的服务，进行注册，例如可以发布到zookeeper上。</li>
     * </ul>
     * 
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            if (!validateConfiguration()) {
                LOG.error("Rpc configuration has some problems, so interrupt initialization process, please figure out.");
                return;
            }

            ApplicationContext factory = WebApplicationContextUtils.getWebApplicationContext(config
                    .getServletContext());
            if (factory == null) {
                LOG.error("That is fatal! No spring factory found in container, which prevent servlet initialization from executing!");
                return;
            }

            // 注册XML方式暴露的bean
            Map<String, NaviRpcExporter> xmlBeans = factory.getBeansOfType(NaviRpcExporter.class);
            if (xmlBeans == null || xmlBeans.isEmpty()) {
                LOG.warn("No navi rpc service found with XML configured.");
            } else {
                for (NaviRpcExporter bean : xmlBeans.values()) {
                    serviceLocator.regiserService(bean);
                }
            }

            // 注册注解方式暴露的bean
            Map<String, Object> annoBeans = factory.getBeansWithAnnotation(NaviRpcService.class);
            if (annoBeans == null || annoBeans.isEmpty()) {
                LOG.warn("No navi rpc service found with annotation configured.");
            } else {
                for (Object bean : annoBeans.values()) {
                    NaviRpcService anno = bean.getClass().getAnnotation(NaviRpcService.class);
                    Class<?> targetClass = bean.getClass();
                    if (anno == null) {
                        targetClass = AopUtils.getTargetClass(bean);
                        anno = targetClass.getAnnotation(NaviRpcService.class);
                        if (anno == null) {
                            continue;
                        }
                        if (anno.serviceInterface() == null) {
                            LOG.error("Rpc service interface not configured for "
                                    + targetClass.getName());
                            continue;
                        }
                    }
                    serviceLocator.regiserService(new NaviRpcExporter(anno.serviceInterface()
                            .getName(), bean));
                }
            }

            // 发布服务
            serviceLocator.publishService(publishHandler);

            LOG.info("Please visit http://" + IPUtils.getLocalHostAddress() + ":${port}"
                    + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + " for details");
        } catch (Exception e) {
            LOG.error("Initialize rpc bean failed, " + e.toString(), e);
        }
    }

    /**
     * 显示管理页面
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showHtmlPage(request, response);
    }

    /**
     * 处理请求
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws ServletException, IOException {
        CallFuture<NaviRpcResponse> callFuture = new CallFuture<NaviRpcResponse>();
        try {
            NaviRpcExporter serviceExporter = getRpcExporter(httpRequest);
            String protocol = getProtocolByHttpContentType(httpRequest);
            LocalContext.getContext().setStartTime().setProtocol(protocol)
                    .setFromIp(IPUtils.getIpAddr(httpRequest))
                    .setServiceName(serviceExporter.getName());

            byte[] byteArray = ByteUtil.readStream(httpRequest.getInputStream(),
                    httpRequest.getContentLength());
            LocalContext.getContext().setReqByteSize(byteArray.length);

            NaviRpcRequest request = new NaviRpcRequest(serviceExporter, byteArray);
            processor.service(request, callFuture);
        } catch (InvalidRequestException e) {
            callFuture.cancel(true);
            LOG.warn(e.getMessage());
            httpResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
        } catch (InvalidProtocolException e) {
            callFuture.cancel(true);
            LOG.warn(e.getMessage());
            httpResponse.setStatus(HttpStatus.SC_NOT_ACCEPTABLE);
        } catch (ServiceNotFoundException e) {
            callFuture.cancel(true);
            LOG.warn(e.getMessage());
            httpResponse.setStatus(HttpStatus.SC_NOT_FOUND);
        } catch (CodecException e) {
            callFuture.cancel(true);
            LOG.warn(e.getMessage());
            httpResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
            callFuture.cancel(true);
        } catch (DeserilizeNullException e) {
            callFuture.cancel(true);
            LOG.warn(e.getMessage());
            httpResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
        } catch (MethodNotFoundException e) {
            callFuture.cancel(true);
            LOG.warn(e.getMessage());
            httpResponse.setStatus(HttpStatus.SC_NOT_FOUND);
        } catch (ServerErrorException e) {
            callFuture.cancel(true);
            LOG.warn(e.getMessage());
            httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            callFuture.cancel(true);
            LOG.error(e.getMessage(), e);
            httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                buildHttpResponse(httpResponse, callFuture.get(), httpRequest.getContentType(),
                        httpRequest.getCharacterEncoding());
            } catch (InterruptedException e2) {
                LOG.error(e2.getMessage(), e2);
            }
            LocalContext.removeContext();
        }
    }

}
