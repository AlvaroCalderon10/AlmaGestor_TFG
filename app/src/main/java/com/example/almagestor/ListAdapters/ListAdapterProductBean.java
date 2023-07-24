package com.example.almagestor.ListAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ean=holder.textEan.getText().toString();
                ean=ean.replace("EAN: ","");
                mData.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                SqliteModel obj=new SqliteModel();
                if(obj.delete_Product(context,ean)){
                    Toast.makeText(context, context.getResources().getString(R.string.ProductoBorrado), Toast.LENGTH_SHORT).show();
                };
            }
        });
    }

    public void setItems(List<ProductDataDTO> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView textProduct,textEan,textQuantite, textPrice;
        FloatingActionButton Delete;

        ViewHolder(View itemView){
            super(itemView);
            iconImage=itemView.findViewById(R.id.IconImageView_productbean);
            textProduct=itemView.findViewById(R.id.product_name_productbean);
            textEan=itemView.findViewById(R.id.product_ean_productbean);
            textQuantite=itemView.findViewById(R.id.quantite_productbean);
            textPrice=itemView.findViewById(R.id.product_price_productbean);
            Delete=itemView.findViewById(R.id.btn_delete_productbean);
        }
        void bindData(final ProductDataDTO item){
            if(!item.getImg().equals("1")){
                iconImage.setImageBitmap(BitmapFactory.decodeByteArray((Base64.getDecoder().decode(item.getImg())),0,(Base64.getDecoder().decode(item.getImg()).length)));
            }
            textProduct.setText(item.getNameProduct());
            textEan.setText("EAN: "+item.getEan());
            textQuantite.setText(String.valueOf(item.getUnits()));
            textPrice.setText(context.getResources().getString(R.string.info_product_price)+String.valueOf(item.getPrice()));
        }
    }
}
