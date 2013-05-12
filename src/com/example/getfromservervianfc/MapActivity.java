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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;

import com.facebook.android.Facebook;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {
	
	static final LatLng BUDAPEST = new LatLng(47.51, 19.09);
	
	public static final String PREF_FILE_NAME = "PrefFile";
	SharedPreferences preferences;
	
	private GoogleMap map;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_map);
        map = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        //set initial position and zoom
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BUDAPEST, 5));
        // sexy factor
        map.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
        
        // create testmarker
        String actPos = preferences.getString("actualPos", "47.5069722222,19.0455777778");
        String coo[] = actPos.split(",");
		LatLng cordTarget = new LatLng(Double.parseDouble(coo[0]), Double.parseDouble(coo[1]));
		
        Marker actual = map.addMarker(new MarkerOptions()
        .position(cordTarget)
        .title("Következõ célpont")
        .snippet("Hajrá!")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.target)));
        
        Long a = preferences.getLong("faceID", 0);
        SendIdentityUtil.sendIdentifyToServer(a, this);
        HttpPost httppost = new HttpPost("http://nfconlab.azurewebsites.net/Home/GetMyCoordinates");
		HttpResponse response;
		try {
			response = HttpClientSingleton.getInstance().execute(httppost);
			String s = EntityUtils.toString(response.getEntity());
			//Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
			
			JSONObject res;
			JSONArray coords;
			try {
				ArrayList<String> locations = new ArrayList<String>();
				//ArrayList<String> points = new ArrayList<String>();
				res = new JSONObject(s);
				// TODO fix this lol
				coords = res.getJSONArray("Coordinates");
				//IDs.add("Helyezés" + "\t" + "ID" + "\t" + "Pontszám");
				for (int i = 0; i < coords.length(); i++) {
					JSONObject p = coords.getJSONObject(i);
					locations.add(p.getString("Location"));
					String location = p.getString("Location");
					String temp[] = location.split(",");
					LatLng cordPrev = new LatLng(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]));
					Marker completed = map.addMarker(new MarkerOptions()
			        .position(cordPrev)
			        .title("Teljesített célpont")
			        .icon(BitmapDescriptorFactory.fromResource(R.drawable.check)));
					//points.add( p.getString("Points"));
					//System.out.println(locations.get(i).toString());
				}
				
				
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map, menu);
        return true;
    }
}
