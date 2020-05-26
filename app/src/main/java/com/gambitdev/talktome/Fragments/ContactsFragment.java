package com.gambitdev.talktome.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gambitdev.talktome.Activities.HomeActivity;
import com.gambitdev.talktome.Adapters.ContactsAdapter;
import com.gambitdev.talktome.Pojo.Contact;
import com.gambitdev.talktome.R;
import com.gambitdev.talktome.DataManager.ContactsViewModel;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class ContactsFragment extends Fragment
        implements EasyPermissions.PermissionCallbacks,
        ContactsAdapter.OnContactClick {

    private Context mContext;

//    private final static int REQUEST_CONTACTS_ACCESS = 0;
//    private FirebaseDatabase db = FirebaseDatabase.getInstance();
//    private DatabaseReference reference = db.getReference().child("users");
//    private ArrayList<User> users;
//    private ArrayList<Contact> contacts;
//    private ValueEventListener eventListener;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public ContactsFragment(Context mContext) {
        this.mContext = mContext;
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
        adapter.setClickListener(this);
        contactList.setAdapter(adapter);
        ContactsViewModel viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        viewModel.getAllContacts().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        });

//        eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                users = new ArrayList<>();
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    User user = child.getValue(User.class);
//                    users.add(user);
//                }
//                requestPermissions();
//                if (contacts != null) {
//                    view.findViewById(R.id.progress_bar).setVisibility(View.GONE);
//                    view.findViewById(R.id.progress_txt).setVisibility(View.GONE);
//                    adapter.setContacts(getRegisteredContacts(contacts, users));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        };
//        reference.addValueEventListener(eventListener);
    }

//    @AfterPermissionGranted(REQUEST_CONTACTS_ACCESS)
//    private void requestPermissions() {
//        if (EasyPermissions.hasPermissions(mContext , Manifest.permission.READ_CONTACTS)) {
//            contacts = getContacts(mContext);
//        } else {
//            EasyPermissions.requestPermissions(this,
//                    "Access to your contacts is necessary to use Talk To Me",
//                    REQUEST_CONTACTS_ACCESS,
//                    Manifest.permission.READ_CONTACTS);
//        }
//    }

//    private ArrayList<Contact> getContacts(Context ctx) {
//        ArrayList<Contact> list = new ArrayList<>();
//        ContentResolver contentResolver = ctx.getContentResolver();
//        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null,
//                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
//        if (cursor != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
//                    Cursor cursorInfo = contentResolver
//                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                                    new String[]{id}, null);
//
//                    if (cursorInfo != null) {
//                        while (cursorInfo.moveToNext()) {
//                            String name = cursor.getString(cursor.getColumnIndex(
//                                    ContactsContract.Contacts.DISPLAY_NAME));
//                            String phoneNumber = cursorInfo.getString(
//                                    cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            Contact current = new Contact(name, phoneNumber);
//                            list.add(current);
//                        }
//                        cursorInfo.close();
//                    }
//                }
//            }
//            cursor.close();
//        }
//        return list;
//    }

//    private ArrayList<Contact> getRegisteredContacts(ArrayList<Contact> phoneContacts,
//                                                     ArrayList<User> registeredUsers) {
//        ArrayList<Contact> registeredContacts = new ArrayList<>();
//        if (phoneContacts != null) {
//            for (int i = 0; i < phoneContacts.size(); i++) {
//                if (isContactRegistered(phoneContacts.get(i), registeredUsers)) {
//                    registeredContacts.add(phoneContacts.get(i));
//                }
//            }
//        }
//        return registeredContacts;
//    }

//    private boolean isContactRegistered(Contact contact , List<User> registeredUsers) {
//        String currentContactPhoneNumber = cleanPhoneNumber(contact.getPhoneNumber());
//        for (int i = 0; i < registeredUsers.size(); i++) {
//            String currentRegisteredUserPhoneNumber = cleanPhoneNumber(registeredUsers.get(i).getPhoneNumber());
//            if (currentContactPhoneNumber.equals(currentRegisteredUserPhoneNumber)) {
//                contact.setUid(registeredUsers.get(i).getUid());
//                contact.setProfilePic(registeredUsers.get(i).getProfilePic());
//                return true;
//            }
//        }
//        return false;
//    }

//    private String cleanPhoneNumber(String originalPhoneNumber) {
//        return originalPhoneNumber.replaceAll("[^0-9+]", "");
//    }

    @Override
    public void onDisplayNameClick(String contactUid , String contactName) {
        ((HomeActivity) mContext).startChat(contactUid , contactName);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        reference.removeEventListener(eventListener);
//    }

    @Override
    public void onProfilePicClick() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

}
