package com.eagskunst.shokworks;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String URL = "https://newsapi.org/v2/top-headlines?" +
            "country=us&" +
            "apiKey=8416a9711e5f4969a01f6e1c55ae75da";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabs = findViewById(R.id.tabs);
        startTabs(tabs);
    }

    private void startTabs(TabLayout tabs) {
        tabs.addTab(tabs.newTab());
    }


}
