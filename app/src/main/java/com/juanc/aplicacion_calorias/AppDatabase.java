package com.juanc.aplicacion_calorias;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
}