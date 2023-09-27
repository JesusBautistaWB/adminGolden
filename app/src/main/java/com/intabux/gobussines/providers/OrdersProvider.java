package com.intabux.gobussines.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OrdersProvider {

    DatabaseReference database;

    public OrdersProvider() {
        database= FirebaseDatabase.getInstance().getReference().child("Orders");
    }
    public DatabaseReference getOrder(String id){
        return database.child(id);
    }

    public Task<Void> updateStatus(String idOrder, int status){
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        return database.child(idOrder).updateChildren(map);
    }
    public DatabaseReference getStatus(String idOrder){
        return database.child(idOrder).child("status");
    }
}
