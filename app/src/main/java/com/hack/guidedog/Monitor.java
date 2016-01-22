package com.hack.guidedog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.sax.StartElementListener;
import android.widget.Toast;

public class Monitor extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			context.startService(new Intent(context, MainService.class));
			System.out.println("Done");
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			System.out.println("Action_Screen_On");
			Intent i = new Intent();
			i.setClassName("com.hack.guidedog",
					"com.hack.guidedog.MainActivity");
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			System.out.println("Action_Screen_Off");
		}
	}

}
