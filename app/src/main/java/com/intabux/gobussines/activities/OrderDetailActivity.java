package com.intabux.gobussines.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intabux.gobussines.Models.FCMBody;
import com.intabux.gobussines.Models.FCMResponse;
import com.intabux.gobussines.Models.Order;
import com.intabux.gobussines.Models.ProductOrder;
import com.intabux.gobussines.R;
import com.intabux.gobussines.adapters.ProductsOrderAdapter;
import com.intabux.gobussines.providers.ClientProvider;
import com.intabux.gobussines.providers.GeofireProvider;
import com.intabux.gobussines.providers.NotificationProvider;
import com.intabux.gobussines.providers.TokenProvider;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {


    String id;
    String key;
    long date;
    String userName,orderString;
    Double total;
    LatLng businessLocation;
    Double mRadius=0.1;
    boolean mDriverFound=false;
    String mIdDriverFound="";
    String mExtraOrigin, mExtraDestination;
    LatLng mDriverFoundLatLng;

    TextView keyView;
    TextView userView;
    TextView phoneView;
    TextView totalView;
    TextView statusView;

    Button btnRequestDriver;

    RecyclerView mRecyclerView;
    ProductsOrderAdapter mAdapter;

    //Providers
    private GeofireProvider geofireProvider;
    private TokenProvider mTokenProvider;
    private NotificationProvider mNotificationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        keyView=findViewById(R.id.keyViewOrderDetail);
        userView=findViewById(R.id.userNameViewOrderDetail);
        phoneView=findViewById(R.id.phoneViewOrderDetail);
        totalView=findViewById(R.id.totalViewOrderDetail);
        btnRequestDriver=findViewById(R.id.btnRequestDriverOrderDetail);
        statusView=findViewById(R.id.statusViewOrderDetail);

        geofireProvider = new GeofireProvider("active_drivers");
        mTokenProvider=new TokenProvider();
        mNotificationProvider = new NotificationProvider();

        businessLocation=new LatLng(19.25475655340999,-98.16506132483482);

        Intent intent = getIntent();

        id=intent.getStringExtra("id");
        userName=intent.getStringExtra("userName");
        orderString = getIntent().getStringExtra("order");

        Order orderExtra;
        Gson gson=new Gson();
        Type type=new TypeToken<Order>(){}.getType();
        orderExtra=gson.fromJson(orderString,type);

        String user = orderExtra.getUser();

        if(userName.equals("")){
            ClientProvider mClientProvider=new ClientProvider();
            mClientProvider.getClient(user).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String name  = dataSnapshot.child("name").getValue().toString();
                        userView.setText(name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            userView.setText(userName);
        }

        key=orderExtra.getKey();
        date=orderExtra.getDateCreateOrder();
        total=orderExtra.getTotalProducts();
        mExtraOrigin="Golden Rock";
        mExtraDestination="Acxotla del monte";
        keyView.setText(key);
        phoneView.setText("2461016050");

        mRecyclerView = findViewById(R.id.recyclerViewProductsOrder);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        btnRequestDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClosestDriver();
            }
        });
    }

    private void getClosestDriver() {
        geofireProvider.getActiveDrivers(businessLocation,mRadius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!mDriverFound){
                    mDriverFound = true;
                    mIdDriverFound = key;
                    mDriverFoundLatLng = new LatLng(location.latitude,location.longitude);
                    statusView.setText("Conductor encontrado\n Esperando respuesta...");
                    sendNotification("1min","2km");
                    Log.d("DRIVER","ID"+mIdDriverFound);
                    checkStatusOrder();
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                //Metodo implementado cuando termina de realizar la busqueda del conductor de un radio de 0.1KM
                if(!mDriverFound){
                    mRadius=mRadius+0.1;
                }
                //no encontro un conductor;
                if(mRadius>5){
                    Toast.makeText(OrderDetailActivity.this, "Por el momento no hay conductores disponibles", Toast.LENGTH_SHORT).show();;
                    return;
                }
                else {
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void checkStatusOrder(){

    }

    private void sendNotification(final String time, final String km) {

        mTokenProvider.getToken(mIdDriverFound).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    Map<String, String> map = new HashMap<>();
                    map.put("title","Nueva solicitud de servicio a: "+time);
                    map.put("body","Un cliente esta solicitando el servicio a una distancia de: "+km +"\n"+
                            "De: "+mExtraOrigin+"\n"+
                            "A: "+mExtraDestination);
                    map.put("origin",mExtraOrigin);
                    map.put("destination",mExtraDestination);
                    map.put("min",time);
                    map.put("distance",km);
                    map.put("idOrder",id);
                    map.put("order",orderString);
                    FCMBody fcmBody = new FCMBody(token,"high","4500s",map);
                    mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body()!=null){
                                if(response.body().getSuccess()==1){
                                    statusView.setText("hola");
                                }
                                else {
                                    Toast.makeText(OrderDetailActivity.this, "Hubo un problema al contactar el conductor", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(OrderDetailActivity.this, "Hubo un problema al contactar el conductor", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error: "+t.getMessage());
                        }
                    });
                }
                else{
                    Toast.makeText(OrderDetailActivity.this, "Hubo un problema al contactar el conductor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(id)
                .child("Products")
                .orderByChild("name");

        FirebaseRecyclerOptions<ProductOrder> options = new FirebaseRecyclerOptions.Builder<ProductOrder>()
                .setQuery(query,ProductOrder.class).build();

        mAdapter = new ProductsOrderAdapter(options,this);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}
