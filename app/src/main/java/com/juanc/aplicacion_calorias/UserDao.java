package com.juanc.aplicacion_calorias;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    long insertUser(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND password = :password LIMIT 1")
    Usuario login(String correo, String password);

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    Usuario findByEmail(String correo);
}