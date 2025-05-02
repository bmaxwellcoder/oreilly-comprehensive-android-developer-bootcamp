package com.example.nodo.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.lifecycle.LiveData;

import com.example.nodo.model.NoDo;

import java.util.List;

@Dao
public interface NoDoDao {
    /// CRUD
    @Insert
    void insert(NoDo noDo);

    @Query("DELETE FROM noDo_table")
    void deleteAll();

    @Query("DELETE FROM noDo_table WHERE id = :id") // R->L: id passed in equals id in table
    int deleteANoDo(int id);
    @Query("UPDATE nodo_table SET noDo_col = :noDoText " +
            "WHERE id = :id")
    int updateNoDoItem(int id, String noDoText);

    @Query("SELECT * FROM noDo_table ORDER BY " +
            "noDo_col DESC")
    LiveData<List<NoDo>> getAllNodDos();
}
