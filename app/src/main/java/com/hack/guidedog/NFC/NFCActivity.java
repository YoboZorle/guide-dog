package com.hack.guidedog.NFC;

import java.util.Locale;

import com.hack.guidedog.R;
import com.hack.guidedog.R.id;
import com.hack.guidedog.R.layout;
import com.hack.guidedog.R.menu;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class NFCActivity extends Activity {
	private TextView mTextView;
	private NfcAdapter mNfcAdapter;
	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(layout.activity_nfc);

		tts = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {

					@Override
					public void onInit(int status) {
						// TODO Auto-generated method stub
						if (status != TextToSpeech.ERROR) {
							String result = null;
							tts.setLanguage(Locale.US);
							if (mNfcAdapter == null) {
								// Stop here, we definitely need NFC
								result = "This device doesn't support NFC.";
								say(result);
								return;
							}
							if (!mNfcAdapter.isEnabled()) {
								say("NFC is disabled.");
							} else {
								say("NFC is enabled.");
							}
						}
					}
				});

		mTextView = (TextView) findViewById(id.text_nfc);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

	}

	private void say(String result) {
		tts.speak(result, TextToSpeech.QUEUE_FLUSH, null);
		mTextView.setText(result);
		while (tts.isSpeaking()) {
		}
		tts.stop();
		tts.shutdown();
		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (tts != null)
			tts.stop();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}

}
