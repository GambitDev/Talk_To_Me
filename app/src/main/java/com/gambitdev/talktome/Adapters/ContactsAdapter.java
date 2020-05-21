package com.gambitdev.talktome.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gambitdev.talktome.Pojo.Contact;
import com.gambitdev.talktome.R;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private ArrayList<Contact> contacts;

    public void setContacts(ArrayList<Contact> contacts) {
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
        if (currentContact.getProfilePic() != null)
            holder.profilePic.setImageBitmap(currentContact.getProfilePic());
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

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic);
            contactName = itemView.findViewById(R.id.contact_name);
        }
    }
}
