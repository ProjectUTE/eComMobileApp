package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.Product;
import vn.edu.ecomapp.view.adapter.CartAdapter;

public class CustomerCartFragment  extends Fragment {

    RecyclerView recyclerViewCart;
    List<Product> products;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_cart, null) ;
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
        loadRecyclerViewCart(view);
    }

    private void loadRecyclerViewCart(View view) {
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        recyclerViewCart.setHasFixedSize(true);
        CartAdapter cartAdapter = new CartAdapter(getContext(), products);
        recyclerViewCart.setAdapter(cartAdapter);
    }

    private void initializeData() {
        this.products = new ArrayList<>();
        this.products.add(new Product("Coffee", 14000));
        this.products.add(new Product("Coffee 1", 15000));
        this.products.add(new Product("Coffee 2", 16000));
        this.products.add(new Product("Coffee 3", 17000));
        this.products.add(new Product("Coffee 4", 18000));
    }
}