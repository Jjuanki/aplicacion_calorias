package com.juanc.aplicacion_calorias;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String correo;
    public String password;

    public Usuario(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }
}