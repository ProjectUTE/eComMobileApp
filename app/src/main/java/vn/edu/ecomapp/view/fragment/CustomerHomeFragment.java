package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.CategoryApi;
import vn.edu.ecomapp.api.ProductApi;
import vn.edu.ecomapp.model.Category;
import vn.edu.ecomapp.model.Product;
import vn.edu.ecomapp.model.Slide;
import vn.edu.ecomapp.oauth2.GoogleAuthManager;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.view.activity.ProductDetailActivity;
import vn.edu.ecomapp.view.adapter.CategoryAdapter;
import vn.edu.ecomapp.view.adapter.PopularProductAdapter;
import vn.edu.ecomapp.view.adapter.SliderAdapter;
import vn.edu.ecomapp.view.adapter.decorator.SpacesItemDecoration;

public class CustomerHomeFragment extends Fragment{

    List<Category> categories;
    List<Product> popularProducts;
    List<Slide> slides;
    RecyclerView recyclerView, recyclerViewPopularProduct;
    CategoryApi categoryApi;
    ProductApi productApi;
    ViewPager2 sliderContainer;
    Handler sliderHandler = new Handler();
    GoogleSignInAccount googleSignInAccount;

    TextView displayName;
    ImageView avatar;

    private void getAccount() {
        googleSignInAccount = GoogleAuthManager.getGoogleSignInAccount(getContext());
    }

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
        initComponents(view);
        initApi();
        loadSlides(view);
        loadCategories(view);
        loadPopularProducts(view);
        getAccount();
        loadInfoAccount();
    }

    private void initComponents(View view) {
        displayName = view.findViewById(R.id.profileName);
        avatar = view.findViewById(R.id.avatar);
    }

    private void loadInfoAccount() {
        if(googleSignInAccount == null)
            return;

        if(!Objects.requireNonNull(googleSignInAccount.getDisplayName()).isEmpty())
            displayName.setText(googleSignInAccount.getDisplayName());

        Uri photoUrl = googleSignInAccount.getPhotoUrl();
        if(photoUrl != null)
            Glide.with(requireContext())
                    .load(photoUrl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
                    .into(avatar);
    }

    private void loadSlides(View view) {
        sliderContainer = view.findViewById(R.id.sliderContainer);
        SliderAdapter sliderAdapter = new SliderAdapter(getContext(), slides, sliderContainer);
        sliderContainer.setAdapter(sliderAdapter);
        sliderContainer.setClipToPadding(false);
        sliderContainer.setClipChildren(false);
        sliderContainer.setOffscreenPageLimit(5);
        sliderContainer.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r*0.15f);
        });
        sliderContainer.setPageTransformer(compositePageTransformer);

        sliderContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000);
            }
        });
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            sliderContainer.setCurrentItem(sliderContainer.getCurrentItem() + 1);
        }
    };


    private void loadPopularProducts(View view) {
        recyclerViewPopularProduct = view.findViewById(R.id.recycler_view_popular_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularProduct.setLayoutManager(linearLayoutManager);
        PopularProductAdapter popularProductAdapter = new PopularProductAdapter(getContext(), this.popularProducts);
        recyclerViewPopularProduct.setAdapter(popularProductAdapter);
        recyclerViewPopularProduct.addItemDecoration(new SpacesItemDecoration(20));
        popularProductAdapter.setOnItemClickListener((position, view1) -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            startActivity(intent);
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
                recyclerView.addItemDecoration(new SpacesItemDecoration(20));
            }
            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Home Activity", t.getMessage());
            }
        });

    }

    private void initializeData() {
//        Categories
        categories = new ArrayList<>();

//        Products
        this.popularProducts = new ArrayList<>();
        this.popularProducts.add(new Product("Coffee", 14000));
        this.popularProducts.add(new Product("Coffee 1", 15000));
        this.popularProducts.add(new Product("Coffee 2", 16000));
        this.popularProducts.add(new Product("Coffee 3", 17000));
        this.popularProducts.add(new Product("Coffee 4", 18000));

//        Slides
        slides = new ArrayList<>();
        slides.add(new Slide("https://images.unsplash.com/photo-1678901783809-4eef2aab00fe?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));
        slides.add(new Slide("https://images.unsplash.com/photo-1672243776760-67aec979f591?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));
        slides.add(new Slide("https://images.unsplash.com/photo-1678599694191-0bd740f04065?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));
        slides.add(new Slide("https://images.unsplash.com/photo-1679150903021-6571b98b4afe?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDIzfGJvOGpRS1RhRTBZfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"));
        slides.add(new Slide("https://images.unsplash.com/photo-1679155506707-a072b305dd91?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDI3fGJvOGpRS1RhRTBZfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"));
    }
}
