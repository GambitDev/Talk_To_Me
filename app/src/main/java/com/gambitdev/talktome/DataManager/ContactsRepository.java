package com.gambitdev.talktome.DataManager;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.RoomDatabase;

import com.gambitdev.talktome.Pojo.Contact;

import java.util.ArrayList;
import java.util.List;

class ContactsRepository {

    private ContactsDao contactsDao;
    private LiveData<List<Contact>> contacts;

    ContactsRepository(Application application) {
        ContactsDatabase db = ContactsDatabase.getDatabase(application);
        contactsDao = db.contactsDao();
        contacts = contactsDao.getAllContacts();
    }

    LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }

    void insertContactList(ArrayList<Contact> contacts) {
        ContactsDatabase.databaseWriteExecutor.execute(() ->
                contactsDao.insertContactList(contacts));
    }
}
