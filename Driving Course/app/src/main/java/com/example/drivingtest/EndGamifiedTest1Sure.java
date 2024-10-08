package com.example.drivingtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class EndGamifiedTest1Sure extends StartLesson2Gamified {
    private static final String MYDEBUG = "MYDEBUG";

    /*
        This method is responsible for navigating to the next activity if the 'next' button is clicked or back to the previous activity if otherwise.
     */
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
        setContentView(R.layout.end_test1_gamified_sure);

        Button yesButton = findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();

                Intent intent = new Intent(EndGamifiedTest1Sure.this, GamifiedLesson6.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button noButton = findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndGamifiedTest1Sure.this, GamifiedTest1Q1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            }
        });
    }

    /*
        After stopAndResetTimer() has been called, this should method should safely call a runnable to start the timer after making sure there is no
        other runnable running (can only happen if the stopAndResetTimer() is not behaving as expected).
     */
    private void startTimer() {
        Timer.setStartTime(this, System.currentTimeMillis());
        Timer.setTimerRunning(this, true);

        if (timerRunnable == null) {
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    if (Timer.isTimerRunning(EndGamifiedTest1Sure.this)) {
                        long elapsedMillis = System.currentTimeMillis() - Timer.getStartTime(EndGamifiedTest1Sure.this);
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
