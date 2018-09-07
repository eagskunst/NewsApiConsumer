package com.eagskunst.shokworks.utility;

import android.content.SharedPreferences;

import com.eagskunst.shokworks.objects.Article;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PreferencesHandler {

    public static List<Article> loadList(SharedPreferences sharedPreferences){
        String list = sharedPreferences.getString("savedList",null);
        Gson gson = new Gson();

        Type type = new TypeToken<List<Article>>(){}.getType();

        List<Article> retrievedList = gson.fromJson(list,type);
        return retrievedList;
    }

    public static void saveList(SharedPreferences.Editor editor, List<Article> savedList){
        Gson gson = new Gson();
        String list = gson.toJson(savedList);
        editor.putString("savedList",list).apply();
    }
}
