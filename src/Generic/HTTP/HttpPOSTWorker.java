package Generic.HTTP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class HttpPOSTWorker {
	private HttpConnection hc = null;
	private String contentType = "application/x-www-form-urlencoded";
	private static HttpPOSTWorker instance = null;
	
	private HttpPOSTWorker() {}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void connect(final String url, final byte[] message, final HttpPOSTCallback callback) {
		Thread t = new Thread() {
			public void run() {
				DataOutputStream out = null;
				DataInputStream in = null;
				byte[] data = null;
				try {
					hc = (HttpConnection) Connector.open(url, Connector.READ_WRITE);
					hc.setRequestMethod(HttpConnection.POST);
					hc.setRequestProperty("Content-Type", contentType);
					out = hc.openDataOutputStream();
					out.write(message);
					in = hc.openDataInputStream();
					int length = (int) hc.getLength();
					data = new byte[length];
					in.read(data);
				} catch (IOException e) {
						e.printStackTrace();
				}
				callback.handleHttpPOSTRespone(data);
			}
		};
		t.start();
	}
	
	public void close() {
		if(hc != null)
			try {
				hc.close();
			} catch (IOException e) {}
		hc = null;
	}

	public static HttpPOSTWorker getWorker() {
		if(instance == null)
			instance = new HttpPOSTWorker();
		return instance;
	}
	
	
}
