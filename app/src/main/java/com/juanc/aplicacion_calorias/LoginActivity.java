package com.juanc.aplicacion_calorias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailLayout, passwordLayout;
    private EditText emailEditText, passwordEditText;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // 🔹 Edge to Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔹 Init UI
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        // 🔹 Init Room DB
        db = AppDatabase.getDatabase(this);

        // 🔹 Login button
        btnLogin.setOnClickListener(v -> loginUser());
    }

    // 🔐 LOGIN REAL CON ROOM
    private void loginUser() {

        if (!validateInputs()) return;

        String correo = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        new Thread(() -> {
            Usuario usuario = db.userDao().login(correo, password);
            runOnUiThread(() -> {
                if (usuario != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("nombre", usuario.nombre);
                    startActivity(intent);
                    finish();
                } else {
                    passwordLayout.setError(getString(R.string.credenciales_incorrectas));
                }
            });
        }).start();
    }

    // 🔥 VALIDACIÓN
    private boolean validateInputs() {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        emailLayout.setError(null);
        passwordLayout.setError(null);

        boolean isValid = true;

        if (email.isEmpty()) {
            emailLayout.setError(getString(R.string.error_correo_vacio));
            isValid = false;

        } else {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

            if (!email.matches(emailRegex)) {
                emailLayout.setError(getString(R.string.error_correo_invalido));
                isValid = false;
            }
        }

        if (password.isEmpty()) {
            passwordLayout.setError(getString(R.string.error_password_vacio));
            isValid = false;

        } else if (password.length() < 6) {
            passwordLayout.setError(getString(R.string.error_password_corto));
            isValid = false;
        }

        return isValid;
    }
}