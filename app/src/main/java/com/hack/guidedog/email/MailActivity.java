package com.hack.guidedog.email;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Session;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hack.guidedog.R;
import com.hack.guidedog.SearchResults;
import com.hack.guidedog.Speaker;

public class MailActivity extends Activity {

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

	Session session = null;
	ProgressDialog pdialog = null;
	Context context = null;
	EditText reciept = null;
	EditText sub = null;
	EditText msg = null;
	String recpient = null;
	String subject = null;
	String textmessage = null;
	private boolean flag1 = true;

	private Speaker speaker;
	private Vibrator vibrator;

	private EditText input;
	private EditText inputAddress;
	private EditText inputSubject;
	private EditText inputMessage;
	private ImageView buttonSpeak;
	private ImageView buttonBack;
	private ImageView buttonEnter;
	private ImageView buttonOne;
	private ImageView buttonTwo;
	private ImageView buttonThree;
	private ImageView buttonFour;
	private ImageView buttonFive;
	private ImageView buttonSix;
	private ImageView buttonSeven;
	private ImageView buttonEight;
	private ImageView buttonNine;
	private ImageView buttonAss;
	private ImageView buttonZero;
	private ImageView buttonHash;

	private static boolean timer;
	private static int count[];
	private static String textInput;
	private static int keyPressed;
	private static String[] pattern = { "1.@,!", "2abc", "3def", "4ghi",
			"5jkl", "6mno", "7pqrs", "8tuv", "9wxyz", "", "0 " };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_mail);

		context = this;
		reciept = (EditText) findViewById(R.id.editText_to);
		sub = (EditText) findViewById(R.id.editText_sub);
		msg = (EditText) findViewById(R.id.editText_text);

		speaker = new Speaker(this);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		count = new int[11];
		timer = false;
		textInput = "";

		inputAddress = (EditText) findViewById(R.id.editText_to);
		inputAddress.setText(textInput);

		inputMessage = (EditText) findViewById(R.id.editText_text);
		inputMessage.setText(textInput);

		inputSubject = (EditText) findViewById(R.id.editText_sub);
		inputSubject.setText(textInput);

		input = inputAddress;

		buttonSpeak = (ImageView) findViewById(R.id.buttonSpeak);
		buttonBack = (ImageView) findViewById(R.id.buttonBack);
		buttonEnter = (ImageView) findViewById(R.id.buttonEnter);
		buttonOne = (ImageView) findViewById(R.id.buttonOne);
		buttonTwo = (ImageView) findViewById(R.id.buttonTwo);
		buttonThree = (ImageView) findViewById(R.id.buttonThree);
		buttonFour = (ImageView) findViewById(R.id.buttonFour);
		buttonFive = (ImageView) findViewById(R.id.buttonFive);
		buttonSix = (ImageView) findViewById(R.id.buttonSix);
		buttonSeven = (ImageView) findViewById(R.id.buttonSeven);
		buttonEight = (ImageView) findViewById(R.id.buttonEight);
		buttonNine = (ImageView) findViewById(R.id.buttonNine);
		buttonAss = (ImageView) findViewById(R.id.buttonAss);
		buttonZero = (ImageView) findViewById(R.id.buttonZero);
		buttonHash = (ImageView) findViewById(R.id.buttonHash);

		buttonSpeak.setOnClickListener(listener);
		buttonBack.setOnClickListener(listener);
		buttonEnter.setOnClickListener(listener);

		buttonOne.setOnClickListener(listener);
		buttonTwo.setOnClickListener(listener);
		buttonThree.setOnClickListener(listener);

		buttonFour.setOnClickListener(listener);
		buttonFive.setOnClickListener(listener);
		buttonSix.setOnClickListener(listener);

		buttonSeven.setOnClickListener(listener);
		buttonEight.setOnClickListener(listener);
		buttonNine.setOnClickListener(listener);

		buttonAss.setOnClickListener(listener);
		buttonZero.setOnClickListener(listener);
		buttonHash.setOnClickListener(listener);

		buttonZero.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// voice for space
				textInput = textInput + " ";
				input.setText(textInput);
				return true;
			}
		});

		buttonSpeak.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (flag1)
					speak();
				return true;
			}
		});

	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			vibrate();
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.buttonSpeak:
				// speak textInput
				buttonSpeak.setImageResource(R.drawable.speak2);
				speaker.speak(input.getText().toString());
				break;
			case R.id.buttonBack:
				buttonBack.setImageResource(R.drawable.back2);
				if (textInput.length() != 0)
					textInput = textInput.substring(0, textInput.length() - 1);
				input.setText(textInput);
				break;
			case R.id.buttonEnter:
				buttonEnter.setImageResource(R.drawable.enter2);
				textInput = "";
				if (input == inputAddress)
					input = inputSubject;
				else if (input == inputSubject)
					input = inputMessage;
				else if (input == inputMessage)
					send();
				break;
			case R.id.buttonOne:
				buttonOne.setImageResource(R.drawable.one1);
				keyPressed = 0;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonTwo:
				buttonTwo.setImageResource(R.drawable.two2);
				keyPressed = 1;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonThree:
				buttonThree.setImageResource(R.drawable.three3);
				keyPressed = 2;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonFour:
				buttonFour.setImageResource(R.drawable.four4);
				keyPressed = 3;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonFive:
				buttonFive.setImageResource(R.drawable.five5);
				keyPressed = 4;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonSix:
				buttonSix.setImageResource(R.drawable.six6);
				keyPressed = 5;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonSeven:
				buttonSeven.setImageResource(R.drawable.seven7);
				keyPressed = 6;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonEight:
				buttonEight.setImageResource(R.drawable.eight8);
				keyPressed = 7;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonNine:
				buttonNine.setImageResource(R.drawable.nine9);
				keyPressed = 8;
				if (!timer) {
					timer = true;
					check();
				}
				count[keyPressed]++;
				break;
			case R.id.buttonAss:
				buttonAss.setImageResource(R.drawable.ass2);
				textInput = textInput + "*";
				speaker.speak("*");
				input.setText(textInput);
				break;
			case R.id.buttonZero:
				buttonZero.setImageResource(R.drawable.zero0);
				speaker.speak("0");
				textInput = textInput + "0";
				input.setText(textInput);
				break;
			case R.id.buttonHash:
				buttonHash.setImageResource(R.drawable.hash2);
				textInput = textInput + "#";
				speaker.speak("#");
				input.setText(textInput);
				break;

			}
			change_images();

		}
	};

	public void check() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				update_pattern();
			}
		}, 1000);
	}

	public void update_pattern() {
		timer = false;
		try {
			speaker.speak(""
					+ pattern[keyPressed].charAt(count[keyPressed] - 1));
			textInput = textInput
					+ pattern[keyPressed].charAt(count[keyPressed] - 1);

		} catch (Exception e) {
			// voice for pattern[keyPressed].charAt(pattern[keyPressed].length()
			// - 1)
			textInput = textInput
					+ pattern[keyPressed]
							.charAt(pattern[keyPressed].length() - 1);
		}

		count = new int[11];
		input.setText(textInput);
	}

	public void change_images() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				change_pics();
			}
		}, 100);
	}

	public void change_pics() {
		buttonSpeak.setImageResource(R.drawable.speak);
		buttonBack.setImageResource(R.drawable.back);
		buttonEnter.setImageResource(R.drawable.enter);

		buttonOne.setImageResource(R.drawable.one);
		buttonTwo.setImageResource(R.drawable.two);
		buttonThree.setImageResource(R.drawable.three);

		buttonFour.setImageResource(R.drawable.four);
		buttonFive.setImageResource(R.drawable.five);
		buttonSix.setImageResource(R.drawable.six);

		buttonSeven.setImageResource(R.drawable.seven);
		buttonEight.setImageResource(R.drawable.eight);
		buttonNine.setImageResource(R.drawable.nine);

		buttonAss.setImageResource(R.drawable.ass);
		buttonZero.setImageResource(R.drawable.zero);
		buttonHash.setImageResource(R.drawable.hash);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

			if (resultCode == RESULT_OK) {

				ArrayList<String> textMatchList = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				if (!textMatchList.isEmpty()) {

					input.setText(input.getText() + " " + textMatchList.get(0));

				}
				// Result code for various error.
			} else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR) {
				showToastMessage("Audio Error");
			} else if (resultCode == RecognizerIntent.RESULT_CLIENT_ERROR) {
				showToastMessage("Client Error");
			} else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR) {
				showToastMessage("Network Error");
			} else if (resultCode == RecognizerIntent.RESULT_NO_MATCH) {
				showToastMessage("No Match");
			} else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR) {
				showToastMessage("Server Error");
			}
		super.onActivityResult(requestCode, resultCode, data);
	}

	void showToastMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	public void checkVoiceRecognition() {
		// Check if voice recognition is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			flag1 = false;
			Toast.makeText(this, "Voice recognizer not present",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void speak() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

		int noOfMatches = 1;

		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mail, menu);
		return true;
	}

	private void send() {
		// TODO Auto-generated method stub
		GMailSender sender = new GMailSender("username@gmail.com", "password");
		try {
			sender.sendMail(inputSubject.getText().toString(), inputMessage
					.getText().toString(), "sahaj.horus@gmail.com",
					inputAddress.getText().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// recpient= reciept.getText().toString();
		// subject= sub.getText().toString();
		// textmessage= msg.getText().toString();
		//
		// Properties props = new Properties();
		// props.put("mail.smtp.host", "smtp.gmail.com");
		// props.put("mail.smtp.socketFactory.port", "465");
		// props.put("mail.smtp.socketFactory.class",
		// "javax.net.ssl.SSLSocketFactory");
		// props.put("mail.smtp.auth", "true");
		// props.put("mail.smtp.port", "465");
		//
		// session = Session.getDefaultInstance(props, new
		// javax.mail.Authenticator() {
		// protected PasswordAuthentication getPasswordAuthentication() {
		// return new PasswordAuthentication("sahaj.horus@gmail.com",
		// "domovoi0109");
		// }
		// });
		// // pdialog = ProgressDialog.show(context, "",
		// "Sending Mail...",true);
		// // RetreiveFeedTask task= new RetreiveFeedTask();
		// // task.execute();
		//
		// Message message = new MimeMessage(session);
		// try {
		// message.setFrom(new InternetAddress("sahaj.horus@gmail.com"));
		// } catch (AddressException e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// } catch (MessagingException e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }
		// try {
		// message.setRecipients(Message.RecipientType.TO,
		// InternetAddress.parse(recpient));
		// } catch (AddressException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// } catch (MessagingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// try {
		// message.setSubject(subject);
		// } catch (MessagingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// message.setContent(textmessage, "text/html; charset=utf-8");
		// } catch (MessagingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// Transport.send(message);
		// } catch (MessagingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	// class RetreiveFeedTask extends AsyncTask<Void,Void,Void> {
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// try {
	// System.out.println("try");
	// Message message = new MimeMessage(session);
	// message.setFrom(new InternetAddress("sahaj.horus@gmail.com"));
	// message.setRecipients(Message.RecipientType.TO,
	// InternetAddress.parse(recpient));
	// message.setSubject(subject);
	// message.setContent(textmessage, "text/html; charset=utf-8");
	//
	// Transport.send(message);
	//
	//
	// }
	// catch (MessagingException e) {
	// e.printStackTrace();
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	//
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// pdialog.dismiss();
	// reciept.setText("");
	// msg.setText("");
	// sub.setText("");
	// speaker.speak("mail sent");
	// Toast.makeText(getApplicationContext(), "Message sent",
	// Toast.LENGTH_LONG).show();
	// }
	// }

	private void vibrate() {
		vibrator.vibrate(50);
	}

}
