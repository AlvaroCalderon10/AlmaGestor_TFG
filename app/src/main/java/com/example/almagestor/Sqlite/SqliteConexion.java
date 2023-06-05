package com.example.almagestor.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteConexion extends SQLiteOpenHelper {

    final String TBL_user="CREATE TABLE user (groupeid INTEGER, codepdv TEXT, company_name TEXT, Password TEXT)";

    final String TBL_press="CREATE TABLE cycle_press (groupeid INTEGER, number INTEGER, weight REAL, reps INTEGER, work REAL, rm REAL, fecha TEXT, day INTEGER)";

    final String TBL_press_2="CREATE TABLE cycle_press_2 (groupeid INTEGER, number INTEGER, weight REAL, reps INTEGER, work REAL, rm REAL, fecha TEXT, day INTEGER)";

    final String TBL_press_3="CREATE TABLE cycle_press_3 (groupeid INTEGER, number INTEGER, weight REAL, reps INTEGER, work REAL, rm REAL, fecha TEXT, day INTEGER)";
    final String TBL_press_general="CREATE TABLE global_cycle_press (groupeid INTEGER, number INTEGER, total_work REAL, total_reps INTEGER, rm_record REAL, fecha TEXT)";
    final String TBL_squat="CREATE TABLE cycle_squat (groupeid INTEGER, number INTEGER, weight REAL, reps INTEGER, work REAL, rm REAL, fecha TEXT, day INTEGER)";

    public SqliteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TBL_user);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
