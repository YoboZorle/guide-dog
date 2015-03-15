package com.hack.guidedog.call;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hack.guidedog.R;
import com.hack.guidedog.Speaker;

public class PhoneActivity extends Activity {

	EditText input;

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
	private boolean flag1 = true;

	private Speaker speaker;
	private Vibrator vibrator;

	public static String textInput;

	ImageView buttonSpeak;
	ImageView buttonBack;
	ImageView buttonEnter;
	ImageView buttonOne;
	ImageView buttonTwo;
	ImageView buttonThree;
	ImageView buttonFour;
	ImageView buttonFive;
	ImageView buttonSix;
	ImageView buttonSeven;
	ImageView buttonEight;
	ImageView buttonNine;
	ImageView buttonAss;
	ImageView buttonZero;
	ImageView buttonHash;

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.buttonSpeak:
				// speak textInput
				buttonSpeak.setImageResource(R.drawable.speak2);
				char[] in = input.getText().toString().toCharArray();
				for (char c : in)
					speaker.speak("" + c);
				break;
			case R.id.buttonBack:
				buttonBack.setImageResource(R.drawable.back2);
				if (textInput.length() != 0)
					textInput = textInput.substring(0, textInput.length() - 1);
				input.setText(textInput);
				break;
			case R.id.buttonEnter:
				buttonEnter.setImageResource(R.drawable.enter2);
				makeCall(input.getText().toString());
				break;
			case R.id.buttonOne:
				buttonOne.setImageResource(R.drawable.one1);
				textInput = textInput + "1";
				speaker.speak("1");
				input.setText(textInput);
				break;
			case R.id.buttonTwo:
				buttonTwo.setImageResource(R.drawable.two2);
				textInput = textInput + "2";
				speaker.speak("2");
				input.setText(textInput);
				break;
			case R.id.buttonThree:
				buttonThree.setImageResource(R.drawable.three3);
				textInput = textInput + "3";
				speaker.speak("3");
				input.setText(textInput);
				break;
			case R.id.buttonFour:
				buttonFour.setImageResource(R.drawable.four4);
				textInput = textInput + "4";
				speaker.speak("4");
				input.setText(textInput);
				break;
			case R.id.buttonFive:
				buttonFive.setImageResource(R.drawable.five5);
				textInput = textInput + "5";
				speaker.speak("5");
				input.setText(textInput);
				break;
			case R.id.buttonSix:
				buttonSix.setImageResource(R.drawable.six6);
				textInput = textInput + "6";
				speaker.speak("6");
				input.setText(textInput);
				break;
			case R.id.buttonSeven:
				buttonSeven.setImageResource(R.drawable.seven7);
				textInput = textInput + "7";
				speaker.speak("7");
				input.setText(textInput);
				break;
			case R.id.buttonEight:
				buttonEight.setImageResource(R.drawable.eight8);
				textInput = textInput + "8";
				speaker.speak("8");
				input.setText(textInput);
				break;
			case R.id.buttonNine:
				buttonNine.setImageResource(R.drawable.nine9);
				textInput = textInput + "9";
				speaker.speak("9");
				input.setText(textInput);
				break;
			case R.id.buttonAss:
				buttonAss.setImageResource(R.drawable.ass2);
				textInput = textInput + "*";
				speaker.speak("*");
				input.setText(textInput);
				break;
			case R.id.buttonZero:
				buttonZero.setImageResource(R.drawable.zero0);
				textInput = textInput + "0";
				speaker.speak("0");
				input.setText(textInput);
				break;
			case R.id.buttonHash:
				buttonHash.setImageResource(R.drawable.hash2);
				textInput = textInput + "#";
				speaker.speak("#");
				input.setText(textInput);
				break;

			}
			change_images();

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_phone);
		textInput = "";

		speaker = new Speaker(this);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		input = (EditText) findViewById(R.id.textView1);
		input.setText(textInput);

		buttonSpeak = (ImageView) findViewById(R.id.buttonSpeak);
		buttonBack = (ImageView) findViewById(R.id.buttonBack);
		buttonEnter = (ImageView) findViewById(R.id.buttonEnter);
		buttonOne = (ImageView) findViewById(R.id.buttonOne);
		buttonTwo = (ImageView) findViewById(R.id.buttonTwo);
		buttonThree = (ImageView) findViewById(R.id.buttonThree);
		buttonFour = (ImageView) findViewById(R.id.buttonFour);
		buttonFive = (ImageView) findViewById(R.id.buttonFive);
		buttonSix = (ImageView) findViewById(R.id.buttonSix);
		buttonSeven = (ImageView) findViewById(R.id.buttonSeven);
		buttonEight = (ImageView) findViewById(R.id.buttonEight);
		buttonNine = (ImageView) findViewById(R.id.buttonNine);
		buttonAss = (ImageView) findViewById(R.id.buttonAss);
		buttonZero = (ImageView) findViewById(R.id.buttonZero);
		buttonHash = (ImageView) findViewById(R.id.buttonHash);

		buttonSpeak.setOnClickListener(listener);
		buttonBack.setOnClickListener(listener);
		buttonEnter.setOnClickListener(listener);

		buttonOne.setOnClickListener(listener);
		buttonTwo.setOnClickListener(listener);
		buttonThree.setOnClickListener(listener);

		buttonFour.setOnClickListener(listener);
		buttonFive.setOnClickListener(listener);
		buttonSix.setOnClickListener(listener);

		buttonSeven.setOnClickListener(listener);
		buttonEight.setOnClickListener(listener);
		buttonNine.setOnClickListener(listener);

		buttonAss.setOnClickListener(listener);
		buttonZero.setOnClickListener(listener);
		buttonHash.setOnClickListener(listener);

		buttonSpeak.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (flag1)
					speak();
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

			if (resultCode == RESULT_OK) {

				ArrayList<String> textMatchList = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				if (!textMatchList.isEmpty()) {

					input.setText(input.getText() + " " + textMatchList.get(0));

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

	public void checkVoiceRecognition() {
		// Check if voice recognition is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			flag1 = false;
			Toast.makeText(this, "Voice recognizer not present",
					Toast.LENGTH_SHORT).show();
		}
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
		speaker.stop();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		speaker.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phone, menu);
		return true;
	}

	public void makeCall(String number) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + number));
		startActivity(callIntent);
		finish();
	}

	public void change_images() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				change_pics();
			}
		}, 100);
	}

	public void change_pics() {

		buttonSpeak.setImageResource(R.drawable.speak);
		buttonBack.setImageResource(R.drawable.back);
		buttonEnter.setImageResource(R.drawable.enter);

		buttonOne.setImageResource(R.drawable.one);
		buttonTwo.setImageResource(R.drawable.two);
		buttonThree.setImageResource(R.drawable.three);

		buttonFour.setImageResource(R.drawable.four);
		buttonFive.setImageResource(R.drawable.five);
		buttonSix.setImageResource(R.drawable.six);

		buttonSeven.setImageResource(R.drawable.seven);
		buttonEight.setImageResource(R.drawable.eight);
		buttonNine.setImageResource(R.drawable.nine);

		buttonAss.setImageResource(R.drawable.ass);
		buttonZero.setImageResource(R.drawable.zero);
		buttonHash.setImageResource(R.drawable.hash);
	}

	private void vibrate() {
		vibrator.vibrate(50);
	}

}