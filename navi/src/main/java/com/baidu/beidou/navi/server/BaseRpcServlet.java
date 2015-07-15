package com.baidu.beidou.navi.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.conf.RpcServerConf;
import com.baidu.beidou.navi.constant.NaviCommonConstant;
import com.baidu.beidou.navi.exception.InvalidRequestException;
import com.baidu.beidou.navi.exception.ServiceNotFoundException;
import com.baidu.beidou.navi.server.locator.ServiceLocator;
import com.baidu.beidou.navi.server.locator.impl.MethodSignatureKeyServiceLocator;
import com.baidu.beidou.navi.server.locator.impl.ZookeeperPublishHandler;
import com.baidu.beidou.navi.server.processor.NaviRpcProcessor;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;
import com.baidu.beidou.navi.util.ClassHtmlUtil;
import com.baidu.beidou.navi.util.NetUtils;
import com.baidu.beidou.navi.util.StringUtil;
import com.baidu.beidou.navi.util.ZkPathUtil;
import com.baidu.beidou.navi.zk.SimpleZooKeeperClient;

/**
 * Base RPC Servlet
 * 
 * @author Zhang Xu
 */
public class BaseRpcServlet extends HttpServlet {

    private static final long serialVersionUID = 7218942946283023839L;

    private static final Logger LOG = LoggerFactory.getLogger(BaseRpcServlet.class);

    /**
     * Navi rpc核心处理器
     */
    protected NaviRpcProcessor processor;

    /**
     * 服务的定位器
     */
    protected ServiceLocator<String, NaviRpcExporter> serviceLocator = new MethodSignatureKeyServiceLocator();

    /**
     * zookeeper发布服务器
     */
    protected ZookeeperPublishHandler publishHandler = new ZookeeperPublishHandler();

    /**
     * 检查配置
     * 
     * @return
     */
    protected boolean validateConfiguration() {
        LOG.info("Start to check navi rpc configuration ...");
        LOG.info("RpcConf.ENABLE_ZK_REGISTRY=" + RpcServerConf.ENABLE_ZK_REGISTRY);
        if (RpcServerConf.ENABLE_ZK_REGISTRY) {
            if (StringUtil.isEmpty(RpcServerConf.ZK_SERVER_LIST)) {
                LOG.error("RpcConf.ZK_SERVER_LIST is empty");
                return false;
            }
            LOG.info("RpcConf.ZK_SERVER_LIST=" + RpcServerConf.ZK_SERVER_LIST);

            if (StringUtil.isEmpty(RpcServerConf.ZK_REGISTRY_NAMESPACE)) {
                LOG.error("RpcConf.ZK_REGISTRY_NAMESPACE is empty");
                return false;
            }
            if (StringUtil.isNotEmpty(RpcServerConf.ZK_DIGEST_AUTH)) {
                LOG.info("RpcConf.ZK_DIGEST_AUTH=" + RpcServerConf.ZK_DIGEST_AUTH);
            }
            LOG.info("ZOOKEEPER_BASE_PATH=" + NaviCommonConstant.ZOOKEEPER_BASE_PATH);
            LOG.info("RpcConf.ZK_REGISTRY_NAMESPACE=" + RpcServerConf.ZK_REGISTRY_NAMESPACE);
        }
        LOG.info("Check navi rpc configuration pass");
        return true;
    }

    /**
     * 从http请求request path中获取<tt>NaviRpcExporter</tt>，起到路由作用
     * 
     * @param httpServletRequest
     * @return
     */
    protected NaviRpcExporter getRpcExporter(HttpServletRequest httpServletRequest) {
        String context = httpServletRequest.getPathInfo();
        if (context == null) {
            throw new InvalidRequestException("Rpc path invalid");
        }
        if (context.length() > 0) {
            context = context.substring(1);
        }
        NaviRpcExporter serviceExporter = serviceLocator.getService(context);
        if (serviceExporter == null) {
            throw new ServiceNotFoundException("No rpc service found for " + context);
        }
        return serviceExporter;
    }

