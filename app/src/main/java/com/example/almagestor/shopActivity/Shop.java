package com.example.almagestor.shopActivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.almagestor.DTOs.AdressDTO;
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

    Button Save,Cancel;
    ImageView Close;
    EditText Street,info_Street,Codepostal;
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
        more_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog= new Dialog(Shop.this);
                dialog.setContentView(R.layout.custom_dialog_adress);
                Save=dialog.findViewById(R.id.btn_yes);
                Cancel=dialog.findViewById(R.id.btn_no);
                Close=dialog.findViewById(R.id.btn_close);
                Street=dialog.findViewById(R.id.Street_edit);
                info_Street=dialog.findViewById(R.id.town_edit);
                Codepostal=dialog.findViewById(R.id.Codepostal_edit);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);
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
                        if(!Street.getText().toString().isEmpty() && !info_Street.getText().toString().isEmpty() && !Codepostal.getText().toString().isEmpty()){
                            AdressDTO adressDTO=new AdressDTO(Street.getText().toString(),info_Street.getText().toString(),Codepostal.getText().toString());
                            update_info_adress(adressDTO);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
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
    public void update_info_adress(AdressDTO adressDTO){
        if(adressDTO!=null){
            if(!adressDTO.getStreet().isEmpty()){
                street_shop.setText(adressDTO.getStreet());
            }
            if (!adressDTO.getInfo_Street().isEmpty() && !adressDTO.getCodePostal().isEmpty()) {
                info_street_shop.setText(adressDTO.getInfo_Street()+"\n"+adressDTO.getCodePostal());
            }
        }
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