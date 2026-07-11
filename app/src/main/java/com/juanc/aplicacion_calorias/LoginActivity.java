package com.juanc.aplicacion_calorias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailLayout, passwordLayout;
    private EditText emailEditText, passwordEditText;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Cargar tema guardado antes de mostrar la UI
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        int themeMode = sharedPreferences.getInt(SettingsActivity.KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(themeMode);

        // Verificación de sesión activa
        if (sharedPreferences.contains(SettingsActivity.KEY_USER_ID)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Configuración de Insets para diseño Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de componentes de la UI
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        // Navegación a registro
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Base de datos
        db = AppDatabase.getDatabase(this);

        // Acción de Login
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        if (!validateInputs()) return;

        String correo = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        new Thread(() -> {
            Usuario usuario = db.userDao().login(correo, password);
            runOnUiThread(() -> {
                if (usuario != null) {
                    // Guardar sesión del usuario
                    SharedPreferences sharedPrefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
                    sharedPrefs.edit()
                            .putString(SettingsActivity.KEY_USER_NAME, usuario.nombre)
                            .putInt(SettingsActivity.KEY_USER_ID, usuario.id)
                            .apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("nombre", usuario.nombre);
                    intent.putExtra("userId", usuario.id);
                    startActivity(intent);
                    finish();
                } else {
                    // Error de credenciales incorrectas
                    String error = getString(R.string.credenciales_incorrectas);
                    // Seteamos el error en ambos para soporte de Espresso (hasErrorText)
                    passwordLayout.setError(error);
                    passwordEditText.setError(error);
                }
            });
        }).start();
    }

    private boolean validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        emailLayout.setError(null);
        passwordLayout.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);

        boolean isValid = true;

        // Validar Email usando el Validador centralizado
        if (email.isEmpty()) {
            String error = getString(R.string.error_correo_vacio);
            emailLayout.setError(error);
            emailEditText.setError(error);
            isValid = false;
        } else if (!Validador.validarCorreo(email)) {
            String error = getString(R.string.error_correo_invalido);
            emailLayout.setError(error);
            emailEditText.setError(error);
            isValid = false;
        }

        // Validar Password usando el Validador centralizado
        if (password.isEmpty()) {
            String error = getString(R.string.error_password_vacio);
            passwordLayout.setError(error);
            passwordEditText.setError(error);
            isValid = false;
        } else if (!Validador.validarPassword(password)) {
            String error = getString(R.string.error_password_corto);
            passwordLayout.setError(error);
            passwordEditText.setError(error);
            isValid = false;
        }

        return isValid;
    }
}
