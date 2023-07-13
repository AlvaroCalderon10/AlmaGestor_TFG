package com.example.almagestor.shopActivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.almagestor.DTOs.AdressDTO;
import com.example.almagestor.DTOs.ShopDTO;
import com.example.almagestor.DTOs.UserDTO;
import com.example.almagestor.FO.SellFo;
import com.example.almagestor.Login.NewUser;
import com.example.almagestor.MainActivity;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.google.android.material.navigation.NavigationView;

public class Shop extends AppCompatActivity {
    ImageView back_img;
    TextView name_shop,pdv_shop,more_information, street_shop, info_street_shop, email_shop, phone_shop,input_data;

    TextView pdv;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout email_box,phone_box;

    Button Save,Cancel;
    ImageView Close;
    EditText Street,info_Street,Codepostal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initViews();
        initBDdata();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Shop.this, SellFo.class);
                startActivity(intent);
                finish();
            }
        });
        name_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialogInput(1);//For name
            }
        });
        phone_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialogInput(2);//For phone
            }
        });
        email_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialogInput(3);//For email
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
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window_pdf);
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
        phone_box=findViewById(R.id.user_phone_box);
        email_box=findViewById(R.id.user_email_box);
    }
    public void initBDdata(){
        SqliteModel obj=new SqliteModel();
        ShopDTO dtData=obj.getShopdata(this,"10014");
        if(dtData!=null){
            name_shop.setText(dtData.getShop_name());
            pdv_shop.setText(dtData.getShop_pdv());
            street_shop.setText(dtData.getShop_street());
            info_street_shop.setText(dtData.getShop_info_street());
            phone_shop.setText(dtData.getPhone());
            email_shop.setText(dtData.getEmail());
        }else{
            UserDTO pdv=obj.getFromLocal(this);
            if(pdv!=null){
                name_shop.setText("");
                pdv_shop.setText(pdv.getCodePDV());
                street_shop.setText("");
                info_street_shop.setText("");
                phone_shop.setText("");
                email_shop.setText("");
            }
        }
    }
    public void initDialogInput(Integer value){
        Dialog dialog= new Dialog(Shop.this);
        dialog.setContentView(R.layout.input_data_shop);
        Save=dialog.findViewById(R.id.btn_yes_inputdata);
        Cancel=dialog.findViewById(R.id.btn_no_inputdata);
        Close=dialog.findViewById(R.id.btn_close_inputdata);
        input_data=dialog.findViewById(R.id.input_data);
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
                if(!input_data.getText().toString().isEmpty()){
                    SqliteModel obj=new SqliteModel();
                    if(value==1){
                        name_shop.setText(input_data.getText().toString().trim());
                        obj.updatecompany_name(Shop.this,input_data.getText().toString().trim());
                    } else if (value==2) {
                        phone_shop.setText(input_data.getText().toString().trim());
                        obj.updatecompany_phone(Shop.this,input_data.getText().toString().trim());
                    } else if (value==3) {
                        email_shop.setText(input_data.getText().toString().trim());
                        obj.updatecompany_email(Shop.this,input_data.getText().toString().trim());
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
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