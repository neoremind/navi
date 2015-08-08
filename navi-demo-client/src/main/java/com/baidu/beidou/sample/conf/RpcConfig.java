package com.baidu.beidou.sample.conf;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baidu.beidou.navi.client.NaviProxyFactoryBean;
import com.baidu.beidou.navi.client.selector.NaviFailStrategy;
import com.baidu.beidou.navi.client.selector.NaviSelectorStrategy;
import com.baidu.beidou.navi.conf.RpcClientConf;
import com.baidu.beidou.navi.protocol.NaviProtocol;
import com.baidu.beidou.sample.annotation.service.CompanyMgr;
import com.baidu.beidou.sample.performancetest.service.PerformanceTestService;

@Configuration
public class RpcConfig {

	///***********************************************************************************
	//
	// Navi Rpc bean proxy are listed below
	//
	//***********************************************************************************
	//@Bean(initMethod = "setup", destroyMethod = "cleanup")
	@Bean
	public CompanyMgr companyMgr() throws Exception {
		return (CompanyMgr) (new NaviProxyFactoryBean("/navi-demo-server/main/", CompanyMgr.class, NaviProtocol.PROTOSTUFF, NaviFailStrategy.FAILOVER, NaviSelectorStrategy.RANDOM ).getObject());
	}
	
	@Bean
	public PerformanceTestService performanceTestService() throws Exception {
		return (PerformanceTestService) (new NaviProxyFactoryBean("/navi-demo-server/main/", PerformanceTestService.class, NaviProtocol.JSON, NaviFailStrategy.FAILOVER, NaviSelectorStrategy.RANDOM ).getObject());
	}

	private final static Logger log = LoggerFactory.getLogger(RpcConfig.class);

	@PostConstruct
	public void init() {
		log.info("<<< Begin to configure annotation Rpc client <<<");
		log.info("RpcConf.ENABLE_ZK_REGISTRY=" + RpcClientConf.ENABLE_ZK_REGISTRY);
		log.info("RpcConf.ZK_SERVER_LIST=" + RpcClientConf.ZK_SERVER_LIST);
		log.info("RpcConf.ZK_DEFAULT_SESSION_TIMEOUT_MILLS=" + RpcClientConf.ZK_DEFAULT_SESSION_TIMEOUT_MILLS);
		log.info("RpcConf.ZK_CONNECTION_TIMEOUT_MILLS=" + RpcClientConf.ZK_CONNECTION_TIMEOUT_MILLS);
		log.info("RpcConf.ZK_WATCH_NAMESPACE_PATHS=" + Arrays.toString(RpcClientConf.ZK_WATCH_NAMESPACE_PATHS));
		log.info("RpcConf.RPC_CONNECTION_TIMEOUT=" + RpcClientConf.RPC_CONNECTION_TIMEOUT);
		log.info("RpcConf.RPC_READ_TIMEOUT=" + RpcClientConf.RPC_READ_TIMEOUT);
		log.info("RpcConf.RPC_RETRY_TIMES=" + RpcClientConf.RPC_RETRY_TIMES);
		log.info("<<< End to configure annotation Rpc client <<<");
	}

}
