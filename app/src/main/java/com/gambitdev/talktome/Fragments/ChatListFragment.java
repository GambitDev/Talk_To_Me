package com.gambitdev.talktome.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.gambitdev.talktome.Activities.HomeActivity;
import com.gambitdev.talktome.Adapters.ChatListAdapter;
import com.gambitdev.talktome.DataManager.MyViewModel;
import com.gambitdev.talktome.Dialogs.ChatListOptionsBottomSheet;
import com.gambitdev.talktome.Interfaces.OnChatClicked;
import com.gambitdev.talktome.Interfaces.OnChatListOptions;
import com.gambitdev.talktome.Models.ChatListItem;
import com.gambitdev.talktome.Models.Contact;
import com.gambitdev.talktome.Models.User;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatListFragment extends Fragment
        implements OnChatClicked , OnChatListOptions {

    private Context mContext;
    private ChatListAdapter adapter;
    private List<Contact> contactList;
    private List<User> userList;
    private FirebaseRecyclerOptions<ChatListItem> options;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ChatListFragment() {
        // Required empty public constructor
    }

    public ChatListFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MyViewModel viewModel = new ViewModelProvider(this)
                .get(MyViewModel.class);
        viewModel.getAllContacts().observe(getViewLifecycleOwner() , contacts ->
                contactList = contacts);
        viewModel.getAllUsers().observe(getViewLifecycleOwner(), users ->
                userList = users);

        if (mAuth.getCurrentUser() != null) {
            String userUid = mAuth.getCurrentUser().getUid();
            DatabaseReference userChatRef = db.getReference()
                    .child("users").child(userUid).child("chats");
            options = new FirebaseRecyclerOptions.Builder<ChatListItem>()
                    .setQuery(userChatRef, new SnapshotParser<ChatListItem>() {
                        @NonNull
                        @Override
                        public ChatListItem parseSnapshot(@NonNull DataSnapshot snapshot) {
                            Contact currentContact = getContactById(snapshot.getKey());
                            if (currentContact != null) {
                                return new ChatListItem(currentContact.getUid(),
                                        currentContact.getName(),
                                        currentContact.getPhoneNumber(),
                                        snapshot.child("last_msg").child("txtMsg").getValue(String.class),
                                        currentContact.getProfilePicUrl(),
                                        true);
                            } else {
                                User currentUser = getUserById(snapshot.getKey());
                                return new ChatListItem(currentUser.getUid(),
                                        currentUser.getPhoneNumber(),
                                        currentUser.getPhoneNumber(),
                                        snapshot.child("last_msg").child("txtMsg").getValue(String.class),
                                        currentUser.getProfilePicUrl(),
                                        false);
                            }
                        }
                    })
                    .build();
        }

        RecyclerView chatList = view.findViewById(R.id.chat_list_container);
        adapter = new ChatListAdapter(options , mContext);
        adapter.setListener(this);
        chatList.setAdapter(adapter);
    }

    private Contact getContactById (String uid) {
        for (Contact contact : contactList) {
            if (contact.getUid().equals(uid)) return contact;
        }
        return null;
    }

    private User getUserById (String uid) {
        for (User user : userList) {
            if (user.getUid().equals(uid)) return user;
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDisplayNameClick(String contactUid, String contactName) {
        ((HomeActivity) mContext).startChat(contactUid , contactName);
    }

    @Override
    public void onProfilePicClick(String uid) {
        ((HomeActivity) mContext).goToContactProfile(uid);
    }

    @Override
    public void onLongClicked(String uid , boolean isContact , String phoneNumber) {
        ChatListOptionsBottomSheet bottomSheet = ChatListOptionsBottomSheet
                .newInstance(uid , isContact , phoneNumber);
        bottomSheet.setListener(this);
        bottomSheet.show(getParentFragmentManager() , "chat_options_dialog");
    }

    @Override
    public void deleteChat(String uid) {
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference userChatRef = db.getReference()
                    .child("users")
                    .child(mAuth.getCurrentUser().getUid())
                    .child("chats")
                    .child(uid);
            userChatRef.removeValue();
        }
    }

    @Override
    public void addContact(String phoneNumber) {
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);
        startActivity(intent);
    }
}
