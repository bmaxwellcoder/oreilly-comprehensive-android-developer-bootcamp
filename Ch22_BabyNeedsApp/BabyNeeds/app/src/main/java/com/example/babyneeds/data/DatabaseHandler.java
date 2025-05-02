package com.example.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.babyneeds.R;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
private final Context context;

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION );
        this.context = context;
    }

    // We create our table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // SQL - Structured Query Language
        /*
        create table _name(id, name, phone_number);
         */
        String CREATE_ITEM_TABLE =
                "CREATE TABLE " + Constants.TABLE_NAME + "("
                        + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                        + Constants.KEY_BABY_ITEM + " TEXT,"
                        + Constants.KEY_QUANTITY + " INTEGER,"
                        + Constants.KEY_COLOR + " TEXT,"
                        + Constants.KEY_SIZE + " INTEGER, "
                        + Constants.KEY_DATE_NAME + " LONG);";

        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.sqLiteDatabase_drop);
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Constants.DATABASE_NAME});

        // Create a table again
        onCreate(sqLiteDatabase);
    }

    /*
    CRUD = Create, Read, Update, Delete
     */
    // Add Item
    public void addItem(Item item) {
        SQLiteDatabase sqLiteDatabase  = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_BABY_ITEM, item.getItemName());
        values.put(Constants.KEY_QUANTITY, item.getItemQuantity());
        values.put(Constants.KEY_COLOR, item.getItemColor());
        values.put(Constants.KEY_SIZE, item.getItemSize());
        values.put(Constants.KEY_DATE_NAME,
                java.lang.System.currentTimeMillis());

        // Insert to row
        sqLiteDatabase.insert(Constants.TABLE_NAME, null, values);
        Log.d("DBHandler", "addItem: " + "item added");
        sqLiteDatabase.close();
    }

    // Get an item
    public Item getItem(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME,
                new String[] {Constants.KEY_ID,
                        Constants.KEY_BABY_ITEM,
                        Constants.KEY_QUANTITY,
                        Constants.KEY_COLOR,
                        Constants.KEY_SIZE,
                        Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        cursor.moveToFirst();

        Item item = new Item();
        item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Constants.KEY_ID))));
        item.setItemName(cursor.getString(cursor.getColumnIndexOrThrow(Constants.KEY_BABY_ITEM)));
        item.setItemColor(cursor.getString(cursor.getColumnIndexOrThrow(Constants.KEY_COLOR)));
        item.setItemQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.KEY_QUANTITY)));
        item.setItemSize(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.KEY_SIZE)));

        //convert Timestamp to something readable
        DateFormat dateFormat = DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(
                new Date(cursor.getLong(
                        cursor.getColumnIndexOrThrow(Constants.KEY_DATE_NAME)))
                .getTime());

        item.setDateItemAdded(formattedDate);

        return item;
    }

    // Get all items
    public List<Item> getAllItems() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME,
                new String[] {Constants.KEY_ID,
                        Constants.KEY_BABY_ITEM,
                        Constants.KEY_QUANTITY,
                        Constants.KEY_COLOR,
                        Constants.KEY_SIZE,
                        Constants.KEY_DATE_NAME},
                null, null, null, null,
                Constants.KEY_DATE_NAME + " DESC");

        // Loop through our data
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Constants.KEY_ID))));
                item.setItemName(cursor.getString(cursor.getColumnIndexOrThrow(Constants.KEY_BABY_ITEM)));
                item.setItemColor(cursor.getString(cursor.getColumnIndexOrThrow(Constants.KEY_COLOR)));
                item.setItemQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.KEY_QUANTITY)));
                item.setItemSize(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.KEY_SIZE)));

                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(
                        new Date(cursor.getLong(
                                cursor.getColumnIndexOrThrow(Constants.KEY_DATE_NAME)))
                                .getTime());

                item.setDateItemAdded(formattedDate);
                // add items to arrayList
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    // Get contacts count
    public int getItemCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);

        return cursor.getCount();
    }

    // Update item
    public int updateItem(Item item) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_BABY_ITEM, item.getItemName());
        values.put(Constants.KEY_QUANTITY, item.getItemQuantity());
        values.put(Constants.KEY_COLOR, item.getItemColor());
        values.put(Constants.KEY_SIZE, item.getItemSize());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system


        // update row
        return sqLiteDatabase.update(Constants.TABLE_NAME,
                values,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    // Delete single contact
    public void deleteItem(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public void clearDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Constants.TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }
}
