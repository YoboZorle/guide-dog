package com.hack.guidedog;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.hack.guidedog.NFC.NFCActivity;
import com.hack.guidedog.alarm.AlarmActivity;
import com.hack.guidedog.call.PhoneActivity;
import com.hack.guidedog.call.PhoneActivity1;
import com.hack.guidedog.email.MailActivity;
import com.hack.guidedog.info.LocationWeather;
import com.hack.guidedog.msg.MessageActivity;
import com.hack.guidedog.msg.MessageActivity1;
import com.hack.guidedog.settings.SettingsActivity;
import com.hack.guidedog.utilities.UtilityActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SensorEventListener {

    private boolean check = false;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mLocationClient;
    private Vibrator vibrator;
    private Speaker speaker;
    private LocationWeather locationWeather;
    private GestureDetector detector;

    private StringBuilder code;
    private String currentString;

    private String preset;
    private String number;

    private ImageView central;
    private ImageView call;
    private ImageView msg;
    private ImageView mail;
    private ImageView nfc;
    private ImageView not;
    private ImageView ocr;
    private ImageView alarm;
    private ImageView settings;

    private static int WIDTH;
    private static int HEIGHT;

    private static float X_MAX;
    private static float X_MIN;

    private static float Y_MAX;
    private static float Y_MIN;

    private boolean flag;
    private boolean flag1;
    private boolean flag2;

    // for sensor accelerometer
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;

    private GestureDetector.OnGestureListener listener = new OnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            System.out.println("single up");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
            System.out.println("show");

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            System.out.println("scroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
            System.out.println("Long Press");
            vibrate();
            flag2 = false;
            if (checkCenter(e.getX(), e.getY()) && flag1)
                speak();

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            System.out.println("on fling");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            System.out.println("on down");
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (checkPlayServices()) {
            mLocationClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        speaker = new Speaker(this);

        code = new StringBuilder();
        currentString = "";
        SharedPreferences prefs = getSharedPreferences(
                SettingsActivity.PREF_NAME, MODE_PRIVATE);
        number = prefs.getString(SettingsActivity.EMERGENCY_CONTACT_KEY, "");
        System.out.println("CALL NUMBER " + number);
        preset = prefs.getString(SettingsActivity.PANIC_KEY, "");

        detector = new GestureDetector(this, listener);
        detector.setIsLongpressEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WIDTH = metrics.widthPixels;
        HEIGHT = metrics.heightPixels;

        // X_MAX = (float)WIDTH*(float)0.75;
        // X_MIN = (float)WIDTH*(float)0.25;
        // Y_MAX = (float)HEIGHT*(float)0.75;
        // Y_MIN = (float)HEIGHT*(float)0.25;

        X_MAX = (float) WIDTH * (float) 0.5 + 100;
        X_MIN = (float) WIDTH * (float) 0.5 - 100;
        Y_MAX = (float) HEIGHT * (float) 0.5 + 100;
        Y_MIN = (float) HEIGHT * (float) 0.5 - 100;

        flag = false;
        flag1 = true;
        flag2 = true;

        central = (ImageView) findViewById(R.id.central);
        call = (ImageView) findViewById(R.id.call);
        msg = (ImageView) findViewById(R.id.msg);
        mail = (ImageView) findViewById(R.id.mail);
        nfc = (ImageView) findViewById(R.id.nfc);
        ocr = (ImageView) findViewById(R.id.ocr);
        alarm = (ImageView) findViewById(R.id.alarm);
        settings = (ImageView) findViewById(R.id.settings);
        not = (ImageView) findViewById(R.id.not);

    }

    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                central.setImageResource(R.drawable.central);
                if (flag) {
                    flag = false;
                    String result = getResult(event.getX(), event.getY());
                    handleResult(result);
                }
                break;

            case MotionEvent.ACTION_DOWN:
                System.out.println("x  :" + event.getX());
                System.out.println("y  :" + event.getY());
                if (checkCenter(event.getX(), event.getY())) {
                    flag = true;
                    vibrate();
                    central.setImageResource(R.drawable.central2);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {


        speaker.stop();
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        speaker.shutdown();
        super.onDestroy();
    }

    boolean checkCenter(float x, float y) {
        return (x > X_MIN && x < X_MAX && y > Y_MIN && y < Y_MAX);
    }

    void handleResult(String result) {
        central.setImageResource(R.drawable.central);
        call.setImageResource(R.drawable.call);
        msg.setImageResource(R.drawable.msg);
        mail.setImageResource(R.drawable.mail);
        nfc.setImageResource(R.drawable.nfc);
        not.setImageResource(R.drawable.not);
        ocr.setImageResource(R.drawable.ocr);
        alarm.setImageResource(R.drawable.util);
        settings.setImageResource(R.drawable.settings);

        if (result.equals("CENTRE"))
            System.out.println("centre");
        else if (result.equals("TOP LEFT"))
            topLeftClicked();
        else if (result.equals("TOP"))
            topClicked();
        else if (result.equals("TOP RIGHT"))
            topRightClicked();
        else if (result.equals("LEFT"))
            leftClicked();
        else if (result.equals("RIGHT"))
            rightClicked();
        else if (result.equals("BOTTOM RIGHT"))
            bottomRightClicked();
        else if (result.equals("BOTTOM"))
            bottomClicked();
        else if (result.equals("BOTTOM LEFT"))
            bottomLeftClicked();

    }

    private void topLeftClicked() {
        settings.setImageResource(R.drawable.settings2);
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void bottomClicked() {
        speaker.speak("OCR yet to be implemented");
    }

    private void bottomRightClicked() {
        nfc.setImageResource(R.drawable.nfc2);
        startActivity(new Intent(this, NFCActivity.class));
    }

    private void leftClicked() {
        speaker.speak("Utilities");
        alarm.setImageResource(R.drawable.util2);
        startActivity(new Intent(this, UtilityActivity.class));
    }

    private void rightClicked() {
        speaker.speak("Send Email");
        mail.setImageResource(R.drawable.mail2);
        startActivity(new Intent(this, MailActivity.class));
    }

    private void topRightClicked() {
        speaker.speak("Send Message");
        msg.setImageResource(R.drawable.msg2);
        startActivity(new Intent(this, MessageActivity1.class));
    }

    private void topClicked() {
        speaker.speak("Make a Call");
        call.setImageResource(R.drawable.call2);
        startActivity(new Intent(this, PhoneActivity1.class));
    }

    private void bottomLeftClicked() {
        not.setImageResource(R.drawable.not2);
        String date = locationWeather.getDate();
        speaker.speak(date);
        String weather = locationWeather.getWeather();
        if (weather != null && !weather.startsWith("Error"))
            speaker.speak(weather);
        else
            System.out.println(weather);
        String location = locationWeather.getLocation();
        if (location != null && !location.startsWith("Error"))
            speaker.speak(location);
    }

    String getResult(float x, float y) {
        String result = null;
        if (x < X_MIN && y < Y_MIN)
            result = "TOP LEFT";
        else if (x > X_MIN && x < X_MAX && y < Y_MIN)
            result = "TOP";
        else if (x > X_MAX && y < Y_MIN)
            result = "TOP RIGHT";
        else if (x < X_MIN && y > Y_MIN && y < Y_MAX)
            result = "LEFT";
        else if (x > X_MIN && x < X_MAX && y > Y_MIN && y < Y_MAX)
            result = "CENTRE";
        else if (x > X_MAX && y > Y_MIN && y < Y_MAX)
            result = "RIGHT";
        else if (x < X_MIN && y > Y_MAX)
            result = "BOTTOM LEFT";
        else if (x > X_MIN && x < X_MAX && y > Y_MAX)
            result = "BOTTOM";
        else if (x > X_MAX && y > Y_MAX)
            result = "BOTTOM RIGHT";
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

            if (resultCode == RESULT_OK) {

                ArrayList<String> textMatchList = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (!textMatchList.isEmpty()) {

                    if ((textMatchList.get(0).contains("go") || textMatchList
                            .get(0).contains("Go"))
                            || textMatchList.get(0).contains("to")) {
                        String[] s = textMatchList.get(0).split(" ");
                        for (String string : s)
                            if (string.equalsIgnoreCase("call")
                                    || string.equalsIgnoreCase("phone"))
                                startActivity(new Intent(this,
                                        PhoneActivity.class));
                            else if (string.equalsIgnoreCase("message"))
                                startActivity(new Intent(this,
                                        MessageActivity.class));
                            else if (string.equalsIgnoreCase("email")
                                    || string.equalsIgnoreCase("e mail"))
                                startActivity(new Intent(this,
                                        MailActivity.class));
                            else if (string.equalsIgnoreCase("N F C"))
                                startActivity(new Intent(this,
                                        NFCActivity.class));
                            else if (string.equalsIgnoreCase("O C R")
                                    || string.equalsIgnoreCase("sight")) {
                            } else if (string.equalsIgnoreCase("notification")
                                    || string.equalsIgnoreCase("notifications"))
                                bottomLeftClicked();
                            else if (string.equalsIgnoreCase("setting")
                                    || string.equalsIgnoreCase("settings"))
                                startActivity(new Intent(this,
                                        SettingsActivity.class));
                            else if (string.equalsIgnoreCase("alarm")
                                    || string.equalsIgnoreCase("alarms"))
                                startActivity(new Intent(this,
                                        AlarmActivity.class));
                    } else if (textMatchList.get(0).contains("search")) {

                        String searchQuery = textMatchList.get(0).replace(
                                "search", " ");
                        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                        search.putExtra(SearchManager.QUERY, searchQuery);
                        startActivity(search);
                    } else {
                        String search_text = textMatchList.get(0);
                        System.out.println(search_text);
                        Intent myIntent = new Intent(this, SearchResults.class);
                        myIntent.putExtra("SearchText", search_text);
                        startActivity(myIntent);

                    }

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

    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // setContentView(R.layout.activity_main);
    // // startService(new Intent(this, MainService.class));
    //
    // }
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

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GuideDog", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void vibrate() {
        vibrator.vibrate(50);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mLocationClient.disconnect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO Auto-generated method stub
        System.out.println("connection failed");
        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        System.out.println("connected");
        locationWeather = new LocationWeather(this, mLocationClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("klsdjflksdjflkdjslfjasdl");
        // TODO Auto-generated method stub
        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_UP:
                if (!check) {
                    currentString = "";
                    check = true;
                    timer_check();
                }
                System.out.println("UP");
                currentString = currentString + "1";
                break;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                System.out.println("DOWN");
                currentString = currentString + "0";
                break;
        }

        return super.onKeyDown(keyCode, event);
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

        if (currentString.equals(preset)) {
            panic_function();
        }
        currentString = "";
    }

    private void panic_function() {
        send_sms();
        call_number();
    }

    private void send_sms() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, "Help me Please.   "
                + locationWeather.getLocation(), null, null);
        //smsManager.sendTextMessage(number, null, "Hey", null, null);
    }

    private void call_number() {
        String num = "tel:" + number;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(num));
        startActivity(callIntent);

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);

            if (deltaX < NOISE)
                deltaX = (float) 0.0;
            if (deltaY < NOISE)
                deltaY = (float) 0.0;
            if (deltaZ < NOISE)
                deltaZ = (float) 0.0;

            mLastX = x;
            mLastY = y;
            mLastZ = z;

            float gX = mLastX / 9.8f;
            float gY = mLastY / 9.8f;
            float gZ = mLastZ / 9.8f;

            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
            System.out.println(gForce);

            if (gForce > 3.0f) {
                send_sms();
                System.out.println("Accident has taken place");
            }

        }
    }

}
