package com.example.almagestor.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almagestor.MainActivity;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.example.almagestor.Validation.Encryption;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText emailEditText;
    EditText password;
    EditText password2;
    EditText codepdv;
    Button recover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqliteModel DB_local=new SqliteModel();
        setContentView(R.layout.activity_forgot_password);

        //emailEditText = findViewById(R.id.email);
        password=findViewById(R.id.password);
        password2=findViewById(R.id.password2);
        codepdv=findViewById(R.id.codePDVrecover);
        recover=findViewById(R.id.recoverBotom);


        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().trim().equals(password2.getText().toString().trim()) && !codepdv.getText().toString().isEmpty()){
                    SqliteModel obj=new SqliteModel();
                    Encryption encryt=new Encryption();
                    try {
                        String sendPass=encryt.EncryptString(password.getText().toString(),codepdv.getText().toString());
                        boolean correct=obj.reset_Password(ForgotPassword.this,sendPass,codepdv.getText().toString());
                        if(correct){
                            Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Back Controller
        Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}