package com.example.almagestor.Products;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ProductsBean extends AppCompatActivity {
    private static final String TAG = "ProductDataBean";
    private static final int IMAGE_PERMISSION_REQUEST_CODE=10;
    List<ProductDataDTO> elements=new ArrayList<>();
    String barCode;
    FloatingActionButton add_product;
    RecyclerView recyclerView;
    Button Save,Cancel,Scan,Photo;
    ImageView Close;
    EditText nameProduct,stockProduct, precioProduct, barcodeProduct;
    String base64IMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_bean);
        add_product=findViewById(R.id.add_btn_product);
        getSupportActionBar().setTitle(ProductsBean.this.getResources().getString(R.string.FOProductos));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ProductsBean.this.getColor(R.color.purple_700)));
        init_list();
        add_product.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ProductsBean.this);
                base64IMG="";
                dialog.setContentView(R.layout.add_product_lay);
                Save = dialog.findViewById(R.id.btn_yes_lay);
                Cancel = dialog.findViewById(R.id.btn_no_lay);
                Scan = dialog.findViewById(R.id.btn_scan_product);
                Photo=dialog.findViewById(R.id.btn_photo_product);
                Close = dialog.findViewById(R.id.btn_close_product_add);
                nameProduct = dialog.findViewById(R.id.editext_name_product);
                stockProduct = dialog.findViewById(R.id.editext_stock_product);
                precioProduct = dialog.findViewById(R.id.editext_price_product);
                barcodeProduct = dialog.findViewById(R.id.product_barcode);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.border_output_box);
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
                        precioProduct.setText(precioProduct.getText().toString().replace(',','.'));
                        dto=dto.create_validate_product(base64IMG,nameProduct.getText().toString(),barcodeProduct.getText().toString(),Integer.valueOf(stockProduct.getText().toString()),Double.valueOf(precioProduct.getText().toString()));
                        if(dto!=null){
                            init_img(dto,base64IMG);
                            //guardar en DB
                            SqliteModel obj=new SqliteModel();
                            if(obj.insert_Product(ProductsBean.this,dto)==true){
                                Toast.makeText(ProductsBean.this,ProductsBean.this.getResources().getString(R.string.saveCorrect), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }else{
                            Toast.makeText(ProductsBean.this,ProductsBean.this.getResources().getString(R.string.miss_name_ean), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                            openCamara();
                        }else{
                            String value=Manifest.permission.CAMERA;
                            String[] permissionRequested=new String[]{value};
                            requestPermissions(permissionRequested,IMAGE_PERMISSION_REQUEST_CODE);
                        }
                    }
                });
                dialog.show();
            }
        }));

    }
    @Override
    public void onRequestPermissionsResult(int requestcode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestcode,permissions,grantResults);
        if (requestcode==IMAGE_PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamara();
            } else{
                Toast.makeText(this, "Unable to invoke camara without permision", Toast.LENGTH_SHORT).show();
            }
        }
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
    private void openCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent,1);
        activityResultLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            int resul=result.getResultCode();
                            Intent data= result.getData();
                            if (resul == -1 && resul == RESULT_OK) {
                                Bundle extras = data.getExtras();
                                Bitmap imgBitmap = (Bitmap) extras.get("data");
                                base64IMG = bitmapToBase64(imgBitmap);
                            }
                        }
                    }
            );

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);
    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.getDecoder().decode(b64);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    public void init(String value){
        elements.add(new ProductDataDTO("img",value,"10",1,2));
        ListAdapterProductBean listAdapterProductBean=new ListAdapterProductBean(elements, this,1);
        recyclerView=findViewById(R.id.listRecyclerView_products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsBean.this));
        recyclerView.setAdapter(listAdapterProductBean);
    }
    public void init_img(ProductDataDTO dto,String img ){
        elements.add(new ProductDataDTO(img,dto.getNameProduct(),dto.getEan(),dto.getUnits(),dto.getPrice()));
        ListAdapterProductBean listAdapterProductBean=new ListAdapterProductBean(elements, this,1);
        recyclerView=findViewById(R.id.listRecyclerView_products);
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
        recyclerView=findViewById(R.id.listRecyclerView_products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsBean.this));
        recyclerView.setAdapter(listAdapterProductBean);
    }
    public void updateStockItem(ProductDataDTO product, int quantite){
        Log.i(TAG,product.print_Product());
        elements.forEach( e ->{
            if(e.getNameProduct().equals(product.getNameProduct()) && e.getEan().equals(product.getEan())){
                e.setUnits(quantite);
            }
        });
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