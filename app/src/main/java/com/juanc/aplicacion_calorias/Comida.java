package com.juanc.aplicacion_calorias;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comidas")
public class Comida {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private int calorias;

    public Comida(String nombre, int calorias) {
        this.nombre = nombre;
        this.calorias = calorias;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }
}