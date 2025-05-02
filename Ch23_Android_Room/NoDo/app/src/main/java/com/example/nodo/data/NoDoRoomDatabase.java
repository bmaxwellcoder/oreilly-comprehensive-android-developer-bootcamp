package com.example.nodo.data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.PopupMenu;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nodo.model.NoDo;

// Specifies that this class is a Room database and defines the entities and version
@Database(entities = {NoDo.class}, version = 1, exportSchema = false)
public abstract class NoDoRoomDatabase extends RoomDatabase {

    // Singleton instance of the database to ensure only one instance is created
    public static volatile NoDoRoomDatabase INSTANCE;

    // Abstract method to provide access to the DAO (Data Access Object)
    public abstract NoDoDao noDoDao();

    // Method to get the singleton instance of the database
    public static NoDoRoomDatabase getDatabase(final Context context) {
        // Check if the database instance is null
        if (INSTANCE == null) {
            // Synchronize to ensure thread safety when creating the database instance
            synchronized (NoDoRoomDatabase.class) {
                // Double-check if the instance is still null before creating it
                if (INSTANCE == null) {
                    // Build the Room database instance with the application context
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    NoDoRoomDatabase.class, "NoDo_database")
                            .addCallback(roomDatabaseCallBack)
                            .build();
                }
            }
        }
        // Return the singleton instance of the database
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallBack =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDatabaseAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {
        private final NoDoDao noDoDao;

        public PopulateDatabaseAsync(NoDoRoomDatabase db) {
            noDoDao = db.noDoDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
//            noDoDao.deleteAll(); // removes all items from our table
            // for testing
//            NoDo noDo = new NoDo("Buy a new Ferari");
//            noDoDao.insert(noDo);
//
//            noDo = new NoDo("Big house");
//            noDoDao.insert(noDo);

            return null;
        }
    }
}