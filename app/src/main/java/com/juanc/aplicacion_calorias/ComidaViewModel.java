package com.juanc.aplicacion_calorias;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComidaViewModel extends AndroidViewModel {
    private final ComidaDao comidaDao;
    private final LiveData<List<Comida>> allComidas;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public ComidaViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        comidaDao = db.comidaDao();
        allComidas = comidaDao.getAllComidas();
    }

    public LiveData<List<Comida>> getAllComidas() {
        return allComidas;
    }

    public void insert(Comida comida) {
        executorService.execute(() -> comidaDao.insert(comida));
    }

    public void update(Comida comida) {
        executorService.execute(() -> comidaDao.update(comida));
    }

    public void delete(Comida comida) {
        executorService.execute(() -> comidaDao.delete(comida));
    }
}