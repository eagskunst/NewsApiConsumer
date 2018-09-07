package com.eagskunst.shokworks.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eagskunst.shokworks.R;
import com.eagskunst.shokworks.objects.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private List<Article> articleList;
    private NewsViewHolder.AdapterClickLister adapterClickLister;

    public NewsAdapter(List<Article> articleList, NewsViewHolder.AdapterClickLister adapterClickLister){
        this.articleList = articleList;
        this.adapterClickLister = adapterClickLister;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_news,parent,false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        if(articleList.get(position).getUrlToImage() != null){
            holder.newsImage.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(articleList.get(position).getUrlToImage())
                    .into(holder.newsImage);
        }
        else
            holder.newsImage.setVisibility(View.GONE);

        holder.title.setText(articleList.get(position).getTitle());
        holder.secondaryText.setText(articleList.get(position).getSource().getName());
        if(articleList.get(position).getDescription() == null)
            holder.description.setVisibility(View.GONE);
        else
            holder.description.setText(articleList.get(position).getDescription());
        holder.bind(articleList.get(position),adapterClickLister);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        private ImageView newsImage;
        private TextView title;
        private TextView secondaryText;
        private TextView description;
        private TextView action1;

        public NewsViewHolder(View itemView) {
            super(itemView);
            this.newsImage = itemView.findViewById(R.id.image_cardview);
            this.title = itemView.findViewById(R.id.title_cardview);
            this.secondaryText = itemView.findViewById(R.id.secondarytext_cardview);
            this.description = itemView.findViewById(R.id.description_cardview);
            this.action1 = itemView.findViewById(R.id.action_cardview);
        }

        private void bind(final Article article, final AdapterClickLister adapterClickLister){
            action1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterClickLister.onItemClickListener(article);
                }
            });
        }

        public interface AdapterClickLister{
            void onItemClickListener(Article article);
        }
    }
}
