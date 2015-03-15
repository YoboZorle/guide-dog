package com.hack.guidedog;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class Speaker implements TextToSpeech.OnInitListener {
	private TextToSpeech tts;
	private Context context;

	public Speaker(Context c) {
		context = c;
		tts = new TextToSpeech(context, this);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status != TextToSpeech.ERROR)
			tts.setLanguage(Locale.US);
	}

	public void stop() {
		if (tts != null)
			tts.stop();
	}

	public void shutdown() {
		if (tts != null)
			tts.shutdown();
	}

	public void speak(String text) {
		tts.speak(text, TextToSpeech.QUEUE_ADD, null);
	}

}
