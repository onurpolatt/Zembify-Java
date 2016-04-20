package yazlab;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class PostId implements Runnable {

	@Override
	public void run() {
		
		        HttpClient Client = new DefaultHttpClient();

                List<NameValuePair> nameValuePairss = new ArrayList<NameValuePair>(1);
				nameValuePairss.add(new BasicNameValuePair("id", JSONParser.id));
				try {           

				    String SetServerString = "";

				    HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost("http://46.101.143.104/yazlab/index.php");
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairss,"UTF-8"));
				    ResponseHandler<String> responseHandlerr = new BasicResponseHandler();

				    SetServerString = httpclient.execute(httppost, responseHandlerr); 
				    System.out.println("paragraf kontrol edildi");

				}  catch(Exception ex) {
				    ex.printStackTrace();
				}
		
	
		}
}
