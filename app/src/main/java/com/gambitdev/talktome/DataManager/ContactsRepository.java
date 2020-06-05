package com.gambitdev.talktome.DataManager;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gambitdev.talktome.Models.Contact;

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

    void insertContactList(List<Contact> contacts) {
        ContactsDatabase.databaseWriteExecutor.execute(() ->
                contactsDao.insertContactList(contacts));
    }
}
