package com.example.drivingtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TraditionalLesson4 extends StartLesson2Traditional {
    private static final String MYDEBUG = "MYDEBUG";
    private boolean isPlaying = false;
    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        /*
            Defines a timer handler here to store the timer across different activites, and gives a string format to output the data of the timer stored whenever
            the timer has been stopped.
         */
        @Override
        public void run() {
            if (!Timer.isTimerRunning(TraditionalLesson4.this)) {
                timerHandler.removeCallbacks(this);
                return;
            }

            long millis = System.currentTimeMillis() - Timer.getStartTime(TraditionalLesson4.this);
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    /*
       This method is responsible for navigating to the next activity if the 'next' button is clicked.
       Our timer handler is being called here to display the timer in this activity based on when it first started originally.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traditional_lesson_4);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        if (Timer.isTimerRunning(this)) {
            timerRunnable.run();
        }

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TraditionalLesson4.this, TraditionalLesson5.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    //Keep the timer going
    @Override
    protected void onResume() {
        super.onResume();
        if (Timer.isTimerRunning(this)) {
            timerRunnable.run();
        }
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
}
