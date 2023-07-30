package com.example.almagestor.CallandMsg;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.almagestor.R;

public class MsgalertDialog {
    ImageView Close;
    TextView output;
    public void initDialog(Context context, String value){

        Dialog dialog= new Dialog(context);
        dialog.setContentView(R.layout.warningdialog);
        Close=dialog.findViewById(R.id.btn_close_warningDialog);
        output=dialog.findViewById(R.id.String_warningDialog);
        output.setText(value);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window_client);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
