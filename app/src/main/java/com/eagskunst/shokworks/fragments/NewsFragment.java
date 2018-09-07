package com.eagskunst.shokworks.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eagskunst.shokworks.FullArticleActivity;
import com.eagskunst.shokworks.MainActivity;
import com.eagskunst.shokworks.R;
import com.eagskunst.shokworks.adapters.NewsAdapter;
import com.eagskunst.shokworks.objects.Article;
import com.eagskunst.shokworks.objects.Info;
import com.eagskunst.shokworks.utility.ArticlesLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment implements MainActivity.FragmentChanged{

    private boolean isSavedFragment;
    private final String TAG = "NewsFragment";
    private final String URL = "https://newsapi.org/v2/top-headlines?" +
            "sources=el-mundo,cnn-es,la-nacion,la-gaceta,infobae,google-news-ar&" +
            "apiKey=8416a9711e5f4969a01f6e1c55ae75da";

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
                    newsAdapter.setArticleList(articleList);
                    Log.d(TAG, "doOnFinish: Enter onFinish"+articleList.get(0).getTitle());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            newsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };
            ArticlesLoader loader = new ArticlesLoader(URL,onArticlesLoaded);
            loader.execute(new Object());
        }
        else {
            loadSavedList();
        }
        newsAdapter = new NewsAdapter(articleList,viewArticle());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        manageRecyclerView(recyclerView);
        return view;
    }

    private void loadSavedList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_PREFERENCES",Context.MODE_PRIVATE);
        String list = sharedPreferences.getString("savedList",null);
        Gson gson = new Gson();

        Type type = new TypeToken<List<Article>>(){}.getType();

        List<Article> retrievedList = gson.fromJson(list,type);
        articleList.addAll(retrievedList);
        Log.d(TAG, "loadSavedList: size:"+articleList.size());
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
        loadSavedList();
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == 1){
            Log.d(TAG, "onActivityResult: enter if!!");
            onFragmentShow();
        }
    }
}
