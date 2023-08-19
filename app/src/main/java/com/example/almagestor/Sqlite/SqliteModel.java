package com.example.almagestor.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.util.Log;
import android.widget.Toast;

import com.example.almagestor.DTOs.AdressDTO;
import com.example.almagestor.DTOs.ClientDTO;
import com.example.almagestor.DTOs.ShopDTO;
import com.example.almagestor.DTOs.UserDTO;
import com.example.almagestor.ListAdapters.ListAdapterProductBean;
import com.example.almagestor.Products.ProductDataDTO;
import com.google.android.gms.common.util.ArrayUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
            if(insert_shop_init(context,dto)){
                return true;
            }else{
                Log.e(TAG,"Failure on DB-insert on shopInit");
            };
            return true;

        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return false;
    }
    public boolean insert_shop_init(Context context, UserDTO dto){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        String sql="INSERT INTO shopinfo (groupeid, codepdv, company_name) VALUES ('"+groupeid+"','"+pdv+"','"+dto.getCompany_name()+"')";
        try {
            SQLiteDatabase db =this.getConn(context);
            db.execSQL(sql);
            db.close();
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
    public boolean insert_Product(Context context, ProductDataDTO dto){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        String sql="INSERT INTO product (groupeid, img, name, ean, stock, price) VALUES ('"+groupeid+"','"+dto.getImg()+"','"+dto.getNameProduct()+"','"+dto.getEan()+"','"+dto.getUnits()+"','"+dto.getPrice()+"')";
        try {
            SQLiteDatabase db =this.getConn(context);
            db.execSQL(sql);
            db.close();
            return true;
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return false;
    }
    public ProductDataDTO getProduct(Context context, String Ean){
        ProductDataDTO data = null;
        try{
            SQLiteDatabase db = this.getConn(context);
            Cursor c = db.rawQuery("SELECT img, name, ean, price  FROM product WHERE ean = ?", new String[]{Ean});
            if (c.moveToFirst()) {
                do {
                    String img=c.getString(0);
                    String name=c.getString(1);
                    String ean =c.getString(2);
                    Double price= c.getDouble(3);
                    data = new ProductDataDTO(img,name,ean,price);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return data;
    }
    public boolean delete_Product(Context context, String eanValue){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        if(!eanValue.isEmpty()){
            try {
                SQLiteDatabase db =this.getConn(context);
                db.delete("product","ean" + "=?",new String[]{eanValue});
                db.close();
                return true;
            }catch (SQLException e){
                Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            }
        }
        return false;
    }
    public ArrayList<ProductDataDTO> list_products(Context context, String groupeid_user) {
        SQLiteDatabase db = this.getConn(context);
        ArrayList<ProductDataDTO> product_list = new ArrayList<>();
        ProductDataDTO product = null;
        String groupeid="10014";
        Cursor c = db.rawQuery("SELECT groupeid, img,name, ean, stock, price FROM product WHERE groupeid = ?", new String[]{groupeid});
        if (c.moveToFirst()) {
            do {
                String img=c.getString(1);
                String name =c.getString(2);
                String ean= c.getString(3);
                Integer stock=c.getInt(4);
                Integer price=c.getInt(5);
                product = new ProductDataDTO(img,name,ean,stock,price);
                product_list.add(product);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return product_list;
    }
    public ArrayList<ClientDTO> list_clients(Context context, String groupeid_user) {
        SQLiteDatabase db = this.getConn(context);
        ArrayList<ClientDTO> client_list = new ArrayList<>();
        ClientDTO client = null;
        String groupeid="10014";
        Cursor c = db.rawQuery("SELECT groupeid, name, nif, phone, streetinfo FROM clientsinfo WHERE groupeid = ?", new String[]{groupeid});
        if (c.moveToFirst()) {
            do {
                String name=c.getString(1);
                String nif =c.getString(2);
                String phone= c.getString(3);
                String street= c.getString(4);
                client = new ClientDTO(name,nif,phone,street);
                client_list.add(client);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return client_list;
    }
    public long insert_client (Context context, ClientDTO client){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        ContentValues values=new ContentValues();
        values.put("groupeid",groupeid);
        values.put("name",client.getName());
        values.put("nif",client.getNif());
        values.put("phone",client.getPhone());
        values.put("streetinfo",client.getStreet());
        try {
            SQLiteDatabase db =this.getConn(context);
            long rowid=db.insert("clientsinfo",null,values);
            db.close();
            return rowid;
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return -1;
    }
    public long insert_logVente (Context context, String money, Calendar date,String file){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

        ContentValues values=new ContentValues();
        values.put("groupeid",groupeid);
        values.put("moneycash",money);
        values.put("date",format.format(date.getTime()));
        values.put("file",file);
        try {
            SQLiteDatabase db =this.getConn(context);
            long rowid=db.insert("logvente",null,values);
            db.close();
            return rowid;
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return -1;
    }
    public boolean insert_logVenteProducts (Context context, List<ProductDataDTO> products,long fk_logvente ){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        ArrayList<Boolean> results=new ArrayList<Boolean>();
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        for(int i=0;i<products.size();i++){
            ContentValues values=new ContentValues();
            values.put("groupeid",groupeid);
            values.put("fk_logvente",fk_logvente);
            values.put("productName",products.get(i).getNameProduct());
            values.put("quantity",products.get(i).getUnits());
            values.put("price",products.get(i).getPrice());
            try {
                SQLiteDatabase db =this.getConn(context);
                long rowid=db.insert("productslogvente",null,values);
                db.close();
                results.add(true);
            }catch (SQLException e){
                Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
                results.add(false);
            }
        }
        int finalize=0;
        for(int a=0;a<results.size();a++){
            if(results.get(a)==true){
                finalize++;
            }
        }
        if(finalize==products.size()){
            return true;
        }else{
            return false;
        }
    }
    public ShopDTO getShopdata(Context context, String groupeid_user) {
        ShopDTO data = null;
        String groupeid="10014";
        try{
            SQLiteDatabase db = this.getConn(context);
            Cursor c = db.rawQuery("SELECT codepdv,company_name, street, infoStreet,codepostal, phone,email,nif  FROM shopinfo WHERE groupeid = ?", new String[]{groupeid});
            if (c.moveToFirst()) {
                do {
                    String pdv=c.getString(0);
                    String company_name=c.getString(1);
                    String street =c.getString(2);
                    String infoStreet= c.getString(3);
                    String codepostal= c.getString(4);
                    String phone=c.getString(5);
                    String email=c.getString(6);
                    String nif =c.getString(7);
                    data = new ShopDTO(company_name,pdv,street,infoStreet,codepostal,phone,email,nif);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return data;
    }
    public boolean updatecompany_name(Context context,String value){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        ContentValues values=new ContentValues();
        values.put("company_name",value);
        try{
            SQLiteDatabase db =this.getConn(context);
            db.update("shopinfo",values,"groupeid= ?",new String[]{groupeid});
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            return false;
        }
        return true;
    }
    public boolean updateaddres_info(Context context, AdressDTO value){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        ContentValues values=new ContentValues();
        // street TEXT, infoStreet TEXT, phone TEXT,email TEXT,nif TEXT
        values.put("street",value.getStreet());
        values.put("infoStreet", value.getInfo_Street());
        values.put("codepostal",value.getCodePostal());
        try{
            SQLiteDatabase db =this.getConn(context);
            db.update("shopinfo",values,"groupeid= ?",new String[]{groupeid});
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            return false;
        }
        return true;
    }
    public boolean updatecompany_phone(Context context,String value){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        ContentValues values=new ContentValues();
        values.put("phone",value);
        try{
            SQLiteDatabase db =this.getConn(context);
            db.update("shopinfo",values,"groupeid= ?",new String[]{groupeid});
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            return false;
        }
        return true;
    }
    public boolean updatecompany_email(Context context,String value){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        ContentValues values=new ContentValues();
        values.put("phone",value);
        try{
            SQLiteDatabase db =this.getConn(context);
            db.update("shopinfo",values,"groupeid= ?",new String[]{groupeid});
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            return false;
        }
        return true;
    }
    public boolean updatecompany_nif(Context context,String value){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        ContentValues values=new ContentValues();
        values.put("nif",value);
        try{
            SQLiteDatabase db =this.getConn(context);
            db.update("shopinfo",values,"groupeid= ?",new String[]{groupeid});
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            return false;
        }
        return true;
    }
    public ArrayList<ClientDTO> getClientforSearch(Context context, String value) {
        ArrayList<ClientDTO> arr_name=clientBYname(context,value);
        ArrayList<ClientDTO> arr_nif=clientBYnif(context,value);
        ArrayList<ClientDTO> arr_phone=clientBYphone(context,value);
        ArrayList<ClientDTO> result= new ArrayList<>();
        if(!arr_name.isEmpty()){
            result.addAll(arr_name);
        } else if (!arr_nif.isEmpty()) {
            result.addAll(arr_nif);
        } else if (!arr_phone.isEmpty()) {
            result.addAll(arr_phone);
        }
        return result;

    }
    private ArrayList<ClientDTO> clientBYname(Context context,String name){
        ClientDTO data = null;
        String groupeid="10014";
        ArrayList<ClientDTO> arrayList=new ArrayList<ClientDTO>();
        String input="%"+name+"%";
        try{
            SQLiteDatabase db = this.getConn(context);
            Cursor c = db.rawQuery("SELECT groupeid, name, nif, phone, streetinfo FROM clientsinfo WHERE name like ?", new String[]{input});
            if (c.moveToFirst()) {
                do {
                    String nameClient=c.getString(1);
                    String nif=c.getString(2);
                    String phone =c.getString(3);
                    String streetinfo= c.getString(4);
                    data = new ClientDTO(nameClient,nif,phone,streetinfo);
                    arrayList.add(data);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return arrayList;
    }
    private ArrayList<ClientDTO> clientBYnif(Context context,String nif){
        ClientDTO data = null;
        String groupeid="10014";
        ArrayList<ClientDTO> arrayList=new ArrayList<ClientDTO>();
        String input=nif+"%";
        try{
            SQLiteDatabase db = this.getConn(context);
            Cursor c = db.rawQuery("SELECT groupeid, name, nif, phone, streetinfo FROM clientsinfo WHERE name like ?", new String[]{input});
            if (c.moveToFirst()) {
                do {
                    String nameClient=c.getString(1);
                    String nifClient=c.getString(2);
                    String phone =c.getString(3);
                    String streetinfo= c.getString(4);
                    data = new ClientDTO(nameClient,nifClient,phone,streetinfo);
                    arrayList.add(data);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return arrayList;
    }
    private ArrayList<ClientDTO> clientBYphone(Context context,String phone){
        ClientDTO data = null;
        String groupeid="10014";
        ArrayList<ClientDTO> arrayList=new ArrayList<ClientDTO>();
        String input=phone+"%";
        try{
            SQLiteDatabase db = this.getConn(context);
            Cursor c = db.rawQuery("SELECT groupeid, name, nif, phone, streetinfo FROM clientsinfo WHERE name like ?", new String[]{input});
            if (c.moveToFirst()) {
                do {
                    String nameClient=c.getString(1);
                    String nif=c.getString(2);
                    String phoneClient =c.getString(3);
                    String streetinfo= c.getString(4);
                    data = new ClientDTO(nameClient,nif,phoneClient,streetinfo);
                    arrayList.add(data);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
        }
        return arrayList;
    }
    public boolean updateStock(Context context,String ean,int units,String name){
        String pdv="28999999";//coger DB
        String groupeid="10014"; //Coger DB
        int stockSelect=0;
        try{
            SQLiteDatabase db = this.getConn(context);
            Cursor c = db.rawQuery("SELECT stock FROM product WHERE ean like ? and groupeid = ?", new String[]{ean,groupeid});
            if (c.moveToFirst()) {
                do {
                    stockSelect=c.getInt(0);
                } while (c.moveToNext());
            }
            c.close();
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            return false;
        }
        ContentValues values=new ContentValues();
        values.put("stock",stockSelect-units);
        try{
            SQLiteDatabase db =this.getConn(context);
            db.update("product",values,"ean= ? and name = ?",new String[]{ean,name});
            db.close();
        }catch (SQLException e){
            Log.e(TAG,"Failure on DB-ACCESS: "+e.getMessage());
            return false;
        }
        return true;
    }


}
