package com.example.almagestor.CallandMsg;




import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CallandMsg extends AppCompatActivity {
    private static final int  MSG_PERMISSION_REQUEST_CODE =100;
    private String sPhone;
    private Context context;
    private static final String TAKE_AWAY_MSG="Tu pedido está listo para ser recogido";
    public boolean sendMSG(String phone, Context Activitycontext){
        context=Activitycontext;
        sPhone=phone.replace("Phone: ","");
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
             //msg
            if(!sPhone.equals("")){
                send();
            }else{
                Toast.makeText(context, "Phone not correct:"+sPhone, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Not allowed to send MSG", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    public boolean sendCALL(String phone, Context Activitycontext){
        context=Activitycontext;
        sPhone=phone.replace("Phone: ","");
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
            //msg
            if(!sPhone.equals("")){
                call();
            }else{
                Toast.makeText(context, "Phone not correct:"+sPhone, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Not allowed to send MSG", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void send(){
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(sPhone,null,TAKE_AWAY_MSG,null,null);
        Toast.makeText(context, "Send message sent Successfully!", Toast.LENGTH_SHORT).show();
    }
    private void call(){
        Intent callIntent=new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+sPhone));
        context.startActivity(callIntent);
    }

}
