package com.example.almagestor.FO;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almagestor.CallandMsg.MsgalertDialog;
import com.example.almagestor.Clients.clients;
import com.example.almagestor.DTOs.ShopDTO;
import com.example.almagestor.Facture.FacturePDF;
import com.example.almagestor.GoogleDrive.GoogleDriveActivity;
import com.example.almagestor.ListAdapters.ListAdapter;
import com.example.almagestor.MainActivity;
import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.Products.ProductsBean;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.example.almagestor.Threads.BDThread;
import com.example.almagestor.Threads.StockThread;
import com.example.almagestor.shopActivity.Shop;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.pdfview.PDFView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SellFo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int STORAGE_PERMISSION_REQUEST_CODE=9;
    private static final int GET_ACOUNTS_PERMISSION_REQUEST_CODE=8;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE=7;
    private static final String TAG = "SellFO";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView screen,money;
    String textOnscreen="";
    MaterialButton btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9;
    MaterialButton btn_ok,btn_finish,btn_scan;
    MaterialButton btn_info,btn_reset,btn_delete;
    List<ProductDataDTO> elements=new ArrayList<>();

    String barCode;
    Double money_shop;
    PDFView viewPDF;
    Button OK,Cancel;
    ImageView Close;
    Button btn_efective,btn_card;
    EditText quantite_return_paiment, input_quantite_paiment;
    TextView quantiteToPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(SellFo.this.getResources().getString(R.string.SELLFOBean));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(SellFo.this.getColor(R.color.purple_700)));
        setContentView(R.layout.activity_sell_fo);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        screen=findViewById(R.id.textView);
        toolbar.setVisibility(View.GONE);
        money=findViewById(R.id.cash_money);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenNavigation,R.string.CloseNavigation);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_sell);

        //buttons
        assignId(btn_1,R.id.btn_1);
        assignId(btn_2,R.id.btn_2);
        assignId(btn_3,R.id.btn_3);
        assignId(btn_4,R.id.btn_4);
        assignId(btn_5,R.id.btn_5);
        assignId(btn_6,R.id.btn_6);
        assignId(btn_7,R.id.btn_7);
        assignId(btn_8,R.id.btn_8);
        assignId(btn_9,R.id.btn_9);
        assignId(btn_ok,R.id.btn_ok);
        assignId(btn_finish,R.id.btn_finish);
        assignId(btn_scan,R.id.btn_scan);
        assignId(btn_info,R.id.btn_info);
        assignId(btn_reset,R.id.btn_reset);
        assignId(btn_delete,R.id.btn_delete);
        money_shop=Double.valueOf(0);

    }
    void assignId(MaterialButton btn, int id){
        btn=findViewById(id);
       btn.setOnClickListener(this::onClick);
    }

    public void onClick(View view){
        MaterialButton button=(MaterialButton) view;

        if(button.getTag()==null){
            screen.setVisibility(View.VISIBLE);
            String buttonText = button.getText().toString();
            textOnscreen=textOnscreen+buttonText;
            screen.setText(textOnscreen.trim());
            return;
        }else{
            if(button.getTag().toString().equals("6")){ //Delete one
                if(!textOnscreen.isEmpty()){
                    textOnscreen=textOnscreen.substring(0,textOnscreen.length()-1);
                    if(textOnscreen.isEmpty()){
                        screen.setVisibility(View.GONE);
                    }
                    screen.setText(textOnscreen.trim());
                }
                return;
            } else if (button.getTag().toString().equals("5")) { // Reset
                textOnscreen="";
                screen.setText(textOnscreen.trim());
                screen.setVisibility(View.GONE);
                return;
            } else if (button.getTag().toString().equals("1")) {
                init(screen.getText().toString());
                screen.setVisibility(View.GONE);
                textOnscreen="";
                screen.setText(textOnscreen.trim());
                return;
            } else if(button.getTag().toString().equals("3")){ //SCAN
                scanCode(); //Scan Scan
                return;
            }else if(button.getTag().toString().equals("2")) {//Finish
                Log.i(TAG,"Finish buy go to payment");
                //Forma de pago
                init_paiment();
            }else if(button.getTag().toString().equals("4")){//menu buton
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        }
    }
    public void init_paiment(){
        Dialog dialog= new Dialog(SellFo.this);
        dialog.setContentView(R.layout.card_efective_lay);
        Close=dialog.findViewById(R.id.btn_close_card_efective);
        btn_efective=dialog.findViewById(R.id.efective_btn_lay);
        btn_card=dialog.findViewById(R.id.tarjeta_btn_lay);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window_client);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processFacture();
                dialog.dismiss();
            }
        });
        btn_efective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init_efective_paiment();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void init_efective_paiment(){
        Dialog dialog= new Dialog(SellFo.this);
        dialog.setContentView(R.layout.efective_paiment_lay);
        Close=dialog.findViewById(R.id.btn_close_efective_paiment);
        OK=dialog.findViewById(R.id.btn_yes_efective_paiment);
        Cancel=dialog.findViewById(R.id.btn_no_lay_efective_paiment);
        quantiteToPay=dialog.findViewById(R.id.total_efective_paiment);
        input_quantite_paiment=dialog.findViewById(R.id.cobrado_efective_paiment);
        quantite_return_paiment=dialog.findViewById(R.id.return_efective_paiment);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window_client);
        quantiteToPay.setText(money_shop.toString().trim());
        input_quantite_paiment.addTextChangedListener(onTextChangedListener());
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
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processFacture();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void processFacture(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
             //Generar factura simple compra continue
        }else{
            String value=Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String[] permissionRequested=new String[]{value};
            requestPermissions(permissionRequested,STORAGE_PERMISSION_REQUEST_CODE);
        }
        FacturePDF objFacture= new FacturePDF();
        try{
            SqliteModel obj=new SqliteModel();
            ShopDTO shopDT = obj.getShopdata(SellFo.this,"10014");
            if(shopDT.someNull() ==false){
                Calendar calendar= Calendar.getInstance();
                //Stock Thread update
                StockThread thread1=new StockThread(this,elements);
                thread1.run();
                File file_pdf=objFacture.createPdf(elements,calendar,this.money_shop,shopDT);
                //Thread works on inputs while execution doesnt stops.
                if(file_pdf!=null){
                    BDThread thread=new BDThread(this,money_shop,calendar, file_pdf.toString(),elements);
                    Log.i(TAG,file_pdf.toString());
                    thread.start();
                    showPDF(file_pdf);
                }else{
                    Log.w(TAG,"Facture creation faileture");
                }

            }else{
                //Dialog de aviso rellenar tienda
                MsgalertDialog alert=new MsgalertDialog();
                alert.initDialog(SellFo.this,getResources().getString(R.string.MagasinNotCompleted));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestcode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestcode,permissions,grantResults);
        if (requestcode==STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                processFacture();
            } else{
                Toast.makeText(this, "Unable to invoke storage without permision", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showPDF(File file_pdf) {
            Dialog dialog= new Dialog(SellFo.this);
            dialog.setContentView(R.layout.custom_show_pdfs);
            OK=dialog.findViewById(R.id.btn_yes_pdf);
            Cancel=dialog.findViewById(R.id.btn_no_pdf);
            Close=dialog.findViewById(R.id.btn_close_pdf);
            viewPDF=dialog.findViewById(R.id.show_pdf);
            viewPDF.fromFile(file_pdf);
            viewPDF.isZoomEnabled();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window_pdf);
            Close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reset_ListProducts();
                    dialog.dismiss();
                    return;
                }
            });
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reset_ListProducts();
                    dialog.dismiss();
                    return;
                }
            });
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reset_ListProducts();
                    dialog.dismiss();
                    return;
                }
            });
            viewPDF.show();
            dialog.show();
    }

    public void init(String value){
        SqliteModel obj =new SqliteModel();
        ProductDataDTO product=obj.getProduct(SellFo.this,value);
        if(product!=null){
            Log.i(TAG,product.print_Product());
            elements.add(new ProductDataDTO(product.getImg(),product.getNameProduct(), product.getEan(), 1, product.getPrice()));
            update_cashMoney(product.getPrice());
            money_shop=money_shop+product.getPrice();
            ListAdapter listAdapter=new ListAdapter(elements,this,1);
            RecyclerView recyclerView=findViewById(R.id.listRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapter);
        }else{
            //Producto no encontrado
            Log.w(TAG,"Product not found");
            MsgalertDialog alert=new MsgalertDialog();
            alert.initDialog(SellFo.this,getResources().getString(R.string.ProductNotFound));
        }

    }
    public void update_cashMoney(Double value){
        money.setText(String.valueOf(money_shop+value)+"€");
        Log.i(TAG,"Updated money shop"+money.toString());
    }
    public void update_quantite(ProductDataDTO product, int quantite){
        Log.i(TAG,product.print_Product());
        elements.forEach( e ->{
            if(e.getNameProduct().equals(product.getNameProduct()) && e.getEan().equals(product.getEan())){
                e.setUnits(quantite);
            }
        });
    }
    private void scanCode() {
        Log.i(TAG,"Scan on FO sell");
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);
        barLaucher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SellFo.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            if(!result.getContents().isEmpty()){
                barCode =result.getContents().toString().trim();
                init(barCode);
            }
        }
    });

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
            //Back Controller
            Intent intent = new Intent(SellFo.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_sell:
                break;
            case R.id.nav_tienda:
                Log.i(TAG,"TIENDA NAV");
                Intent intent = new Intent(SellFo.this, Shop.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.nav_clientes:
                Log.i(TAG,"Clientes NAV");
                Toast.makeText(this,"Clientes", Toast.LENGTH_SHORT).show();
                Intent intent3=new Intent(SellFo.this, clients.class);
                startActivity(intent3);
                overridePendingTransition(0,0);
                break;
            case R.id.nav_producto:
                Log.i(TAG,"Productos NAV");
                Intent intent2 = new Intent(SellFo.this, ProductsBean.class);
                startActivity(intent2);
                overridePendingTransition(0,0);
                break;
            case R.id.nav_bdExport_tienda:
                Log.i(TAG,"BDExport NAV");
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    //Generar factura simple compra continue
                }else{
                    String value=Manifest.permission.READ_EXTERNAL_STORAGE;
                    String value1=Manifest.permission.WRITE_EXTERNAL_STORAGE;
                    String[] permissionRequested=new String[]{value,value1};
                    requestPermissions(permissionRequested,STORAGE_PERMISSION_REQUEST_CODE);
                }
                Intent intent4 = new Intent(SellFo.this, GoogleDriveActivity.class);
                startActivity(intent4);
                overridePendingTransition(0,0);
        }

        return true;
    }
    public Double getMoney_shop() {
        return money_shop;
    }
    public void setMoney_shop(Double money_shop) {
        this.money_shop = money_shop;
    }

    public void reset_ListProducts(){
        Log.i(TAG,"Restart list elements");
        elements=new ArrayList<>();
        money.setText("0.00"+"€");
        money_shop=0.00;
        ListAdapter listAdapter=new ListAdapter(elements,this,1);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                input_quantite_paiment.removeTextChangedListener(this);
                DecimalFormat df = new DecimalFormat("#.00");
                try {
                    String inputString=input_quantite_paiment.getText().toString();
                    Double toPay= Double.valueOf(df.format(money_shop.doubleValue()));
                    Double inputDouble=Double.valueOf(inputString);
                    String returnValue=df.format(toPay-inputDouble);

                    //setting text after format to EditText
                    quantite_return_paiment.setText(returnValue);
                    quantite_return_paiment.setSelection(input_quantite_paiment.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                input_quantite_paiment.addTextChangedListener(this);
            }
        };
    }

}