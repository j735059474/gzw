package nc.scap.pub.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {
	
	public static String post(String registryUrl, String parameters, String charset) throws IOException {

        StringBuilder str = new StringBuilder();

        URL url = new URL(registryUrl);
        URLConnection uc = url.openConnection();
        uc.setDoOutput(true);
        uc.setUseCaches(false);
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        uc.setRequestProperty("Content-Length", Long.toString(parameters.length()));

        HttpURLConnection hc = (HttpURLConnection) uc;
        hc.setRequestMethod("POST");

        OutputStream os = null;
        DataOutputStream dos = null;
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            os = hc.getOutputStream();
            dos = new DataOutputStream(os);
            dos.writeBytes(parameters);
            dos.flush();
            is = hc.getInputStream();
            reader = new InputStreamReader(is, charset);
            int i;
            while ((i = reader.read()) != -1) {
                str.append((char) i);
            }
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (Exception e) {
            }
            if (is != null) try {
                is.close();
            } catch (Exception e) {
            }
            if (os != null) try {
                os.close();
            } catch (Exception e) {
            }
            if (dos != null) try {
                dos.close();
            } catch (Exception e) {
            }
        }

        return str.toString();
    }
}
