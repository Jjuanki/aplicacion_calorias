package com.juanc.aplicacion_calorias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btnCreate = findViewById(R.id.btnCreateAccount);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "app_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        btnCreate.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔴 Evitar duplicados
        Usuario existing = db.userDao().findByEmail(email);
        if (existing != null) {
            Toast.makeText(this, R.string.error_correo_registrado, Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 Insert en Room
        Usuario user = new Usuario(name, email, password);
        db.userDao().insertUser(user);

        Toast.makeText(this, R.string.usuario_creado, Toast.LENGTH_SHORT).show();

        // 🔁 Navegación a MainActivity
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.putExtra("nombre", name);
        startActivity(intent);
        finish();
    }
}