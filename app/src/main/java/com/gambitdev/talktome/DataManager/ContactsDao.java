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
abstract class ContactsDao {

    @Query("SELECT * FROM contacts")
    abstract LiveData<List<Contact>> getAllContacts();

    @Query("DELETE FROM contacts")
    abstract void deleteAll();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    abstract void insertContact(Contact contact);

    @Transaction
    void insertContactList(List<Contact> contacts) {
        for (Contact contact : contacts) {
            insertContact(contact);
        }
    }
}
