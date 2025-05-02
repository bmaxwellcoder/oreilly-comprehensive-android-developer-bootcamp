package com.example.nodo.util;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.nodo.data.NoDoDao;
import com.example.nodo.data.NoDoRoomDatabase;
import com.example.nodo.model.NoDo;

import java.util.List;

public class NoDoRepository {
    // Data Access Object (DAO) for interacting with the database
    private NoDoDao noDoDao;

    // LiveData object to observe changes in the list of NoDo items
    private LiveData<List<NoDo>> allNoDos;

    // Constructor to initialize the repository
    public NoDoRepository(Application application) {
        // Get the database instance
        NoDoRoomDatabase db = NoDoRoomDatabase.getDatabase(application);

        // Get the DAO from the database
        noDoDao = db.noDoDao();

        // Retrieve all NoDo items from the database
        allNoDos = noDoDao.getAllNodDos();
    }

    // Method to get all NoDo items as LiveData
    public LiveData<List<NoDo>> getAllNoDos() {
        return allNoDos;
    }

    // Method to insert a NoDo item into the database
    public void insert(NoDo noDo) {
        // Perform the insert operation asynchronously
        new insertAsyncTask(noDoDao).execute(noDo);
    }

    // AsyncTask class to handle database insert operations in the background
    private class insertAsyncTask extends AsyncTask<NoDo, Void, Void> {
        // DAO for performing the insert operation
        private NoDoDao asyncTaskDao;

        // Constructor to initialize the AsyncTask with the DAO
        public insertAsyncTask(NoDoDao dao) {
            asyncTaskDao = dao;
        }

        // Background task to insert the NoDo item into the database
        @Override
        protected Void doInBackground(NoDo... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}