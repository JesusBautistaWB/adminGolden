package com.intabux.gobussines.adapters;

import android.content.Context;
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
import com.intabux.gobussines.Models.ProductOrder;
import com.intabux.gobussines.R;
import com.intabux.gobussines.providers.ProductProvider;
import com.squareup.picasso.Picasso;

public class ProductsOrderAdapter extends FirebaseRecyclerAdapter<ProductOrder, ProductsOrderAdapter.ViewHolder> {

    Context context;
    ProductProvider mProductProvider;

    public ProductsOrderAdapter(FirebaseRecyclerOptions<ProductOrder> options, Context context){
        super(options);
        this.context=context;
        mProductProvider=new ProductProvider();
    }


    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final ProductOrder productOrder) {
        final String id = getRef(position).getKey();
        String number = String.valueOf(productOrder.getNumber());
        //Double unit_price = productOrder.getUnit_price();

        holder.textViewNumber.setText(number);
        mProductProvider.getProduct(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();

                    holder.textViewName.setText(name);
                    if(dataSnapshot.hasChild("image")){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.with(context).load(image).into(holder.imageViewProduct);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_products_order,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewNumber;
        ImageView imageViewProduct;
        View viewHolder;
        public ViewHolder(View view){
            super(view);
            textViewName = view.findViewById(R.id.nameViewProductsOrderCard);
            textViewNumber = view.findViewById(R.id.numberViewProductsOrderCard);
            imageViewProduct = view.findViewById(R.id.imageViewProductOrderCard);
            viewHolder = view;
        }
    }
}
