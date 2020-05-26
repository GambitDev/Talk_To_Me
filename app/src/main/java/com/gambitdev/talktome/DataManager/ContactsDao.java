package com.gambitdev.talktome.DataManager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.gambitdev.talktome.Pojo.Contact;

import java.util.ArrayList;
import java.util.List;

@Dao
abstract class ContactsDao {

    @Query("SELECT * FROM contacts")
    abstract LiveData<List<Contact>> getAllContacts();

    @Query("DELETE FROM contacts")
    abstract void deleteAll();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    abstract void insertContact(Contact contact);

    @Transaction
    void insertContactList(ArrayList<Contact> contacts) {
        for (Contact contact : contacts) {
            insertContact(contact);
        }
    }
}
