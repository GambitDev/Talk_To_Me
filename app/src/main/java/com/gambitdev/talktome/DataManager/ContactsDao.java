package com.gambitdev.talktome.DataManager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.gambitdev.talktome.Models.Contact;
import com.gambitdev.talktome.Models.User;

import java.util.ArrayList;
import java.util.List;

@Dao
interface ContactsDao {

    @Query("SELECT * FROM contacts")
    LiveData<List<Contact>> getAllContacts();

    @Query("DELETE FROM contacts")
    void deleteAll();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insertContact(Contact contact);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insertContactList(List<Contact> contacts);

}
