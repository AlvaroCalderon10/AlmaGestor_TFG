package com.example.almagestor.Threads;

import android.content.Context;
import android.icu.util.Calendar;
import android.widget.Toast;

import com.example.almagestor.FO.SellFo;
import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.Sqlite.SqliteModel;

import java.util.List;

public class BDThread extends Thread{
        Context context;
        Double money;
        Calendar calendar;
        String file;
        List<ProductDataDTO> products;
        long id_fk;
        public BDThread(Context context, Double money, Calendar calendar, String file, List<ProductDataDTO> products){
            this.context=context;
            this.money=money;
            this.calendar=calendar;
            this.file=file;
            this.products=products;
        }
        @Override
        public void run(){
            SqliteModel obj=new SqliteModel();
            id_fk= obj.insert_logVente(context,money.toString(),calendar,file);
            if(id_fk!=-1){
                Boolean Bd_insert=obj.insert_logVenteProducts(context,products,id_fk);
                if(Bd_insert==false){
                    Toast.makeText(context,"DB incorrect insert on products", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context,"DB incorrect id on logvente", Toast.LENGTH_SHORT).show();
            }
        }
}

