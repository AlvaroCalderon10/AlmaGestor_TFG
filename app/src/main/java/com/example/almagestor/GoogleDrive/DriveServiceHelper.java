package com.example.almagestor.GoogleDrive;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.almagestor.MainActivity;
import com.example.almagestor.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DriveServiceHelper {

    private final Executor mExecutor= Executors.newSingleThreadExecutor();
    private Drive mDriveService;

    public DriveServiceHelper (Drive mDriveService){
        this.mDriveService=mDriveService;
    }

    public void generateCopyDB(String OutputFilepath, Context context){
        String inFile=context.getDatabasePath("AlmaGestor_DB").toString();
        //data/data/com.example.almagestor/databases/AlmaGestor_DB
        try{
            File dbFile=new File(inFile);
            FileInputStream fis =new FileInputStream( dbFile);
            OutputStream outputStream = new FileOutputStream(OutputFilepath);
            byte[] buffer = new byte[1024];
            int length;
            while((length=fis.read(buffer))>0){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            outputStream.close();
            fis.close();
        }catch (Exception e){
            Toast.makeText(context, "Unable To backup", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public Task<com.google.api.services.drive.model.File> backup (String OutputFilepath, Context context) throws IOException {

        final TaskCompletionSource<com.google.api.services.drive.model.File> tcs = new TaskCompletionSource<com.google.api.services.drive.model.File>();
        ExecutorService service = Executors.newFixedThreadPool(1);

        service.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        com.google.api.services.drive.model.File fileMetaData=new com.google.api.services.drive.model.File();
                        fileMetaData.setName("Backup_Almagestor");
                        java.io.File file = new java.io.File(OutputFilepath);
                        FileContent mediaContent=new FileContent("application/db",file);
                        com.google.api.services.drive.model.File myFile=null;
                        try{
                            myFile=mDriveService.files().create(fileMetaData,mediaContent).execute();
                            if(myFile==null){
                                throw new IOException("NULL result when requesting file creation");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        com.google.api.services.drive.model.File finalResult = myFile;
                        new Handler(Looper.getMainLooper()).postDelayed(() -> tcs.setResult(finalResult), 1000);

                    }
                });

        return tcs.getTask();
    }
}
