package com.gambitdev.talktome.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.gambitdev.talktome.Adapters.TabsPagerAdapter;
import com.gambitdev.talktome.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeActivity();
    }

    private void initializeActivity() {
        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager , (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.chats);
                tab.setIcon(R.drawable.ic_chat);
            } else {
                tab.setText(R.string.contacts);
                tab.setIcon(R.drawable.ic_contacts);
            }
        }).attach();
    }

    public void startChat(String contactUid , String contactName) {
        Intent goToChat = new Intent(HomeActivity.this , ChatActivity.class);
        goToChat.putExtra("contact_uid" , contactUid);
        goToChat.putExtra("contact_name" , contactName);
        startActivity(goToChat);
    }
}