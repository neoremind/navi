package net.neoremind.navi.server.handler;

import java.util.Map;
import java.util.TreeMap;

import net.neoremind.navi.exception.InvalidProtocolException;
import net.neoremind.navi.server.handler.protobuf.ProtobufRpcHandler;
import net.neoremind.navi.server.handler.protostuff.ProtostuffRpcHandler;
import net.neoremind.navi.server.protocol.NaviProtocol;

/**
 * Handler factory
 * 
 * @author Zhang Xu
 */
public class HandlerFactory {

	final static NaviRpcHandler protostuffHandler = new ProtostuffRpcHandler();
	final static NaviRpcHandler protobufHandler = new ProtobufRpcHandler();

	final static Map<String, NaviRpcHandler> handlerMap = new TreeMap<String, NaviRpcHandler>();
	static {
		handlerMap.put(NaviProtocol.PROTOSTUFF.getName(), protostuffHandler);
		handlerMap.put(NaviProtocol.PROTOBUF.getName(), protobufHandler);
	}

	/**
	 * Find handler by content-type, protocol is determined by HTTP Content-Type
	 * @param type HTTP Content-Type
	 * @return Handler
	 * @throws InvalidProtocolException
	 */
	public NaviRpcHandler getHandlerByProtocal(String type) throws InvalidProtocolException {
		NaviRpcHandler handler = handlerMap.get(type);
		if (handler != null) {
			return handler;
		}
		throw new InvalidProtocolException("RPC protocol not supported");
	}

}
