package com.example.easyjob;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {//create a TimerTask to execute the code of function run
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this,LoginActivity.class);
                startActivity(intent);
            }
        };
        mTimer.schedule(mTimerTask, 5000);//delay 5 second to run QuoteReaderActivity
    }
}
