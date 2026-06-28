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
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        // Note: Room's LiveData getAllComidas() is async, but we need a synchronous call for the worker.
        // Let's assume we might need a sync method in Dao or use the LiveData value if it's already there (not ideal).
        // Actually, workers run on a background thread, so we should use a non-LiveData query.
        
        // I should check if ComidaDao has a synchronous version of getAllComidas.
        // Looking at previous read_file of ComidaDao, it only has LiveData<List<Comida>> getAllComidas().
        // I need to add a synchronous one or handle it.
        
        List<Comida> comidas = db.comidaDao().getAllComidasSync();
        int totalCalories = 0;
        for (Comida c : comidas) {
            totalCalories += c.getCalorias();
        }

        int objetivoDiario = 2000; // This should ideally be shared via preferences or data, but 2000 is used in MainActivity.

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
