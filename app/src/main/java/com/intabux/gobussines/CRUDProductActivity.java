package com.intabux.gobussines;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.intabux.gobussines.Models.Product;
import com.intabux.gobussines.providers.ImageProvider;
import com.intabux.gobussines.providers.ProductProvider;
import com.intabux.gobussines.utils.FileUtil;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class CRUDProductActivity extends AppCompatActivity {

    EditText inputName;
    EditText inputPrice;
    EditText inputDescription;
    CircleImageView circleImageViewBack;

    String name;
    String price;
    String description;

    ImageView imageProduct;
    Button btnAddProduct;
    ImageProvider imageProvider;
    ProductProvider productProvider;
    AlertDialog mAlertDialog;
    File mImageFile;
    private final int GALLERY_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudproduct);

        inputName=findViewById(R.id.inputNameProductRegister);
        inputPrice=findViewById(R.id.inputPriceRegister);
        inputDescription=findViewById(R.id.inputDescriptionProductRegister);
        circleImageViewBack=findViewById(R.id.circleImgBackProduct);

        imageProvider = new ImageProvider();
        productProvider = new ProductProvider();
        mAlertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Agregando productos")
                .setCancelable(false)
                .build();

        imageProduct=findViewById(R.id.addImageProduct);
        btnAddProduct=findViewById(R.id.btnSaveProduct);

        imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalley();
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAddProduct();
            }
        });
        circleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void clickAddProduct() {
        name = inputName.getText().toString();
        price = inputPrice.getText().toString();
        description = inputDescription.getText().toString();
        if(!name.isEmpty() && !price.isEmpty() && !description.isEmpty()){
            if(mImageFile!=null){
                saveImage();
            }else{
                Toast.makeText(this, "Selecciona una imagen", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Completa los campos para continuar", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        mAlertDialog.show();
        imageProvider.save(CRUDProductActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    imageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Product product = new Product();

                            product.setName(name);
                            Double priceDouble=Double.parseDouble(price);
                            product.setPrice(priceDouble);
                            product.setDescription(description);
                            product.setImage(url);

                            productProvider.create(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    mAlertDialog.dismiss();
                                    if(taskSave.isSuccessful()){
                                        clearForm();
                                        Toast.makeText(CRUDProductActivity.this, "Hurra", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(CRUDProductActivity.this, "Bad", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }else{
                    mAlertDialog.dismiss();
                    Toast.makeText(CRUDProductActivity.this, "La imagen no se pudo almacenar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearForm() {
        inputName.setText("");
        inputPrice.setText("");
        inputDescription.setText("");
        imageProduct.setImageResource(R.drawable.ic_add_photo);
        name="";
        price="";
        description="";
        mImageFile=null;
    }

    private void openGalley() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK){
            try {
                mImageFile= FileUtil.from(this,data.getData());
                imageProduct.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR","Se produjo un error"+e.getMessage());
                Toast.makeText(this,"Se produjo un error"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
