package com.hack.guidedog.utilities;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.KeyEvent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.Locale;

import com.hack.guidedog.R;
import com.hack.guidedog.Speaker;

public class Mp3PlayerActivity extends Activity {
	
	ListView lv;
	
	private Speaker speaker;
	static int position= 0;
	static int size;
	
	private static final String LOG_TAG = "debugger";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mpplayer);
		
		speaker = new Speaker(this);
		
		
		final String dir=Environment.getExternalStorageDirectory().getPath();
	    ArrayList<String> FilesInFolder = GetFiles(dir);
	    lv = (ListView)findViewById(R.id.filelist);

	    lv.setAdapter(new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, FilesInFolder));
	    size=lv.getCount();
	    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            // Clicking on items
	    		String str= (String) lv.getItemAtPosition(position);
	    		openmp3(str, dir);
	         }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public ArrayList<String> GetFiles(String DirectoryPath) {
	    ArrayList<String> MyFiles = new ArrayList<String>();
	    File f = new File(DirectoryPath);

	    f.mkdirs();
	    File[] files = f.listFiles();
	    if (files.length == 0)
	        return null;
	    else {
	        for (int i=0; i<files.length; i++) 
	        	if(fileFormat(files[i], files[i].getName()))
	        		MyFiles.add(files[i].getName());
	    }

	    return MyFiles;
	}
	
	 public boolean fileFormat(File dir, String name)  
     {  
             return ((name.endsWith(".mp3")));
     }  
	 
	 public void openmp3(String filename, String dir){
		 Intent intent= new Intent();
		 intent.setAction(Intent.ACTION_VIEW);
		 File file=new File(dir+"/"+filename);
		 intent.setDataAndType(Uri.fromFile(file), "audio/*");
		 startActivity(intent);
	 }

	 
	 public void readFile(){
		 
	 }
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		 String str;
		 switch(keyCode){

		 case KeyEvent.KEYCODE_VOLUME_DOWN:
			 if(position<size)
			 {
				 position++;
				 str= (String) lv.getItemAtPosition(position);
				 str=str.substring(0,str.indexOf(".mp3")+1);
				 speaker.speak(str);
				 Log.i(LOG_TAG, "DOWN");
			 }
			 
			 break;

		 case KeyEvent.KEYCODE_VOLUME_UP:

			 if(position!=0)
			 {
				 position--;
				 System.out.println("Enter 2");
				 Log.i(LOG_TAG, "TWO");
				 str= (String) lv.getItemAtPosition(position);
				 str=str.substring(0,str.indexOf(".mp3")+1);
				 speaker.speak(str);
			 }
			 
			 break;



		 }

		 return super.onKeyDown(keyCode, event);
	 }
	 
	 
	 @Override
		protected void onPause() {
			speaker.stop();
			super.onPause();
		}

		@Override
		protected void onDestroy() {
			speaker.shutdown();
			super.onDestroy();
		}

	 
	
}
