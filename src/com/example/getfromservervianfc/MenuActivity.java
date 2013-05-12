package com.example.getfromservervianfc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MenuActivity extends Activity{

	TextView text_welcome;
	
	public static final String PREF_FILE_NAME = "PrefFile";
	SharedPreferences preferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
		setContentView(R.layout.activity_menu);

		text_welcome = (TextView) findViewById(R.id.welcomeText);
		Button button_question = (Button) findViewById(R.id.button_question);
		Button button_map = (Button) findViewById(R.id.button_map);
		Button button_score = (Button) findViewById(R.id.button_score);
		Button button_toplist = (Button) findViewById(R.id.button_toplist);
		Button button_exit = (Button) findViewById(R.id.button_exit);

		text_welcome.setText("Facebook azonosítás folyamatban...");
		
		button_question.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuActivity.this,
						DisplayQuestionActivity.class);
				startActivity(intent);
			}
				

		});
		
		button_question.setVisibility(View.INVISIBLE);
		
		button_map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this,
						MapActivity.class);
				startActivity(intent);
			}
		});
		
		button_score.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuActivity.this,
						MyScoreActivity.class);
				startActivity(intent);
			}
				

		});

		button_toplist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuActivity.this,
						ToplistActivity.class);
				startActivity(intent);
			}
				

		});
		
		
		
		button_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Session.openActiveSession(this, true, new Session.StatusCallback() {

		      // callback when session changes state
		      @Override
		      public void call(Session session, SessionState state, Exception exception) {
		        if (session.isOpened()) {

		          // make request to the /me API
		          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

		            // callback after Graph API response with user object
		            @Override
		            public void onCompleted(GraphUser user, Response response) {
		              if (user != null) {
		                text_welcome.setText("Üdv, " + user.getFirstName() + "!");
		                long userid= Long.parseLong( user.getId() );	                
		                SendIdentityUtil.sendIdentifyToServer(userid, getApplicationContext());
		                SharedPreferences.Editor editor = preferences.edit();						
						editor.putLong("faceID", userid); 
						editor.commit();
		              }
		            }
		          });
		        }
		      }
		    });
	}
}
