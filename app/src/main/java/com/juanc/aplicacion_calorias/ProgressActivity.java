package com.juanc.aplicacion_calorias;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressActivity extends AppCompatActivity {

    private BarChartView barChart;
    private TextView tvAvgCalories, tvDaysMet;
    private ComidaViewModel comidaViewModel;
    private int objetivoDiario;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        tvAvgCalories = findViewById(R.id.tvAvgCalories);
        tvDaysMet = findViewById(R.id.tvDaysMet);
        barChart = findViewById(R.id.barChart);

        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        objetivoDiario = prefs.getInt(SettingsActivity.KEY_CALORIE_LIMIT, 2000);
        userId = prefs.getInt(SettingsActivity.KEY_USER_ID, -1);

        comidaViewModel = new ViewModelProvider(this).get(ComidaViewModel.class);

        long sevenDaysAgo = getStartOfDay(7);
        comidaViewModel.getComidasSince(userId, sevenDaysAgo).observe(this, this::processData);
    }

    private long getStartOfDay(int daysAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private void processData(List<Comida> comidas) {
        Map<Integer, Integer> dailyCalories = new HashMap<>();
        
        // Initialize last 7 days with 0
        for (int i = 0; i < 7; i++) {
            dailyCalories.put(i, 0);
        }

        Calendar cal = Calendar.getInstance();
        long now = System.currentTimeMillis();

        for (Comida comida : comidas) {
            long diff = now - comida.getTimestamp();
            int dayIndex = 6 - (int) (diff / (1000 * 60 * 60 * 24));
            if (dayIndex >= 0 && dayIndex < 7) {
                int current = dailyCalories.getOrDefault(dayIndex, 0);
                dailyCalories.put(dayIndex, current + comida.getCalorias());
            }
        }

        List<Integer> chartData = new ArrayList<>();
        int totalSum = 0;
        int daysMetCount = 0;

        for (int i = 0; i < 7; i++) {
            int calories = dailyCalories.get(i);
            chartData.add(calories);
            totalSum += calories;
            if (calories <= objetivoDiario && calories > 0) {
                daysMetCount++;
            }
        }

        int average = totalSum / 7;

        tvAvgCalories.setText(getString(R.string.promedio_diario, average));
        tvDaysMet.setText(getString(R.string.dias_objetivo_cumplido, daysMetCount));
        barChart.setData(chartData, objetivoDiario);
    }
}