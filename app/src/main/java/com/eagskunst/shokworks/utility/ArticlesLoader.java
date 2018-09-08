package com.eagskunst.shokworks.utility;

import com.eagskunst.shokworks.objects.Info;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ArticlesLoader{

    private String URL;
    private onFinishListener finishListener;
    private Call<Info> infoCall;

    public ArticlesLoader(String URL, onFinishListener finishListener){
        if(URL == null || URL.isEmpty())
            URL = "";
        this.URL = URL;
        this.finishListener = finishListener;
    }

    public void makeCall(){
        final String sources = "el-mundo,cnn-es,la-nacion,la-gaceta,infobae,google-news-ar";
        final String apiKey = "8416a9711e5f4969a01f6e1c55ae75da";
        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RestClient restClient = retrofit.create(RestClient.class);
        infoCall = restClient.getData(sources,apiKey);
    }

    public void articlesOnCallback(){
        infoCall.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {
                if(!response.isSuccessful()){
                    finishListener.doOnFinish(null);
                }
                switch (response.code()){
                    case 200:
                        finishListener.doOnFinish(response.body());
                        break;
                     default:
                         finishListener.doOnFinish(null);
                         break;
                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                finishListener.doOnFinish(null);
            }
        });
    }

    public interface onFinishListener{
        void doOnFinish(Info info);
    }

    public interface RestClient{
        @GET("top-headlines")
        Call<Info> getData(@Query("sources")String sources,@Query("apiKey")String apiKey);
    }
}
