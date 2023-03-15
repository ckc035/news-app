package com.laioffer.tinnews;

import android.app.Application;

import androidx.room.Room;

import com.laioffer.tinnews.database.TinNewsDatabase;

// singleton (public static ... )
// app created, this class willl created, if destroyed, this also does
public class TinNewsApplication extends Application {
    // Singleton for TinNewsApplication class
    // database is also singleton and static -> for this class
    // static field will not be recycled, only when process destoryed this will destroy
    private static TinNewsDatabase database; //

    @Override
    public void onCreate() {
        super.onCreate();
        // application context, so we can also use getApplicationContext() here
        // we have two type of context here, application or activity context
        // we cannot use activity context here, because it will be destoryed.
        // app context will be destroyed only when app exit / process stopped
        // Note: in MainActivity, we can access App context created here, becuase this is bigger
        database = Room.databaseBuilder(this, TinNewsDatabase.class, "tinnews_db").build();
    }

    public static TinNewsDatabase getDatabase() {
        return database;
    }
}
