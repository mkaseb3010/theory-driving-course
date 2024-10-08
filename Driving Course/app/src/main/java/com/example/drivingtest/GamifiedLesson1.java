package com.example.drivingtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class GamifiedLesson1 extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private GestureDetector gestureDetector;

    TextView timerTextView;
    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        /*
            Defines a timer handler here to store the timer across different activites, and gives a string format to output the data of the timer stored
            whenever the timer has been stopped.
         */
        @Override
        public void run() {
            if (!Timer.isTimerRunning(GamifiedLesson1.this)) {
                timerHandler.removeCallbacks(this);
                return;
            }

            long millis = System.currentTimeMillis() - Timer.getStartTime(GamifiedLesson1.this);
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    /*
        This method is responsible for navigating to the next activity if the 'next' button is clicked or back to the previous activity if otherwise.
        Our timer handler is being called here to display the timer in this activity based on when it first started originally.
        Uses the gesture detector to be able to detect users hand movements when on the application, if the user swipes up it should display the other side
        of the flip card, and if they swipe down it should take them back to the original screen (the video displayed at first).
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamified_lesson_1);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        if (Timer.isTimerRunning(this)) {
            timerRunnable.run();
        }

        viewFlipper = findViewById(R.id.viewFlipper);
        final VideoView videoView = findViewById(R.id.videoView);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamifiedLesson1.this, GamifiedLesson2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamifiedLesson1.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            }
        });


        //Detects the gesture and hand movements on the screen.
        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getY() - e2.getY() > 50 && Math.abs(velocityY) > 100) {
                    if (viewFlipper.getDisplayedChild() == 0) {
                        viewFlipper.showNext();
                    }
                    return true;
                } else if (e2.getY() - e1.getY() > 50 && Math.abs(velocityY) > 100) {
                    if (viewFlipper.getDisplayedChild() == 1) {
                        viewFlipper.showPrevious();
                        videoView.seekTo(0);
                        videoView.start();
                    }
                    return true;
                }
                return false;
            }
        });
        loadVideo();
    }


    //Loads the video on the flip card once the activity is started
    private void loadVideo() {
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.lane_merge;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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
