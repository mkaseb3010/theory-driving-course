package com.example.drivingtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class TraditionalTest1Q2 extends TraditionalTest1Q1 {
    private static final String MYDEBUG = "MYDEBUG";
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;

    /*
      This method is responsible for navigating to the next activity if the 'next' button is clicked or back to the previous activity if otherwise.
      Our timer handler is being called here to display the timer in this activity based on when it first started originally.
      The user is presented with a series of multiple choice questions in which they have to select the right choice based on the previous lessons
      they went through.
   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traditional_test1_q2);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        if (Timer.isTimerRunning(this)) {
            timerRunnable.run();
        }

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TraditionalTest1Q2.this, TraditionalTest1Q3.class);
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (TraditionalTest1Q2.this, TraditionalTest1Q1.class);
                startActivity(intent);
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswers();
            }
        });
    }

    /*
       This method checks the user's input, and if correct or incorrect it should log this and use the counter class we have defined to update
       the status, and finally should reset the button and allow the use of it again depending on the answer.
    */
    private void validateAnswers() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radioButton2) {
            radioButton2.setTextColor(Color.GREEN);
            resetIncorrectRadioButtons(selectedId);
            radioGroup.setEnabled(false);
        } else {
            RadioButton selectedRadioButton = findViewById(selectedId);
            if (selectedRadioButton != null) {
                selectedRadioButton.setTextColor(Color.RED);
                Counter.incrementWrongAnswerCount(this, "Question 2");
                int wrongAnswerCounter = Counter.getWrongAnswerCount(this, "Question 2");
                Log.i(MYDEBUG, "You've answered Test 1 Question 2 incorrectly " + wrongAnswerCounter + " times.");
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetRadioButtons();
                    radioGroup.clearCheck();
                    radioGroup.setEnabled(true);
                }
            }, 2000);
        }
    }

    /*
        This method should reset the buttons if the choice is incorrect.
     */
    private void resetIncorrectRadioButtons(int correctRadioButtonId) {
        if (radioButton1.getId() != correctRadioButtonId) {
            radioButton1.setTextColor(Color.BLACK);
        }
        if (radioButton3.getId() != correctRadioButtonId) {
            radioButton3.setTextColor(Color.BLACK);
        }
        if(radioButton4.getId() != correctRadioButtonId) {
            radioButton4.setTextColor(Color.BLACK);
        }
        if (radioButton5.getId() != correctRadioButtonId) {
            radioButton5.setTextColor(Color.BLACK);
        }
    }

    //Resets the color back to its default
    private void resetRadioButtons() {
        radioButton1.setTextColor(Color.WHITE);
        radioButton2.setTextColor(Color.WHITE);
        radioButton3.setTextColor(Color.WHITE);
        radioButton4.setTextColor(Color.WHITE);
        radioButton5.setTextColor(Color.WHITE);
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

