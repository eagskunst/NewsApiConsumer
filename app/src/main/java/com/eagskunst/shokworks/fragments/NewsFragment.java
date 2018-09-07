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


public class NewsFragment extends Fragment {

    private boolean isSavedFragment;
    private final String TAG = "NewsFragment";
    private final String URL = "https://newsapi.org/v2/top-headlines?" +
            "sources=el-mundo,cnn-es,la-nacion,la-gaceta,infobae,google-news-ar&" +
            "apiKey=8416a9711e5f4969a01f6e1c55ae75da";

    private ArticlesLoader.onFinishListener onArticlesLoaded;
    private NewsAdapter newsAdapter;
    private List<Article> articleList = new ArrayList<>();


    private OnFragmentInteractionListener mListener;

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        loadSavedList();
        newsAdapter.notifyDataSetChanged();
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


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
