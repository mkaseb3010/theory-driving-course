package com.example.drivingtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class GamifiedTest1Q3 extends GamifiedTest1Q2{
    private static final String MYDEBUG = "MYDEBUG";

    /*
     This method is responsible for navigating to the next activity if the 'next' button is clicked or back to the previous activity if otherwise.
     Our timer handler is being called here to display the timer in this activity based on when it first started originally.
     Displays a format of a multiple choice questions using videos where the user is presented with a video and they have to pick which answer is correct
     from the videos provided below.
  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamified_test1_q3);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        if (Timer.isTimerRunning(this)) {
            timerRunnable.run();
        }

        VideoView videoView1 = findViewById(R.id.videoView1);
        VideoView videoView2 = findViewById(R.id.videoView2);
        VideoView videoView3 = findViewById(R.id.videoView3);

        videoView1.setFocusable(false);
        videoView2.setFocusable(false);
        videoView3.setFocusable(false);

        videoView1.setOnClickListener(this::onChoiceClick);
        videoView2.setOnClickListener(this::onChoiceClick);
        videoView3.setOnClickListener(this::onChoiceClick);

        setupVideoView(R.id.videoView2, R.raw.l1q3a2);
        setupVideoView(R.id.videoView1, R.raw.l1q3a1);
        setupVideoView(R.id.videoView3, R.raw.l1q3a3);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamifiedTest1Q3.this, StartLesson2Gamified.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamifiedTest1Q3.this, GamifiedTest1Q2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            }
        });
    }

    /*
       This method is responsible to handle the effects that follow after a right or wrong answer has been clicked, if the answer is right it should
       display the color green across all 4 corners of the screen, otherwise display red and shake the phone or vibrate it to allow for live feedbacks
       when the user is answering the question.
    */
    public void onChoiceClick(View view) {
        boolean isAnswerCorrect = "correct".equals(view.getTag());

        int topLeftDrawable, topRightDrawable, bottomLeftDrawable, bottomRightDrawable;

        if (isAnswerCorrect) {
            topLeftDrawable = R.drawable.green_top_left_corner;
            topRightDrawable = R.drawable.green_top_right_corner;
            bottomLeftDrawable = R.drawable.green_bottom_left_corner;
            bottomRightDrawable = R.drawable.green_bottom_right_corner;
        }else {
            topLeftDrawable = R.drawable.red_top_left_corner;
            topRightDrawable = R.drawable.red_top_right_corner;
            bottomLeftDrawable = R.drawable.red_bottom_left_corner;
            bottomRightDrawable = R.drawable.red_bottom_right_corner;
        }

        View topLeftCorner = findViewById(R.id.bottomRightCorner);
        View topRightCorner = findViewById(R.id.bottomLeftCorner);
        View bottomLeftCorner = findViewById(R.id.topRightCorner);
        View bottomRightCorner = findViewById(R.id.topLeftCorner);

        topLeftCorner.setBackgroundResource(topLeftDrawable);
        topRightCorner.setBackgroundResource(topRightDrawable);
        bottomLeftCorner.setBackgroundResource(bottomLeftDrawable);
        bottomRightCorner.setBackgroundResource(bottomRightDrawable);

        topLeftCorner.setVisibility(View.VISIBLE);
        topRightCorner.setVisibility(View.VISIBLE);
        bottomLeftCorner.setVisibility(View.VISIBLE);
        bottomRightCorner.setVisibility(View.VISIBLE);

        if (!"correct".equals(view.getTag())) {
            Counter.incrementWrongAnswerCount(this, "Question 3");
            int wrongAnswerCounter = Counter.getWrongAnswerCount(this, "Question 3");
            Log.i(MYDEBUG, "You've answered Gamified Test 1 Question 3 incorrectly " + wrongAnswerCounter + " times.");

            View rootView = findViewById(android.R.id.content);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            rootView.startAnimation(shake);
        }

        new Handler().postDelayed(() -> {
            topLeftCorner.setVisibility(View.INVISIBLE);
            topRightCorner.setVisibility(View.INVISIBLE);
            bottomLeftCorner.setVisibility(View.INVISIBLE);
            bottomRightCorner.setVisibility(View.INVISIBLE);
        }, 2000);
    }

    private void setupVideoView(int videoViewId, int videoResourceId) {
        VideoView videoView = findViewById(videoViewId);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResourceId);
        videoView.setVideoURI(videoUri);
        videoView.setOnCompletionListener(mp -> videoView.start());
        videoView.start();
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
