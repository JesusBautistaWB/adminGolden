package com.intabux.gobussines.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intabux.gobussines.Models.Order;
import com.intabux.gobussines.Models.ProductOrder;
import com.intabux.gobussines.R;
import com.intabux.gobussines.adapters.ProductsOrderAdapter;
import com.intabux.gobussines.providers.ClientProvider;
import com.intabux.gobussines.providers.OrdersProvider;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotificationOrderActivity extends AppCompatActivity {

    TextView txtCounter;

    Button btnAccept;
    Button btnCancel;

    private ValueEventListener mListener;
    RecyclerView mRecyclerView;
    ProductsOrderAdapter mAdapter;
    OrdersProvider mOrderProvider;
    ClientProvider mClientProvider;

    String mExtraIdOrder,order;


    private int mCounter=15;
    private Handler mHandler;

    MediaPlayer mMediaPlayer;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mCounter=mCounter-1;
            txtCounter.setText(String.valueOf(mCounter));
            if(mCounter>0){
                initTimer();
            }
            else {
                cancelBooking();
            }
        }
    };


    private void initTimer() {
        mHandler = new Handler();
        mHandler.postDelayed(runnable,1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_order);
        mClientProvider=new ClientProvider();
        txtCounter = findViewById(R.id.txtCounter);

        btnAccept = findViewById(R.id.btnAcceptBooking);
        btnCancel = findViewById(R.id.btnCancelBooking);

        mExtraIdOrder = getIntent().getStringExtra("idOrder");
        order = getIntent().getStringExtra("order");
        mOrderProvider=new OrdersProvider();


        mMediaPlayer = MediaPlayer.create(this,R.raw.best_relax_ringtone);
        mMediaPlayer.setLooping(true);


        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                        WindowManager.LayoutParams.PARCELABLE_WRITE_RETURN_VALUE|
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        initTimer();
        checkIfClientCancelBooking();
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });
        mRecyclerView = findViewById(R.id.recyclerViewOrderNotification);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void checkIfClientCancelBooking(){/*
        mListener = mClientBookingProvider.getClientBooking(mExtraIdClient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    if(mHandler!=null){
                        Toast.makeText(NotificationBookingActivity.this, "El cliente ha cancelado el viaje", Toast.LENGTH_LONG).show();
                        mHandler.removeCallbacks(runnable);
                        Intent intent = new Intent(NotificationBookingActivity.this,MapDriverActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void cancelBooking() {/*

        if(mHandler!=null){
            mHandler.removeCallbacks(runnable);
        }

        mClientBookingProvider.updateStatus(mExtraIdClient,"cancel");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent = new Intent(NotificationBookingActivity.this,MapDriverActivity.class);
        startActivity(intent);
        finish();*/
    }

    private void acceptOrder() {
        if(mHandler!=null){
            mHandler.removeCallbacks(runnable);
        }

        Order orderExtra;
        Gson gson=new Gson();
        Type type=new TypeToken<Order>(){}.getType();
        orderExtra=gson.fromJson(order,type);
        orderExtra.setStatus(2);

        mOrderProvider.updateStatus(mExtraIdOrder,2);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent1 = new Intent(NotificationOrderActivity.this, OrderDetailActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.setAction(Intent.ACTION_RUN);
        intent1.putExtra("id",mExtraIdOrder);
        intent1.putExtra("userName","");
        intent1.putExtra("order",order);
        startActivity(intent1);
    }


    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(mExtraIdOrder)
                .child("Products")
                .orderByChild("name");

        FirebaseRecyclerOptions<ProductOrder> options = new FirebaseRecyclerOptions.Builder<ProductOrder>()
                .setQuery(query,ProductOrder.class).build();

        mAdapter = new ProductsOrderAdapter(options,this);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.release();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaPlayer != null) {
            if(!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler!=null){
            mHandler.removeCallbacks(runnable);
        }

        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
        }
        /*if(mListener!=null){
            //mClientBookingProvider.getClientBooking(mExtraIdClient).removeEventListener(mListener);
        }*/
    }
}