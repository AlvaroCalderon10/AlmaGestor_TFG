package com.example.almagestor.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.almagestor.DTOs.UserDTO;

public class SqliteModel {
    private static final String TAG = "SQLiteModel";
    public SQLiteDatabase getConn(Context context){
        try{
            SqliteConexion conn= new SqliteConexion(context, "AlmaGestor_DB", null,1);
            return conn.getWritableDatabase();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-CONN: "+e.getMessage());
        }
        return null;
    }

    public UserDTO getFromLocal(Context context){
        int exist=0;
        UserDTO user=null;
        try {
            SQLiteDatabase db =this.getConn(context);
            Cursor c = db.rawQuery("SELECT  groupeid, codepdv, company_name, Password FROM user Limit ?", new String[] {"1"});
            if(c.moveToFirst()){
                do{
                    Integer groupeid=c.getInt(0);
                    String codepdv=c.getString(1);
                    String company_name=c.getString(2);
                    String password=c.getString(3);
                    user=new UserDTO(groupeid,codepdv,company_name,password);
                    exist=1;
                }while (c.moveToNext());
            }
            c.close();
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        if(user!=null && exist==1){
            return user;
        }else{
            return null;
        }
    }
}
