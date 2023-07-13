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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almagestor.DTOs.AdressDTO;
import com.example.almagestor.Facture.FacturePDF;
import com.example.almagestor.ListAdapters.ListAdapter;
import com.example.almagestor.MainActivity;
import com.example.almagestor.Products.ProductDataDTO;
import com.example.almagestor.Products.ProductsBean;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.example.almagestor.shopActivity.Shop;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.pdfview.PDFView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SellFo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_fo);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        screen=findViewById(R.id.textView);
        toolbar.setVisibility(View.GONE);
        money=findViewById(R.id.cash_money);

        setSupportActionBar(toolbar);
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
                init(screen.getText().toString(),10.00);
                screen.setVisibility(View.GONE);
                textOnscreen="";
                screen.setText(textOnscreen.trim());
                return;
            } else if(button.getTag().toString().equals("3")){ //SCAN
                scanCode(); //Scan Scan
                return;
            }else if(button.getTag().toString().equals("2")) {//Finish
                //Generar factura simple compra
                FacturePDF objFacture= new FacturePDF();
                try{
                    Calendar calendar= Calendar.getInstance();
                    File file_pdf=objFacture.createPdf(elements,calendar,this.money_shop);
                    //Thread works on inputs while execution doesnt stops.
                    BDThread thread=new BDThread(this,money_shop,calendar, file_pdf.toString(),elements);
                    thread.start();
                    if(file_pdf!=null){
                        showPDF(file_pdf);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
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
                    dialog.dismiss();
                    return;
                }
            });
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    return;
                }
            });
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    return;
                }
            });
            viewPDF.show();
            dialog.show();
    }

    public void init(String value,double price){
        elements.add(new ProductDataDTO("img","Producto Prueba",value,1,price));
        update_cashMoney(price);
        money_shop=money_shop+price;
        ListAdapter listAdapter=new ListAdapter(elements,this,1);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

    }
    public void update_cashMoney(Double value){
        money.setText(String.valueOf(money_shop+value)+"€");
    }
    private void scanCode() {
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
                init(barCode,10.00);
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
                Intent intent = new Intent(SellFo.this, Shop.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.nav_facturas:
                Toast.makeText(this,"Facturas", Toast.LENGTH_SHORT).show();
            case R.id.nav_producto:
                Intent intent2 = new Intent(SellFo.this, ProductsBean.class);
                startActivity(intent2);
                overridePendingTransition(0,0);
                break;
        }

        return true;
    }
    public Double getMoney_shop() {
        return money_shop;
    }
    public void setMoney_shop(Double money_shop) {
        this.money_shop = money_shop;
    }

    class BDThread extends Thread{
        Context context;
        Double money;
        Calendar calendar;
        String file;
        List<ProductDataDTO> products;
        long id_fk;
        BDThread (Context context,Double money,Calendar calendar,String file, List<ProductDataDTO> products){
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
                    Toast.makeText(SellFo.this,"DB incorrect insert on products", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(SellFo.this,"DB incorrect id on logvente", Toast.LENGTH_SHORT).show();
            }
        }
    }

}