    /**
     * 获取http请求的request中的content-type，如没有设置则设置为默认的
     * {@link com.baidu.beidou.navi.constant.NaviCommonConstant#DEFAULT_PROTOCAL_CONTENT_TYPE}
     * 
     * @param httpServletRequest
     * @return
     */
    protected String getProtocolByHttpContentType(HttpServletRequest httpServletRequest) {
        if (StringUtil.isEmpty(httpServletRequest.getContentType())) {
            throw new InvalidRequestException("Rpc protocol invalid");
        }
        String protocol = httpServletRequest.getContentType().split(";")[0];
        if (protocol == null) {
            protocol = NaviCommonConstant.DEFAULT_PROTOCAL_CONTENT_TYPE;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Content-type set to default value "
                        + NaviCommonConstant.DEFAULT_PROTOCAL_CONTENT_TYPE);
            }
        } else {
            protocol = protocol.toLowerCase();
        }
        return protocol;
    }

    /**
     * 设置请求结果
     * 
     * @param httpServletResponse
     * @param response
     * @param contentType
     * @param encoding
     * @throws IOException
     */
    protected void buildHttpResponse(HttpServletResponse httpServletResponse,
            NaviRpcResponse response, String contentType, String encoding) throws IOException {
        httpServletResponse.setContentType(contentType);
        httpServletResponse.setCharacterEncoding(encoding);
        if (response == null || response.getResponse() == null) {
            httpServletResponse.setContentLength(0);
        } else {
            httpServletResponse.setContentLength(response.getResponse().length);
            httpServletResponse.getOutputStream().write(response.getResponse());
        }
        httpServletResponse.flushBuffer();
    }

    /**
     * 显示管理console的html页面
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void showHtmlPage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String context = request.getPathInfo();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (context != null && context.contains(NaviCommonConstant.ZOOKEEPER_URL_PATH)) {
            if (RpcServerConf.ENABLE_ZK_REGISTRY == true) {
                SimpleZooKeeperClient zkClient = publishHandler.getZkClient();
                out.println("<h1>Zookeeper Configuration</h1>");
                out.print("<table border=\"1\">");
                out.println("<tr><th>Property</th><th>Value</th></tr>");
                out.println("<td>Server IP</td>" + "<td>" + NetUtils.getLocalHostIP()
                        + "</td></tr>");
                out.println("<td>Server hostname</td>" + "<td>" + NetUtils.getHostName()
                        + "</td></tr>");
                out.println("<td>Server port</td>" + "<td>" + RpcServerConf.SERVER_PORT
                        + "</td></tr>");
                out.println("<td>Zookeeper server</td>" + "<td>" + RpcServerConf.ZK_SERVER_LIST
                        + "</td></tr>");
                out.println("<td>Registered Service path</td>"
                        + "<td>"
                        + ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH,
                                RpcServerConf.ZK_REGISTRY_NAMESPACE) + "</td></tr>");
                out.println("<td>Zookpeeper sessoin timeout(ms)</td>" + "<td>"
                        + RpcServerConf.ZK_DEFAULT_SESSION_TIMEOUT_MILLS + "</td></tr>");
                out.println("<td>Zookpeeper connection timeout(ms)</td>" + "<td>"
                        + RpcServerConf.ZK_CONNECTION_TIMEOUT_MILLS + "</td></tr>");
                out.println("<td>Zookpeeper digest auth</td>" + "<td>"
                        + RpcServerConf.ZK_DIGEST_AUTH + "</td></tr>");

                try {
                    States states = zkClient.getState();
                    out.println("<td>Zookpeeper state</td>" + "<td>" + states.name() + "</td></tr>");
                    out.println("</table>");
                    out.println("</br>");
                    out.print("<table border=\"1\">");
                    out.println("<tr><th>Service Name</th><th>App Instance Num.</th><th>IP and Port</th><th>Host</th></tr>");
                    String basePath = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH,
                            RpcServerConf.ZK_REGISTRY_NAMESPACE);
                    List<String> rootServiceChildren = zkClient.getChildren(basePath);
                    for (String service : rootServiceChildren) {
                        String servicePath = ZkPathUtil.buildPath(basePath, service);
                        List<String> serverList = zkClient.getChildren(servicePath);
                        int rowNumber = serverList.size();
                        for (int i = 0; i < serverList.size(); i++) {
                            String host = new String(zkClient.getData(ZkPathUtil.buildPath(
                                    servicePath, serverList.get(i))));
                            if (i == 0) {
                                out.println("<tr><td rowspan=\"" + rowNumber + "\">" + service
                                        + "</td><td rowspan=\"" + rowNumber
                                        + "\" align=\"center\" >" + rowNumber
                                        + "</td><td><a href=\""
                                        + NaviCommonConstant.TRANSPORT_PROTOCOL + serverList.get(i)
                                        + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + "\">"
                                        + serverList.get(i) + "</a></td><td>" + host + "</td></tr>");
                            } else {
                                out.println("<td><a href=\""
                                        + NaviCommonConstant.TRANSPORT_PROTOCOL + serverList.get(i)
                                        + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + "\">"
                                        + serverList.get(i) + "</a></td>" + "<td>" + host
                                        + "</td></tr>");
                            }
                        }
                    }
                    out.println("</table></br>");
                    out.println("<input type=\"button\" name=\"Submit\" onclick=\"javascript:history.back(-1);\" value=\"Back\">");
                } catch (Exception e) {
                    LOG.error("Zookeeper client invocation failed, " + e.getMessage(), e);
                    out.print("Zookeeper client invocation failed, " + e.getMessage());
                }
            } else {
                out.println("<h1>Rpc has not enabled zookeeper service registry</h1></br>");
                out.println("<br><input type=\"button\" name=\"Submit\" onclick=\"javascript:history.back(-1);\" value=\"Back\">");
            }
        } else if (context == null || context.equals("/*") || context.equals("/")) {
            out.println("<h1>Navi Rpc Service Summary</h1>");
            out.println("<table border=\"1\"><tr>" + "<th>Service</th>" + "<th>Interface</th>"
                    + "</tr>");
            for (NaviRpcExporter i : serviceLocator.getAllServices()) {
                out.println("<tr><td>" + i.getName() + "</td><td><a href=\""
                        + request.getContextPath() + request.getServletPath() + "/" + i.getName()
                        + "\">" + i.getServiceInterfaceName() + "</a></td></tr>");
            }
            out.println("</table></br>");
            out.println("<a href=\"" + NaviCommonConstant.TRANSPORT_URL_BASE_PATH
                    + NaviCommonConstant.ZOOKEEPER_URL_PATH + "\">Zookeper configuration</a>");
        } else {
            context = context.substring(1);
            if (context.startsWith(ClassHtmlUtil.CLASS_URL_PATH)) {
                String className = request.getParameter("class");
                if (StringUtil.isEmpty(className)) {
                    out.println("No class specified!<br>");
                } else {
                    out.println("<h1>Class Defination: " + className + "</h1>");
                    try {
                        Set alreayRecurseClazz = new HashSet();
                        out.println(ClassHtmlUtil.getBeanDefinationHtml(Class.forName(className),
                                alreayRecurseClazz));
                    } catch (ClassNotFoundException e) {
                        out.println("Class not found!<br>");
                    } catch (Exception e) {
                        out.println(e.toString());
                    }
                }
                out.println("<br><input type=\"button\" name=\"Submit\" onclick=\"javascript:history.back(-1);\" value=\"Back\">");
            } else {
                NaviRpcExporter serviceExporter = serviceLocator.getService(context);
                if (serviceExporter != null) {
                    out.println("<h1>Service: " + serviceExporter.getServiceInterfaceName()
                            + "</h1>");
                    try {
                        out.println(ClassHtmlUtil.getServiceHtml(serviceExporter
                                .getServiceInterface()));
                        out.println("<br><input type=\"button\" name=\"Submit\" onclick=\"javascript:history.back(-1);\" value=\"Back\">");
                    } catch (Exception e) {
                        out.println(e.toString());
                    }
                } else {
                    response.setStatus(404);
                }
            }
        }
    }

}
