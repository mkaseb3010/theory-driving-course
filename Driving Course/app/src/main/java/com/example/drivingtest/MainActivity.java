/*
    EECS4443 Final project, an applicaion that acts as a theory based road test exam. This application has 2 methods of delivering the content to the
    users, either using Traditional style or using Gamified style. The users will be prompted a choice between both styles and can complete the
    exam in however style they want at first. This application is designed for research purposes and has timers and counters built in for data gathering
    purposes and will store data of the users, all participants are notified with their information being used for this research and to have their data
    collected after every run. Some images and videos that may be used in the implementation of this project are taken online with safe usage, as the
    application developer of this project I do not claim all image or video copyrights used in this project as my own. Credits belong to the providers
    of these contents wherever applicable, references can be provided upon request.

    Authors: Mazen Kaseb.
 */


package com.example.drivingtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /*
        Sets a timer handler to handle the timer across multiple activities, without resetting and storing the data when a new activity is started
     */
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
        }
    };

    /*
        This method is responsible for navigating to Traditional or Gamified, respective to what is clicked by the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button traditionalButton = findViewById(R.id.traditionalButton);
        traditionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Timer.isTimerRunning(MainActivity.this)) {
                    Timer.setStartTime(MainActivity.this, System.currentTimeMillis());
                    Timer.setTimerRunning(MainActivity.this, true);
                    startTimer();
                }

                Intent intent = new Intent(MainActivity.this, TraditionalLesson1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button gamifiedButton = findViewById(R.id.gamifiedButton);
        gamifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Timer.isTimerRunning(MainActivity.this)) {
                    Timer.setStartTime(MainActivity.this, System.currentTimeMillis());
                    Timer.setTimerRunning(MainActivity.this, true);
                    startTimer();
                }

                Intent intent = new Intent(MainActivity.this, GamifiedLesson1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void startTimer() {
        timerRunnable.run();
    }

    /*
       If the a new lesson or test has been started by the user, if the onPause state has been called here, it should reset the timer and start a new one to time users
       for the second lesson.
   */
    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    //Keep the timer going
    @Override
    protected void onResume() {
        super.onResume();
        if (Timer.isTimerRunning(this)) {
            startTimer();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timer.setTimerRunning(this, false);
    }
}
