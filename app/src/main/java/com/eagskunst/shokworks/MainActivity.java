package com.eagskunst.shokworks;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.eagskunst.shokworks.fragments.NewsFragment;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar,R.string.app_name,false);
        TabLayout tabs = findViewById(R.id.tabs);
        startTabs(tabs);
        makeFragmentTransaction(NewsFragment.newInstance(false));
    }

    private void setToolbar(Toolbar toolbar, int title, boolean setHomeButton) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(setHomeButton);
    }

    private void startTabs(TabLayout tabs) {
        tabs.addTab(tabs.newTab().setText(R.string.lastest).setTag("Last"));
        tabs.addTab(tabs.newTab().setText(R.string.favorites).setTag("Saved"));
    }

    private void makeFragmentTransaction(NewsFragment newsFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,newsFragment,"LAST")
                .addToBackStack(null)
                .commit();
    }


}
