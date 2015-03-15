package com.hack.guidedog.ocr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.hack.guidedog.R;
import com.hack.guidedog.face.FaceDetectionActivity;

public class OCRActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		findViewById(R.id.textBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent mainIntent = new Intent(OCRActivity.this,
						CameraShot.class);
				startActivity(mainIntent);

			}
		});

		findViewById(R.id.faceBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent mainIntent = new Intent(OCRActivity.this,
						FaceDetectionActivity.class);
				startActivity(mainIntent);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ocr, menu);
		return true;
	}

}
