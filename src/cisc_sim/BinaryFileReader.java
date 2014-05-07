package cisc_sim;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BinaryFileReader {

	public static byte[] toByteArray(String filename) throws FileNotFoundException,
			IOException {
		FileInputStream fs = new FileInputStream(filename);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		byte[] data = new byte[4096];
		int count = fs.read(data);
		while (count != -1) {
			dos.write(data, 0, count);
			count = fs.read(data);
		}
		fs.close();
		baos.close();
		dos.close();
		return baos.toByteArray();
	}
}
