package com.example.drivingtest;

import android.content.Context;
import android.content.SharedPreferences;

public class Timer {
        private static final String PREF_NAME = "TimerPreferences";
        private static final String START_TIME = "startTime";
        private static final String IS_RUNNING = "isRunning";
        private static final String TIMER_DATA = "timerData";

        /*
            Sets the status of the timer to 'RUNNING' when another activity has been created, should be defined in onCreate to allow the program to know
            that a timer is currently running from a previous activity and to resume it.
         */
        public static void setTimerRunning(Context context, boolean isRunning) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(IS_RUNNING, isRunning);
            editor.apply();
        }

        /*
            Checks if a timer is currently running or not.
         */
        public static boolean isTimerRunning(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            return prefs.getBoolean(IS_RUNNING, false);
        }

        /*
            This sets a start time for our timer (typically is 0).
         */
        public static void setStartTime(Context context, long startTime) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(START_TIME, startTime);
            editor.apply();
        }

        /*
            Gets the start time we set in the previous method.
         */
        public static long getStartTime(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            return prefs.getLong(START_TIME, 0);
        }

        /*
            This method is responsible for saving our time data to allow us to store the data from the timers.
         */
        public static void saveTimerData(Context context, String data) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(TIMER_DATA, data);
            editor.apply();
        }

        /*
            Defined just in case we choose to have the output generated to an external file instead of locally in the terminal (not used in this project).
         */
        public static String getTimerData(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            return prefs.getString(TIMER_DATA, "00:00");
        }
}
