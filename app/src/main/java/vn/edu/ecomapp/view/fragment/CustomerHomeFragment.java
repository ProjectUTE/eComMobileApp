package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.CategoryApi;
import vn.edu.ecomapp.api.ProductApi;
import vn.edu.ecomapp.model.Category;
import vn.edu.ecomapp.model.Product;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.view.activity.ProductDetailActivity;
import vn.edu.ecomapp.view.adapter.CategoryAdapter;
import vn.edu.ecomapp.view.adapter.PopularProductAdapter;
import vn.edu.ecomapp.view.adapter.decorator.SpacesItemDecoration;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class CustomerHomeFragment extends Fragment{

    List<Category> categories;
    List<Product> popularProducts;
    RecyclerView recyclerView, recyclerViewPopularProduct;
    CategoryApi categoryApi;
    ProductApi productApi;


    private void initApi() {
        categoryApi = RetrofitClient.getRetrofit().create(CategoryApi.class);
        productApi = RetrofitClient.getRetrofit().create(ProductApi.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_home, null);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
        initApi();
        loadCategories(view);
        loadPopularProducts(view);
    }


    private void loadPopularProducts(View view) {
        recyclerViewPopularProduct = view.findViewById(R.id.recycler_view_popular_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularProduct.setLayoutManager(linearLayoutManager);
        PopularProductAdapter popularProductAdapter = new PopularProductAdapter(getContext(), this.popularProducts);
        recyclerViewPopularProduct.setAdapter(popularProductAdapter);
        recyclerViewPopularProduct.addItemDecoration(new SpacesItemDecoration(40));
        popularProductAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loadCategories(View view) {
        // Get Data
        categoryApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                categories = response.body();
                // Display view
                recyclerView = view.findViewById(R.id.recycler_view_category);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
                recyclerView.setAdapter(categoryAdapter);
//                Set click in item
                categoryAdapter.setOnItemClickListener((position, view1) -> Log.d("Home activity", String.format("===============%d================", position)));
                recyclerView.addItemDecoration(new SpacesItemDecoration(40));
            }
            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Home Activity", t.getMessage());
            }
        });

    }

    private void initializeData() {
        categories = new ArrayList<>();

        this.popularProducts = new ArrayList<>();
        this.popularProducts.add(new Product("Coffee", 14000));
        this.popularProducts.add(new Product("Coffee 1", 15000));
        this.popularProducts.add(new Product("Coffee 2", 16000));
        this.popularProducts.add(new Product("Coffee 3", 17000));
        this.popularProducts.add(new Product("Coffee 4", 18000));
    }
}
