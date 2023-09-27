package com.intabux.gobussines.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intabux.gobussines.Models.Product;


import java.util.HashMap;
import java.util.Map;

public class ProductProvider {

    DatabaseReference database;
    private AuthProvider mAuthProvider;
    public ProductProvider() {
        mAuthProvider = new AuthProvider();
        database=FirebaseDatabase.getInstance().getReference().child("Products");
    }
    public Task<Void> create(Product product){

        String key = database.push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("name",product.getName());
        map.put("price",product.getPrice());
        map.put("description",product.getDescription());
        map.put("image",product.getImage());

        return database.child(key).setValue(map);
    }
    public Task<Void> update(Product product,Product update){


        Map<String, Object> map = new HashMap<>();

        if(!product.getName().equals(update.getName())){
            map.put("name",product.getName());
        }
        if(!product.getPrice().equals(update.getPrice())){
            map.put("price",product.getPrice());
        }
        if(!product.getDescription().equals(update.getDescription())){
            map.put("description",product.getDescription());
        }
        if(product.getImage().equals(update.getImage())){
            map.put("image",product.getImage());
        }

        return database.child(product.getId()).updateChildren(map);
    }
    public DatabaseReference getProduct(String idProduct){
        return database.child(idProduct);
    }
}
