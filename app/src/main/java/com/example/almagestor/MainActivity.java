package com.example.almagestor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.almagestor.DTOs.UserDTO;
import com.example.almagestor.FO.SellFo;
import com.example.almagestor.Login.ForgotPassword;
import com.example.almagestor.Login.LogoHeader;
import com.example.almagestor.Login.NewUser;
import com.example.almagestor.Sqlite.SqliteModel;
import com.example.almagestor.Validation.Encryption;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;
    TextView new_user;
    TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqliteModel DB_local=new SqliteModel();
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        new_user=findViewById(R.id.signupText);
        forgotPassword=findViewById(R.id.forgotPassword);
        Objects.requireNonNull(getSupportActionBar()).hide();
        UserDTO user_data= DB_local.getFromLocal(MainActivity.this);
        String password_encrypted="";
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Encryption encrypt=new Encryption();
                try {
                    if(user_data!=null){
                        if (username.getText().toString().equals(user_data.getCodePDV()) && user_data.getPassword().equals(encrypt.EncryptString(password.getText().toString(),user_data.getCodePDV()))) {
                            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            //INTENT A menú principal
                            Intent intent =new Intent(MainActivity.this, SellFo.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Login Failed!, NON DATA", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, NewUser.class);
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair<View, String>(new_user,"cardViewTrans");
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                    finish();
                }else{
                    startActivity(intent);
                    finish();
                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, ForgotPassword.class);
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair<View, String>(forgotPassword,"cardViewTrans");
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                    finish();
                }else{
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}