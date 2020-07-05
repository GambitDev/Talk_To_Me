package com.gambitdev.talktome.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gambitdev.talktome.interfaces.OnContactClick;
import com.gambitdev.talktome.models.Contact;
import com.gambitdev.talktome.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<Contact> contacts;
    private OnContactClick clickListener;

    public void setClickListener(OnContactClick clickListener) {
        this.clickListener = clickListener;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_cell_layout, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Contact currentContact = contacts.get(position);
        holder.contactName.setText(currentContact.getName());
        holder.contactName.setOnClickListener(v ->
                clickListener.onDisplayNameClick(currentContact.getUid(),
                        currentContact.getName())
        );
        if (currentContact.getProfilePicUrl() != null)
            Picasso.get().load(currentContact.getProfilePicUrl()).into(holder.profilePic);
        holder.profilePic.setOnClickListener(v ->
                clickListener.onProfilePicClick(currentContact.getUid()));
    }

    @Override
    public int getItemCount() {
        if (contacts == null)
            return 0;
        else
            return contacts.size();
    }

    static class ContactsViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView contactName;

        ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic);
            contactName = itemView.findViewById(R.id.contact_name);
        }
    }
}
