package com.gambitdev.talktome.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gambitdev.talktome.Activities.HomeActivity;
import com.gambitdev.talktome.Adapters.ChatListAdapter;
import com.gambitdev.talktome.DataManager.ContactsViewModel;
import com.gambitdev.talktome.Interfaces.OnContactClick;
import com.gambitdev.talktome.Models.ChatListItem;
import com.gambitdev.talktome.Models.Contact;
import com.gambitdev.talktome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatListFragment extends Fragment
        implements OnContactClick {

    private Context mContext;
    private ChatListAdapter adapter;
    private List<Contact> contactList;
    private FirebaseRecyclerOptions<ChatListItem> options;

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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ContactsViewModel viewModel = new ViewModelProvider(this)
                .get(ContactsViewModel.class);
        viewModel.getAllContacts().observe(getViewLifecycleOwner() , contacts ->
                contactList = contacts);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userUid = mAuth.getCurrentUser().getUid();
            DatabaseReference userChatRef = db.getReference()
                    .child("users").child(userUid).child("chats");
            options = new FirebaseRecyclerOptions.Builder<ChatListItem>()
                    .setQuery(userChatRef, snapshot -> {
                        Contact currentContact = getContactById(snapshot.getKey());
                        return new ChatListItem(
                                currentContact != null ? currentContact.getUid() : null,
                                currentContact != null ? currentContact.getName() : null,
                                snapshot.child("last_msg").child("txtMsg").getValue(String.class),
                                currentContact != null ? currentContact.getProfilePicUrl() : null
                        );
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
    public void onProfilePicClick() {

    }
}
