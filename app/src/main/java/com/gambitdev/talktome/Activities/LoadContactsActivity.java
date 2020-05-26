package com.gambitdev.talktome.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.gambitdev.talktome.Pojo.Contact;
import com.gambitdev.talktome.Pojo.PhoneContact;
import com.gambitdev.talktome.Pojo.User;
import com.gambitdev.talktome.R;
import com.gambitdev.talktome.DataManager.ContactsViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoadContactsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference().child("users");
    private ValueEventListener eventListener;
    private ContactsViewModel viewModel;
    private ArrayList<PhoneContact> phoneContacts;
    private ArrayList<User> users;

    private final static int REQUEST_CONTACTS_ACCESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_contacts);

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    users.add(user);
                }
                requestPermissions();
                if (phoneContacts != null) {
                    viewModel.insertContactList(getRegisteredContacts(phoneContacts, users));
                }
                Intent goToHomeActivity = new Intent(
                        LoadContactsActivity.this,
                        HomeActivity.class
                );
                startActivity(goToHomeActivity);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoadContactsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        reference.addValueEventListener(eventListener);
    }

    @AfterPermissionGranted(REQUEST_CONTACTS_ACCESS)
    private void requestPermissions() {
        if (EasyPermissions.hasPermissions(this , Manifest.permission.READ_CONTACTS)) {
            phoneContacts = getContacts(this);
        } else {
            EasyPermissions.requestPermissions(this,
                    "Access to your contacts is necessary to use Talk To Me",
                    REQUEST_CONTACTS_ACCESS,
                    Manifest.permission.READ_CONTACTS);
        }
    }

    private ArrayList<PhoneContact> getContacts(Context ctx) {
        ArrayList<PhoneContact> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);

                    if (cursorInfo != null) {
                        while (cursorInfo.moveToNext()) {
                            String name = cursor.getString(cursor.getColumnIndex(
                                    ContactsContract.Contacts.DISPLAY_NAME));
                            String phoneNumber = cursorInfo.getString(
                                    cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            PhoneContact current = new PhoneContact(name, phoneNumber);
                            list.add(current);
                        }
                        cursorInfo.close();
                    }
                }
            }
            cursor.close();
        }
        return list;
    }

    private ArrayList<Contact> getRegisteredContacts(ArrayList<PhoneContact> phoneContacts,
                                                     ArrayList<User> registeredUsers) {
        ArrayList<Contact> registeredContacts = new ArrayList<>();
        if (phoneContacts != null) {
            for (int i = 0; i < phoneContacts.size(); i++) {
                Contact contact = createContact(phoneContacts.get(i), registeredUsers);
                if (contact != null) {
                    registeredContacts.add(contact);
                }
            }
        }
        return registeredContacts;
    }

    private Contact createContact(PhoneContact phoneContact , List<User> registeredUsers) {
        String currentContactPhoneNumber = cleanPhoneNumber(phoneContact.getPhoneNumber());
        for (int i = 0; i < registeredUsers.size(); i++) {
            String currentRegisteredUserPhoneNumber = cleanPhoneNumber(registeredUsers.get(i).getPhoneNumber());
            if (currentContactPhoneNumber.equals(currentRegisteredUserPhoneNumber)) {
                String contactName = phoneContact.getName();
                String contactPhone = phoneContact.getPhoneNumber();
                String uid = registeredUsers.get(i).getUid();
                Bitmap profilePic = registeredUsers.get(i).getProfilePic();
                return new Contact(uid , contactName , contactPhone , profilePic);
            }
        }
        return null;
    }

    private String cleanPhoneNumber(String originalPhoneNumber) {
        return originalPhoneNumber.replaceAll("[^0-9+]", "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.removeEventListener(eventListener);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
