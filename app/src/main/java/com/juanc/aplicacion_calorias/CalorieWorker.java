package com.juanc.aplicacion_calorias;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

public class CalorieWorker extends Worker {

    public static final String CHANNEL_ID = "calorie_alerts";
    private static final int NOTIFICATION_ID = 1;

    public CalorieWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        android.content.SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        boolean notificationsEnabled = sharedPreferences.getBoolean(SettingsActivity.KEY_NOTIFICATIONS, true);
        int userId = sharedPreferences.getInt(SettingsActivity.KEY_USER_ID, -1);

        if (!notificationsEnabled || userId == -1) {
            return Result.success();
        }

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        List<Comida> comidas = db.comidaDao().getAllComidasSync(userId);
        int totalCalories = 0;
        for (Comida c : comidas) {
            totalCalories += c.getCalorias();
        }

        int objetivoDiario = sharedPreferences.getInt(SettingsActivity.KEY_CALORIE_LIMIT, 2000);

        if (totalCalories > objetivoDiario) {
            sendNotification(totalCalories);
        }

        return Result.success();
    }

    private void sendNotification(int totalCalories) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    getApplicationContext().getString(R.string.notif_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(getApplicationContext().getString(R.string.notif_channel_desc));
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(getApplicationContext().getString(R.string.notif_title_exceso))
                .setContentText(getApplicationContext().getString(R.string.notif_content_exceso, totalCalories))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
