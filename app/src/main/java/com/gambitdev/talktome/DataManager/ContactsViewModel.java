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

    public ContactsViewModel(@NonNull Application application) {
        super(application);

        repository = new ContactsRepository(application);
    }

    public LiveData<List<Contact>> getAllContacts() {
        return repository.getAllContacts();
    }

    public void insertContactList(List<Contact> contacts) {
        repository.insertContactList(contacts);
    }
}
