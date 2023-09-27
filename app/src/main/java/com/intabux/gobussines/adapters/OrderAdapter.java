package com.intabux.gobussines.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.intabux.gobussines.Models.Order;
import com.intabux.gobussines.R;
import com.intabux.gobussines.activities.OrderDetailActivity;
import com.intabux.gobussines.providers.ClientProvider;
import com.squareup.picasso.Picasso;


public class OrderAdapter  extends FirebaseRecyclerAdapter<Order, OrderAdapter.ViewHolder> {

    Context context;
    ClientProvider mClientProvider;
    String userName="";

    public OrderAdapter(FirebaseRecyclerOptions<Order> options, Context context){
        super(options);
        this.context=context;
        mClientProvider=new ClientProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Order order) {

        final String id = getRef(position).getKey();

        String date = "31-Enero-21";//order.getDateCreateOrder();
        String key = "ASXSR";//order.getKey();
        int status = order.getStatus();
        String statusString;
        switch(status){
            case 0: statusString="Pendiente";
            break;
            case 1: statusString="En progreso";
            break;
            case 2:statusString="Aceptado";
            break;
            case 3:statusString="Solicitando repartidor";
            break;
            case 4:statusString="Repartidor en ruta del negocio";
                break;
            case 5:statusString="Repartidor en ruta del cliente";
                break;
            default:statusString="Finalizado";
        }

        String total = String.valueOf(order.getTotalProducts());
        String user = order.getUser();

        holder.textViewKey.setText(key);
        holder.textViewDate.setText(date);
        holder.textViewStatus.setText(statusString);
        holder.textViewTotal.setText(total);

        mClientProvider.getClient(user).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    userName=name;
                    holder.textViewUser.setText(name);
                    if(dataSnapshot.hasChild("image")){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.with(context).load(image).into(holder.imgUser);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Gson gson = new Gson();
        final String orderString = gson.toJson(order);

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("userName",userName);
                intent.putExtra("order",orderString);
                context.startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_orders,parent,false);
        return new ViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUser;
        TextView textViewKey;
        TextView textViewDate;
        TextView textViewStatus;
        TextView textViewTotal;
        ImageView imgUser;

        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewUser = view.findViewById(R.id.nameUserViewOrderCard);
            textViewKey = view.findViewById(R.id.keyViewOrderCard);
            textViewDate = view.findViewById(R.id.dateViewOrder);
            textViewStatus = view.findViewById(R.id.statusViewOrder);
            textViewTotal = view.findViewById(R.id.totalViewOrder);
            imgUser = view.findViewById(R.id.imgViewUserOrderCard);
            viewHolder = view;
        }
    }
}
