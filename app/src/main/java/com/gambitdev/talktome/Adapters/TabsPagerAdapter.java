package com.gambitdev.talktome.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gambitdev.talktome.Fragments.ChatListFragment;
import com.gambitdev.talktome.Fragments.ContactsFragment;

public class TabsPagerAdapter extends FragmentStateAdapter {

    private final Context mContext;

    public TabsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mContext = fragmentActivity;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ChatListFragment(mContext);
        } else {
            return new ContactsFragment(mContext);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
