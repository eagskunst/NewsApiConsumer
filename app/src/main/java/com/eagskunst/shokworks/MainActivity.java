package com.eagskunst.shokworks;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.eagskunst.shokworks.fragments.NewsFragment;
import com.eagskunst.shokworks.fragments.PageAdapter;
import com.eagskunst.shokworks.objects.Article;
import com.eagskunst.shokworks.objects.Source;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFERENCES",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("first_launch",true)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            List<Article> articleList = new ArrayList<>();
            Gson gson = new Gson();
            String list = gson.toJson(articleList);
            editor.putString("savedList",list).apply();
            editor.putBoolean("first_launch",false);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager viewPager = findViewById(R.id.viewpager);
        PageAdapter pageAdapter = new PageAdapter(this,getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        setToolbar(toolbar,R.string.app_name,false);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void setToolbar(Toolbar toolbar, int title, boolean setHomeButton) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(setHomeButton);
    }


}
