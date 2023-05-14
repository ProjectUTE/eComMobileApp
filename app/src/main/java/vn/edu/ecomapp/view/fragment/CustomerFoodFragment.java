package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.CategoryApi;
import vn.edu.ecomapp.api.ProductApi;
import vn.edu.ecomapp.dto.Category;
import vn.edu.ecomapp.dto.Product;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.prefs.CategoryManager;
import vn.edu.ecomapp.util.prefs.ProductManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.CategoryAdapter;
import vn.edu.ecomapp.view.adapter.ProductAdapter;
import vn.edu.ecomapp.view.adapter.decorator.SpacesItemDecoration;

public class CustomerFoodFragment  extends Fragment {
    private final String TAG = CustomerFoodFragment.class.getName();
    List<Category> categories;
    List<Product> products;
    RecyclerView recyclerViewCategories, recyclerViewProducts;
    CategoryManager categoryManager;
    ProductManager productManager;
    CategoryApi categoryApi;
    ProductApi productApi;
    TokenManager tokenManager;
    String categoryId;
    BottomNavigationView bottomNavigationView;
    ProgressDialog pd;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        categoryManager = CategoryManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CATEGORY, Context.MODE_PRIVATE));
        productManager =  ProductManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_PRODUCT, Context.MODE_PRIVATE));
        tokenManager = TokenManager
                .getInstance(context.getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categories = new ArrayList<>();
        products = new ArrayList<>();
        categoryApi = RetrofitClient.createApiWithAuth(CategoryApi.class, tokenManager);
        productApi = RetrofitClient.createApiWithAuth(ProductApi.class, tokenManager);
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav);
        pd = new ProgressDialog(getContext());
        pd.setCanceledOnTouchOutside(false);

        loadRecyclerViewCategory(view);
        loadRecyclerViewProducts(view);
    }

    private void loadRecyclerViewProducts(View view) {

        categoryId = categoryManager.getCategoryId();
        pd.setTitle("Load products");
        pd.setMessage("Load products in progress, please wait");
        pd.show();
        productApi.getProductByCategoryId(categoryId, "0").enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                pd.dismiss();
                if (response.body() == null) {
                    Log.d(TAG, "response null");
                   return;
                } else {
                    Log.d(TAG, Integer.toString(response.body().size()));
                }
                products = response.body();
                recyclerViewProducts = view.findViewById(R.id.productRecyclerView);
                GridLayoutManager gridLayoutManager = new
                GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                recyclerViewProducts.setLayoutManager(gridLayoutManager);
                ProductAdapter productAdapter = new ProductAdapter(getContext(), products);
                recyclerViewProducts.setAdapter(productAdapter);
                productAdapter.setOnItemClickListener((position, view1) -> {
                    TextView tvProductId = view1.findViewById(R.id.tvProductId);
                    if(tvProductId == null) return;
                    String pId = tvProductId.getText().toString();
                    productManager.saveProductId(pId);
                    FragmentManager.nextFragment(requireActivity(), new ProductDetailFragment());
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                pd.dismiss();
                Log.d(TAG, t.getMessage());
            }
        });

    }

    private void loadRecyclerViewCategory(View view) {
        pd.setTitle("Load categories");
        pd.setMessage("Load categories in progress, please wait");
        pd.show();
        categoryApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                pd.dismiss();
                if(response.body() == null) return;
                categories = response.body();

                recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerViewCategories.setLayoutManager(linearLayoutManager);
                CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
                recyclerViewCategories.setAdapter(categoryAdapter);
                recyclerViewCategories.addItemDecoration(new SpacesItemDecoration(40));
                categoryAdapter.setOnItemClickListener((position, view1) -> {
                TextView categoryId = view1.findViewById(R.id.categoryId);
                if(categoryId == null)  return;
                Log.d("Category", categoryId.getText().toString());
                String id = categoryId.getText().toString();
                categoryManager.saveCategoryId(id);
                loadRecyclerViewProducts(view);
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                pd.dismiss();
                Log.d(TAG, t.getMessage());
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_food, null) ;
       requireActivity().setTitle("Food");
       return  view;
    }
}


