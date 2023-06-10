package com.example.almagestor.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almagestor.DTOs.UserDTO;
import com.example.almagestor.MainActivity;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.example.almagestor.Validation.Encryption;
import com.example.almagestor.Validation.ValidateClases;

public class NewUser extends AppCompatActivity {
    EditText codePDV;
    EditText password;
    EditText shopName;
    Button register;
    TextView new_user;
    ValidateClases validation = new ValidateClases();
    Encryption encryption = new Encryption();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqliteModel DB_local=new SqliteModel();
        setContentView(R.layout.activity_new_user);
        codePDV = findViewById(R.id.codePDV);
        password = findViewById(R.id.password);
        shopName=findViewById(R.id.companyName);
        register = findViewById(R.id.registerButom);

        new_user=findViewById(R.id.signupText);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqliteModel obj=new SqliteModel();
                UserDTO user =new UserDTO("",codePDV.getText().toString(),shopName.getText().toString(),password.getText().toString());
                String validate= validation.validteUserDto(user);
                if(!validate.equals("correct")){
                    if(validate.equals("pdv")){
                        Toast.makeText(NewUser.this, "PDV MISSING", Toast.LENGTH_SHORT).show();
                    } else if (validate.equals("password")) {
                        Toast.makeText(NewUser.this, "PASSWORD MISSING", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String password_Encrypted= null;
                    try {
                        password_Encrypted = encryption.EncryptString(password.getText().toString(),codePDV.getText().toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    user.setPassword(password_Encrypted);
                    if(obj.insert_user(NewUser.this,user)){
                        Intent intent = new Intent(NewUser.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Back Controller
        Intent intent = new Intent(NewUser.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}