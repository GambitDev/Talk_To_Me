package com.gambitdev.talktome.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.gambitdev.talktome.Models.Contact;
import com.gambitdev.talktome.Models.PhoneContact;
import com.gambitdev.talktome.Models.User;
import com.gambitdev.talktome.R;
import com.gambitdev.talktome.DataManager.MyViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoadContactsActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference().child("users");
    private ValueEventListener listener;
    private final static int REQUEST_CONTACTS_ACCESS = 0;
    private List<PhoneContact> phoneContacts;
    private List<User> users = new ArrayList<>();
    private MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_contacts);

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        viewModel.deleteAllContacts();
        viewModel.deleteAllUsers();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    users.add(user);
                }
                viewModel.insertUserList(users);
                if (phoneContacts != null) {
                    List<Contact> registeredContacts = getRegisteredContacts();
                    viewModel.insertContactList(registeredContacts);
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
                Toast.makeText(LoadContactsActivity.this,
                        databaseError.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        };
        initContactList();
    }

    @AfterPermissionGranted(REQUEST_CONTACTS_ACCESS)
    private void initContactList() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)) {
            phoneContacts = getPhoneContacts();
            getUsers();
        } else {
            EasyPermissions.requestPermissions(this,
                    "Access to your contacts is necessary to use Talk To Me",
                    REQUEST_CONTACTS_ACCESS,
                    Manifest.permission.READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode , permissions , grantResults , this);
    }

    private List<PhoneContact> getPhoneContacts() {
        ArrayList<PhoneContact> contacts = new ArrayList<>();
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = resolver
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                    if (cursorInfo != null) {
                        while (cursorInfo.moveToNext()) {
                            String contactName = cursorInfo.getString(
                                    cursorInfo.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            String phoneNumber = cursorInfo.getString(
                                    cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            PhoneContact contact = new PhoneContact(contactName , phoneNumber);
                            contacts.add(contact);
                        }
                        cursorInfo.close();
                    }
                }
            }
            cursor.close();
        }
        return contacts;
    }

    private void getUsers() {
        reference.addValueEventListener(listener);
    }

    private ArrayList<Contact> getRegisteredContacts() {
        ArrayList<Contact> registeredContacts = new ArrayList<>();
        if (phoneContacts != null) {
            for (int i = 0; i < phoneContacts.size(); i++) {
                Contact contact = createContact(phoneContacts.get(i));
                if (contact != null) {
                    registeredContacts.add(contact);
                }
            }
        }
        return registeredContacts;
    }

    private Contact createContact(PhoneContact phoneContact) {
        String currentContactPhoneNumber = cleanPhoneNumber(phoneContact.getPhoneNumber());
        for (int i = 0; i < users.size(); i++) {
            String currentRegisteredUserPhoneNumber = cleanPhoneNumber(users.get(i).getPhoneNumber());
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() != null) {
                if (currentContactPhoneNumber.equals(currentRegisteredUserPhoneNumber)
                        && !users.get(i).getUid().equals(mAuth.getCurrentUser().getUid())) {
                    String contactName = phoneContact.getName();
                    String contactPhone = phoneContact.getPhoneNumber();
                    String uid = users.get(i).getUid();
                    String profilePic = users.get(i).getProfilePicUrl();
                    String status = users.get(i).getStatus();
                    return new Contact(uid, contactName, contactPhone, profilePic, status);
                }
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
        reference.removeEventListener(listener);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
