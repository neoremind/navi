package com.baidu.beidou.sample.simpletest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.baidu.beidou.navi.codec.Codec;
import com.baidu.beidou.navi.codec.protobuf.ProtobufCodec;
import com.baidu.beidou.navi.server.vo.RequestDTO;
import com.baidu.beidou.navi.util.MethodUtil;

/**
 * Socket sample for communicate with server the leverage navi-rpc
 * 
 * @author Zhang Xu
 */
public class SimpleSocketClientTest {

    private static final String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 8080;

    private static final String SERVICE_NAME = "CompanyMgr";
    private static final String METHOD_NAME = "getAll";
    private static final Object[] PARAMETERS = new Object[] {};

    // TODO read block
    public static void main(String[] args) throws Exception {
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            System.out.println(String.format("Start to call %s.%s() on %s", SERVICE_NAME,
                    METHOD_NAME, SERVER_IP + ":" + SERVER_PORT));

            long start = System.currentTimeMillis();

            /**
             * 1. Construct request DTO
             */
            RequestDTO request = new RequestDTO();
            request.setTraceId(99999999l);
            request.setMethod(METHOD_NAME);
            request.setParameters(PARAMETERS);
            request.setParamterTypes(MethodUtil.getArgsTypeNameArray(new Class<?>[] {}));

            /**
             * 2. Prepare to serialize requestDTO to binary data
             */
            Codec codec = new ProtobufCodec();
            byte[] data = codec.encode(RequestDTO.class, request);
            System.out.println("Send data serialization finish. Byte size:" + data.length);

            /**
             * 3. Get connect to server
             */
            socket = new Socket(SERVER_IP, SERVER_PORT);
            socket.setKeepAlive(false);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(2000);

            /**
             * 4. Write header and body data to connection output stream according to HTTP protocol
             */
            out = socket.getOutputStream();
            out.write(buildHeader(data.length));
            out.write(CRLF);
            out.write(data);
            out.flush();
            // socket.shutdownOutput();

            System.out.println("Send data ok");

            /**
             * 5. Get response from HTTP response
             */
            System.out.println("Start to get reponse...");
            in = socket.getInputStream();

            byte[] responseByte = readStream(in, socket);
            long end = System.currentTimeMillis();
            System.out.println("Receive byte size is " + responseByte.length);

            /**
             * 6. Deserialize binary data to Object
             */
            // TODO process http response header and body
            String rawData = new String(responseByte);
            System.out.println(rawData);

            System.out.println("Cost time: " + (end - start) + "ms");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static final byte CR = '\r';
    private static final byte LF = '\n';
    private static final byte[] CRLF = { CR, LF };

    @SuppressWarnings("unused")
    private static final String PROTCOL_PROTOSTUFF = "application/baidu.protostuff";
    private static final String PROTCOL_PROTOBUF = "application/baidu.protobuf";

    private static byte[] readStream(InputStream inStream, Socket socket) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            System.out.println("Get byte size:" + len);
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    private static byte[] buildHeader(int dataLength) {
        StringBuffer sb = new StringBuffer();
        sb.append("POST /service_api/" + SERVICE_NAME + " HTTP/1.1\r\n");
        sb.append("Host:" + SERVER_IP + "\r\n");
        sb.append("Content-Type:" + PROTCOL_PROTOBUF + "\r\n");
        sb.append("Content-Length:" + dataLength + "\r\n");
        return sb.toString().getBytes();
    }

}
