package com.eagskunst.shokworks.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eagskunst.shokworks.FullArticleActivity;
import com.eagskunst.shokworks.MainActivity;
import com.eagskunst.shokworks.R;
import com.eagskunst.shokworks.adapters.NewsAdapter;
import com.eagskunst.shokworks.objects.Article;
import com.eagskunst.shokworks.objects.Info;
import com.eagskunst.shokworks.utility.ArticlesLoader;
import com.eagskunst.shokworks.utility.PreferencesHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class NewsFragment extends Fragment implements MainActivity.FragmentChanged{

    private boolean isSavedFragment;
    private final String TAG = "NewsFragment";
    private final String URL = "https://newsapi.org/v2/";
    private ArticlesLoader.onFinishListener onArticlesLoaded;
    private NewsAdapter newsAdapter;
    private List<Article> articleList = new ArrayList<>();

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(boolean isSavedFragment) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putBoolean("is_saved",isSavedFragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSavedFragment = getArguments().getBoolean("is_saved");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        if(!isSavedFragment){
            onArticlesLoaded = new ArticlesLoader.onFinishListener() {
                @Override
                public void doOnFinish(Info info) {
                    articleList = info.getArticleList();
                    try{
                        newsAdapter.setArticleList(articleList);
                        Log.d(TAG, "doOnFinish: Enter onFinish"+articleList.get(0).getTitle());
                    }catch(NullPointerException e){
                        Toast.makeText(getActivity(), R.string.failed_retrieve, Toast.LENGTH_SHORT).show();
                    }finally {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newsAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            };
            if(isConnectedToInternet(getActivity().getApplicationContext())){
                ArticlesLoader loader = new ArticlesLoader(URL,onArticlesLoaded);
                loader.makeCall();
                loader.articlesOnCallback();
            }
            else{
                Toast.makeText(getActivity(), R.string.connect_internet, Toast.LENGTH_SHORT).show();
                Timer timer = new Timer();
                timer.schedule(connectionTask(timer),0,4000);
            }
        }
        else {
            onFragmentShow();
        }
        newsAdapter = new NewsAdapter(articleList,viewArticle());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        manageRecyclerView(recyclerView);
        return view;
    }

    private TimerTask connectionTask(final Timer timer) {
        return new TimerTask() {
            @Override
            public void run() {
                if(isConnectedToInternet(getActivity().getApplicationContext())){
                    Log.d(TAG, "run: enter timer task");
                    ArticlesLoader loader = new ArticlesLoader(URL,onArticlesLoaded);
                    loader.makeCall();
                    loader.articlesOnCallback();
                    if(articleList.isEmpty())
                        Toast.makeText(getActivity(), R.string.failed_retrieve, Toast.LENGTH_SHORT).show();
                    timer.cancel();
                }
            }
        };
    }

    private NewsAdapter.NewsViewHolder.AdapterClickLister viewArticle() {
        return new NewsAdapter.NewsViewHolder.AdapterClickLister() {
            @Override
            public void onItemClickListener(Article article) {
                Intent i = new Intent(getActivity(),FullArticleActivity.class);
                i.putExtra("url",article.getUrl());
                i.putExtra("article",article);
                if(isSavedFragment)
                    startActivityForResult(i,1);
                else
                    startActivity(i);
            }
        };
    }


    private void manageRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onFragmentShow() {
        articleList.clear();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_PREFERENCES",Context.MODE_PRIVATE);
        articleList.addAll(PreferencesHandler.loadList(sharedPreferences));
        if(newsAdapter != null)
            newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == 1){
            onFragmentShow();
        }
    }
    private boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
