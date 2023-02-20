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
import vn.edu.ecomapp.view.adapter.PopularProductAdapter;

public class CustomerHomeFragment extends Fragment {

    List<Category> categories;
    List<Product> popularProducts;
    RecyclerView recyclerView, recyclerViewPopularProduct;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_home, null);
        requireActivity().setTitle("Home");
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
       loadRecyclerViewCategory(view);
       loadRecyclerViewPopularProduct(view);
    }

    private void loadRecyclerViewPopularProduct(View view) {
        recyclerViewPopularProduct = view.findViewById(R.id.recycler_view_popular_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularProduct.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        PopularProductAdapter popularProductAdapter = new PopularProductAdapter(getContext(), this.popularProducts);
        recyclerViewPopularProduct.setAdapter(popularProductAdapter);
    }


    private void loadRecyclerViewCategory(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void initializeData() {
        categories = new ArrayList<>();
        categories.add(new Category("1", "Coffee"));
        categories.add(new Category("2", "Drinks"));
        categories.add(new Category("3", "Cakes"));
        categories.add(new Category("4", "Beer"));
        categories.add(new Category("5", "Others"));

        this.popularProducts = new ArrayList<>();
        this.popularProducts.add(new Product("Coffee", 14000));
        this.popularProducts.add(new Product("Coffee 1", 15000));
        this.popularProducts.add(new Product("Coffee 2", 16000));
        this.popularProducts.add(new Product("Coffee 3", 17000));
        this.popularProducts.add(new Product("Coffee 4", 18000));
    }
}
