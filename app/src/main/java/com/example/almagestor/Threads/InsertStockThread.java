package com.example.almagestor.Threads;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.Sqlite.SqliteModel;

import java.util.ArrayList;
import java.util.List;

public class InsertStockThread extends Thread{
    private static final String TAG = "StockThreadProductBean";
    Context context;
    ProductDataDTO element=new ProductDataDTO();
    int quantite;
    public InsertStockThread(Context context, ProductDataDTO element ,int quantite){
        this.context=context;
        this.element=element;
        this.quantite=quantite;
    }
    @Override
    public void run(){
        SqliteModel obj=new SqliteModel();
        Boolean result;

        try{
            result=obj.updateStock(context,element.getEan(),quantite,element.getNameProduct());
            if (!result) {
                Log.w(TAG,"Not UPDATED STOCK");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
