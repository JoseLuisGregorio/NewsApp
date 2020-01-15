package com.joseluis.newsapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.joseluis.newsapp.R;
import com.joseluis.newsapp.model.News;
import com.joseluis.newsapp.model.NewsRespuesta;
import com.joseluis.newsapp.newsapi.ListaNewsAdapter;
import com.joseluis.newsapp.newsapi.NewsaService;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    ImageView imgExit;
    FirebaseAuth firebaseAuth;
    private static final String TAG = "POKEDEX";

    private RecyclerView recyclerView;
    private ListaNewsAdapter listaNewsAdapter;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_news );

        imgExit=findViewById( R.id.imageViewExit );

        firebaseAuth = FirebaseAuth.getInstance();
        eventos();
        iniciaAdapter();
        iniciaRetrofit();
        obtenerValores();
    }

    private void iniciaAdapter() {

        recyclerView=findViewById( R.id.RecyclerViewNews );
        listaNewsAdapter=new ListaNewsAdapter(this);
        recyclerView.setAdapter( listaNewsAdapter );
        recyclerView.setHasFixedSize( true );
        GridLayoutManager layoutManager=new GridLayoutManager( this,1 );
        recyclerView.setLayoutManager( layoutManager );
    }

    private void obtenerValores() {
        NewsaService service=retrofit.create( NewsaService.class );
        Call<NewsRespuesta> newsRespuestaCall= service.obtenerListaNews();
        newsRespuestaCall.enqueue( new Callback<NewsRespuesta>() {
            @Override
            public void onResponse(Call<NewsRespuesta> call, Response<NewsRespuesta> response) {
                if (response.isSuccessful()){
                    NewsRespuesta newsRespuesta=response.body();
                    ArrayList<News> listaNews=newsRespuesta.getArticles();

                    listaNewsAdapter.adicionarListaNews(listaNews);
                    //Toast.makeText( NewsActivity.this, "Successful", Toast.LENGTH_SHORT ).show();
                }else {
                    Log.e(TAG, " onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NewsRespuesta> call, Throwable t) {
                Log.e(TAG, " onFailure: " + t.getMessage());
            }
        } );
    }

    private void iniciaRetrofit() {
        retrofit=new Retrofit.Builder().baseUrl( "http://newsapi.org/v2/" ).addConverterFactory( GsonConverterFactory.create() ).build();
    }

    private void eventos() {
        imgExit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent exit=new Intent( NewsActivity.this,RegisterActivity.class );
                startActivity( exit );
                finish();
            }
        } );
    }
}
