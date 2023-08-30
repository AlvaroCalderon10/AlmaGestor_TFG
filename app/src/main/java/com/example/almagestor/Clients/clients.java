package com.example.almagestor.Clients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.almagestor.DTOs.ClientDTO;
import com.example.almagestor.ListAdapters.ListAdapterClientBean;
import com.example.almagestor.Products.ProductsBean;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class clients extends AppCompatActivity {
    private static final String TAG = "Clients";
    private static final int PHONE_PERMISSION_REQUEST_CODE =100;
    List<ClientDTO> elementsLocal=new ArrayList<>();
    List<ClientDTO> elementsSearch=new ArrayList<>();
    EditText inputdata;
    Button search;
    FloatingActionButton add_client;
    EditText nameInput,NIFInput,phoneInput,streetInput;
    Button Save,Cancel;
    ImageView Close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        inputdata=findViewById(R.id.input_text_clients);
        search=findViewById(R.id.search_btn_clients);
        add_client=findViewById(R.id.add_btn_client);
        getSupportActionBar().setTitle(clients.this.getResources().getString(R.string.FOClients));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(clients.this.getColor(R.color.purple_700)));
        init_list();
        init_permissions();

        add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog= new Dialog(clients.this);
                dialog.setContentView(R.layout.input_data_client);
                Save=dialog.findViewById(R.id.btn_yes_inputdata_client);
                Cancel=dialog.findViewById(R.id.btn_no_inputdata_client);
                Close=dialog.findViewById(R.id.btn_close_inputdata_client);
                nameInput=dialog.findViewById(R.id.inputName_data_clientData);
                NIFInput=dialog.findViewById(R.id.inputNIF_data_clientData);
                phoneInput=dialog.findViewById(R.id.inputPHONE_data_clientData);
                streetInput=dialog.findViewById(R.id.inputSTREET_data_clientData);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window_client);
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
                        if(!nameInput.getText().toString().isEmpty() && !NIFInput.getText().toString().isEmpty() && !phoneInput.getText().toString().isEmpty() && !streetInput.getText().toString().isEmpty()){
                            ClientDTO clientDTO=new ClientDTO(nameInput.getText().toString(),NIFInput.getText().toString(),phoneInput.getText().toString(),streetInput.getText().toString());
                            //Insert on DB
                            insert_client(clientDTO);
                            //List on ListElement
                            Log.i(TAG,"InsertedClient");
                            init_list();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Searching");
                String data=inputdata.getText().toString().trim();
                if(!data.isEmpty()){
                    SqliteModel obj=new SqliteModel();
                    ArrayList<ClientDTO> list_DB=obj.getClientforSearch(clients.this,data);
                    if(!list_DB.isEmpty()){
                        //CreateView ListAdapter
                        init_searchlist(list_DB);
                    }
                }else{
                    init_list();
                }
            }
        });

    }

    private void insert_client(ClientDTO clientDTO) {
        Log.i(TAG,"InsertClient");
        SqliteModel obj=new SqliteModel();
        obj.insert_client(clients.this,clientDTO);
    }

    public void init_list(){
        elementsLocal=new ArrayList<>();
        SqliteModel obj=new SqliteModel();
        ArrayList<ClientDTO> clients_list=obj.list_clients(clients.this,"10014");
        for(ClientDTO e:clients_list){
            elementsLocal.add(new ClientDTO(e.getName(),e.getNif(),e.getPhone(),e.getStreet()));
        }
        ListAdapterClientBean listAdapterClientBean=new ListAdapterClientBean(elementsLocal, this,1);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView_clients);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(clients.this));
        recyclerView.setAdapter(listAdapterClientBean);
    }
    public void init_searchlist(ArrayList<ClientDTO> data){
        ListAdapterClientBean listAdapterClientBean=new ListAdapterClientBean(data, this,1);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView_clients);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(clients.this));
        recyclerView.setAdapter(listAdapterClientBean);
    }
    public void init_permissions(){
        if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){

        }else{
            String value=Manifest.permission.SEND_SMS;
            String[] permissionRequested=new String[]{value};
            requestPermissions(permissionRequested, PHONE_PERMISSION_REQUEST_CODE);
        }
        if(checkSelfPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){

        }else{
            String value=Manifest.permission.CALL_PHONE;
            String[] permissionRequested=new String[]{value};
            requestPermissions(permissionRequested, PHONE_PERMISSION_REQUEST_CODE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestcode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestcode,permissions,grantResults);
        if (requestcode== PHONE_PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                return;
            } else{
                Log.e(TAG,"Unable to invoke Send mesagges without permision");
                Toast.makeText(this, "Unable to invoke Send mesagges without permision", Toast.LENGTH_SHORT).show();
            }
        }
    }
}