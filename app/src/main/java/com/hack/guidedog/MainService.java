package com.hack.guidedog;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

public class MainService extends NoStopIntentService {
	public MainService() {
		super("MainService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver receiver = new Monitor();
		registerReceiver(receiver, filter);
		System.out.println("service done");
	}

}
