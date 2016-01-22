package com.hack.guidedog.msg;
import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;

import com.hack.guidedog.R;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class MessageActivity1 extends Activity  implements OnInitListener {
	
	ImageView b1;
	ImageView b2;
	ImageView b3;
	ImageView b4;
	ImageView b5;
	ImageView b6;
	ImageView ok;
	TextView text;
	private Vibrator vibrator;
    boolean arr[]=new boolean[6];
    boolean flag=false;
    boolean alpha=true;
    String alphabets[][]={{"100000","A"},{"101000","B"},{"110000","C"},{"110100","D"},{"100100","E"},
    		{"111000","F"},{"111100","G"},{"101100","H"},{"011000","I"},{"011100","J"},
    		{"100010","K"},{"101010","L"},{"110010","M"},{"110110","N"},{"100110","O"},
    		{"111010","P"},{"111110","Q"},{"101110","R"},{"011010","S"},{"011110","T"},
    		{"100011","U"},{"101011","V"},{"011101","W"},{"110011","X"},{"110111","Y"},
    		{"100111","Z"},{"010010"," "},{"100001","\b"}};
    String numbers[][]={{"010111","#"},{"011100","0"},{"100000","1"},{"101000","2"},{"110000","3"},
            {"110100","4"},{"100100","5"},{"111000","6"},{"111100","7"},{"101100","8"},
            {"011000","9"},{"010010"," "},{"100001","\b"}};
    
    TextToSpeech tts;
    String ch="";
    private TextToSpeech myTTS;	
    private int MY_DATA_CHECK_CODE = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone1);
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		ch="number mode";
		alpha = false;
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
		
		b1=(ImageView) findViewById(R.id.button1);
		b2=(ImageView) findViewById(R.id.button2);
		b3=(ImageView) findViewById(R.id.button3);
		b4=(ImageView) findViewById(R.id.button4);
		b5=(ImageView) findViewById(R.id.button5);
		b6=(ImageView) findViewById(R.id.button6);
		ok=(ImageView) findViewById(R.id.button7);
		
		ok.setImageResource(R.drawable.msg);
		
		text=(TextView) findViewById(R.id.textView1);
		text.setText("");
		b1.setOnClickListener(new OnClickListener() 
		{        	
            @Override
            public void onClick(View v) { 
            	arr[0]=true;
            	b1.setClickable(false);
            	b1.setImageResource(R.drawable.braille2);
            	if(!flag)
            	{
            		flag=true;
            		check();
            	}
				
        }});
		b2.setOnClickListener(new OnClickListener() 
		{        	
            @Override
            public void onClick(View v) {    	
            	arr[1]=true;
            	b2.setClickable(false);
            	b2.setImageResource(R.drawable.braille2);
            	if(!flag)
            	{
            		flag=true;
            		check();
            	}
				
        }});
		b3.setOnClickListener(new OnClickListener() 
		{        	
            @Override
            public void onClick(View v) {    	
            	arr[2]=true;
            	b3.setClickable(false);
            	b3.setImageResource(R.drawable.braille2);
            	if(!flag)
            	{
            		flag=true;
            		check();
            	}
        }});
		b4.setOnClickListener(new OnClickListener() 
		{        	
            @Override
            public void onClick(View v) {    	
            	arr[3]=true;
            	b4.setClickable(false);
            	b4.setImageResource(R.drawable.braille2);
            	if(!flag)
            	{
            		flag=true;
            		check();
            	}
        }});
		b5.setOnClickListener(new OnClickListener() 
		{        	
            @Override
            public void onClick(View v) {    	
            	arr[4]=true;
            	b5.setClickable(false);
            	b5.setImageResource(R.drawable.braille2);
            	if(!flag)
            	{
            		flag=true;
            		check();
            	}
        }});
		b6.setOnClickListener(new OnClickListener() 
		{        	
            @Override
            public void onClick(View v) {    	
            	arr[5]=true;
            	b6.setClickable(false);
            	b6.setImageResource(R.drawable.braille2);
            	if(!flag)
            	{
            		flag=true;
            		check();
            	}
        }});
		ok.setOnClickListener(new OnClickListener() 
		{        	
			
            @Override
            public void onClick(View v) {   
            	vibrate();
            	ok.setImageResource(R.drawable.msg2);
            	read_out();
            	ok.setImageResource(R.drawable.msg);
        }});
		ok.setOnLongClickListener(new OnLongClickListener() {

	        @Override
	        public boolean onLongClick(View v) {
	        	vibrate();
	        	ok.setImageResource(R.drawable.msg2);
	        	perform_action();
	        	ok.setImageResource(R.drawable.msg);
	        	return true;
	           
	        }
	    });
	}
	public void perform_action()
	{
		String str=(String)text.getText();
		String words[]=str.split(" ");
		
			SmsManager smsManager = SmsManager.getDefault();
			String content="";
			for(int i=1;i<words.length;i++)
				content=content+" "+ words[i];
			smsManager.sendTextMessage(words[0], null, content, null, null);
		
				
	}
	public void read_out()
	{
		String s=(String)text.getText();
		String spacedDigit = "";
		String[] words = s.split(" ");
		
		try
		{
			for (int i = 0; i < words[0].length(); i++)
			{
				spacedDigit = spacedDigit + s.charAt(i) + " ";			
			}
			for (int i = 1; i < words.length; i++)
				spacedDigit = spacedDigit + " "+ words[i];
		}
		catch (Exception e)
		{
			
		}
		
		speakWords(spacedDigit);
	}
	public void check()
	{
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                  get_pattern();
               }
        }, 1500);
	}
    public void get_pattern()
    {
    	String temp="";
    	ch="";
    	for(int i=0;i<6;i++)
    	{
    		if(arr[i])
    			temp=temp+"1";
    		else
    			temp=temp+"0";
    		
    	}
    	reset_all();
    	if(temp.equals("111111"))
    	{
    		if(alpha)
    			ch="number mode";
    		else
    			ch="alpha mode";
    		alpha=!alpha;
    		speakWords(ch);
    		return;
    	}
    	String str=(String)text.getText();
    	if(temp.equals("100001"))
    	{
    		str=str.substring(0,str.length()-1);
    		ch="backspace";
    	}
    	else if(alpha)
    	{
    		for(int i=alphabets.length-1;i>=0;i--)
    		{	
    			if(temp.equals(alphabets[i][0]))
    			{
    				str=str+alphabets[i][1];
    				ch=alphabets[i][1];
    				if(temp.equals("010010"))
    					ch="space";
    			}
    		}
        			
    	}
    	else
    	{
    		for(int i=numbers.length-1;i>=0;i--)
    		{	
    			if(temp.equals(numbers[i][0]))
    			{
    				str=str+numbers[i][1];
    				ch=numbers[i][1];
    				if(temp.equals("010010"))
    					ch="space";
    			}
    		}
        			
    	}
    	if(!(ch.equals("")))
    		speakWords(ch);
    	text.setText(str);
   
    
    }
    public void reset_all()
    {
    	b1.setClickable(true);
    	b2.setClickable(true);
    	b3.setClickable(true);
    	b4.setClickable(true);
    	b5.setClickable(true);
    	b6.setClickable(true);
    	
    	
    	b1.setImageResource(R.drawable.braille);
    	b2.setImageResource(R.drawable.braille);
    	b3.setImageResource(R.drawable.braille);
    	b4.setImageResource(R.drawable.braille);
    	b5.setImageResource(R.drawable.braille);
    	b6.setImageResource(R.drawable.braille);
    	
    	ok.setImageResource(R.drawable.msg);
    	
    	flag=false;
    	for(int i=0;i<6;i++)
    		arr[i]=false;
    }
    private void speakWords(String speech) {
		//speak straight away
    	myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
}
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				//the user has the necessary data - create the TTS
			myTTS = new TextToSpeech(this, this);
			}
			else {
					//no data - install it now
				Intent installTTSIntent = new Intent();
				installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}
	}
		//setup TTS
	public void onInit(int initStatus) {
			//check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
				myTTS.setLanguage(Locale.UK);
		}
		else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
		}
	}
    
   
	private void vibrate() {
		vibrator.vibrate(50);
	}

}
