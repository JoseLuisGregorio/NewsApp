package com.joseluis.newsapp.newsapi;

import com.joseluis.newsapp.model.NewsRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsaService {

    @GET("top-headlines?country=mx&apiKey=ddd17427f62844438c217efb4c0e4bc5")
    Call<NewsRespuesta> obtenerListaNews();
}
