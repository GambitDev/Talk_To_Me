package com.gambitdev.talktome.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gambitdev.talktome.Adapters.ContactsAdapter;
import com.gambitdev.talktome.Pojo.Contact;
import com.gambitdev.talktome.Pojo.User;
import com.gambitdev.talktome.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ContactsFragment extends Fragment  implements EasyPermissions.PermissionCallbacks {

    private final static int REQUEST_CONTACTS_ACCESS = 0;
    private Context mContext;

    private DatabaseReference reference;
    private ArrayList<User> users;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public ContactsFragment(Context mContext) {
        this.mContext = mContext;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView contactList = view.findViewById(R.id.contact_list_container);
        contactList.setLayoutManager(new LinearLayoutManager(mContext));
        ContactsAdapter adapter = new ContactsAdapter();
        contactList.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    users.add(user);
                }
                ArrayList<Contact> contacts = readContacts(mContext);
                adapter.setContacts(getRegisteredContacts(contacts , users));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @AfterPermissionGranted(REQUEST_CONTACTS_ACCESS)
    private ArrayList<Contact> getContacts() {
        if (EasyPermissions.hasPermissions(mContext , Manifest.permission.READ_CONTACTS)) {
            return readContacts(mContext);
        } else {
            EasyPermissions.requestPermissions(this,
                    "Access to your contacts is necessary to use Talk To Me",
                    REQUEST_CONTACTS_ACCESS,
                    Manifest.permission.READ_CONTACTS);
        }
        return null;
    }

    private ArrayList<Contact> readContacts(Context ctx) {
        ArrayList<Contact> list = new ArrayList<>();
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
                            Contact current = new Contact(name, phoneNumber);
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

    private ArrayList<Contact> getRegisteredContacts(ArrayList<Contact> phoneContacts,
                                                     ArrayList<User> registeredUsers) {
        ArrayList<Contact> registeredContacts = new ArrayList<>();
        if (phoneContacts != null) {
            for (int i = 0; i < phoneContacts.size(); i++) {
                if (isContactRegistered(phoneContacts.get(i), registeredUsers)) {
                    registeredContacts.add(phoneContacts.get(i));
                }
            }
        }
        return registeredContacts;
    }

    private boolean isContactRegistered(Contact contact , List<User> registeredUsers) {
        String currentContactPhoneNumber = cleanPhoneNumber(contact.getPhoneNumber());
        for (int i = 0; i < registeredUsers.size(); i++) {
            String currentRegisteredUserPhoneNumber = cleanPhoneNumber(registeredUsers.get(i).getPhoneNumber());
            if (currentContactPhoneNumber.equals(currentRegisteredUserPhoneNumber)) {
                contact.setUid(registeredUsers.get(i).getUid());
                contact.setProfilePic(registeredUsers.get(i).getProfilePic());
                return true;
            }
        }
        return false;
    }

    private String cleanPhoneNumber(String originalPhoneNumber) {
        return originalPhoneNumber.replaceAll("[^0-9+]", "");
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
