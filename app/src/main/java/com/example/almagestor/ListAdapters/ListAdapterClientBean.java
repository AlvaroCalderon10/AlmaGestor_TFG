package com.example.almagestor.ListAdapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.almagestor.CallandMsg.CallandMsg;
import com.example.almagestor.DTOs.ClientDTO;
import com.example.almagestor.R;

import java.util.Base64;
import java.util.List;

public class ListAdapterClientBean extends RecyclerView.Adapter<ListAdapterClientBean.ViewHolder> {
    private List<ClientDTO> mData;
    final LayoutInflater mInflater;
    final Context context;
    int line;
    public ListAdapterClientBean(List<ClientDTO> itemList, Context context, int line){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mData=itemList;
        this.line=line;
    }

    @Override
    public int getItemCount(){return mData==null ? 0 : mData.size();}

    @Override
    public ListAdapterClientBean.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view=mInflater.inflate(R.layout.client_data_bean,null);
        return new ListAdapterClientBean.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterClientBean.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));

        holder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallandMsg obj=new CallandMsg();
                if(!obj.sendMSG(holder.phoneClient.getText().toString(),context)){
                    Toast.makeText(context, "FAIL on send MSG", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallandMsg obj=new CallandMsg();
                if(!obj.sendCALL(holder.phoneClient.getText().toString(),context)){
                    Toast.makeText(context, "FAIL on send CALL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setItems(List<ClientDTO> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameClient,nifClient,phoneClient;
        ImageButton call,msg;
        ViewHolder(View itemView){
            super(itemView);
            nameClient=itemView.findViewById(R.id.client_name_clientbean);
            nifClient=itemView.findViewById(R.id.client_nif_clientbean);
            phoneClient=itemView.findViewById(R.id.client_phone_clientbean);
            call=itemView.findViewById(R.id.callPhone_btn_clientbean);
            msg=itemView.findViewById(R.id.msg_btn_clientbean);
        }
        void bindData(final ClientDTO item){
            nameClient.setText(item.getName());
            nifClient.setText("NIF: "+item.getNif());
            phoneClient.setText("Phone: "+item.getPhone());
        }
    }
}
