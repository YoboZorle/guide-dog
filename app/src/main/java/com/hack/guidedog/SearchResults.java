package com.hack.guidedog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAImage;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.speech.tts.TextToSpeech.OnInitListener;

public class SearchResults extends Activity {

	String input;
	EditText text;
	ImageView search;
	ArrayList<String> title = new ArrayList<String>();
	ArrayList<String> urls = new ArrayList<String>();
	TextView[] tv = new TextView[10];
	ImageView[] im = new ImageView[10];
	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results);
		Bundle extras = getIntent().getExtras();
		input = extras.getString("SearchText");

		tts = new TextToSpeech(this, new OnInitListener() {

			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if (status != TextToSpeech.ERROR) {
					tts.setLanguage(Locale.US);
					doStuff();
				}
			}
		});
	}

	/*
	 * Intent checkIntent = new Intent();
	 * checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	 * startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	 * 
	 * 
	 * tts = new TextToSpeech(getApplicationContext(), new
	 * TextToSpeech.OnInitListener() {
	 * 
	 * @Override public void onInit(int status) { // TODO Auto-generated method
	 * stub if(status != TextToSpeech.ERROR){ tts.setLanguage(Locale.US); }
	 * 
	 * } });
	 */
	private void doStuff() {
		tv[0] = (TextView) findViewById(R.id.textView1);
		tv[1] = (TextView) findViewById(R.id.textView2);
		tv[2] = (TextView) findViewById(R.id.textView3);
		tv[3] = (TextView) findViewById(R.id.textView4);
		tv[4] = (TextView) findViewById(R.id.textView5);
		tv[5] = (TextView) findViewById(R.id.textView6);
		tv[6] = (TextView) findViewById(R.id.textView7);
		tv[7] = (TextView) findViewById(R.id.textView8);
		tv[8] = (TextView) findViewById(R.id.textView9);
		tv[9] = (TextView) findViewById(R.id.textView10);

		im[0] = (ImageView) findViewById(R.id.imageView1);
		im[1] = (ImageView) findViewById(R.id.imageView2);
		im[2] = (ImageView) findViewById(R.id.imageView3);
		im[3] = (ImageView) findViewById(R.id.imageView4);
		im[4] = (ImageView) findViewById(R.id.imageView5);
		im[5] = (ImageView) findViewById(R.id.imageView6);
		im[6] = (ImageView) findViewById(R.id.imageView7);
		im[7] = (ImageView) findViewById(R.id.imageView8);
		im[8] = (ImageView) findViewById(R.id.imageView9);
		im[9] = (ImageView) findViewById(R.id.imageView10);

		setData();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		title.clear();
		urls.clear();
		wolfram_alpha();
		setData();

		// text=(EditText) findViewById(R.id.editText1);
		// search=(ImageView) findViewById(R.id.search);

		/*
		 * search.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { title.clear(); urls.clear();
		 * wolfram_alpha(); setData();
		 * 
		 * }});
		 */
	}

	@Override
	protected void onPause() {
		if (tts != null)
			tts.stop();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (tts != null)
			tts.shutdown();
		super.onDestroy();
	}

	public void setData() {
		int i = 0;
		int size = title.size();
		for (i = 0; i < Math.min(10, size); i++)
			tv[i].setText(title.get(i));
		for (; i < 10; i++)
			tv[i].setText("");
		i = 0;
		size = urls.size();
		for (i = 0; i < Math.min(10, size); i++) {
			im[i].setImageBitmap(getBitmapFromURL(urls.get(i)));
		}
		for (; i < 10; i++)
			im[i].setImageBitmap(null);
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			Log.e("src", src);
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			Log.e("Bitmap", "returned");
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Exception", e.getMessage());
			return null;
		}
	}

	public void wolfram_alpha() {

		String str = "";
		String appid = "TP26T8-WWRY7KHYPR";
		// String input = "microsoft ceo";

		// input = text.getText().toString();

		// if (args.length > 0)
		// input = args[0];

		// The WAEngine is a factory for creating WAQuery objects,
		// and it also used to perform those queries. You can set properties of
		// the WAEngine (such as the desired API output format types) that will
		// be inherited by all WAQuery objects created from it. Most
		// applications
		// will only need to crete one WAEngine object, which is used throughout
		// the life of the application.
		WAEngine engine = new WAEngine();

		// These properties will be set in all the WAQuery objects created from
		// this WAEngine.
		engine.setAppID(appid);
		engine.addFormat("plaintext,image");

		// Create the query.
		WAQuery query = engine.createQuery();

		// Set properties of the query.
		query.setInput(input);

		try {
			// For educational purposes, print out the URL we are about to send:
			System.out.println("Query URL:");
			System.out.println(engine.toURL(query));
			System.out.println("");

			// This sends the URL to the Wolfram|Alpha server, gets the XML
			// result
			// and parses it into an object hierarchy held by the WAQueryResult
			// object.
			WAQueryResult queryResult = engine.performQuery(query);

			if (queryResult.isError()) {
				System.out.println("Query error");
				System.out.println("  error code: "
						+ queryResult.getErrorCode());
				System.out.println("  error message: "
						+ queryResult.getErrorMessage());
			} else if (!queryResult.isSuccess()) {
				System.out
						.println("Query was not understood; no results available.");
			} else {
				// Got a result.
				System.out.println("Successful query. Pods follow:\n");
				for (WAPod pod : queryResult.getPods()) {
					if (!pod.isError()) {
						// System.out.println(pod.getTitle());

						title.add(pod.getTitle());
						// System.out.println("------------");
						for (WASubpod subpod : pod.getSubpods()) {
							for (Object element : subpod.getContents()) {
								if (element instanceof WAPlainText) {
									// System.out.println(((WAPlainText)
									// element).getText());
									// System.out.println("");
									str = str
											+ ((WAPlainText) element).getText()
											+ "\n";

								}
								if (element instanceof WAImage) {
									String u = ((WAImage) element).getURL();
									urls.add(u);

								}
							}
						}
						// System.out.println("");
					}
				}
				System.out.println(str);
				tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
				// We ignored many other types of Wolfram|Alpha output, such as
				// warnings, assumptions, etc.
				// These can be obtained by methods of WAQueryResult or objects
				// deeper in the hierarchy.
			}
		} catch (WAException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
