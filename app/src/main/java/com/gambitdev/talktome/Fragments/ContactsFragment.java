package com.gambitdev.talktome.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gambitdev.talktome.Activities.HomeActivity;
import com.gambitdev.talktome.Adapters.ContactsAdapter;
import com.gambitdev.talktome.DataManager.ContactsViewModel;
import com.gambitdev.talktome.Interfaces.OnContactClick;
import com.gambitdev.talktome.R;

public class ContactsFragment extends Fragment
        implements OnContactClick {

    private Context mContext;

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
        viewModel.getAllContacts().observe(getViewLifecycleOwner(),
                adapter::setContacts);
    }

    @Override
    public void onDisplayNameClick(String contactUid , String contactName) {
        ((HomeActivity) mContext).startChat(contactUid , contactName);
    }

    @Override
    public void onProfilePicClick(String uid) {
        ((HomeActivity) mContext).goToContactProfile(uid);
    }
}
