package net.neoremind.navi.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoremind.navi.conf.RpcServerConf;
import net.neoremind.navi.constant.NaviCommonConstant;
import net.neoremind.navi.exception.InvalidRequestException;
import net.neoremind.navi.server.handler.HandlerFactory;
import net.neoremind.navi.server.vo.NaviRpcResponse;
import net.neoremind.navi.util.ZkPathUtil;
import net.neoremind.navi.zk.SimpleServerWatcher;
import net.neoremind.navi.zk.SimpleZooKeeperClient;

/**
 * Base RPC Servlet
 * 
 * @author Zhang Xu
 */
public class BaseRpcServlet extends HttpServlet {

	private static final long serialVersionUID = 7218942946283023839L;
	
	private final static Logger log = LoggerFactory.getLogger(BaseRpcServlet.class); 

	/**
	 * All navi service exporter k-v
	 */
	protected Map<String, NaviRpcExporter> exporters = new HashMap<String, NaviRpcExporter>();

	/**
	 * RPC process handler factory
	 */
	protected HandlerFactory handlerFactory = new HandlerFactory();
	
	/**
	 * Zookeeper client
	 */
	protected SimpleZooKeeperClient zkClient;
	
	/**
	 * Check rpc configuration.
	 * @return if success
	 */
	protected boolean validateRpcConfig() {
		log.info("Start to check RPC configuration ...");
		log.info("RpcConf.ENABLE_ZK_REGISTRY=" + RpcServerConf.ENABLE_ZK_REGISTRY);
		if (RpcServerConf.ENABLE_ZK_REGISTRY) {
			if (StringUtils.isEmpty(RpcServerConf.ZK_SERVER_LIST)) {
				log.error("RpcConf.ZK_SERVER_LIST is empty") ;
				return false;
			}
			log.error("RpcConf.ZK_SERVER_LIST=" + RpcServerConf.ZK_SERVER_LIST) ;
			
			if (StringUtils.isEmpty(RpcServerConf.ZK_REGISTRY_NAMESPACE)) {
				log.error("RpcConf.ZK_REGISTRY_NAMESPACE is empty") ;
				return false;
			}
			log.info("ZOOKEEPER_BASE_PATH=" + NaviCommonConstant.ZOOKEEPER_BASE_PATH);
			log.info("RpcConf.ZK_REGISTRY_NAMESPACE=" + RpcServerConf.ZK_REGISTRY_NAMESPACE);
			
		}
		log.info("Check RPC configuration OK");
		return true;
	}
	
	/**
	 * Connect to zookeeper if enabled and try to build root path
	 * @return if success
	 */
	protected boolean createZkRootPathIfEnabled() {
		if (RpcServerConf.ENABLE_ZK_REGISTRY) {
			String path = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, RpcServerConf.ZK_REGISTRY_NAMESPACE);
			try {
				log.info("Connecting to zookeeper server - " + RpcServerConf.ZK_SERVER_LIST);
				zkClient = new SimpleZooKeeperClient(RpcServerConf.ZK_SERVER_LIST, new SimpleServerWatcher());
				log.info("Connect to zookeeper server successfully!");
				log.info("Creating root path - " + path);
				zkClient.createNodeForRecursive(path, "".getBytes());
				log.info("Create root path successfully!");
			} catch (NodeExistsException e) {
				log.warn("Zookeeper path is already exists - " + path);
			} catch (Exception e) {
				log.error("Zookeeper client initialization failed, " + e.getMessage(), e);
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Get rpc exporter from http request
	 * 
	 * @param httpServletRequest
	 * @return navi rpc exporter
	 */
	protected NaviRpcExporter getRpcExporter(HttpServletRequest httpServletRequest) {
		String context = httpServletRequest.getPathInfo();
		if (context == null) {
			log.error("Exporter path invalid");
			throw new InvalidRequestException("Path invalid");
		}
		if (context.length() > 0) {
			context = context.substring(1);
		}
		NaviRpcExporter serviceExporter = exporters.get(context);
		if (serviceExporter == null) {
			log.error("No rpc service found for " + context);
			throw new InvalidRequestException("Rpc service not found");
		}
		return serviceExporter;
	}

	/**
	 * Get content type from http request
	 * 
	 * @param httpServletRequest
	 * @return content type
	 */
	protected String getHttpContentType(HttpServletRequest httpServletRequest) {
		String contentType = httpServletRequest.getContentType().split(";")[0];
		if (contentType == null) {
			contentType = NaviCommonConstant.DEFAULT_PROTOCAL_CONTENT_TYPE;
			log.debug("Content-type set to default value " + NaviCommonConstant.DEFAULT_PROTOCAL_CONTENT_TYPE);
		} else {
			contentType = contentType.toLowerCase();
			//log.debug("Content-type=" + contentType);
		}
		return contentType;
	}

	/**
	 * Set http response
	 * 
	 * @param httpServletResponse
	 * @param response
	 * @param contentType
	 * @param encoding
	 * @throws IOException
	 */
	protected void buildHttpResponse(HttpServletResponse httpServletResponse, NaviRpcResponse response, String contentType, String encoding) throws IOException{
		httpServletResponse.setContentType(contentType);
		httpServletResponse.setContentLength(response.getResponse().length);
		httpServletResponse.setCharacterEncoding(encoding);
		httpServletResponse.getOutputStream().write(response.getResponse());
	}
	
}
