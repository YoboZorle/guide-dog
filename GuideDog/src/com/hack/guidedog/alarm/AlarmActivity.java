package com.hack.guidedog.alarm;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.hack.guidedog.R;

public class AlarmActivity extends Activity {

	private int time[] = { 0, 0, 0 };
	private int ctr = 0;
	private TextToSpeech tts;
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_alarm);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		tts = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {

					@Override
					public void onInit(int status) {
						// TODO Auto-generated method stub
						if (status != TextToSpeech.ERROR) {
							tts.setLanguage(Locale.US);
							findViewById(R.id.image).setOnClickListener(
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											vibrate();
											ctr++;
											if (ctr == 1) {
												tts.speak(
														"Please set minutes",
														TextToSpeech.QUEUE_FLUSH,
														null);
											} else if (ctr == 2) {
												tts.speak(
														"Please set am or pm",
														TextToSpeech.QUEUE_FLUSH,
														null);
											} else {
												set_alarm();
											}
										}
									});
						}

					}
				});
	}

	private void set_alarm() {
		Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);

		if (time[2] == 2)
			time[0] += 12;

		i.putExtra(AlarmClock.EXTRA_HOUR, time[0]);
		i.putExtra(AlarmClock.EXTRA_MINUTES, time[1]);
		startActivity(i);
		finish();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP: {

			time[ctr]++;
			if (ctr == 0) {
				time[ctr] %= 13;
				if (time[ctr] == 0)
					time[ctr] = 1;
				tts.speak(String.valueOf(time[ctr]), TextToSpeech.QUEUE_FLUSH,
						null);
			} else if (ctr == 1) {
				time[ctr] += 4;
				time[ctr] %= 60;
				tts.speak(String.valueOf(time[ctr]), TextToSpeech.QUEUE_FLUSH,
						null);
			} else if (ctr == 2) {
				time[ctr] %= 3;
				if (time[ctr] == 0)
					time[ctr] = 1;
				if (time[ctr] == 1)
					tts.speak("a m", TextToSpeech.QUEUE_FLUSH, null);
				else
					tts.speak("p m", TextToSpeech.QUEUE_FLUSH, null);
			}
			// tts.speak("up", TextToSpeech.QUEUE_FLUSH, null);
			return true;
		}
		case KeyEvent.KEYCODE_VOLUME_DOWN: {

			time[ctr]--;
			if (ctr == 0) {
				if (time[ctr] == 0)
					time[ctr] = 12;
				tts.speak(String.valueOf(time[ctr]), TextToSpeech.QUEUE_FLUSH,
						null);
			} else if (ctr == 1) {
				time[ctr] -= 4;
				if (time[ctr] == 0) {
					time[ctr] = 60;
					tts.speak("0", TextToSpeech.QUEUE_FLUSH, null);
				} else {
					tts.speak(String.valueOf(time[ctr]),
							TextToSpeech.QUEUE_FLUSH, null);

				}
			} else if (ctr == 2) {
				if (time[ctr] == 0)
					time[ctr] = 2;
				tts.speak(String.valueOf(time[ctr]), TextToSpeech.QUEUE_FLUSH,
						null);
			}

			// tts.speak("down", TextToSpeech.QUEUE_FLUSH, null);
			return true;
		}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}

	private void vibrate() {
		vibrator.vibrate(50);
	}

}
