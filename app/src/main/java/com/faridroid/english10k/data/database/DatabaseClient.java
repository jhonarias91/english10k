package com.faridroid.english10k.data.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.faridroid.english10k.data.dao.CategoryDao;

import java.util.concurrent.Executors;

public class DatabaseClient {
    private static Room10kDatabase database;

    public static Room10kDatabase getDatabase(Context context) {
        if (database == null) {
            try{
                database = Room.databaseBuilder(context.getApplicationContext(),
                                Room10kDatabase.class, "english10k.db")
                        .createFromAsset("english10k.db") //load db from assets
                        .addMigrations(migration1To5)
                        //.addMigrations(migration2To3)
                        //.fallbackToDestructiveMigration() //fallbackToDestructiveMigration: lost data just for development
                        .build();

            }catch (Exception e){
                // Log the exception or handle it according to your app's policy
                Log.e("DatabaseClient", "Error initializing database", e);
            }

        }
        return database;
    }

    /*
     * Migración de la versión 1 a la 5
     */
    private static Migration migration1To5 = new Migration(1, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Categories
            database.execSQL("DROP TABLE IF EXISTS categories");

            database.execSQL("CREATE TABLE IF NOT EXISTS categories (" +
                    "id TEXT PRIMARY KEY NOT NULL, " +
                    "name TEXT, " +
                    "original_name TEXT, " +
                    "user_id TEXT)");

            // Migración de la tabla 'users'
            database.execSQL("CREATE TABLE IF NOT EXISTS users_new (" +
                    "id TEXT PRIMARY KEY NOT NULL, " +
                    "username TEXT, " +
                    "email TEXT, " +
                    "createdAt INTEGER, " +  // Permitir que 'createdAt' sea NULL
                    "xp INTEGER NOT NULL DEFAULT 0)");

            // Migrar los datos de 'users' a 'users_new'
            database.execSQL("INSERT INTO users_new (id, username, email, createdAt, xp) " +
                    "SELECT id, username, email, createdAt, xp FROM users");

            // Eliminar la tabla 'users' antigua y renombrar la nueva
            database.execSQL("DROP TABLE IF EXISTS users");
            database.execSQL("ALTER TABLE users_new RENAME TO users");


            // Progreso del Usuario (user_progress)
            database.execSQL("CREATE TABLE IF NOT EXISTS user_progress_new (" +
                    "user_progress_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "word_id INTEGER NOT NULL, " +
                    "user_id TEXT NOT NULL, " +
                    "status INTEGER NOT NULL, " +
                    "updated INTEGER, " +
                    "progress_type TEXT NOT NULL DEFAULT '1')");

            // Migrar los datos desde la tabla antigua a la nueva
            database.execSQL("INSERT INTO user_progress_new (user_progress_id, word_id, user_id, status, updated, progress_type) " +
                    "SELECT user_progress_id, word_id, user_id, status, updated, progress_type FROM user_progress");

            // Eliminar la tabla antigua
            database.execSQL("DROP TABLE IF EXISTS user_progress");

            // Renombrar la nueva tabla con el nombre original
            database.execSQL("ALTER TABLE user_progress_new RENAME TO user_progress");

            // Eliminar y recrear la tabla 'custom_lists'
            database.execSQL("DROP TABLE IF EXISTS custom_lists");

            database.execSQL("CREATE TABLE IF NOT EXISTS custom_lists (" +
                    "id TEXT PRIMARY KEY NOT NULL, " +
                    "name TEXT, " +
                    "original_name TEXT, " +
                    "category_id TEXT, " +
                    "FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE)");

            // Eliminar y recrear la tabla 'custom_words'
            database.execSQL("DROP TABLE IF EXISTS custom_words");

            database.execSQL("CREATE TABLE IF NOT EXISTS custom_words (" +
                    "id TEXT PRIMARY KEY NOT NULL, " +
                    "list_id TEXT NOT NULL, " +  // Asegurar que 'list_id' sea NOT NULL
                    "word TEXT, " +
                    "spanish TEXT, " +
                    "FOREIGN KEY(list_id) REFERENCES custom_lists(id) ON DELETE CASCADE)");

            // Eliminar y recrear la tabla 'user_custom_progress'
            database.execSQL("DROP TABLE IF EXISTS user_custom_progress");

            database.execSQL("CREATE TABLE IF NOT EXISTS user_custom_progress (" +
                    "id TEXT PRIMARY KEY NOT NULL, " +
                    "custom_word_id TEXT NOT NULL, " +
                    "progress INTEGER NOT NULL, " +
                    "last_updated INTEGER, " +
                    "progress_type TEXT NOT NULL DEFAULT '1', " +  // Asegurar que 'progress_type' tenga un valor por defecto
                    "FOREIGN KEY(custom_word_id) REFERENCES custom_words(id) ON DELETE CASCADE)");
        }
    };



    /*
    * Default data
    * */

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {

            super.onCreate(db);
            // Aquí se puede hacer la inserción inicial
            Executors.newSingleThreadExecutor().execute(() -> {
                CategoryDao categoryDao = database.categoryDao();

            });
        }
    };

}
