package net.neoremind.navi.util;

import java.io.IOException;
import java.io.InputStream;

public class ByteUtil {

	public static byte[] readStream(InputStream input, int length) throws IOException {
		byte[] bytes = new byte[length];
		int offset = 0;
		while (offset < bytes.length) {
			int bytesRead = input.read(bytes, offset, bytes.length - offset);
			if (bytesRead == -1)
				break; // end of stream
			offset += bytesRead;
		}
		return bytes;
	}
	
}
