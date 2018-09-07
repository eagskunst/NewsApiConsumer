package com.eagskunst.shokworks;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
            Log.d(TAG, "onCreate: enter first_launch");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            List<Article> articleList = new ArrayList<>();
            Gson gson = new Gson();
            String list = gson.toJson(articleList);
            editor.putString("savedList",list).apply();
            editor.putBoolean("first_launch",false).apply();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager viewPager = findViewById(R.id.viewpager);

        PageAdapter pageAdapter = new PageAdapter(this,getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(onPageListener(pageAdapter,viewPager));
        setToolbar(toolbar,R.string.app_name,false);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private ViewPager.OnPageChangeListener onPageListener(final PageAdapter pageAdapter, final ViewPager viewPager) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: position = "+position);
                NewsFragment fragment = (NewsFragment)pageAdapter.instantiateItem(viewPager,1);
                fragment.onFragmentShow();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private void setToolbar(Toolbar toolbar, int title, boolean setHomeButton) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(setHomeButton);
    }

    public interface FragmentChanged{
        void onFragmentShow();
    }
}
