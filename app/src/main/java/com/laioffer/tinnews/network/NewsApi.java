package com.laioffer.tinnews.network;

import com.laioffer.tinnews.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// call everything && topHeadline endpoints
public interface NewsApi {
    // https://newsapi.org/v2/top-headlines?country=us
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(@Query("country") String country);

    // https://newsapi.org/v2/everything?q=bitcoin&pageSize=100 -> json -> NewsResponse
    // need to use annotation from retrofit, then retrofit will implement this interface
    @GET("everything")
    Call<NewsResponse> getEverything(
            @Query("q") String query, @Query("pageSize") int pageSize);


}
