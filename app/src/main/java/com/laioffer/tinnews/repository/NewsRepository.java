package com.laioffer.tinnews.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.laioffer.tinnews.TinNewsApplication;
import com.laioffer.tinnews.database.TinNewsDatabase;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.network.NewsApi;
import com.laioffer.tinnews.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// get, save data
public class NewsRepository {
    // Retrofit
    private final NewsApi newsApi;
    private final TinNewsDatabase database;

    // constructor
    public NewsRepository() {
        newsApi = RetrofitClient.newInstance().create(NewsApi.class);
        database = TinNewsApplication.getDatabase();
    }

    public LiveData<NewsResponse> getTopHeadlines(String country) {
        Call<NewsResponse> call = newsApi.getTopHeadlines(country); // 2s
        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
        // 异步 async 防止卡UI, need to wait for networking call
        call.enqueue(new Callback<NewsResponse>() { //CallBack is an interface
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    topHeadlinesLiveData.setValue(response.body());
                    Log.d("getTopHeadlines", response.body().toString());
                } else {
                    topHeadlinesLiveData.setValue(null);
                    Log.d("getTopHeadlines", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                topHeadlinesLiveData.setValue(null);
                Log.d("getTopHeadlines", t.toString());
            }
        });
        return topHeadlinesLiveData;
    }

    public LiveData<NewsResponse> searchNews(String query) {
        Call<NewsResponse> call = newsApi.getEverything(query, 40); // 2s
        MutableLiveData<NewsResponse> everyThingLiveData = new MutableLiveData<>();
        // 异步 async 防止卡UI, need to wait for networking call
        call.enqueue(new Callback<NewsResponse>() { //CallBack is an interface
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                    Log.d("getTopHeadlines", response.body().toString());
                } else {
                    everyThingLiveData.setValue(null);
                    Log.d("getTopHeadlines", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
                Log.d("getTopHeadlines", t.toString());
            }
        });
        return everyThingLiveData;
    }

    public LiveData<Boolean> favoriteArticle(Article article) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
        new FavoriteAsyncTask(database, resultLiveData).execute(article);
        return resultLiveData;
    }

    public LiveData<List<Article>> getAllSavedArticles() { // LiveData do this async
        // reactive data query, if db changed, we will see the change
        return database.articleDao().getAllArticles();
    }

    public void deleteSavedArticle(Article article) {
        AsyncTask.execute(() -> database.articleDao().deleteArticle(article));
    }

    // static nested class
    private static class FavoriteAsyncTask extends AsyncTask<Article, Void, Boolean> {

        private final TinNewsDatabase database;
        private final MutableLiveData<Boolean> liveData;

        private FavoriteAsyncTask(TinNewsDatabase database, MutableLiveData<Boolean> liveData) {
            this.database = database;
            this.liveData = liveData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Article... articles) {
            Article article = articles[0];
            try {
                database.articleDao().saveArticle(article);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Void... values)  {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            liveData.setValue(success);
        }




    }
}
