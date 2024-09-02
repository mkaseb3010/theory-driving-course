package com.example.drivingtest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class GamifiedTest1Q1 extends startTest1GamifiedSure{
    private static final String MYDEBUG = "MYDEBUG";
    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        /*
            Sets a timer handler to handle the timer across multiple activities, without resetting and storing the data when a new activity is started
         */
        @Override
        public void run() {
            if (!Timer.isTimerRunning(GamifiedTest1Q1.this)) {
                timerHandler.removeCallbacks(this);
                return;
            }

            long millis = System.currentTimeMillis() - Timer.getStartTime(GamifiedTest1Q1.this);
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
       Displays a format of a multiple choice questions using images where the user is presented with a video and they have to pick which sign is missing
       based on the choices they are provided.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamified_test1_q1);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        if (Timer.isTimerRunning(this)) {
            timerRunnable.run();
        }

        ImageView imageView6 = findViewById(R.id.imageView6);
        ImageView imageView8 = findViewById(R.id.imageView8);
        ImageView imageView9 = findViewById(R.id.imageView9);
        ImageView imageView10 = findViewById(R.id.imageView10);

        imageView6.setOnClickListener(this::onChoiceClick);
        imageView8.setOnClickListener(this::onChoiceClick);
        imageView9.setOnClickListener(this::onChoiceClick);
        imageView10.setOnClickListener(this::onChoiceClick);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamifiedTest1Q1.this, GamifiedTest1Q2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamifiedTest1Q1.this, startTest1Gamified.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            }
        });

        VideoView videoView = findViewById(R.id.videoView);

        //Accesses the video from the file imported
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.l1q1;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                videoView.start();
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
            Counter.incrementWrongAnswerCount(this, "Question 1");
            int wrongAnswerCounter = Counter.getWrongAnswerCount(this, "Question 1");
            Log.i(MYDEBUG, "You've answered Gamfiied Test 1 Question 1 incorrectly " + wrongAnswerCounter + " times.");

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
