package com.joseluis.newsapp.newsapi;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joseluis.newsapp.R;
import com.joseluis.newsapp.model.News;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaNewsAdapter extends RecyclerView.Adapter<ListaNewsAdapter.ViewHolder> {
    private ArrayList<News> dataset;
    private String urlimg;
    private Context context;

    public ListaNewsAdapter(Context context){
        this.context=context;
        dataset=new ArrayList<>(  );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from( parent.getContext()).inflate( R.layout.newslayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News n=dataset.get( position );
        holder.title.setText( n.getTitle() );
        holder.author.setText( n.getAuthor() );
        holder.content.setText( n.getContent() );
        holder.description.setText( n.getDescription() );
        holder.url.setText( Html.fromHtml( n.getUrl()+
                "<a href="+n.getUrl()) );
        holder.publishedAt.setText( n.getPublishedAt() );
        urlimg=n.getUrlToImage();
        Glide.with( context ).load( urlimg ).centerCrop().diskCacheStrategy( DiskCacheStrategy.ALL ).into( holder.imageViewNews );
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
    public void adicionarListaNews(ArrayList<News> listaNews) {
        dataset.addAll( listaNews );
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView author, title, description, url, publishedAt, content;
        private ImageView imageViewNews;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            author=itemView.findViewById( R.id.textViewAuthor );
            title=itemView.findViewById( R.id.textViewTitle );
            description=itemView.findViewById( R.id.textViewDescription );
            url=itemView.findViewById( R.id.textViewURL );
            publishedAt=itemView.findViewById( R.id.textViewPub );
            content=itemView.findViewById( R.id.textViewContent );
            imageViewNews=itemView.findViewById( R.id.imageViewImg );
        }
    }
}
