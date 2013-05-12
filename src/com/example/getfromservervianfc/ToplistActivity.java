package com.example.getfromservervianfc;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ToplistActivity extends ListActivity {

	public static final String PREF_FILE_NAME = "PrefFile";
	SharedPreferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});

		Thread tid = new Thread() {
			public void run() {
				Long a = preferences.getLong("faceID", 0);
				SendIdentityUtil.sendIdentifyToServer(a, getApplicationContext());
			}
		};
		tid.run();

		Thread t = new Thread() {
			public void run() {
				HttpPost httppost = new HttpPost("http://nfconlab.azurewebsites.net/Home/GetTopPlayers");
				HttpResponse response;
				try {
					response = HttpClientSingleton.getInstance().execute(httppost);
					String s = EntityUtils.toString(response.getEntity());
					//Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

					JSONObject res;
					JSONArray players;
					try {
						ArrayList<String> IDs = new ArrayList<String>();
						//ArrayList<String> points = new ArrayList<String>();
						res = new JSONObject(s);
						players = res.getJSONArray("players");
						//IDs.add("Helyezés" + "\t" + "ID" + "\t" + "Pontszám");
						for (int i = 0; i < players.length(); i++) {
							JSONObject p = players.getJSONObject(i);
							IDs.add( i+1+ "." +"\t\t" + p.getString("Id") + "\t\t" + p.getString("Points"));
							//points.add( p.getString("Points"));
						}

						setListAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_toplist,IDs));

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
		t.run();
	}
}
