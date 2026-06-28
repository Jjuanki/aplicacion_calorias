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

    @Query("SELECT * FROM comidas ORDER BY id DESC")
    LiveData<List<Comida>> getAllComidas();

    @Query("SELECT * FROM comidas")
    List<Comida> getAllComidasSync();
}