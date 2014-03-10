package net.neoremind.navi.client.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoremind.navi.codec.Codec;
import net.neoremind.navi.codec.protobuf.ProtobufCodec;
import net.neoremind.navi.exception.rpc.ParseErrorException;
import net.neoremind.navi.server.protocol.NaviProtocol;
import net.neoremind.navi.server.vo.RequestDTO;
import net.neoremind.navi.server.vo.ResponseDTO;

public class ProtobufRpcProxy extends AbstractRpcProxy implements Cloneable {
	
	private final static Logger log = LoggerFactory.getLogger(ProtobufRpcProxy.class); 
	
	private static final Codec codec = new ProtobufCodec();

	@Override
	protected ResponseDTO deserialize(byte[] req) throws ParseErrorException {
		try {
			return codec.decode(ResponseDTO.class, req);
		} catch (Exception e) {
			log.error("Deserialize byte failed", e);
			throw new ParseErrorException("Deserialize byte error");
		}
	}

	@Override
	protected byte[] serialize(RequestDTO res) throws ParseErrorException {
		try {
			return codec.encode(RequestDTO.class, res);
		} catch (Exception e) {
			log.error("Serialize object failed", e);
			throw new ParseErrorException("Serialize object error");
		}
	}

	public ProtobufRpcProxy(String url) {
		super(url);
	}

	@Override
	protected String contentType() {
		return NaviProtocol.PROTOBUF.getName();
	}

}
