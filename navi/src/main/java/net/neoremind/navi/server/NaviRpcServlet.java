package net.neoremind.navi.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.neoremind.navi.conf.RpcServerConf;
import net.neoremind.navi.constant.NaviCommonConstant;
import net.neoremind.navi.exception.InvalidProtocolException;
import net.neoremind.navi.exception.InvalidRequestException;
import net.neoremind.navi.server.annotation.NaviRpcService;
import net.neoremind.navi.server.handler.NaviRpcHandler;
import net.neoremind.navi.server.vo.NaviRpcRequest;
import net.neoremind.navi.server.vo.NaviRpcResponse;
import net.neoremind.navi.util.ByteUtil;
import net.neoremind.navi.util.EncodingUtil;
import net.neoremind.navi.util.NetUtils;
import net.neoremind.navi.util.SystemPropertiesUtils;
import net.neoremind.navi.util.ZkPathUtil;

/**
 * RPC visit entrance including <br>
 * 1) Initialize all rpc bean(either by annotation or xml way) in spring container, <br>
 * 2) Listing of all services (GET Method), <br>
 * 3) Rpc service invocation (POST Method)
 * 
 * @author Zhang Xu
 */
public class NaviRpcServlet extends BaseRpcServlet {

	private static final long serialVersionUID = 3953082371382281560L;

	private final static Logger log = LoggerFactory.getLogger(NaviRpcServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		ApplicationContext factory = null;
		try {

			/*
			 * Pre-requisite steps
			 * 1) Check if rpc.conf is valid 
			 * 2) Try to connect to zookeeper if enabled and create root path which the server suppose to registered
			 */
			if (!validateRpcConfig() || !createZkRootPathIfEnabled()) {
				log.error("Rpc Configuration seems to have some problem, interrupt init process, please correct the error.");
				return;
			}

			factory = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());

			/*
			 * Get all Navi Rpc services through Annotation or Xml way
			 */
			String[] beanNamesForType = factory.getBeanNamesForType(NaviRpcExporter.class);
			Map<String, Object> beanNamesWithAnnotation = factory.getBeansWithAnnotation(NaviRpcService.class);

			/*
			 * Init Xml configured services
			 */
			if (beanNamesForType == null || beanNamesForType.length == 0) {
				log.warn("No navi rpc service found with XML configured.");
			} else {
				for (String beanName : beanNamesForType) {
					NaviRpcExporter exporter = (NaviRpcExporter) factory.getBean(beanName);
					String context = "";
					try {
						Class<?> interf = exporter.getServiceInterface();
						context = interf.getSimpleName();
						if (interf.isAssignableFrom(exporter.getServiceBean().getClass())) {
							exporters.put(context, exporter);
							log.info("Find Rpc service configured by Xml style, interface is " + context + " , implementation is " + exporter.getServiceBean().getClass().getName());
						} else {
							log.error("The interface " + interf.getName() + " is not compatible with the bean " + beanName);
						}
					} catch (ClassNotFoundException e) {
						log.error("The interface " + exporter.getServiceInterfaceName() + " not found.");
					}
				}
			}

			/*
			 * Init Annotation configured services
			 */
			if (beanNamesWithAnnotation == null || beanNamesWithAnnotation.size() == 0) {
				log.warn("No navi rpc service found with annotation configured.");
			} else {
				for (Entry<String, Object> beanWithAnnotationMap : beanNamesWithAnnotation.entrySet()) {
					Object serviceBean = beanWithAnnotationMap.getValue();
					Class<?> interf = serviceBean.getClass().getAnnotation(NaviRpcService.class).serviceInterface();
					if (interf == null) {
						log.error("Rpc service interface not configured for " + serviceBean.getClass().getName());
						continue;
					}
					String context = "";
					try {
						NaviRpcExporter exporter = new NaviRpcExporter(interf.getName(), serviceBean);
						context = interf.getSimpleName();
						if (interf.isAssignableFrom(serviceBean.getClass())) {
							if (exporters.containsKey(context)) {
								log.warn("Duplicate service=[" + context + "] configured, Annotation way will override Xml way.");
							}
							exporters.put(context, exporter);
							log.info("Find Rpc service configured by Annotation style, interface is " + context + " , implementation is " + serviceBean.getClass().getName());
						} else {
							log.error("The interface " + interf.getName() + " is not compatible with the bean " + serviceBean);
						}
					} catch (Exception e) {
						log.error("The interface " + context + " configured error");
					}
				}
			}

			/*
			 * Service registry in zookeeper
			 */
			String localIp = NetUtils.getLocalHostIP();
			String localHostname = NetUtils.getHostName();
			// override configuration files' port
			RpcServerConf.SERVER_PORT = Integer.valueOf(SystemPropertiesUtils.getSystemProperty(NaviCommonConstant.SYSTEM_PROPERTY_SERVER_PORT_FROM_SLASH_D, NaviCommonConstant.SYSTEM_PROPERTY_SERVER_PORT_FROM_ENV, "" + RpcServerConf.SERVER_PORT)); 
			List<String> failedRegistryServiceList = new ArrayList<String>();
			if (RpcServerConf.ENABLE_ZK_REGISTRY && zkClient != null) {
				for (Entry<String, NaviRpcExporter> exporter : exporters.entrySet()) {
					String serviceName = exporter.getKey();
					String zkPath = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, RpcServerConf.ZK_REGISTRY_NAMESPACE, serviceName, localIp + ":" + RpcServerConf.SERVER_PORT);
					try {
						if (zkClient.exists(zkPath) != null) {
							zkClient.delete(zkPath);
						}
						zkClient.createSessionNodeForRecursive(zkPath, localHostname.getBytes());
						log.info("Zookeeper service registry successfully - " + zkPath);
					} catch (NoNodeException e) {
						log.warn("Zookeeper path cannot found - " + zkPath);
					} catch (NodeExistsException e) {
						log.warn("Zookeeper path is already exists - " + zkPath);
					} catch (Exception e) {
						log.error("Zookeeper create path failed for " + zkPath, e);
						failedRegistryServiceList.add(zkPath);
					}
				}
			}

