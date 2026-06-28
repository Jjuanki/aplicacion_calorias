package com.juanc.aplicacion_calorias;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ComidaViewModel comidaViewModel;
    private TextView tvConsumed, tvRemaining, tvEmpty;
    private int objetivoDiario = 2000;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Las notificaciones están desactivadas. No recibirás alertas de calorías.", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvConsumed = findViewById(R.id.tvConsumed);
        tvRemaining = findViewById(R.id.tvRemaining);
        tvEmpty = findViewById(R.id.tvEmpty);
        RecyclerView recyclerView = findViewById(R.id.rvComidas);
        FloatingActionButton fab = findViewById(R.id.fabAddComida);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ComidaAdapter adapter = new ComidaAdapter();
        recyclerView.setAdapter(adapter);

        comidaViewModel = new ViewModelProvider(this).get(ComidaViewModel.class);

        createNotificationChannel();
        checkNotificationPermission();

        comidaViewModel.getAllComidas().observe(this, comidas -> {
            adapter.setComidas(comidas);
            updateCalories(comidas);
            tvEmpty.setVisibility(comidas.isEmpty() ? View.VISIBLE : View.GONE);

            // Trigger WorkManager to check calories limit
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CalorieWorker.class).build();
            WorkManager.getInstance(this).enqueue(workRequest);
        });

        adapter.setOnItemClickListener(new ComidaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Comida comida) {
                showAddFoodDialog(comida);
            }

            @Override
            public void onDeleteClick(Comida comida) {
                showDeleteConfirmationDialog(comida);
            }
        });

        fab.setOnClickListener(v -> showAddFoodDialog(null));

        LinearLayout bottomNav = findViewById(R.id.bottomNav);
        bottomNav.getChildAt(2).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        String nombre = getIntent().getStringExtra("nombre");
        if (nombre != null) {
            tvWelcome.setText(getString(R.string.hola_usuario_con_nombre, nombre));
        } else {
            tvWelcome.setText(R.string.hola_usuario);
        }
    }

    private void updateCalories(List<Comida> comidas) {
        int consumidas = 0;
        for (Comida c : comidas) {
            consumidas += c.getCalorias();
        }
        tvConsumed.setText(String.valueOf(consumidas));
        tvRemaining.setText(String.valueOf(objetivoDiario - consumidas));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CalorieWorker.CHANNEL_ID,
                    getString(R.string.notif_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(getString(R.string.notif_channel_desc));
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void showAddFoodDialog(Comida comidaToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_comida, null);
        builder.setView(dialogView);

        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        EditText etName = dialogView.findViewById(R.id.etFoodName);
        EditText etCalories = dialogView.findViewById(R.id.etFoodCalories);

        if (comidaToEdit != null) {
            tvTitle.setText(R.string.editar_alimento);
            etName.setText(comidaToEdit.getNombre());
            etCalories.setText(String.valueOf(comidaToEdit.getCalorias()));
        }

        builder.setPositiveButton(comidaToEdit == null ? R.string.agregar : R.string.actualizar, (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String caloriesStr = etCalories.getText().toString().trim();

            if (!name.isEmpty() && !caloriesStr.isEmpty()) {
                int calories = Integer.parseInt(caloriesStr);
                if (comidaToEdit == null) {
                    comidaViewModel.insert(new Comida(name, calories));
                } else {
                    comidaToEdit.setNombre(name);
                    comidaToEdit.setCalorias(calories);
                    comidaViewModel.update(comidaToEdit);
                }
            } else {
                Toast.makeText(this, R.string.error_completar_campos, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancelar, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog(Comida comida) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.eliminar_alimento)
                .setMessage(R.string.confirmar_eliminacion)
                .setPositiveButton(R.string.eliminar, (dialog, which) -> {
                    comidaViewModel.delete(comida);
                    showUndoSnackbar(comida);
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void showUndoSnackbar(Comida comida) {
        Snackbar.make(findViewById(R.id.main), R.string.alimento_eliminado, Snackbar.LENGTH_LONG)
                .setAction(R.string.deshacer, v -> comidaViewModel.insert(comida))
                .show();
    }
}