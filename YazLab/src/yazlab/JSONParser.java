package yazlab;

import org.json.JSONException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.zemberek.araclar.turkce.YaziBirimi;
import net.zemberek.araclar.turkce.YaziBirimiTipi;
import net.zemberek.araclar.turkce.YaziIsleyici;
import net.zemberek.erisim.Zemberek;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.DilAyarlari;
import net.zemberek.yapi.KelimeTipi;

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
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JSONParser implements Runnable{
	static Zemberek zemberek=new Zemberek(new TurkiyeTurkcesi());
	static List<YaziBirimi> analizDizisi;
	String url="http://46.101.143.104/yazlab/request.php?type=paragraf";
	static String icerik="";
	static String id="";
	static String response="";
	static ArrayList<String> kokler;
	static ArrayList<String> kelime;
	static ArrayList<String> yeniKelimeler;
	static String[] kelimeler;
	static int sayac=0;
	@Override
	public void run() {
		sayac++;
		
			
					String jsonData="";
					HttpClient client = new DefaultHttpClient();
			        HttpGet httpGet = new HttpGet(url);
			        	
			        try {
			            HttpResponse execute = client.execute(httpGet);
			            InputStream content = execute.getEntity().getContent();
			            BufferedReader buffer = new BufferedReader(
			                    new InputStreamReader(content,Charset.forName("UTF-8")),8);
			            String s = "";
			            while ((s = buffer.readLine()) != null) {
			                jsonData += s;
			            }
			            System.out.println("JSON DATA:"+jsonData);

			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			        try {
			        	if(!jsonData.contains("response")){
				        	JSONArray jsonArray = new JSONArray(jsonData);
				        	JSONObject json = jsonArray.getJSONObject(0);
				            System.out.println("RESPONSE ÝCERDE"+response);
				           	icerik=json.getString("icerik");
				           	id=json.getString("id");
				           	System.out.println("Ýcerik:"+json.getString("icerik"));
				            analizDizisi = YaziIsleyici.analizDizisiOlustur(icerik);
				           	response="true";
			        	}else{
			        		response="false";
			        	}
						
						
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
		
		}
						
			

	public static String[] kelimeAyristir(String icerik){
		icerik = icerik.replaceAll("[!?,]", "");
		kelimeler = icerik.split("\\s+");
	
		return kelimeler;
	}
	
	public static String[]KelimeleriAyýr(String icerik){
		kelime=new ArrayList<String>();	
		
		for (int i = 0; i < (analizDizisi.size()); i++) {
			if(analizDizisi.get(i).tip==YaziBirimiTipi.KELIME){				
					kelime.add(analizDizisi.get(i).icerik);
		            System.out.println(analizDizisi.get(i).icerik+"----"+(analizDizisi.size()));
				}   		
		}				
		return kelimeler;
	}
	
	public static ArrayList<String> fiilleriAyýr(){
		yeniKelimeler=new ArrayList<String>();
		
		for(int i=0;i<kelime.size();i++){
			if(zemberek.kelimeDenetle(kelime.get(i))){
				if(zemberek.kelimeCozumle(kelime.get(i))[0].kok().tip() != KelimeTipi.FIIL && zemberek.kelimeCozumle(kelime.get(i))[0].kok().tip() == KelimeTipi.ISIM){
					yeniKelimeler.add(kelime.get(i));	
				}				
			}			
		}	
		return yeniKelimeler;
	}
	
	public static ArrayList<String> kokBul(){
		Zemberek zemberek=new Zemberek(new TurkiyeTurkcesi());
		
		kokler=new ArrayList<String>(yeniKelimeler.size());

		
			for(int i=0;i<yeniKelimeler.size();i++){
					if(zemberek.kelimeDenetle(yeniKelimeler.get(i))){	
						if((zemberek.kelimeCozumle(yeniKelimeler.get(i))[0].kok().icerik()!=yeniKelimeler.get(i)) && 
								   !(zemberek.kelimeCozumle(yeniKelimeler.get(i))[0].ekZinciriStr().toString().contains("ISIM_DONUSUM_LES")
							   	   || zemberek.kelimeCozumle(yeniKelimeler.get(i))[0].ekZinciriStr().toString().contains("ISIM_BULUNMA_LI")
							   	   || zemberek.kelimeCozumle(yeniKelimeler.get(i))[0].ekZinciriStr().toString().contains("ISIM_ILGI_CI")
							   	   || zemberek.kelimeCozumle(yeniKelimeler.get(i))[0].ekZinciriStr().toString().contains("ISIM_YOKLUK_SIZ")) ){
								kokler.add(zemberek.kelimeCozumle(yeniKelimeler.get(i))[0].kok().icerik());							   	   		   
								System.out.println(zemberek.kelimeCozumle(yeniKelimeler.get(i))[0].ekZinciriStr());
						}
						else{						
							kokler.add(yeniKelimeler.get(i));
						}
					}
					else{						
						kokler.add(yeniKelimeler.get(i));
					}				
			}
		return kokler;
	}
		

	public static void main(String[] args) throws JSONException, InterruptedException{
		
		 
		  Zemberek zm=new Zemberek(new TurkiyeTurkcesi());
		  CozumlemeSeviyesi strateji=CozumlemeSeviyesi.TUM_KOK_VE_EKLER;
		  String kok="";
		  
		
		  Timer t = new Timer( );
		  t.scheduleAtFixedRate(new TimerTask() {

		      @Override
		      public void run() {
		    	  
		        System.out.println("Hey");
		        Thread t1=new Thread(new JSONParser());
		        
		        t1.start();
		        try {
					t1.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        System.out.println("RESPONSE"+response);
		        if(response!="false"){
		        	KelimeleriAyýr(icerik);
		        	fiilleriAyýr();
					  for(int j=0;j<yeniKelimeler.size();j++){
						  System.out.println(yeniKelimeler.get(j));
					  }
				  System.out.println("-----------------");
				  kokBul();
					  for(int k=0;k<kokler.size();k++){
						 // System.out.println(kokler.get(k));
						  System.out.println(kokler.get(k));
						  
					  }
				  Thread t2=new Thread(new PostWords());
				  if(!t2.isAlive()){
					  t2.start();
					  try {
						t2.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				  }
				  Thread t3=new Thread(new PostId());
				  if(!t3.isAlive()){
					  t3.start();
					  try {
						t3.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				  }
				  
		        }
		        
		      }
		  }, 0,2000);
		  
		  System.out.println(icerik);

	}

	
}
