package com.example.almagestor.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.almagestor.MainActivity;
import com.example.almagestor.R;

import java.util.Objects;

public class LogoHeader extends AppCompatActivity {
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo_header);
        Animation animation1= AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        image=findViewById(R.id.imageView);
        image.setAnimation(animation1);
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LogoHeader.this, MainActivity.class);
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair<View, String>(image,"cardViewTrans");
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LogoHeader.this,pairs);
                    startActivity(intent,options.toBundle());
                }else{
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }
}