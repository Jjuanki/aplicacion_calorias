package com.juanc.aplicacion_calorias;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private EditText etName, etCalorieLimit;
    private SwitchCompat switchNotifications;
    private RadioGroup rgTheme;
    private SharedPreferences sharedPreferences;

    public static final String PREFS_NAME = "CalorieAppPrefs";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CALORIE_LIMIT = "calorie_limit";
    public static final String KEY_NOTIFICATIONS = "notifications_enabled";
    public static final String KEY_THEME = "app_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etName = findViewById(R.id.etSettingsName);
        etCalorieLimit = findViewById(R.id.etSettingsCalorieLimit);
        switchNotifications = findViewById(R.id.switchNotifications);
        rgTheme = findViewById(R.id.rgTheme);
        Button btnSave = findViewById(R.id.btnSaveSettings);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        loadSettings();

        btnSave.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {
        String name = sharedPreferences.getString(KEY_USER_NAME, "");
        int limit = sharedPreferences.getInt(KEY_CALORIE_LIMIT, 2000);
        boolean notifs = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true);
        int theme = sharedPreferences.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        etName.setText(name);
        etCalorieLimit.setText(String.valueOf(limit));
        switchNotifications.setChecked(notifs);

        if (theme == AppCompatDelegate.MODE_NIGHT_NO) {
            rgTheme.check(R.id.rbLight);
        } else if (theme == AppCompatDelegate.MODE_NIGHT_YES) {
            rgTheme.check(R.id.rbDark);
        } else {
            rgTheme.check(R.id.rbSystem);
        }
    }

    private void saveSettings() {
        String name = etName.getText().toString().trim();
        String limitStr = etCalorieLimit.getText().toString().trim();

        if (name.isEmpty() || limitStr.isEmpty()) {
            Toast.makeText(this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
            return;
        }

        int limit = Integer.parseInt(limitStr);
        boolean notifs = switchNotifications.isChecked();
        
        int selectedThemeId = rgTheme.getCheckedRadioButtonId();
        int themeMode;
        if (selectedThemeId == R.id.rbLight) {
            themeMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (selectedThemeId == R.id.rbDark) {
            themeMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, name);
        editor.putInt(KEY_CALORIE_LIMIT, limit);
        editor.putBoolean(KEY_NOTIFICATIONS, notifs);
        editor.putInt(KEY_THEME, themeMode);
        editor.apply();

        AppCompatDelegate.setDefaultNightMode(themeMode);
        
        Toast.makeText(this, "Ajustes guardados", Toast.LENGTH_SHORT).show();
        finish();
    }
}
