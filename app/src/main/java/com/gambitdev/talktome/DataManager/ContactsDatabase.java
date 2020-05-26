package com.gambitdev.talktome.DataManager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gambitdev.talktome.Pojo.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class} , version = 1 , exportSchema = false)
@TypeConverters({Converters.class})
abstract class ContactsDatabase extends RoomDatabase {

    abstract ContactsDao contactsDao();

    private static volatile ContactsDatabase instance = null;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ContactsDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (ContactsDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            ContactsDatabase.class,
                            "contacts_db")
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
                ContactsDao contactsDao = instance.contactsDao();
                contactsDao.deleteAll();
            });
        }
    };
}
