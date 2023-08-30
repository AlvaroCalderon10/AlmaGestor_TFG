package com.example.almagestor.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.almagestor.FO.SellFo;
import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Base64;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ProductDataDTO> mData;
    final LayoutInflater mInflater;
    final Context context;
    int line;
    public ListAdapter(List<ProductDataDTO> itemList, Context context, int line){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mData=itemList;
        this.line=line;
    }

    @Override
    public int getItemCount(){return mData==null ? 0 : mData.size();}

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view=mInflater.inflate(R.layout.product_cardview,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        holder.bindData(mData.get(holder.getAdapterPosition()));
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer quantite=Integer.valueOf(holder.textQuantite.getText().toString());
                    quantite++;
                    if (quantite<0){
                        holder.textQuantite.setText("0");
                        mData.get(holder.getAdapterPosition()).setUnits(0);
                    }else{
                        holder.textQuantite.setText(quantite.toString());
                        mData.get(holder.getAdapterPosition()).setUnits(quantite);
                        TextView txtView =((Activity)context).findViewById(R.id.cash_money);
                        Double money=((SellFo) context).getMoney_shop();
                        Double value=Double.valueOf(mData.get(holder.getAdapterPosition()).getPrice());
                        Double sum=Math.round((money+value)*100.0)/100.0;
                        txtView.setText(String.valueOf(sum)+"€");
                        ((SellFo) context).setMoney_shop(sum);
                        ((SellFo) context).update_quantite(mData.get(holder.getAdapterPosition()),quantite);
                    }
                }
            });
        holder.btn_less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer quantite=Integer.valueOf(holder.textQuantite.getText().toString());
                    quantite--;
                    if (quantite<0){
                        holder.textQuantite.setText("0");
                        mData.get(holder.getAdapterPosition()).setUnits(0);
                    }else{
                        holder.textQuantite.setText(quantite.toString());
                        mData.get(holder.getAdapterPosition()).setUnits(quantite);
                        TextView txtView =((Activity)context).findViewById(R.id.cash_money);
                        Double money=((SellFo) context).getMoney_shop();
                        Double value=Double.valueOf(mData.get(holder.getAdapterPosition()).getPrice());
                        Double minus=Math.round((money-value)*100.0)/100.0;
                        txtView.setText(String.valueOf(minus)+"€");
                        ((SellFo) context).setMoney_shop(minus);
                        ((SellFo) context).update_quantite(mData.get(holder.getAdapterPosition()),quantite);
                    }
                }
            });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtView =((Activity)context).findViewById(R.id.cash_money);
                Double money=((SellFo) context).getMoney_shop();
                Double price=Math.round((mData.get(holder.getAdapterPosition()).getPrice())*100.0)/100.0;
                Double multiply=price*(mData.get(holder.getAdapterPosition()).getUnits());
                multiply=Math.round(multiply*100.0)/100.0;
                Double minus=Math.round((money-multiply)*100.0)/100.0;
                txtView.setText(String.valueOf(minus+"€"));
                ((SellFo) context).setMoney_shop(minus);
                mData.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        }

    public void setItems(List<ProductDataDTO> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView textProduct,textEan,textPrice,textQuantite;
        FloatingActionButton btn_less, btn_plus,btn_delete;
        ViewHolder(View itemView){
            super(itemView);
            iconImage=itemView.findViewById(R.id.IconImageView);
            textProduct=itemView.findViewById(R.id.product_name);
            textEan=itemView.findViewById(R.id.product_ean);
            textPrice=itemView.findViewById(R.id.product_price);
            textQuantite=itemView.findViewById(R.id.quantite);
            btn_less=itemView.findViewById(R.id.btn_minus_stock);
            btn_plus=itemView.findViewById(R.id.btn_plus_stock);
            btn_delete=itemView.findViewById(R.id.btn_delete_sellFo);
            }
        void bindData(final ProductDataDTO item){
            if(!item.getImg().equals("1")){
                iconImage.setImageBitmap(BitmapFactory.decodeByteArray((Base64.getDecoder().decode(item.getImg())),0,(Base64.getDecoder().decode(item.getImg()).length)));
            }
            textProduct.setText(item.getNameProduct());
            textPrice.setText(String.valueOf(item.getPrice())+" €");
            textEan.setText("EAN: "+item.getEan());
            textQuantite.setText(String.valueOf(item.getUnits()));
        }
    }
}
