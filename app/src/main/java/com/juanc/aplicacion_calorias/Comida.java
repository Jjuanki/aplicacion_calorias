package com.juanc.aplicacion_calorias;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comidas")
public class Comida {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private int calorias;
    private long timestamp;
    private int usuarioId;

    public Comida(String nombre, int calorias, int usuarioId) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.usuarioId = usuarioId;
        this.timestamp = System.currentTimeMillis();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
}