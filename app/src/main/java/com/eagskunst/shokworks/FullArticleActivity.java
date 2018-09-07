package com.eagskunst.shokworks;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eagskunst.shokworks.objects.Article;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FullArticleActivity extends AppCompatActivity {

    private final String TAG = "FullArticleActivity";
    private boolean isSaved = false;
    private WebView webView;
    private SharedPreferences sharedPreferences;
    private List<Article> savedList = new ArrayList<>();
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("USER_PREFERENCES",MODE_PRIVATE);
        loadSavedList();
        setContentView(R.layout.activity_full_article);
        String url = getIntent().getExtras().getString("url");
        article = getIntent().getExtras().getParcelable("article");
        webView = findViewById(R.id.mainwebview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar,url,true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String list = gson.toJson(savedList);
                editor.remove("savedList").commit();
                editor.putString("savedList",list).commit();
                Log.d(TAG, "onClick: saved!"+article.getTitle());
                finish();
            }
        });


        configureWebView();
        webView.loadUrl(url);
        webView.setVisibility(View.VISIBLE);
    }


    private void loadSavedList() {
        String list = sharedPreferences.getString("savedList",null);
        Gson gson = new Gson();

        Type type = new TypeToken<List<Article>>(){}.getType();

        List<Article> retrievedList = gson.fromJson(list,type);
        savedList.addAll(retrievedList);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_buttons,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:{
                isSaved = true;
                savedList.add(article);
                Log.d(TAG, "onOptionsItemSelected: size: "+savedList.size());
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setToolbar(Toolbar toolbar, String title, boolean setHomeButton) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(setHomeButton);
    }


    private void configureWebView() {
        webView.setWebViewClient(new WebViewClient());
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }
}
