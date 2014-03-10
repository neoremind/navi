package net.neoremind.navi.client.invoker;

import net.neoremind.navi.client.proxy.ProtobufRpcProxy;
import net.neoremind.navi.client.proxy.ProtostuffRpcProxy;
import net.neoremind.navi.client.proxy.ProxyFactory;
import net.neoremind.navi.exception.InvalidProtocolException;
import net.neoremind.navi.exception.rpc.RpcException;
import net.neoremind.navi.server.protocol.NaviProtocol;

public class InvokerFactory {

	public static NaviServiceInvoker getInvokerByProtocal(NaviProtocol type, final Class<?> targetClass, final String serviceUrl, final int connectionTimeout, final int readTimeout) throws InvalidProtocolException {
		if (type.equals(NaviProtocol.PROTOSTUFF)) {
			return new NaviServiceInvoker() {
				public Object getInvoker() throws RpcException {
					ProtostuffRpcProxy proxy = new ProtostuffRpcProxy(serviceUrl);
					proxy.setConnectTimeout(connectionTimeout);
					proxy.setReadTimeout(readTimeout);
					return ProxyFactory.createProxy(targetClass, proxy);
				}
			};
		} else if (type.equals(NaviProtocol.PROTOBUF)) {
			return new NaviServiceInvoker() {
				public Object getInvoker() throws RpcException {
					ProtobufRpcProxy proxy = new ProtobufRpcProxy(serviceUrl);
					proxy.setConnectTimeout(connectionTimeout);
					proxy.setReadTimeout(readTimeout);
					return ProxyFactory.createProxy(targetClass, proxy);
				}
			};
		} 
		throw new InvalidProtocolException("RPC protocol not supported");
	}

}
