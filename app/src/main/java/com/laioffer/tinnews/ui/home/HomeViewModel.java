package com.laioffer.tinnews.ui.home;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.repository.NewsRepository;

public class HomeViewModel extends ViewModel {
    // talk to repository
    private final NewsRepository repository;
    private final MutableLiveData<String> countryInput = new MutableLiveData<>();

    public void setCountryInput(String country) {
        countryInput.setValue(country);
    }

    public HomeViewModel(NewsRepository newsRepository) {
        this.repository = newsRepository;
    }

    // [1,2,3].map(item -> {item + 1}) return [2,3,4] switchMap works the same
    public LiveData<NewsResponse> getTopHeadlines() {
        // country change -> repository.getTopHeadlines(country)
        // string (countryInput de livedata) converts into -> LiveData<NewsResponse>
//        return Transformations.switchMap(countryInput, new Function<String, LiveData<NewsResponse>>() {
//            @Override
//            public LiveData<NewsResponse> apply(String input) {
//                return repository.getTopHeadlines(input);
//            }
//        });
        return Transformations.switchMap(countryInput, repository::getTopHeadlines);
    }
//    class A {String hello(int a) {}}
//    interface {String peter(int pp) }
//    pp -> {}
//    a::hello

    public void setFavoriteArticleInput(Article article) {
        repository.favoriteArticle(article);
    }
}
