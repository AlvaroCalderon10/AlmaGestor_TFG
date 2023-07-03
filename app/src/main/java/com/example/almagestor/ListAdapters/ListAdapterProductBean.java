package com.example.almagestor.ListAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.R;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

public class ListAdapterProductBean extends RecyclerView.Adapter<ListAdapterProductBean.ViewHolder>{
    private List<ProductDataDTO> mData;
    final LayoutInflater mInflater;
    final Context context;
    int line;
    public ListAdapterProductBean(List<ProductDataDTO> itemList, Context context, int line){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mData=itemList;
        this.line=line;
    }

    @Override
    public int getItemCount(){return mData==null ? 0 : mData.size();}

    @Override
    public ListAdapterProductBean.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view=mInflater.inflate(R.layout.product_data_bean,null);
        return new ListAdapterProductBean.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterProductBean.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ProductDataDTO> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView textProduct,textEan,textQuantite, textPrice;
        ViewHolder(View itemView){
            super(itemView);
            iconImage=itemView.findViewById(R.id.IconImageView_productbean);
            textProduct=itemView.findViewById(R.id.product_name_productbean);
            textEan=itemView.findViewById(R.id.product_ean_productbean);
            textQuantite=itemView.findViewById(R.id.quantite_productbean);
            textPrice=itemView.findViewById(R.id.product_price_productbean);
        }
        void bindData(final ProductDataDTO item){
            if(!item.getImg().equals("1")){
                iconImage.setImageBitmap(BitmapFactory.decodeByteArray((Base64.getDecoder().decode(item.getImg())),0,(Base64.getDecoder().decode(item.getImg()).length)));
            }
            textProduct.setText(item.getNameProduct());
            textEan.setText(item.getEan());
            textQuantite.setText("1");
            textPrice.setText("5");
        }
    }
}
