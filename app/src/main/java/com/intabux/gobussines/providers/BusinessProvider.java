package com.intabux.gobussines.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intabux.gobussines.Models.Business;

import java.util.HashMap;
import java.util.Map;

public class BusinessProvider {

    DatabaseReference mDataBase;

    public BusinessProvider() {
        mDataBase= FirebaseDatabase.getInstance().getReference().child("Users").child("Business");
    }
    public Task<Void> create(Business business){
        Map<String, Object> map = new HashMap<>();
        map.put("name",business.getName());
        map.put("email",business.getEmail());
        map.put("image",business.getImage());
        map.put("phone",business.getPhone());
        map.put("description",business.getDescription());
        map.put("type",business.getType());
        return mDataBase.child(business.getId()).setValue(map);
    }
    public Task<Void> update(Business business){
        Map<String, Object> map = new HashMap<>();
        map.put("name",business.getName());
        map.put("email",business.getEmail());
        map.put("image",business.getImage());
        map.put("description",business.getDescription());
        map.put("type",business.getType());
        return mDataBase.child(business.getId()).updateChildren(map);
    }
    public DatabaseReference getBusiness(String idBusiness){
        return mDataBase.child(idBusiness);
    }
}
