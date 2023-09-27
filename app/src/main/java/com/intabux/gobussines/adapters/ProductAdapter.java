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
import com.intabux.gobussines.Models.Product;
import com.intabux.gobussines.R;
import com.intabux.gobussines.activities.ProductDetailActivity;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.ViewHolder> {

    Context context;

    public ProductAdapter(FirebaseRecyclerOptions<Product> options,Context context){
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Product product) {

        final String id = getRef(position).getKey();
        final String name = product.getName();
        final String price = String.valueOf(product.getPrice());
        final String description = product.getDescription();

        holder.textViewName.setText(product.getName());

        holder.textViewPrice.setText(price);
        holder.textViewDescription.setText(product.getDescription());
        if(product.getImage()!=null){
            if(!product.getImage().isEmpty()){
                Picasso.with(context).load(product.getImage()).into(holder.imageViewProduct);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("price",price);
                intent.putExtra("description",description);
                intent.putExtra("image", product.getImage());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewDescription;
        TextView textViewPrice;
        ImageView imageViewProduct;
        View viewHolder;
        public ViewHolder(View view){
            super(view);
            textViewName = view.findViewById(R.id.nameViewProductCard);
            textViewDescription = view.findViewById(R.id.descriptionViewProductCard);
            textViewPrice = view.findViewById(R.id.priceViewProductCard);
            imageViewProduct = view.findViewById(R.id.imageViewProductCard);
            viewHolder = view;
        }
    }
}
