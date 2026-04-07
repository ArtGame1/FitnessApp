package com.example.fitnessapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Показываем уведомление
        NotificationHelper.showWorkoutReminder(context);
    }
}