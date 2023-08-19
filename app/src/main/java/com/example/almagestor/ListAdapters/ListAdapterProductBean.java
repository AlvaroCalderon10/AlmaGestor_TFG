package com.example.almagestor.ListAdapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.almagestor.DTOs.ClientDTO;
import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.Products.ProductsBean;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.example.almagestor.Threads.InsertStockThread;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

public class ListAdapterProductBean extends RecyclerView.Adapter<ListAdapterProductBean.ViewHolder>{
    private List<ProductDataDTO> mData;
    final LayoutInflater mInflater;
    final Context context;
    int line;

    EditText stockInput;
    Button Save,Cancel;
    ImageView Close;
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
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifiedStockDialog(holder);
            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(holder);
            }
        });
    }

    public void setItems(List<ProductDataDTO> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView textProduct,textEan,textQuantite, textPrice;
        FloatingActionButton Delete;
        LinearLayout box;

        ViewHolder(View itemView){
            super(itemView);
            iconImage=itemView.findViewById(R.id.IconImageView_productbean);
            textProduct=itemView.findViewById(R.id.product_name_productbean);
            textEan=itemView.findViewById(R.id.product_ean_productbean);
            textQuantite=itemView.findViewById(R.id.quantite_productbean);
            textPrice=itemView.findViewById(R.id.product_price_productbean);
            Delete=itemView.findViewById(R.id.btn_delete_productbean);
            box=itemView.findViewById(R.id.LinearLayout_productDataBean);
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
    public void modifiedStockDialog(ViewHolder holder){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_stock_product_lay);
        Save=dialog.findViewById(R.id.btn_yes_lay_UPDATESTOCK);
        Cancel=dialog.findViewById(R.id.btn_no_lay_UPDATESTOCK);
        Close=dialog.findViewById(R.id.btn_close_UPDATESTOCK);
        stockInput=dialog.findViewById(R.id.editext_stock_UPDATESTOCK);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.border_output_box);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(Integer.valueOf(stockInput.getText().toString())!=mData.get(holder.getAdapterPosition()).getUnits()){
                        holder.textQuantite.setText(stockInput.getText().toString());
                        ((ProductsBean) context).updateStockItem(mData.get(holder.getAdapterPosition()),Integer.valueOf(stockInput.getText().toString()));
                        //Lanzar hilo en BD
                        InsertStockThread thread=new InsertStockThread(context,mData.get(holder.getAdapterPosition()),Integer.valueOf(stockInput.getText().toString()));
                        thread.run();
                    }

                    dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void deleteProduct(ViewHolder holder){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.are_you_sure_dialog);
        Save=dialog.findViewById(R.id.btn_yes_areyouSure);
        Cancel=dialog.findViewById(R.id.btn_no_areyouSure);
        Close=dialog.findViewById(R.id.btn_close_areyousure);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.border_output_box);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
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

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
