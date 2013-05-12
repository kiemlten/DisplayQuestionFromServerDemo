package com.example.getfromservervianfc;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class SendIdentityUtil {
	
	private SendIdentityUtil() {}

	public static void sendIdentifyToServer(long userid, Context c)  {
		// ID
			try {
				HttpPost httppost = new HttpPost("http://nfconlab.azurewebsites.net/Home/Identify");
					
				// generates random int between 1 and 1000
				//Random random = new Random();
				//int userid = random.nextInt(1000-1+1)+1;
				
				JSONObject json = new JSONObject();
				json.put("UserID", userid);

				Date d = new Date();
				json.put("Date", d);

				StringEntity se = new StringEntity(json.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				httppost.setEntity(se);
				HttpResponse response = HttpClientSingleton.getInstance().execute(httppost);
				String s = EntityUtils.toString(response.getEntity());
				Log.d("identify", s);
				if (s.equals("User identification complete")) {
					Toast.makeText(c.getApplicationContext(), "Sikeres felhasználói azonosítás, id: "+userid, Toast.LENGTH_LONG).show();
				} else if (s.equals("New user added")) {
					Toast.makeText(c.getApplicationContext(), "Új felhasználó létrehozva, id: "+userid, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(c.getApplicationContext(), "Sikertelen felhasználói azonosítás", Toast.LENGTH_LONG).show();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(c.getApplicationContext(), "IOException", Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
	}
