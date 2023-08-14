package com.example.almagestor;

import static org.junit.Assert.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.almagestor.DTOs.UserDTO;
import com.example.almagestor.Sqlite.SqliteConexion;
import com.example.almagestor.Sqlite.SqliteModel;

import org.junit.Test;

public class BDConnTest {
    final SqliteModel bd=new SqliteModel();


    @Test
    public void TestBDConexion() throws Exception {
        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        String resolution;
        try{
            SQLiteDatabase test=bd.getConn(context);
            resolution="true";
        }catch (SQLiteException e){
            resolution="false";
        }
        assertEquals("true",resolution);
    }
    @Test
    public void TestSelectBD() throws Exception {
        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        String resolution;
        try{
            UserDTO user=bd.getFromLocal(context);
            //if user null, nothing on DB but select funcionality it´s correct
            resolution="true";
        }catch (SQLiteException e){
            resolution="false";
        }
        assertEquals("true",resolution);
    }

}
