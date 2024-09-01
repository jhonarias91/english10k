package com.faridroid.english10k.data.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseClient {
    private static Room10kDatabase database;

    public static Room10kDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    Room10kDatabase.class, "english10k.db")
                    .createFromAsset("english10k.db") //load db from assets
                    .addMigrations(migration1To2)
                    //.fallbackToDestructiveMigration() //fallbackToDestructiveMigration: lost data just for development
                    .build();
        }
        return database;
    }


    private static Migration migration1To2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create a new table with the desired schema
            database.execSQL("CREATE TABLE categories_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, original_name TEXT)");

            // Copy the data from the old table to the new table with changes in the column names
            database.execSQL("INSERT INTO categories_new (id, name) SELECT id, categoryName FROM categories");

            // Remove the old table
            database.execSQL("DROP TABLE categories");

            // Rename the new table to the original name
            database.execSQL("ALTER TABLE categories_new RENAME TO categories");
        }
    };

}
