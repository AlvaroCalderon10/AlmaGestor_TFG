package com.example.almagestor.GoogleDrive;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.almagestor.FO.SellFo;
import com.example.almagestor.MainActivity;
import com.example.almagestor.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

public class GoogleDriveActivity extends AppCompatActivity {
    DriveServiceHelper driveServiceHelper;
    private String url_copied_DB= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+"backup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive);
        requestSignIn();
    }
    public void init_process(){
        //Crear la copia BD
        this.driveServiceHelper.generateCopyDB(url_copied_DB,GoogleDriveActivity.this);

        //Subir archivo
        uploadFile();
    }
    public void requestSignIn(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(GoogleDriveActivity.this,signInOptions);
        someActivityResultLauncher.launch(client.getSignInIntent());
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        handleSignInIntent(data);
                    }
                }
            });
    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        GoogleAccountCredential credential = GoogleAccountCredential
                                .usingOAuth2(GoogleDriveActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));
                        credential.setSelectedAccount(googleSignInAccount.getAccount());
                        Drive googleDriveService = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName("BD on Drive")
                                .build();
                        driveServiceHelper=new DriveServiceHelper(googleDriveService);
                        init_process();
                        Intent intent = new Intent( GoogleDriveActivity.this, SellFo.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void uploadFile(){
        Boolean success=false;
        File folder =new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
        try{
            success=true;
            {
                if(!folder.exists())
                    try{
                        Files.createDirectory(folder.toPath());
                        success=true;
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                if (success){
                    this.driveServiceHelper.backup(url_copied_DB,GoogleDriveActivity.this);
                }else{
                    Toast.makeText(GoogleDriveActivity.this, "UNABLE CREATE DIRECTORY", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();;
            success=false;

        }

    }
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            //Back Controller
            Intent intent = new Intent(GoogleDriveActivity.this, SellFo.class);
            startActivity(intent);
            finish();
    }
}