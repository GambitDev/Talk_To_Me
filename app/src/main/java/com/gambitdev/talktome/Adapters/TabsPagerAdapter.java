package com.gambitdev.talktome.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gambitdev.talktome.Fragments.ChatListFragment;
import com.gambitdev.talktome.Fragments.ContactsFragment;
import com.gambitdev.talktome.R;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES =
            new int[] {R.string.chats,
                    R.string.contacts};
    private final Context mContext;

    public TabsPagerAdapter(Context mContext, @NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = mContext;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ChatListFragment(mContext);
        } else {
            return new ContactsFragment(mContext);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
