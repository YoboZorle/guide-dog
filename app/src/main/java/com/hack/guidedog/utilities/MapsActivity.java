package com.hack.guidedog.utilities;




import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hack.guidedog.*;



import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MapsActivity extends Activity {

	
	private TextToSpeech tts;
	private Vibrator vibrator;	
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	private static final String TAG_STATUS = "status";
	private static final String Log_Tag="df";
	
	private EditText from, dest;
	private class LongOperation extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Hashmap for ListView
			//ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
			try {
				// Creating JSON Parser instance
				JSONParser jParser = new JSONParser();

				// getting JSON string from URL
				JSONObject json = jParser.getJSONFromUrl(params[0]);
				return json;

			} catch(Exception ex) { // many diffent exceptions better handle seperatelly
				Log.e("error", "error", ex);
				return null;
			}

		}      
	}




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		from = (EditText) findViewById(R.id.from);
		dest = (EditText) findViewById(R.id.destination);

		tts = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {

					@Override
					public void onInit(int status) {
						// TODO Auto-generated method stub
						if (status != TextToSpeech.ERROR) {
							tts.setLanguage(Locale.US);						
							
						}
					}
				});
		
		findViewById(R.id.image).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						vibrate();
						speak();
						
						
					}
				});
		
		

	}
	//String ins=status.getString("html_instructions");
	public String GetDirections(String origin,String Destination){
		
			
		String url = "http://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+Destination+"&region=in&sensor=false";
		
		JSONObject json=new LongOperation().doInBackground(url);
		Log.i(Log_Tag, "dib done");
		String inst="";
		try{
			JSONObject route=json.getJSONArray("routes").getJSONObject(0);
			Log.i(Log_Tag, "routes done");
			JSONObject leg=route.getJSONArray("legs").getJSONObject(0);	
			Log.i(Log_Tag, "legs done");

			JSONArray steps=leg.getJSONArray("steps");
			for(int i =0 ;i <steps.length();i++){
				Log.i(Log_Tag, "steps done");
				String test=steps.getJSONObject(i).getString("html_instructions");
				Log.i(Log_Tag, "html ins done");
				//TextView v1=(TextView) findViewById(R.id.textView1);
				inst=inst+test;
				//v1.setText(Html.fromHtml(inst));
				
			}
			return Html.fromHtml(inst).toString();
		}
		catch(JSONException e){
			//TextView v1=(TextView) findViewById(R.id.textView1);
			//v1.setText("Error");
			return "";
		}
	}

	//	TextView v1=(TextView) findViewById(R.id.textView1);
	//v1.setText("Testing");



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void checkVoiceRecognition() {
		// Check if voice recognition is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			
			Toast.makeText(this, "Voice recognizer not present",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

			if (resultCode == RESULT_OK) {

				ArrayList<String> textMatchList = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				if (!textMatchList.isEmpty()) {					
						
						String s = textMatchList.toString();
						s = s.substring(1, s.length() - 1);
						
						String[] words = s.split(" to ");
						
						try
						{
							from.setText(words[0]);
							dest.setText(words[1]);
							
							words[0] = words[0].replaceAll(" ", "%20");
							words[1] = words[1].replaceAll(" ", "%20");
							
							String directions = GetDirections(words[0],words[1]);
							System.out.println("Result : "+ directions);
							tts.setSpeechRate(0.8f);
							tts.speak(
									("Directions are as follows " + directions),
									TextToSpeech.QUEUE_FLUSH,
									null);
						}
						catch (Exception e)
						{
							
						}
						
						

				}
				// Result code for various error.
			} else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR) {
				showToastMessage("Audio Error");
			} else if (resultCode == RecognizerIntent.RESULT_CLIENT_ERROR) {
				showToastMessage("Client Error");
			} else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR) {
				showToastMessage("Network Error");
			} else if (resultCode == RecognizerIntent.RESULT_NO_MATCH) {
				showToastMessage("No Match");
			} else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR) {
				showToastMessage("Server Error");
			}
		super.onActivityResult(requestCode, resultCode, data);
	}
	void showToastMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	public void speak() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

		int noOfMatches = 1;

		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (tts != null) {
			tts.stop();
			// tts.shutdown();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (tts != null)
			tts.shutdown();
		super.onDestroy();
	}
	
	private void vibrate() {
		vibrator.vibrate(50);
	}
	

}
