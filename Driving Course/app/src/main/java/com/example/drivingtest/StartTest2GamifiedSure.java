package com.example.drivingtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class StartTest2GamifiedSure extends StartTest2Gamified{
    private static final String MYDEBUG = "MYDEBUG";
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_test2_gamified_sure);

        Counter.resetWrongAnswerCount(this,"Question 1");
        Counter.resetWrongAnswerCount(this,"Question 2");

        Button yesButton = findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();

                Intent intent = new Intent(StartTest2GamifiedSure.this, GamifiedTest2Q1.class);
                startActivity(intent);
            }
        });

        Button noButton = findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTest2GamifiedSure.this, GamifiedLesson6.class);
                startActivity(intent);
            }
        });
    }

    private void startTimer() {
        Timer.setStartTime(this, System.currentTimeMillis());
        Timer.setTimerRunning(this, true);

        if (timerRunnable == null) {
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    if (Timer.isTimerRunning(StartTest2GamifiedSure.this)) {
                        long elapsedMillis = System.currentTimeMillis() - Timer.getStartTime(StartTest2GamifiedSure.this);
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
