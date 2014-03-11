package net.neoremind.navi.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.neoremind.navi.codec.Codec;
import net.neoremind.navi.codec.protobuf.ProtobufCodec;
import net.neoremind.navi.server.vo.RequestDTO;

/**
 * Socket sample for communicate with server the leverage navi-rpc
 * 
 * @author Zhang Xu
 */
public class SimpleSocketClientTest {

	private static final byte CR = '\r';
	private static final byte LF = '\n';
	private static final byte[] CRLF = { CR, LF };

	private static String SERVER_IP = "10.48.52.17";
	private static int SERVER_PORT = 8260;

	@SuppressWarnings("unused")
	private static final String PROTCOL_PROTOSTUFF = "application/baidu.protostuff";
	private static final String PROTCOL_PROTOBUF = "application/baidu.protobuf";

	public static void main(String[] args) throws Exception {
		try {

			/**
			 * 1. Construct request DTO
			 */
			RequestDTO request = new RequestDTO();
			request.setId(99999999l);
			request.setMethod("getAll");
			request.setParameters(new Object[] {});

			/**
			 * 2. Prepare to serialize requestDTO to binary data
			 */
			Codec codec = new ProtobufCodec();
			byte[] data = codec.encode(RequestDTO.class, request);
			System.out.println("Post data size:" + data.length);

			/**
			 * 3. Get connect to server
			 */
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);

			OutputStream out = socket.getOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			/**
			 * 4. Write header and body data to connection output stream according to HTTP protocol
			 */
			out.write(buildHeader(data.length));
			out.write(CRLF);
			out.write(data);
			out.write(CRLF);

			/**
			 * 5. Get response from HTTP response
			 */
			String line = "";
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static byte[] buildHeader(int dataLength) {
		StringBuffer sb = new StringBuffer();
		sb.append("POST /service_api/BookMgr HTTP/1.1\r\n");
		sb.append("Host:" + SERVER_IP + "\r\n");
		sb.append("Content-Type:" + PROTCOL_PROTOBUF + "\r\n");
		sb.append("Content-Length:" + dataLength + "\r\n");
		return sb.toString().getBytes();
	}
	
}
