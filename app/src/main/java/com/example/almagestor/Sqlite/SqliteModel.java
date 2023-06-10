package com.example.almagestor.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

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
                    String groupeid=c.getString(0);
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
    public boolean insert_user(Context context, UserDTO dto){
        int res=0;
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        String sql="INSERT INTO user (groupeid, codepdv, company_name, Password) VALUES ('"+groupeid+"','"+pdv+"','"+dto.getCompany_name()+"','"+dto.getPassword()+"')";
        try {
            SQLiteDatabase db =this.getConn(context);
            db.execSQL(sql);
            return true;
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return false;
    }
    public boolean reset_Password(Context context, String pass, String pdv){
        String groupeid = comprobarPdv(context,pdv);

        if(groupeid!=null){
            int push=0;
            try{
                SQLiteDatabase db =this.getConn(context);
                ContentValues contentValues=new ContentValues();
                contentValues.put("Password", String.valueOf(pass));
                push=db.update("user",contentValues,"groupeid= '"+groupeid+"'",null);
                db.close();
                if(push==1){
                    return true;
                }
                return false;
            }catch (SQLException e){
                Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            }
        }else{
            //no existe
            return false;
        }

        return false;
    }
    private String comprobarPdv(Context context,String codepdv){
        try{
            SQLiteDatabase db =this.getConn(context);
            Cursor c = db.rawQuery("SELECT groupeid FROM user WHERE codepdv = ?", new String[] {codepdv});
            if(c.moveToFirst()){
                do{
                    Integer groupeid=c.getInt(0);
                    if(groupeid!=null){
                        return groupeid.toString().trim();
                    }
                }while (c.moveToNext());
            }
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return null;
    }


}
