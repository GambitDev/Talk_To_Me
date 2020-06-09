package com.gambitdev.talktome.DataManager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.gambitdev.talktome.Models.Contact;
import com.gambitdev.talktome.Models.User;

import java.util.ArrayList;
import java.util.List;

@Dao
abstract class MyDao {

    @Query("SELECT * FROM contacts")
    abstract LiveData<List<Contact>> getAllContacts();

    @Query("DELETE FROM contacts")
    abstract void deleteAllContacts();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    abstract void insertContact(Contact contact);

    @Transaction
    void insertContactList(List<Contact> contacts) {
        for (Contact contact : contacts) {
            insertContact(contact);
        }
    }

    @Query("SELECT * FROM users")
    abstract LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM users")
    abstract void deleteAllUsers();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    abstract void insertUser(User user);

    @Transaction
    void insertUserList(List<User> users) {
        for (User user : users) {
            insertUser(user);
        }
    }
}