package com.example.drivingtest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class EndTraditionalLesson extends TraditionalTest2Q2{
    private static final String MYDEBUG = "MYDEBUG";

    /*
        This method is responsible for navigating to the next activity if the 'next' button is clicked or back to the previous activity if otherwise.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_test2_traditional);

        Button yesButton = findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAndResetTimer();

                Intent intent = new Intent(EndTraditionalLesson.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button noButton = findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndTraditionalLesson.this, TraditionalTest2Q2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            }
        });
    }

    /*
        This method should stop the timer whenever needed (typically means we need to output the stored timer in the terminal to check the data), this
        should also reset the timer to 0:00 (according to the given format), and safely remove any call backs that could affect the timer starting again.
        The use of removeCallBacks is important to avoid other runnables from running when not instruted to.
     */
    private void stopAndResetTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        long elapsedTimeMillis = System.currentTimeMillis() - Timer.getStartTime(this);
        String elapsedTimeStr = Long.toString(elapsedTimeMillis);
        int seconds = (int) (elapsedTimeMillis / 1000) % 60;
        int minutes = (int) ((elapsedTimeMillis / (1000*60)) % 60);
        String elapsedTimeFormatted = String.format("%d min, %d sec", minutes, seconds);
        Timer.saveTimerData(this, elapsedTimeFormatted);
        Log.i(MYDEBUG, "Traditional Test 2 Timer. Elapsed time: " + elapsedTimeFormatted);
        Timer.setStartTime(this, 0);
        Timer.setTimerRunning(this, false);
    }
}
