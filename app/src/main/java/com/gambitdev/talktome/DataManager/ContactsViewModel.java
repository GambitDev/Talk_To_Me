package com.gambitdev.talktome.DataManager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gambitdev.talktome.Models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private ContactsRepository repository;
    private LiveData<List<Contact>> contacts;

    public ContactsViewModel(@NonNull Application application) {
        super(application);

        repository = new ContactsRepository(application);
        contacts = repository.getAllContacts();
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }

    public void insertContact(Contact contact) {
        repository.insertContact(contact);
    }

    public void insertContactList(List<Contact> contacts) {
        repository.insertContactList(contacts);
    }
}
