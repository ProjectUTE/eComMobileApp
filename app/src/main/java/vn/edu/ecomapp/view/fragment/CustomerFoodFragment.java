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
import vn.edu.ecomapp.model.Category;
import vn.edu.ecomapp.model.Product;
import vn.edu.ecomapp.view.adapter.CategoryAdapter;
import vn.edu.ecomapp.view.adapter.ProductAdapter;
import vn.edu.ecomapp.view.adapter.decorator.SpacesItemDecoration;

public class CustomerFoodFragment  extends Fragment {

    List<Category> categories;
    List<Product> products;

    RecyclerView recyclerViewCategories, recyclerViewProducts;

  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
        loadRecyclerViewCategory(view);
        loadRecyclerViewProducts(view);
    }

    private void loadRecyclerViewProducts(View view) {
        recyclerViewProducts = view.findViewById(R.id.productRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewProducts.setLayoutManager(linearLayoutManager);
        ProductAdapter productAdapter = new ProductAdapter(getContext(), products);
        recyclerViewProducts.setAdapter(productAdapter);
//        recyclerViewCategories.addItemDecoration(new SpacesItemDecoration(40));
    }

    private void loadRecyclerViewCategory(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
        recyclerViewCategories.setAdapter(categoryAdapter);
        recyclerViewCategories.addItemDecoration(new SpacesItemDecoration(40));
    }

    private void initializeData() {
        categories = new ArrayList<>();
        categories.add(new Category("1", "Coffee"));
        categories.add(new Category("2", "Drinks"));
        categories.add(new Category("3", "Cakes"));
        categories.add(new Category("4", "Beer"));
        categories.add(new Category("5", "Others"));

        products = new ArrayList<>();
        products.add(new Product("Coffee 1", 10000));
        products.add(new Product("Coffee 2", 10000));
        products.add(new Product("Coffee 3", 10000));
        products.add(new Product("Coffee 4", 10000));
        products.add(new Product("Coffee 5", 10000));
        products.add(new Product("Coffee 6", 10000));
        products.add(new Product("Coffee 7", 10000));
        products.add(new Product("Coffee 8", 10000));
        products.add(new Product("Coffee 9", 10000));
        products.add(new Product("Coffee 10", 10000));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_food, null) ;
       requireActivity().setTitle("Food");
       return  view;
    }
}


