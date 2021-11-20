package com.example.easyjob;


import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private EditText textUsername;
    private EditText textPassword;
    private Button button;
    int tryNum = 3;

    String msg = "Android : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textUsername = findViewById(R.id.editTextTextPersonName);
        textPassword = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.button);

    }

    /* click login button go to main page */
    public void login(@NonNull View view) {
        switch (view.getId()) {
            case R.id.button:
                if(tryNum  == 0){
                    Toast.makeText(this, "Try too many times, your account has been blocked, please contact customer service!",
                            Toast.LENGTH_LONG).show();
                    return;
                };
                if (textUsername.getText().length() == 0) {
                    tryNum--;
                    Toast.makeText(this, "Wrong Credentials, Please enter a valid User name," +
                                    tryNum + " attempts are left ",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (textPassword.getText().length() == 0) {
                    tryNum--;
                    Toast.makeText(this, "Wrong Credentials, Please enter a valid Password," +
                                    tryNum + " attempts are left ",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(textUsername.getText().length() > 0 && !textUsername.getText().toString().equals("admin")){
                    tryNum--;
                    Log.d(msg, textPassword.getText().toString());
                    Toast.makeText(this, "Wrong Credentials, Your user name is wrong, Please enter another one, " +
                                    tryNum + " attempts are left ",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(textPassword.getText().length() > 0 && !textPassword.getText().toString().equals("123456")){
                    tryNum--;
                    Log.d(msg, textPassword.getText().toString());
                    Toast.makeText(this, "Wrong Credentials, Your user password is wrong, Please enter another one, " +
                                    tryNum + " attempts are left ",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(textUsername.getText().toString().equals("admin") && textPassword.getText().toString().equals("123456")){
                    Log.d(msg, "go to main page");
                    Toast.makeText(this, "Redirecting…", Toast.LENGTH_LONG).show();
                    Timer mTimer = new Timer();
                    TimerTask mTimerTask = new TimerTask() {//create a TimerTask to execute the code of function run
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    };
                    mTimer.schedule(mTimerTask, 3000);//delay 5 second to run MainActivity
                }
        }
    }
}
