package com.example.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.babyneeds.R;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // We create our table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // SQL - Structured Query Language
        /*
        create table _name(id, name, phone_number);
         */
        String CREATE_ITEM_TABLE = "CREATE TABLE "
                + Util.TABLE_NAME + "(" + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_NAME + " TEXT," + Util.KEY_QUANTITY
                + " TEXT," + Util.KEY_COLOR + " TEXT," + Util.KEY_SIZE
                + " TEXT " + ")";
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.sqLiteDatabase_drop);
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        // Create a table again
        onCreate(sqLiteDatabase);
    }

    /*
    CRUD = Create, Read, Update, Delete
     */
    // Add Contact
    public void addItem(Item item) {
        SQLiteDatabase sqLiteDatabase  = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, item.getName());
        values.put(Util.KEY_QUANTITY, item.getQuantity());
        values.put(Util.KEY_COLOR, item.getColor());
        values.put(Util.KEY_SIZE, item.getSize());

        // Insert to row
        sqLiteDatabase.insert(Util.TABLE_NAME, null, values);
        Log.d("DBHandler", "addItem: " + "item added");
        sqLiteDatabase.close();
    }

    // Get a contact
    public Item getItem(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(Util.TABLE_NAME,
                new String[] { Util.KEY_ID, Util.KEY_NAME, Util.KEY_QUANTITY,
                Util.KEY_COLOR, Util.KEY_SIZE},
                Util.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Item item = new Item();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setName(cursor.getString(1));
        item.setQuantity(cursor.getString(2));
        item.setColor(cursor.getString(3));
        item.setSize(cursor.getString(4));

        return item;
    }

    // Get all items
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        // Select all
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(selectAll, null);

        // Loop through our data
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setName(cursor.getString(1));
                item.setQuantity(cursor.getString(2));
                item.setColor(cursor.getString(3));
                item.setSize(cursor.getString(4));

                // add items objects
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    // Get contacts count
    public int getCount() {
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);

        return cursor.getCount();
    }

    // Update item
    public int updateItem(Item item) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, item.getName());
        values.put(Util.KEY_QUANTITY, item.getQuantity());
        values.put(Util.KEY_COLOR, item.getColor());
        values.put(Util.KEY_SIZE, item.getSize());

        // update row
        // update (tableName, values, where id = 43)
        return sqLiteDatabase.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    // Delete single contact
    public void deleteItem(Item item) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(Util.TABLE_NAME, Util.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
        sqLiteDatabase.close();
    }

    public void clearDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Util.TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }
}
