package com.eagskunst.shokworks.utility;

import android.os.AsyncTask;

import com.eagskunst.shokworks.objects.Info;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ArticlesLoader extends AsyncTask {
    private String URL;
    private Info info;
    private onFinishListener finishListener;

    public ArticlesLoader(String URL, onFinishListener finishListener){
        this.URL = URL;
        this.finishListener = finishListener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Gson gson = new GsonBuilder().create();
        try {
            String json = readUrl(URL);
            info = gson.fromJson(json,Info.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        finishListener.doOnFinish(info);
    }

    private String readUrl(String urlString) throws Exception{
        BufferedReader reader = null;

        try {
            java.net.URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer stringBuffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while((read = reader.read(chars))!= -1){
                stringBuffer.append(chars,0,read);
            }
            return stringBuffer.toString();
        }finally {
            if(reader != null)
                reader.close();
        }
    }

    public interface onFinishListener{
        void doOnFinish(Info info);
    }
}
