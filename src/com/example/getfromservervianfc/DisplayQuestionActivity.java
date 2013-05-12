package com.example.getfromservervianfc;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayQuestionActivity extends Activity {
	
	private TextView ques;
	private Button answ1;
	private Button answ2;
	private Button answ3;
	private Button answ4;
	private ImageView im;
	public static final String PREF_FILE_NAME = "PrefFile";
	SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_display_question);
        
        ques = (TextView) findViewById(R.id.ATextView);
        answ1 = (Button ) findViewById(R.id.Abutton1);
		answ2 = (Button ) findViewById(R.id.Abutton2);
		answ3 = (Button ) findViewById(R.id.Abutton3);
		answ4 = (Button ) findViewById(R.id.Abutton4);
		im = (ImageView) findViewById(R.id.AimageView);
		im.setVisibility(View.INVISIBLE);
		
		ques.setText(preferences.getString("actualQuestion", "Nincs aktuális kérdés!"));
		answ1.setText(preferences.getString("actualAnswer1", ""));
		answ2.setText(preferences.getString("actualAnswer2", ""));
		answ3.setText(preferences.getString("actualAnswer3", ""));
		answ4.setText(preferences.getString("actualAnswer4", ""));
		
		
		try {
			String imageURL = preferences.getString("actualImageUrl", "");
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
			im.setImageBitmap(bitmap); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_display_question, menu);
        return true;
    }
}
