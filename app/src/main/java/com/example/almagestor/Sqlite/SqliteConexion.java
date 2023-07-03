package com.example.almagestor.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteConexion extends SQLiteOpenHelper {

    final String TBL_user="CREATE TABLE user (groupeid INTEGER, codepdv TEXT, company_name TEXT, Password TEXT)";
    final String TBL_products="CREATE TABLE product (groupeid INTEGER, img TEXT, name TEXT, ean TEXT, stock INTEGER, price INTEGER)";


    public SqliteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TBL_user);
        db.execSQL(TBL_products);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
