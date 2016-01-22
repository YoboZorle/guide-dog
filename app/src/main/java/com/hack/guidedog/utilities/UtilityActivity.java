package com.hack.guidedog.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hack.guidedog.R;
import com.hack.guidedog.Speaker;
import com.hack.guidedog.alarm.AlarmActivity;
import com.hack.guidedog.info.LocationWeather;

public class UtilityActivity extends Activity {

    private boolean check = false;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private Vibrator vibrator;
    private Speaker speaker;
    private LocationWeather locationWeather;
    private GestureDetector detector;


    private StringBuilder code;
    private String currentString;

    private ImageView central;
    private ImageView maps;
    private ImageView calculator;
    private ImageView alarm;
    private ImageView mp3player;

    private static int WIDTH;
    private static int HEIGHT;

    private static float X_MAX;
    private static float X_MIN;

    private static float Y_MAX;
    private static float Y_MIN;

    private boolean flag;
    private boolean flag1;
    private boolean flag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_utilities);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        speaker = new Speaker(this);

        code = new StringBuilder();
        currentString = "";


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
        calculator = (ImageView) findViewById(R.id.calculator);
        mp3player = (ImageView) findViewById(R.id.mpplayer);
        alarm = (ImageView) findViewById(R.id.alarm);
        maps = (ImageView) findViewById(R.id.maps);


    }

    private OnGestureListener listener = new OnGestureListener() {

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
            //vibrate();
            flag2 = false;
            //if (checkCenter(e.getX(), e.getY()) && flag1)
            //	speak();

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
                    //vibrate();
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
        alarm.setImageResource(R.drawable.alarm);
        calculator.setImageResource(R.drawable.calc);
        maps.setImageResource(R.drawable.map);
        mp3player.setImageResource(R.drawable.mp);


        if (result.equals("CENTRE"))
            System.out.println("centre");

        else if (result.equals("TOP"))
            topClicked();

        else if (result.equals("LEFT"))
            leftClicked();
        else if (result.equals("RIGHT"))
            rightClicked();

        else if (result.equals("BOTTOM"))
            bottomClicked();

    }


    private void bottomClicked() {
        speaker.speak("Maps");
        maps.setImageResource(R.drawable.map2);
        startActivity(new Intent(this, MapsActivity.class));
    }


    private void leftClicked() {
        speaker.speak("M P 3 player");
        mp3player.setImageResource(R.drawable.mp2);
        startActivity(new Intent(this, Mp3PlayerActivity.class));
    }

    private void rightClicked() {
        speaker.speak("Calculator");
        calculator.setImageResource(R.drawable.calc2);
        startActivity(new Intent(this, CalculatorActivity.class));
    }

    private void topClicked() {
        speaker.speak("Set Alarm");
        alarm.setImageResource(R.drawable.alarm2);
        startActivity(new Intent(this, AlarmActivity.class));
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


    private void vibrate() {
        vibrator.vibrate(50);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
