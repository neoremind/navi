package net.neoremind.navi.server.handler.protobuf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoremind.navi.codec.Codec;
import net.neoremind.navi.codec.protobuf.ProtobufCodec;
import net.neoremind.navi.exception.rpc.ParseErrorException;
import net.neoremind.navi.server.handler.NaviRpcHandlerBase;
import net.neoremind.navi.server.vo.RequestDTO;
import net.neoremind.navi.server.vo.ResponseDTO;

/**
 * Protobuf ser/deser handler
 * 
 * @author Zhang Xu
 */
public class ProtobufRpcHandler extends NaviRpcHandlerBase {
	
	private final static Logger log = LoggerFactory.getLogger(ProtobufRpcHandler.class); 

	private static final Codec codec = new ProtobufCodec();

	@Override
	protected RequestDTO deserialize(byte[] req) throws ParseErrorException {
		try {
			return codec.decode(RequestDTO.class, req);
		} catch (Exception e) {
			log.error("Deserialize byte failed", e);
			throw new ParseErrorException("Deserialize byte error");
		}
	}

	@Override
	protected byte[] serialize(ResponseDTO res) throws ParseErrorException {
		try {
			return codec.encode(ResponseDTO.class, res);
		} catch (Exception e) {
			log.error("Serialize object failed", e);
			throw new ParseErrorException("Serialize object error");
		}
	}

}
