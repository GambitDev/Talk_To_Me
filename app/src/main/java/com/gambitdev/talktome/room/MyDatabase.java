package com.gambitdev.talktome.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gambitdev.talktome.models.Contact;
import com.gambitdev.talktome.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class, User.class} , version = 4 , exportSchema = false)
abstract class MyDatabase extends RoomDatabase {

    abstract MyDao dao();

    private static volatile MyDatabase instance = null;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MyDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (MyDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class,
                            "db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback initializeDatabase = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            databaseWriteExecutor.execute(() -> {
                MyDao contactsDao = instance.dao();
                contactsDao.deleteAllContacts();
            });
        }
    };
}
