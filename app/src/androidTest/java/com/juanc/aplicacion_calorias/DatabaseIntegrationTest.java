package com.juanc.aplicacion_calorias;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseIntegrationTest {
    private UserDao userDao;
    private ComidaDao comidaDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        comidaDao = db.comidaDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        Usuario user = new Usuario("Juan", "juan@test.com", "password123");
        userDao.insertUser(user);
        Usuario byName = userDao.login("juan@test.com", "password123");
        assertNotNull(byName);
        assertEquals(byName.nombre, "Juan");
    }

    @Test
    public void testDataIsolationBetweenUsers() throws Exception {
        // Create two users
        Usuario user1 = new Usuario("User1", "user1@test.com", "pass");
        Usuario user2 = new Usuario("User2", "user2@test.com", "pass");
        
        int id1 = (int) userDao.insertUser(user1);
        int id2 = (int) userDao.insertUser(user2);

        // Insert meals for user 1
        Comida meal1 = new Comida("Manzana", 100, id1);
        comidaDao.insert(meal1);

        // Insert meals for user 2
        Comida meal2 = new Comida("Pizza", 800, id2);
        comidaDao.insert(meal2);

        // Verify user 1 only sees their own meals
        List<Comida> user1Meals = comidaDao.getAllComidasSync(id1);
        assertEquals(1, user1Meals.size());
        assertEquals("Manzana", user1Meals.get(0).getNombre());

        // Verify user 2 only sees their own meals
        List<Comida> user2Meals = comidaDao.getAllComidasSync(id2);
        assertEquals(1, user2Meals.size());
        assertEquals("Pizza", user2Meals.get(0).getNombre());
    }
}
