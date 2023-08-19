package com.example.almagestor.Threads;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.Sqlite.SqliteModel;

import java.util.ArrayList;
import java.util.List;

public class StockThread extends Thread{
    private static final String TAG = "StockThread";
    Context context;
    List<ProductDataDTO> elements=new ArrayList<>();
    public StockThread (Context context,List<ProductDataDTO> elements ){
        this.context=context;
        this.elements=elements;
    }
    @Override
    public void run(){
        SqliteModel obj=new SqliteModel();
        Boolean results;
        for(int i=0;i<elements.size();i++){
            try{
                results=obj.updateStockBuy(context,elements.get(i).getEan(),elements.get(i).getUnits(),elements.get(i).getNameProduct());
                if(results==false){
                    Log.w(TAG,elements.get(i).getNameProduct() +" No ha sido actualizado su Stock");
                }else{
                    Log.i(TAG,elements.get(i).getNameProduct() +" Stock UPDATED");
                }
            }catch (SQLException e){
                Log.w(TAG,e.getMessage());
            }
        }
    }
}
