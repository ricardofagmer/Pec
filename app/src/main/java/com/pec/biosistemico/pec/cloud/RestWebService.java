package com.pec.biosistemico.pec.cloud;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class RestWebService extends Activity{
	
	String URL = "http://open.api.ebay.com/shopping?callname=FindPopularSearches&version=695";  
    String result = "";  
    String deviceId = "xxxxx" ;  
    final String tag = "Your Logcat tag: ";  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		  callWebService("");
		 
		 
		   }
	
	public void callWebService(String serviceEndPoint){  
        HttpClient httpclient = new DefaultHttpClient();  
        HttpGet request = new HttpGet(URL + serviceEndPoint);
        //add the parameters to your request
        request.addHeader("paramname", deviceId);  
        ResponseHandler<String> handler = new BasicResponseHandler();  
        try {  
            result = httpclient.execute(request, handler);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        httpclient.getConnectionManager().shutdown();  
        Log.i(tag, result);  
    } // end callWebService()  
}
