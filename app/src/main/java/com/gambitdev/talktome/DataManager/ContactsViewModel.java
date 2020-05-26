package com.gambitdev.talktome.DataManager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gambitdev.talktome.Pojo.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private ContactsRepository repository;
    private LiveData<List<Contact>> contactsData;

    public ContactsViewModel(@NonNull Application application) {
        super(application);

        repository = new ContactsRepository(application);
        contactsData = repository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return repository.getAllContacts();
    }

    public void insertContactList(ArrayList<Contact> contacts) {
        repository.insertContactList(contacts);
    }
}
