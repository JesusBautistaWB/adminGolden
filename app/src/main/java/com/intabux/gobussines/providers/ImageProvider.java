package com.intabux.gobussines.providers;

import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.intabux.gobussines.utils.CompressorBitmapImage;

import java.io.File;
import java.util.Date;

public class ImageProvider {
    private StorageReference mStorage;
    public ImageProvider(){
        mStorage = FirebaseStorage.getInstance().getReference("product_image");
    }
    public UploadTask save(Context context, File image){
        byte [] imageByte = CompressorBitmapImage.getImage(context,image.getPath(),500,500);
        final StorageReference storage = FirebaseStorage.getInstance().getReference("product_image").child(new Date()+".jpg");
        mStorage = storage;
        UploadTask uploadTask = storage.putBytes(imageByte);
        return uploadTask;
    }
    public StorageReference getStorage(){
        return mStorage;
    }
}
