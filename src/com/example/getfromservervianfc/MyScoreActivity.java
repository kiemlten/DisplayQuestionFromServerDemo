package com.example.getfromservervianfc;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyScoreActivity extends Activity {
	
	private TextView text_score;
	private TextView text_pos;
	private Button button_back;
	
	public static final String PREF_FILE_NAME = "PrefFile";
	SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_my_score);
        
        text_score = (TextView) findViewById(R.id.uscore);
        text_pos = (TextView) findViewById(R.id.upos);
        button_back = (Button) findViewById(R.id.button_back);
        
        text_score.setText("Pillanat...");
        text_pos.setText("Pillanat...");
     
		button_back.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			finish();
		}			
		});
		
		Long a = preferences.getLong("faceID", 0);
        SendIdentityUtil.sendIdentifyToServer(a, this);
		Thread t = new Thread() {
			public void run() {
				HttpPost httppost = new HttpPost("http://nfconlab.azurewebsites.net/Home/GetMyPoints");
				HttpResponse response;
				try {
					response = HttpClientSingleton.getInstance().execute(httppost);
					String s = EntityUtils.toString(response.getEntity());
					//Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
					
					JSONObject res;
					try {
						res = new JSONObject(s);
						// TODO fix this lol
						final String point = res.getString("Points");
						final String pos = res.getString("Position");

						runOnUiThread(new Runnable() {
						     public void run() {

						    	text_score.setText("  "+point);
								text_pos.setText("  "+pos);

						    }
						});
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		
    }


}
