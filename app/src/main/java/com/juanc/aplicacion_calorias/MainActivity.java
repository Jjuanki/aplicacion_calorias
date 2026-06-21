package com.juanc.aplicacion_calorias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        LinearLayout bottomNav = findViewById(R.id.bottomNav);

        // Logout logic (Ajustes button)
        bottomNav.getChildAt(2).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // 🔹 Recibir nombre del usuario desde Login
        String nombre = getIntent().getStringExtra("nombre");

        if (nombre != null) {
            tvWelcome.setText(getString(R.string.hola_usuario_con_nombre, nombre));
        } else {
            tvWelcome.setText(R.string.hola_usuario);
        }
    }
}