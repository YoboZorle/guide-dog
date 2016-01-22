package com.hack.guidedog.msg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hack.guidedog.R;
import com.hack.guidedog.Speaker;

public class MessageActivity extends Activity {

	private Speaker speaker;
	private Vibrator vibrator;

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
	private boolean flag1 = true;

	private EditText input;
	private EditText inputNumber;
	private EditText inputMessage;
	private ImageView buttonSpeak;
	private ImageView buttonBack;
	private ImageView buttonEnter;
	private ImageView buttonOne;
	private ImageView buttonTwo;
	private ImageView buttonThree;
	private ImageView buttonFour;
	private ImageView buttonFive;
	private ImageView buttonSix;
	private ImageView buttonSeven;
	private ImageView buttonEight;
	private ImageView buttonNine;
	private ImageView buttonAss;
	private ImageView buttonZero;
	private ImageView buttonHash;

	private static boolean timer;
	private static int count[];
	private static String textInput;
	private static int keyPressed;
	private static String[] pattern = { "1.@,!", "2abc", "3def", "4ghi",
			"5jkl", "6mno", "7pqrs", "8tuv", "9wxyz", "", "0 " };

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			vibrate();
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.buttonSpeak:
				// speak textInput
				buttonSpeak.setImageResource(R.drawable.speak2);
				if (input == inputNumber) {
					char[] in = input.getText().toString().toCharArray();
					for (char c : in)
						speaker.speak("" + c);
				} else {
					speaker.speak(input.getText().toString());
				}

				break;
			case R.id.buttonBack:
				buttonBack.setImageResource(R.drawable.back2);
				if (textInput.length() != 0)
					textInput = textInput.substring(0, textInput.length() - 1);
				input.setText(textInput);
				break;
			case R.id.buttonEnter:
				buttonEnter.setImageResource(R.drawable.enter2);
				textInput = "";
				if (input == inputNumber)
					input = inputMessage;
				else
					sendMessage(inputNumber.getText().toString(), inputMessage
							.getText().toString());

				break;
			case R.id.buttonOne:
				buttonOne.setImageResource(R.drawable.one1);
				keyPressed = 0;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonTwo:
				buttonTwo.setImageResource(R.drawable.two2);
				keyPressed = 1;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonThree:
				buttonThree.setImageResource(R.drawable.three3);
				keyPressed = 2;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonFour:
				buttonFour.setImageResource(R.drawable.four4);
				keyPressed = 3;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonFive:
				buttonFive.setImageResource(R.drawable.five5);
				keyPressed = 4;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonSix:
				buttonSix.setImageResource(R.drawable.six6);
				keyPressed = 5;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonSeven:
				buttonSeven.setImageResource(R.drawable.seven7);
				keyPressed = 6;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonEight:
				buttonEight.setImageResource(R.drawable.eight8);
				keyPressed = 7;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonNine:
				buttonNine.setImageResource(R.drawable.nine9);
				keyPressed = 8;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonAss:
				buttonAss.setImageResource(R.drawable.ass2);
				textInput = textInput + "*";
				speaker.speak("*");
				input.setText(textInput);
				break;
			case R.id.buttonZero:
				buttonZero.setImageResource(R.drawable.zero0);
				speaker.speak("0");
				textInput = textInput + "0";
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

	public void check() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				update_pattern();
			}
		}, 1000);
	}

	public void update_pattern() {
		timer = false;
		try {
			speaker.speak(""
					+ pattern[keyPressed].charAt(count[keyPressed] - 1));
			textInput = textInput
					+ pattern[keyPressed].charAt(count[keyPressed] - 1);

		} catch (Exception e) {
			// voice for pattern[keyPressed].charAt(pattern[keyPressed].length()
			// - 1)
			textInput = textInput
					+ pattern[keyPressed]
							.charAt(pattern[keyPressed].length() - 1);
		}

		count = new int[11];
		input.setText(textInput);
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_message);

		speaker = new Speaker(this);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		count = new int[11];
		timer = false;
		textInput = "";

		inputNumber = (EditText) findViewById(R.id.textView1);
		inputNumber.setText(textInput);

		inputMessage = (EditText) findViewById(R.id.textView2);
		inputMessage.setText(textInput);

		input = inputNumber;

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

		buttonZero.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// voice for space
				textInput = textInput + " ";
				input.setText(textInput);
				return true;
			}
		});

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

	private void sendMessage(String phoneNumber, String message) {
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				MessageActivity.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

	private void vibrate() {
		vibrator.vibrate(50);
	}

}