			/*
			 * Print necessary logs
			 */
			log.info("Export RPC services: " + Arrays.toString(exporters.keySet().toArray(new String[] {})));
			log.info("Export total " + exporters.size() + " RPC services");
			if (RpcServerConf.ENABLE_ZK_REGISTRY && zkClient != null) {
				if (CollectionUtils.isEmpty(failedRegistryServiceList)) {
					log.info("Registry all services to zookeeper successfully");
				} else {
					log.info("Registry services to zookeeper encounter some problem, the failure registry path are " + Arrays.toString(failedRegistryServiceList.toArray(new String[] {})));
				}
			} else {
				log.info("Export services soley at localhost NOT register at zookeeper");
			}
			log.info("Please visit http://" + localIp + ":" + RpcServerConf.SERVER_PORT + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + "/${ServiceName} for details");
		} catch (Exception e) {
			log.error("Initialzing RPC bean failed, " + e.toString(), e);
		} 
	}

	@Override
	public void destroy() {
		
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String context = request.getPathInfo();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		log.info("context=" + context);
		if (context != null && context.contains(NaviCommonConstant.ZOOKEEPER_URL_PATH)) {
			if (RpcServerConf.ENABLE_ZK_REGISTRY == true) {
				out.println("<h1>Zookeeper Configuration</h1></br>");
				out.print("<table border=\"1\">");
				out.println("<tr><th>Item</th><th>Value</th></tr>");
				out.println("<td>Localhost IP</td>" + "<td>" + NetUtils.getLocalHostIP() + "</td></tr>");
				out.println("<td>Localhost name</td>" + "<td>" + NetUtils.getHostName() + "</td></tr>");
				out.println("<td>Local server port</td>" + "<td>" + RpcServerConf.SERVER_PORT + "</td></tr>");
				out.println("<td>Zookeeper server</td>" + "<td>" + RpcServerConf.ZK_SERVER_LIST + "</td></tr>");
				out.println("<td>Zookpeeper root path</td>" + "<td>" + ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, RpcServerConf.ZK_REGISTRY_NAMESPACE) + "</td></tr>");
				out.println("<td>Zookpeeper sessoin timeout in mills</td>" + "<td>" + RpcServerConf.ZK_DEFAULT_SESSION_TIMEOUT_MILLS + "</td></tr>");

				try {
					States states = zkClient.getState();
					out.println("<td>Zookpeeper state</td>" + "<td>" + states.name() + "</td></tr>");
					out.println("</table>");
					out.println("</br>");
					out.print("<table border=\"1\">");
					out.println("<tr><th>Service Name</th><th>Application Number</th><th>IP and Port</th><th>Host</th></tr>");
					String basePath = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, RpcServerConf.ZK_REGISTRY_NAMESPACE);
					List<String> rootServiceChildren = zkClient.getChildren(basePath);
					for (String service : rootServiceChildren) {
						String servicePath = ZkPathUtil.buildPath(basePath, service);
						List<String> serverList = zkClient.getChildren(servicePath);
						int rowNumber = serverList.size();
						for (int i = 0; i < serverList.size(); i++) {
							String host = new String(zkClient.getData(ZkPathUtil.buildPath(servicePath, serverList.get(i))));
							if (i == 0) {
								out.println("<tr><td rowspan=\"" + rowNumber + "\">" + service + "</td><td rowspan=\"" + rowNumber + "\" align=\"center\" >" + rowNumber + "</td><td><a href=\"" + NaviCommonConstant.TRANSPORT_PROTOCOL + serverList.get(i) + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + "\">" + serverList.get(i) + "</a></td><td>" + host + "</td></tr>");
							} else {
								out.println("<td><a href=\"" + NaviCommonConstant.TRANSPORT_PROTOCOL + serverList.get(i) + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + "\">" + serverList.get(i) + "</a></td>" + "<td>" + host + "</td></tr>");
							}
						}
					}
					out.println("</table></br>");
					out.println("<a href=\"" + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + "\">back</a>");
				} catch (Exception e) {
					log.error("Zookeeper client invocation failed, " + e.getMessage(), e);
					out.print("Zookeeper client invocation failed, " + e.getMessage());
				}
			} else {
				out.println("<h1>Rpc has not enabled zookeeper service registry</h1></br>");
				out.println("<a href=\"" + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + "\">back</a>");
			}
		} else if (context == null || context.equals("/*") || context.equals("/")) {
			out.println("<h1>Rpc Service Summary</h1>");
			out.println("The following lists all supported services");
			out.println("<table border=\"1\"><tr>" + "<th>Service</th>" + "<th>Interface</th>" + "</tr>");
			for (Entry<String, NaviRpcExporter> i : exporters.entrySet()) {
				out.println("<tr><td>" + i.getKey() + "</td><td><a href=\"" + request.getContextPath() + request.getServletPath() + "/" + i.getKey() + "\">" + i.getValue().getServiceInterfaceName() + "</a></td></tr>");
			}
			out.println("</table></br>");
			out.println("<a href=\"" + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + NaviCommonConstant.ZOOKEEPER_URL_PATH + "\">Zookeper configuration</a>");
		} else {
			context = context.substring(1);
			NaviRpcExporter serviceExporter = exporters.get(context);
			if (serviceExporter != null) {
				out.println("<h1>Interface Summary</h1>");
				out.println("The following lists all functions in <a href=\"" + request.getContextPath() + request.getServletPath() + "\"/>service </a>" + serviceExporter.getServiceInterfaceName());
				try {
					out.println("<table border=\"1\"><tr>" + "<th>Method</th>" + "<th>Signature</th>" + "</tr>");
					for (Method m : serviceExporter.getServiceInterface().getMethods()) {
						out.println("<tr>" + "<td>" + m.getName() + "</td>" + "<td>" + EncodingUtil.escapeHTML(m.toGenericString().substring(16).replaceAll("java\\.lang\\.", "").replaceAll("java\\.util\\.", "").replaceAll(serviceExporter.getServiceInterfaceName() + ".", "")) + "</td></tr>");
					}
					out.println("</table></br>");
					out.println("<a href=\"" + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + "\">back</a>");
				} catch (Exception e) {
					out.println("e.toString()");
				}
			} else {
				response.setStatus(404);
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		try {
			NaviRpcExporter serviceExporter = getRpcExporter(httpServletRequest);

			String encoding = httpServletRequest.getCharacterEncoding();
			String contentType = getHttpContentType(httpServletRequest);

			NaviRpcHandler handler = handlerFactory.getHandlerByProtocal(contentType);

			byte[] bytes = ByteUtil.readStream(httpServletRequest.getInputStream(), httpServletRequest.getContentLength());
			//log.debug("Exporter interface is " + serviceExporter.getServiceInterface() + ", bean is " + serviceExporter.getServiceBean());
			log.debug(bytes.length + " bytes received");
			NaviRpcRequest request = new NaviRpcRequest(serviceExporter.getServiceInterface(), serviceExporter.getServiceBean(), bytes);

			NaviRpcResponse response = handler.service(request);

			buildHttpResponse(httpServletResponse, response, contentType, encoding);
		} catch (InvalidRequestException e) {
			log.error(e.getMessage(), e);
			httpServletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
		} catch (InvalidProtocolException e) {
			log.error(e.getMessage(), e);
			httpServletResponse.setStatus(HttpStatus.SC_NOT_ACCEPTABLE);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			httpServletResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
