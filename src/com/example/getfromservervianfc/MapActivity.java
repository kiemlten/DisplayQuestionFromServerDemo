package com.example.getfromservervianfc;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {
	
	static final LatLng BUDAPEST = new LatLng(47.51, 19.09);
	
	private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        map = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        
        //set initial position and zoom
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BUDAPEST, 5));
        // sexy factor
        map.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
        
        // create testmarker
        Marker kiel = map.addMarker(new MarkerOptions()
        .position(BUDAPEST)
        .title("Következõ célpont")
        .snippet("Hajrá!"));
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map, menu);
        return true;
    }
}
