package com.example.android.inventoryapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by miche on 7/24/2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;


    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_INVENTORY_DATABASE = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + " ("
                + InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.InventoryEntry.COLUMN_INVENTORY_NAME + " TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE + " TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY + " TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_INVENTORY_IMAGE + " BLOB" + ");";

        db.execSQL(SQL_CREATE_INVENTORY_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public Cursor selectData() {

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_NAME,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_IMAGE

        };

        Cursor cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, null, null, null, null, null);

        return cursor;
    }

    public boolean insertInventory(String name, String price, String quantity, byte[] image) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_NAME, name);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE, price);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY, quantity);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_IMAGE, image);

        long dataInserted = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, contentValues);

        return dataInserted != -1;
    }

    public void deleteInventories() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(InventoryContract.InventoryEntry.TABLE_NAME, null, null);
    }

    public void updateInventory(int quantity, int productID) {

        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + InventoryContract.InventoryEntry.TABLE_NAME + " SET " + InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY + " = '" +
                quantity + "' WHERE " + InventoryContract.InventoryEntry._ID + " = " + productID;
        db.execSQL(query);
    }

    public void deleteInventory(int productID) {

        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + InventoryContract.InventoryEntry.TABLE_NAME + " WHERE " + InventoryContract.InventoryEntry._ID + "= '" + productID + "'";
        db.execSQL(query);
    }

    public Cursor selectInventory(int productID) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String query = "SELECT * FROM " + InventoryContract.InventoryEntry.TABLE_NAME + " WHERE " + InventoryContract.InventoryEntry._ID + "= '" + productID + "'";
        cursor = db.rawQuery(query, null);

        return cursor;
    }
}