package com.juanc.aplicacion_calorias;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ComidaDao {
    @Insert
    void insert(Comida comida);

    @Update
    void update(Comida comida);

    @Delete
    void delete(Comida comida);

    @Query("SELECT * FROM comidas WHERE usuarioId = :userId ORDER BY id DESC")
    LiveData<List<Comida>> getAllComidas(int userId);

    @Query("SELECT * FROM comidas WHERE usuarioId = :userId AND timestamp >= :since")
    LiveData<List<Comida>> getComidasSince(int userId, long since);

    @Query("SELECT * FROM comidas WHERE usuarioId = :userId")
    List<Comida> getAllComidasSync(int userId);
}