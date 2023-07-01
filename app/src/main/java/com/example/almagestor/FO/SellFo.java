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
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almagestor.ListAdapters.ListAdapter;
import com.example.almagestor.MainActivity;
import com.example.almagestor.Products.ProductsBean;
import com.example.almagestor.R;
import com.example.almagestor.shopActivity.Shop;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class SellFo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView screen;
    String textOnscreen="";
    MaterialButton btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9;
    MaterialButton btn_ok,btn_finish,btn_scan;
    MaterialButton btn_info,btn_reset,btn_delete;
    List<ProductDataList> elements=new ArrayList<>();

    String barCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_fo);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        screen=findViewById(R.id.textView);
        toolbar.setVisibility(View.GONE);

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
            }

        }
    }

    public void init(String value){
        elements.add(new ProductDataList("img","Producto Prueba",value,"1"));
        ListAdapter listAdapter=new ListAdapter(elements,this,1);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

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
}