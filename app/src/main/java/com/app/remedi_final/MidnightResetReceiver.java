package com.app.remedi_final;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class MidnightResetReceiver extends BroadcastReceiver {
    private static final String TAG = "MidnightResetReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Midnight reset triggered");

        // Create intent to start CheckBox activity with reset flag
        Intent checkboxIntent = new Intent(context, check_box.class);
        checkboxIntent.putExtra("reset_checkboxes", true);
        checkboxIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(checkboxIntent);

        // Schedule next midnight reset
        scheduleMidnightReset(context);
    }

    public static void scheduleMidnightReset(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Set calendar to next midnight
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        Intent intent = new Intent(context, MidnightResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent);
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent);
            }

            Log.d(TAG, "Midnight reset scheduled for: " + calendar.getTime());
        }
    }
}