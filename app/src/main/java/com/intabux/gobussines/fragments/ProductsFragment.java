package com.intabux.gobussines.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.intabux.gobussines.CRUDProductActivity;
import com.intabux.gobussines.Models.Product;
import com.intabux.gobussines.R;
import com.intabux.gobussines.adapters.ProductAdapter;
import com.intabux.gobussines.providers.AuthProvider;

import java.util.EventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    View mView;
    FloatingActionButton mFab;
    RecyclerView mRecyclerView;
    ProductAdapter mAdapter;
    AuthProvider mAuthProvider;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_products, container, false);
        mAuthProvider = new AuthProvider();
        mRecyclerView = mView.findViewById(R.id.recyclerViewProducts);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);



        mFab = mView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCRUDProduct();
            }
        });
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Products")
                .orderByChild("id_business").equalTo(mAuthProvider.getId());

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class).build();

        mAdapter = new ProductAdapter(options,getContext());

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void goToCRUDProduct() {
        Intent intent = new Intent(getContext(), CRUDProductActivity.class);
        startActivity(intent);
    }

}
