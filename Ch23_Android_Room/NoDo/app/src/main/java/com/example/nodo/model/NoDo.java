package com.example.nodo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "noDo_table")
public class NoDo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull // makes never null
    @ColumnInfo(name = "noDo_col")
    private String noDo;

    public NoDo(@NonNull String noDo) {
        this.noDo = noDo;
    }

    public String getNoDo() {
        return noDo;
    }

    public void setNoDo(@NonNull String noDo) {
        this.noDo = noDo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
