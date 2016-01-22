package com.hack.guidedog.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.hack.guidedog.R;



public class SettingsActivity extends Activity {

	public static String PREF_NAME = "myPrefs";
	public static final String EMAIL_KEY = "email";
	public static final String PASSWORD_KEY = "password";
	public static final String EMERGENCY_CONTACT_KEY = "contact";
	public static final String PANIC_KEY = "panic";
	private EditText email;
	private EditText password;
	private EditText emergency;
	private EditText preset;
	private SharedPreferences.Editor editor;
	private String currentString;
	boolean flag;
	boolean check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		currentString = "";
		editor = prefs.edit();
		flag = false;
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		emergency = (EditText) findViewById(R.id.emergency);
		preset = (EditText) findViewById(R.id.preset);

		email.setText(prefs.getString(EMAIL_KEY, ""));
		password.setText(prefs.getString(PASSWORD_KEY, ""));
		emergency.setText(prefs.getString(EMERGENCY_CONTACT_KEY, ""));
		if (prefs.getString(PANIC_KEY, "").equals(""))
			preset.setText("1100");
		else
			preset.setText(prefs.getString(PANIC_KEY, ""));

		findViewById(R.id.startRecord).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						flag = true;
					}
				});

		findViewById(R.id.saveBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save();
			}
		});
	}

	private void save() {
		editor.putString(EMAIL_KEY, email.getText().toString());
		editor.putString(PASSWORD_KEY, password.getText().toString());
		editor.putString(EMERGENCY_CONTACT_KEY, emergency.getText().toString());
		editor.putString(PANIC_KEY, preset.getText().toString());
		editor.commit();
		finish();
	}

	public void timer_check() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				check = false;
				check_entered_pattern();
			}
		}, 3000);
	}

	public void check_entered_pattern() {
		System.out.println(currentString);
		System.out.println("preset    " + preset);

		editor.putString(PANIC_KEY, currentString);
		preset.setText(currentString);
		currentString = "";
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (flag)
			switch (keyCode) {

			case KeyEvent.KEYCODE_VOLUME_UP:
				if (!check) {
					check = true;
					currentString = "";
					timer_check();
				}
				System.out.println("UP");
				currentString = currentString + "1";
				break;

			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (!check) {
					check = true;
					currentString = "";
					timer_check();
				}
				System.out.println("DOWN");
				currentString = currentString + "0";
				break;
			}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
