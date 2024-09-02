package com.example.drivingtest;

import android.content.Context;
import android.content.SharedPreferences;

public class Counter {
    private static final String PREFS_NAME = "WrongAnswerCounts";
    private static final String MYDEBUG = "MYDEBUG";

    /*
        This method is responsbible to increment the counter if the chosen answer is wrong.
     */
    public static void incrementWrongAnswerCount(Context context, String questionId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentCount = prefs.getInt(questionId, 0) + 1;
        prefs.edit().putInt(questionId, currentCount).apply();
    }

    /*
        This method is used to get the total number of incorrect tries when wanting to store the data.
     */
    public static int getWrongAnswerCount(Context context, String questionId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int count = prefs.getInt(questionId, 0);
        return count;
    }

    /*
        Resets the counter whenever we enter a new quiz.
     */
    public static void resetWrongAnswerCount(Context context, String questionId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(questionId).apply();
    }
}
