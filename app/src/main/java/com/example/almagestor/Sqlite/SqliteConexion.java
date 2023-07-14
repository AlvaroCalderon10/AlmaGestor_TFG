package com.example.almagestor.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteConexion extends SQLiteOpenHelper {

    final String TBL_user="CREATE TABLE user (groupeid INTEGER, codepdv TEXT, company_name TEXT, Password TEXT)";
    final String TBL_shop="CREATE TABLE shopinfo (groupeid INTEGER, codepdv TEXT, company_name TEXT, street TEXT, infoStreet TEXT, phone TEXT,email TEXT)";
    final String TBL_clients="CREATE TABLE clientsinfo (groupeid INTEGER, name TEXT, nif TEXT, phone TEXT, streetinfo TEXT)";
    final String TBL_products="CREATE TABLE product (groupeid INTEGER, img TEXT, name TEXT, ean TEXT, stock INTEGER, price INTEGER)";

    final String TBL_logVente="CREATE TABLE logvente (groupeid INTEGER, moneycash TEXT, date TEXT, file TEXT)";
    final String TBL_productLogvente="CREATE TABLE productslogvente (groupeid INTEGER, fk_logvente INTEGER, productName TEXT, quantity INTEGER, price TEXT)";


    public SqliteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TBL_user);
        db.execSQL(TBL_shop);
        db.execSQL(TBL_products);
        db.execSQL(TBL_logVente);
        db.execSQL(TBL_productLogvente);
        db.execSQL(TBL_clients);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
