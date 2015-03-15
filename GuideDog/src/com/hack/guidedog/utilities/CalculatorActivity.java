package com.hack.guidedog.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.hack.guidedog.R;
import com.hack.guidedog.SearchResults;
import com.hack.guidedog.NFC.NFCActivity;
import com.hack.guidedog.alarm.AlarmActivity;
import com.hack.guidedog.call.PhoneActivity;
import com.hack.guidedog.email.MailActivity;
import com.hack.guidedog.msg.MessageActivity;
import com.hack.guidedog.ocr.CameraShot;
import com.hack.guidedog.settings.SettingsActivity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class CalculatorActivity extends Activity{
	
	private TextToSpeech tts;
	private Vibrator vibrator;	
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static String calcString = "";
	private EditText displayString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_calculator);
		
		displayString = (EditText) findViewById(R.id.equation);
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
						System.out.println("CALULATOR RESULT : "+ s);
						displayString.setText(s);
										
							calcString = evaluateString(s);
							
							showToastMessage("Result : " + calcString);
							tts.speak(
									("Result is " + calcString),
									TextToSpeech.QUEUE_FLUSH,
									null);
						

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
	
	public String evaluateString(String input)
	{
		try
		{
			input = input.replaceAll("divide", "/");
			input = input.replaceAll("into", "*");
			input = input.replaceAll("minus", "-");
			input = input.replaceAll("plus", "/");
			input = input.replaceAll(" ", "");
			input="0"+input+"+";
			double arr[]=new double[100];
			char op[]=new char[100];
			String temp="";
			int a=0,b=0;
			for(int i=0;i<input.length();i++)
			{
				char ch=input.charAt(i);
				if(ch!='+'&&ch!='-'&&ch!='*'&&ch!='/')
				   temp=temp+ch;
				else
				{
					arr[a++]=Double.parseDouble(temp);
					op[b++]=ch;
					temp="";
				}
			}
			b--;
			char operators[]={'/','*','-','+'};
			for(int i=0;i<4;i++)
			{
				for(int j=0;j<b;j++)
				{
					if(op[j]==operators[i])
					{
						if(i==0)
						   arr[j]=1.0*arr[j]/arr[j+1];
						else if(i==1)
						   arr[j]=1.0*arr[j]*arr[j+1];
						else if(i==2)
						   arr[j]=arr[j]-arr[j+1];
						else if(i==3)
						   arr[j]=arr[j]+arr[j+1];
						
						a--;
						b--; 
						 
						for(int k=j+1;k<a;k++)
						   arr[k]=arr[k+1];
						for(int k=j;k<b;k++)
						   op[k]=op[k+1];
						
						j--;
					}
				}
			}
			return String.valueOf(arr[0]);
		}
		catch(Exception e)
		{
			return "";
		}
		
	}
}
