package com.intabux.gobussines.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.intabux.gobussines.Models.Order;
import com.intabux.gobussines.R;
import com.intabux.gobussines.adapters.OrderAdapter;
import com.intabux.gobussines.providers.AuthProvider;
import com.intabux.gobussines.providers.TokenProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View mView;
    RecyclerView mRecyclerView;
    OrderAdapter mAdapter;
    private TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerViewOrders);

        mAuthProvider = new AuthProvider();
        mTokenProvider = new TokenProvider();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        generateToken();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        super.onStart();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .orderByChild("date");
        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(query,Order.class).build();

        mAdapter = new OrderAdapter(options,getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
    public void generateToken(){
        mTokenProvider.create(mAuthProvider.getId());
    }
}
