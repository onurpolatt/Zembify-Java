package yazlab;

import org.json.JSONException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.zemberek.erisim.Zemberek;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.DilAyarlari;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PostWords implements Runnable{

	public int sayac=0;
	
	@Override
	public void run() {
			
			
				JSONArray array = new JSONArray();
				JSONObject json=new JSONObject();
			
				if(sayac < JSONParser.kokler.size())
				{
					sayac++;
					HttpClient Client = new DefaultHttpClient();
					String jsonArray = "[";
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				//	nameValuePairs.add(new BasicNameValuePair("id", JSONParser.id));
				//	nameValuePairs.add(new BasicNameValuePair("word", JSONParser.kelimeler[sayac-1]));
					for(int i=0;i<JSONParser.kokler.size();i++){
						if(i==JSONParser.kokler.size()-1){
							jsonArray += "{\"kelime\":\" "+JSONParser.kokler.get(i).toString()+"\"}";
						}else	jsonArray += "{\"kelime\":\""+JSONParser.kokler.get(i).toString()+"\"},";
					}
					jsonArray += "]";
					nameValuePairs.add(new BasicNameValuePair("word",jsonArray));
					System.out.println("Json: "+jsonArray.toString());
					
					try {           

					    String SetServerString = "";

					    HttpClient httpclient = new DefaultHttpClient();
					    HttpPost httppost = new HttpPost("http://46.101.143.104/yazlab/index.php");
					    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
					    ResponseHandler<String> responseHandler = new BasicResponseHandler();

					    SetServerString = httpclient.execute(httppost, responseHandler); 
					    System.out.println("----kokler yollandý------");
					   
						} catch (ClientProtocolException e) {

							e.printStackTrace();
						} catch (IOException e) {

							e.printStackTrace();
						}
					  
					}  
		}
}	

			
		
			
			
		
		
