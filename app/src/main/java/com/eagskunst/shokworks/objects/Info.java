package com.eagskunst.shokworks.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Info {
    private String status;
    private int totalResults;
    @SerializedName("articles")
    private List<Article> articleList;

    public Info() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public String toString() {
        return "Info{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articleList=" + articleList +
                '}';
    }
}
