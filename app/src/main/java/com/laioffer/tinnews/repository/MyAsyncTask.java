package com.laioffer.tinnews.repository;

import android.os.Handler;
import android.os.Looper;

import com.laioffer.tinnews.model.Article;

public abstract class MyAsyncTask<Param, Progress, Result> {

    private Handler handler = new Handler(Looper.getMainLooper());
    public void execute(Param params) { // Param... params 多参数(varagus) list
        onPreExecute(); // #1

        new Thread(new Runnable() { // background thread
            @Override
            public void run() {
                Result result = doInBackground(params); //#2
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result);    // 3 back to main thread
                    }
                });
            }
        }).start();

    }

    public void onPreExecute() {

    }

    public abstract Result doInBackground(Param param);

    public void onProgressUpdate(Progress progress) {

    }

    public void publishProgress(Progress progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onProgressUpdate(progress); // 4 must be run in main thread
            }
        });
    }

    public void onPostExecute(Result result) {

    }

    public static void main() {
        new MyAsyncTask<Article, Void, Boolean>() {
            @Override
            public Boolean doInBackground(Article article) {
                return null;
            }
        }.execute((new Article()));  // #1
    }


}
