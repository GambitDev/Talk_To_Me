package com.gambitdev.talktome.DataManager;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gambitdev.talktome.Models.Contact;
import com.gambitdev.talktome.Models.User;

import java.util.List;

class MyRepository {

    private MyDao dao;
    private LiveData<List<Contact>> contacts;
    private LiveData<List<User>> users;

    MyRepository(Application application) {
        MyDatabase db = MyDatabase.getDatabase(application);
        dao = db.dao();
        contacts = dao.getAllContacts();
        users = dao.getAllUsers();
    }

    void deleteAllContacts(){
        MyDatabase.databaseWriteExecutor.execute(() ->
                dao.deleteAllContacts());

    }

    LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }

    void insertContactList(List<Contact> contacts) {
        MyDatabase.databaseWriteExecutor.execute(() ->
                dao.insertContactList(contacts));
    }

    LiveData<List<User>> getAllUsers() {
        return users;
    }

    void deleteAllUsers() {
        MyDatabase.databaseWriteExecutor.execute(() ->
                dao.deleteAllUsers());
    }

    void insertUserList(List<User> users) {
        MyDatabase.databaseWriteExecutor.execute(() ->
                dao.insertUserList(users));
    }
}
