package com.intabux.gobussines.providers;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intabux.gobussines.Models.Client;

import java.util.HashMap;
import java.util.Map;

public class ClientProvider {
    DatabaseReference mDataBase;

    public ClientProvider(){
        mDataBase= FirebaseDatabase.getInstance().getReference().child("Users").child("Client");
    }
    public Task<Void> create(Client client){
        Map<String, Object> map = new HashMap<>();
        map.put("name",client.getName());
        map.put("email",client.getEmail());
        return mDataBase.child(client.getId()).setValue(map);
    }

    public Task<Void> update(Client client){
        Map<String, Object> map = new HashMap<>();
        map.put("name",client.getName());
        map.put("image",client.getImage());
        return mDataBase.child(client.getId()).updateChildren(map);
    }
    public DatabaseReference getClient(String idClient){
        return mDataBase.child(idClient);
    }
}
