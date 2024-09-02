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
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

public class GamifiedTest2Q1 extends StartTest2GamifiedSure{
    private final int[] correctOrder = { R.id.buttonStep1, R.id.buttonStep2, R.id.buttonStep3, R.id.buttonStep4, R.id.buttonStep5 };
    private final List<Integer> userSelections = new ArrayList<>();
    private static final String MYDEBUG = "MYDEBUG";
    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        /*
            Defines a timer handler here to store the timer across different activites, and gives a string format to output the data of the timer stored
            whenever the timer has been stopped.
         */
        @Override
        public void run() {
            if (!Timer.isTimerRunning(GamifiedTest2Q1.this)) {
                timerHandler.removeCallbacks(this);
                return;
            }

            long millis = System.currentTimeMillis() - Timer.getStartTime(GamifiedTest2Q1.this);
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
     Displays a format of a multiple choice questions using videos where the user is presented with a video and they have to pick which sequence is correct
     from the videos provided below.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamified_test2_q1);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        if (Timer.isTimerRunning(this)) {
            timerRunnable.run();
        }

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamifiedTest2Q1.this, GamifiedTest2Q2.class);
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamifiedTest2Q1.this, StartTest2GamifiedSure.class);
                startActivity(intent);
            }
        });

        setupButton((Button) findViewById(R.id.buttonStep1));
        setupButton((Button) findViewById(R.id.buttonStep2));
        setupButton((Button) findViewById(R.id.buttonStep3));
        setupButton((Button) findViewById(R.id.buttonStep4));
        setupButton((Button) findViewById(R.id.buttonStep5));

        VideoView videoView = findViewById(R.id.videoView);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.l2q1;
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
        This method is responsible for enabled the buttong and giving the clicked animation if the
        user has clicked the button and will read the entries to see if it matches the correct
        sequence.
     */
    private void setupButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                button.setSelected(true);
                userSelections.add(button.getId());

                if (userSelections.size() == correctOrder.length) {
                    checkSequence();
                }
            }
        });
    }

    /*
      This method is responsible to handle the effects that follow after a right or wrong answer has been clicked, if the answer is right it should
      display the color green across all 4 corners of the screen, otherwise display red and shake the phone or vibrate it to allow for live feedbacks
      when the user is answering the question.
   */
    private void checkSequence() {
        int topLeftDrawable, topRightDrawable, bottomLeftDrawable, bottomRightDrawable;

        boolean isCorrect = true;
        for (int i = 0; i < correctOrder.length; i++) {
            if (!userSelections.get(i).equals(correctOrder[i])) {
                isCorrect = false;
                break;
            }
        }

        if (isCorrect) {
            topLeftDrawable = R.drawable.green_top_left_corner;
            topRightDrawable = R.drawable.green_top_right_corner;
            bottomLeftDrawable = R.drawable.green_bottom_left_corner;
            bottomRightDrawable = R.drawable.green_bottom_right_corner;
        } else {
            topLeftDrawable = R.drawable.red_top_left_corner;
            topRightDrawable = R.drawable.red_top_right_corner;
            bottomLeftDrawable = R.drawable.red_bottom_left_corner;
            bottomRightDrawable = R.drawable.red_bottom_right_corner;

            Counter.incrementWrongAnswerCount(this, "Question 1");
            int wrongAnswerCounter = Counter.getWrongAnswerCount(this, "Question 1");
            Log.i(MYDEBUG, "You've answered Test 2 Question 1 incorrectly " + wrongAnswerCounter + " times.");

            View rootView = findViewById(android.R.id.content);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            rootView.startAnimation(shake);

            resetButtons();
            userSelections.clear();
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

    //Resets the buttons to use again
    private void resetButtons() {
        for (int buttonId : correctOrder) {
            Button button = findViewById(buttonId);
            button.setEnabled(true);
            button.setSelected(false);
        }
    }
}
