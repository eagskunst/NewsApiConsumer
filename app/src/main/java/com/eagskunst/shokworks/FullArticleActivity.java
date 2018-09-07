package com.eagskunst.shokworks;

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
import android.widget.Toast;

import com.eagskunst.shokworks.objects.Article;
import com.eagskunst.shokworks.utility.PreferencesHandler;

import java.util.ArrayList;
import java.util.List;

public class FullArticleActivity extends AppCompatActivity {

    private final String TAG = "FullArticleActivity";
    private boolean isSaved;
    private WebView webView;
    private SharedPreferences sharedPreferences;
    private List<Article> savedList = new ArrayList<>();
    private Article article;
    private MenuItem savedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("USER_PREFERENCES",MODE_PRIVATE);
        savedList.addAll(PreferencesHandler.loadList(sharedPreferences));

        setContentView(R.layout.activity_full_article);
        String url = getIntent().getExtras().getString("url");
        article = getIntent().getExtras().getParcelable("article");
        isSaved = checkIfArticleExist();
        webView = findViewById(R.id.mainwebview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar,url,true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferencesHandler.saveList(sharedPreferences.edit(),savedList);
                Log.d(TAG, "onClick: saved!"+article.getTitle());
                finish();
            }
        });


        configureWebView();
        webView.loadUrl(url);
        webView.setVisibility(View.VISIBLE);
    }


    private boolean checkIfArticleExist() {
        int size = savedList.size();
        for(int i = 0;i<size;i++){
            if(savedList.get(i).getUrl().equals(article.getUrl())){
                this.article = savedList.get(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_buttons,menu);
        savedItem = menu.getItem(0);
        if (isSaved)
            savedItem.setIcon(R.drawable.ic_action_save);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:{
                if(!isSaved){
                    isSaved = true;
                    savedList.add(0,article);
                    Log.d(TAG, "onOptionsItemSelected: size: "+savedList.size());
                    savedItem.setIcon(R.drawable.ic_action_save);
                    Toast.makeText(this, R.string.saving, Toast.LENGTH_SHORT).show();
                }
                else {
                    isSaved = false;
                    savedItem.setIcon(R.drawable.ic_action_unsaved);
                    savedList.remove(article);
                }
                setResult(1);
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
