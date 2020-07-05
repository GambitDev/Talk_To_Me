package com.gambitdev.talktome.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gambitdev.talktome.models.Contact;
import com.gambitdev.talktome.models.User;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    private MyRepository repository;
    private LiveData<List<Contact>> contacts;
    private LiveData<List<User>> users;

    public MyViewModel(@NonNull Application application) {
        super(application);

        repository = new MyRepository(application);
        contacts = repository.getAllContacts();
        users = repository.getAllUsers();
    }

    public void deleteAllContacts(){
        repository.deleteAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }

    public void insertContactList(List<Contact> contacts) {
        repository.insertContactList(contacts);
    }

    public void deleteAllUsers() {
        repository.deleteAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return users;
    }

    public void insertUserList(List<User> users) {
        repository.insertUserList(users);
    }
}
