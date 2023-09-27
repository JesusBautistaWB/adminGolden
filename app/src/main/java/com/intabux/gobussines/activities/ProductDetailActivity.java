package com.intabux.gobussines.activities;

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
import com.intabux.gobussines.CRUDProductActivity;
import com.intabux.gobussines.Models.Product;
import com.intabux.gobussines.R;
import com.intabux.gobussines.providers.ImageProvider;
import com.intabux.gobussines.providers.ProductProvider;
import com.intabux.gobussines.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import dmax.dialog.SpotsDialog;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageViewProduct;
    EditText editName;
    EditText editPrice;
    EditText editDescription;
    Button btnUpdate;

    String id;
    String name;
    String price;
    String description;
    String image;

    ImageProvider imageProvider;
    ProductProvider productProvider;
    AlertDialog mAlertDialog;
    File mImageFile;

    Boolean changeImage=false;
    private final int GALLERY_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imageProvider = new ImageProvider();
        productProvider = new ProductProvider();

        editName=findViewById(R.id.updateNameProduct);
        editPrice=findViewById(R.id.updatePriceProduct);
        editDescription=findViewById(R.id.updateDescriptionProduct);
        imageViewProduct = findViewById(R.id.updateImageProduct);
        btnUpdate = findViewById(R.id.btnUpdateProduct);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        image = intent.getStringExtra("image");

        editName.setText(name);
        editPrice.setText(price);
        editDescription.setText(description);

        mAlertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Actualizando producto.")
                .setCancelable(false)
                .build();
        if(image!=null){
            if(!image.isEmpty()){
                Picasso.with(this).load(image).into(imageViewProduct);
            }
        }
        imageViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalley();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickUpdate();
            }
        });
    }

    private void clickUpdate() {
        if(changeImage){
            saveImage();
        }else{
            saveProduct();
        }
    }
    private void saveProduct(){
        mAlertDialog.show();
        Product product = new Product();
        Product productUpdate = new Product();

        product.setName(name);
        Double priceDouble=Double.parseDouble(price);
        product.setPrice(priceDouble);
        product.setDescription(description);
        product.setImage(image);
        product.setId(id);

        Double updatePrice = Double.parseDouble(editPrice.getText().toString());
        productUpdate.setName(editName.getText().toString());
        productUpdate.setPrice(updatePrice);
        productUpdate.setDescription(editDescription.getText().toString());
        productUpdate.setImage(image);
        productUpdate.setId(id);
        productProvider.update(product,productUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mAlertDialog.dismiss();
                clearForm();
                if(task.isSuccessful()){

                    Toast.makeText(ProductDetailActivity.this, "Hurra", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ProductDetailActivity.this, "Bad", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void saveImage() {
        mAlertDialog.show();
        imageProvider.save(ProductDetailActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    imageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            Product product = new Product();
                            Product productUpdate = new Product();

                            product.setName(name);
                            Double priceDouble=Double.parseDouble(price);
                            product.setPrice(priceDouble);
                            product.setDescription(description);
                            product.setImage(url);
                            product.setId(id);

                            Double updatePrice = Double.parseDouble(editPrice.getText().toString());
                            productUpdate.setName(editName.getText().toString());
                            productUpdate.setPrice(updatePrice);
                            productUpdate.setDescription(editDescription.getText().toString());
                            productUpdate.setImage(url);
                            productUpdate.setId(id);
                            productProvider.update(product,productUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskUpdate) {
                                    mAlertDialog.dismiss();
                                    clearForm();
                                    if(taskUpdate.isSuccessful()){

                                        Toast.makeText(ProductDetailActivity.this, "Hurra", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ProductDetailActivity.this, "Bad", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }else{
                    mAlertDialog.dismiss();
                    Toast.makeText(ProductDetailActivity.this, "La imagen no se pudo almacenar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void clearForm(){}
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
                imageViewProduct.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                changeImage=true;
            }catch (Exception e){
                Log.d("ERROR","Se produjo un error"+e.getMessage());
                Toast.makeText(this,"Se produjo un error"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
