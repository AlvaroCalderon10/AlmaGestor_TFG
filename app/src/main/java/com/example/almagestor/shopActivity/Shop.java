package com.example.almagestor.shopActivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.almagestor.FO.SellFo;
import com.example.almagestor.Login.NewUser;
import com.example.almagestor.MainActivity;
import com.example.almagestor.R;
import com.google.android.material.navigation.NavigationView;

public class Shop extends AppCompatActivity {
    ImageView back_img;
    TextView name_shop,pdv_shop,more_information, street_shop, info_street_shop, email_shop, phone_shop;
    TextView pdv;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initViews();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Shop.this, SellFo.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void initViews(){
        back_img=findViewById(R.id.back_img);
        name_shop=findViewById(R.id.user_name_shop);
        pdv_shop=findViewById(R.id.user_pdv_shop);
        more_information=findViewById(R.id.user_addInformation);
        street_shop=findViewById(R.id.user_street);
        info_street_shop=findViewById(R.id.user_info_street);
        email_shop=findViewById(R.id.user_email);
        phone_shop=findViewById(R.id.user_phone);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Back Controller
        Intent intent = new Intent(Shop.this, SellFo.class);
        startActivity(intent);
        finish();
    }

}