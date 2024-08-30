package com.faridroid.english10k.data.database;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {
    private static English10kDatabase database;

    public static English10kDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    English10kDatabase.class, "english10k.db")
                    .createFromAsset("english10k.db") //load db from assets
                    //.fallbackToDestructiveMigration() // lost data just for development
                    .build();
        }
        return database;
    }
}
