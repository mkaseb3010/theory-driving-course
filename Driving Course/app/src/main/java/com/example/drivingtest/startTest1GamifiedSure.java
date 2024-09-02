package com.example.drivingtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class startTest1GamifiedSure extends startTest1Gamified{
    private static final String MYDEBUG = "MYDEBUG";
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
        }
    };

    /*
       This method is responsible for navigating to the next activity if the 'next' button is clicked or back to the previous activity if otherwise.
       Our timer handler is being called here to display the timer in this activity based on when it first started originally.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_test1_gamified_sure);

        Counter.resetWrongAnswerCount(this,"Question 1");
        Counter.resetWrongAnswerCount(this,"Question 2");
        Counter.resetWrongAnswerCount(this,"Question 3");

        Button yesButton = findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();

                Intent intent = new Intent(startTest1GamifiedSure.this, GamifiedTest1Q1.class);
                startActivity(intent);
            }
        });

        Button noButton = findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(startTest1GamifiedSure.this, GamifiedLesson5.class);
                startActivity(intent);
            }
        });
    }

    /*
        This method starts the timer when the activity has started.
     */
    private void startTimer() {
        Timer.setStartTime(this, System.currentTimeMillis());
        Timer.setTimerRunning(this, true);

        if (timerRunnable == null) {
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    if (Timer.isTimerRunning(startTest1GamifiedSure.this)) {
                        long elapsedMillis = System.currentTimeMillis() - Timer.getStartTime(startTest1GamifiedSure.this);
                        int seconds = (int) (elapsedMillis / 1000) % 60;
                        int minutes = (int) ((elapsedMillis / (1000*60)) % 60);
                        timerHandler.postDelayed(this, 1000);
                    }
                }
            };
        }
        timerHandler.postDelayed(timerRunnable, 0);
    }
}
