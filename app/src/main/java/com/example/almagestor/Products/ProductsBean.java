package com.example.almagestor.Products;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.almagestor.FO.CaptureActivity;
import com.example.almagestor.FO.SellFo;
import com.example.almagestor.ListAdapters.ListAdapter;
import com.example.almagestor.ListAdapters.ListAdapterProductBean;
import com.example.almagestor.R;
import com.example.almagestor.Sqlite.SqliteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class ProductsBean extends AppCompatActivity {

    List<ProductDataDTO> elements=new ArrayList<>();
    String barCode;
    FloatingActionButton add_product;
    RecyclerView recyclerView;
    Button Save,Cancel,Scan;
    ImageView Close;
    EditText nameProduct,stockProduct, precioProduct, barcodeProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_bean);
        add_product=findViewById(R.id.add_btn_product);
        init_list();
        add_product.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ProductsBean.this);
                dialog.setContentView(R.layout.add_product_lay);
                Save = dialog.findViewById(R.id.btn_yes_lay);
                Cancel = dialog.findViewById(R.id.btn_no_lay);
                Scan = dialog.findViewById(R.id.btn_scan_product);
                Close = dialog.findViewById(R.id.btn_close_product_add);
                nameProduct = dialog.findViewById(R.id.editext_name_product);
                stockProduct = dialog.findViewById(R.id.editext_stock_product);
                precioProduct = dialog.findViewById(R.id.editext_price_product);
                barcodeProduct = dialog.findViewById(R.id.product_barcode);
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
                Scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scanCode();
                        return;
                    }
                });
                Save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductDataDTO dto=new ProductDataDTO();
                        dto=dto.create_validate_product(nameProduct.getText().toString(),barcodeProduct.getText().toString(),Integer.valueOf(stockProduct.getText().toString()),Integer.valueOf(precioProduct.getText().toString()));
                        if(dto!=null){
                            init(nameProduct.getText().toString());
                            //guardar en DB
                            SqliteModel obj=new SqliteModel();
                            if(obj.insert_Product(ProductsBean.this,dto)==true){
                                Toast.makeText(ProductsBean.this,"@string/saveCorrect", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }else{
                            Toast.makeText(ProductsBean.this,"@string/miss_name_ean", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        }));


    }
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);
        barLaucher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductsBean.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            if(!result.getContents().isEmpty()){
                barCode =result.getContents().toString().trim();
                barcodeProduct.setText(barCode);
            }
        }
    });


    public void init(String value){
        elements.add(new ProductDataDTO("img",value,"10",1,2));
        ListAdapterProductBean listAdapterProductBean=new ListAdapterProductBean(elements, this,1);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView_products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsBean.this));
        recyclerView.setAdapter(listAdapterProductBean);
    }
    public void init_list(){
        SqliteModel obj=new SqliteModel();
        ArrayList<ProductDataDTO> products_list=obj.list_products(ProductsBean.this,"10014");
        for(ProductDataDTO e:products_list){
            elements.add(new ProductDataDTO(e.getImg(),e.getNameProduct(),e.getEan(),e.getUnits(),e.getPrice()));
        }
        ListAdapterProductBean listAdapterProductBean=new ListAdapterProductBean(elements, this,1);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView_products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsBean.this));
        recyclerView.setAdapter(listAdapterProductBean);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Back Controller
        Intent intent = new Intent(ProductsBean.this, SellFo.class);
        startActivity(intent);
        finish();
    }
}