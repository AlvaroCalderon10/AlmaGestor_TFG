package com.example.almagestor.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.almagestor.FO.SellFo;
import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                        txtView.setText(String.valueOf(money+mData.get(holder.getAdapterPosition()).getPrice())+"€");
                        ((SellFo) context).setMoney_shop(money+mData.get(holder.getAdapterPosition()).getPrice());
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
                        txtView.setText(String.valueOf(money-mData.get(holder.getAdapterPosition()).getPrice())+"€");
                        ((SellFo) context).setMoney_shop(money-mData.get(holder.getAdapterPosition()).getPrice());
                    }
                }
            });
        }

    public void setItems(List<ProductDataDTO> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView textProduct,textEan,textQuantite;
        FloatingActionButton btn_less, btn_plus;
        ViewHolder(View itemView){
            super(itemView);
            iconImage=itemView.findViewById(R.id.IconImageView);
            textProduct=itemView.findViewById(R.id.product_name);
            textEan=itemView.findViewById(R.id.product_ean);
            textQuantite=itemView.findViewById(R.id.quantite);
            btn_less=itemView.findViewById(R.id.btn_minus_stock);
            btn_plus=itemView.findViewById(R.id.btn_plus_stock);
            }
        void bindData(final ProductDataDTO item){
            //iconImage.setImageIcon();
            textProduct.setText(item.getNameProduct());
            textEan.setText(item.getEan());
            textQuantite.setText("1");
        }
    }
}
