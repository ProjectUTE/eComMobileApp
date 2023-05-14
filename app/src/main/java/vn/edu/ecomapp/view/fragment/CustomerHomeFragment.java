package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.CategoryApi;
import vn.edu.ecomapp.api.ProductApi;
import vn.edu.ecomapp.api.ProfileApi;
import vn.edu.ecomapp.api.SlideApi;
import vn.edu.ecomapp.dto.Category;
import vn.edu.ecomapp.dto.Customer;
import vn.edu.ecomapp.dto.Product;
import vn.edu.ecomapp.dto.Slide;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.services.oauth2.GoogleAuthManager;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.constants.UrlConstants;
import vn.edu.ecomapp.util.prefs.CategoryManager;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.CategoryAdapter;
import vn.edu.ecomapp.view.adapter.SliderAdapter;
import vn.edu.ecomapp.view.adapter.decorator.SpacesItemDecoration;

public class CustomerHomeFragment extends Fragment{
    List<Category> categories;
    List<Product> popularProducts;
    List<Slide> slides;
    RecyclerView recyclerView;
    CategoryApi categoryApi;
    ProductApi productApi;
    SlideApi slideApi;
    ViewPager2 sliderContainer;
    Handler sliderHandler = new Handler();
    GoogleSignInAccount googleSignInAccount;
    TextView displayName;
    ImageView avatar;
    EditText search;
    TokenManager tokenManager;
    SharedPreferences prefs;

    BottomNavigationView bottomNavigationView;
    CategoryManager categoryManager;
    CustomerManager customerManager;
    Customer profile;

    ProfileApi profileApi;
    private void getAccount() {
        googleSignInAccount = GoogleAuthManager.getGoogleSignInAccount(getContext());
    }

    private void initApi() {
        if(prefs != null) {
            tokenManager = TokenManager.getInstance(prefs);
        }
        slideApi = RetrofitClient.createApiWithAuth(SlideApi.class, tokenManager);
        categoryApi = RetrofitClient.createApiWithAuth(CategoryApi.class, tokenManager);
        productApi = RetrofitClient.getRetrofit().create(ProductApi.class);
        profileApi= RetrofitClient.createApiWithAuth(ProfileApi.class, tokenManager);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
         prefs = context.getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE);
         categoryManager = CategoryManager
                 .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CATEGORY, Context.MODE_PRIVATE));
         customerManager = CustomerManager
                 .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CUSTOMER, Context.MODE_PRIVATE));
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
        getAccount();
        loadInfoAccount();
    }

    private void initComponents(View view) {
        displayName = view.findViewById(R.id.profileName);
        avatar = view.findViewById(R.id.avatar);
        search = view.findViewById(R.id.search);
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav);
    }
    private void loadInfoAccount() {
        profileApi.geCustomerById(customerManager.getCustomer().getCustomerId())
                .enqueue(new Callback<Customer>() {
                    @Override
                    public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
                        if (response.body() == null) return;
                        profile = response.body();
                        String avatarStr = "";
                        if(profile.getAvatar() == null || profile.getAvatar().equals("")) {
                            if(googleSignInAccount == null) avatarStr = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
                            else {
                                avatarStr = Objects.requireNonNull(googleSignInAccount.getPhotoUrl()).toString();
                            }
                        }
                        else {
                            avatarStr = profile.getAvatar().replace(UrlConstants.BASE_URL_LOCAL, UrlConstants.BASE_URL);
                        }
                        profile.setAvatar(avatarStr);
                        customerManager.removeCustomer();
                        customerManager.saveCustomer(profile);

                        displayName.setText(profile.getDisplayName());
                        Glide.with(requireContext())
                            .load(profile.getAvatar())
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(1000)))
                            .into(avatar);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
                        Log.d("TAG", t.getMessage());
                    }
                });

    }
    private void loadSlides(View view) {
        slideApi.getAllSlide().enqueue(new Callback<List<Slide>>() {
            @Override
            public void onResponse(@NonNull Call<List<Slide>> call, @NonNull Response<List<Slide>> response) {
               if(response.body() == null)  return;
               slides = response.body();
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

            @Override
            public void onFailure(@NonNull Call<List<Slide>> call, @NonNull Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });



    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            sliderContainer.setCurrentItem(sliderContainer.getCurrentItem() + 1);
        }
    };


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
                categoryAdapter.setOnItemClickListener((position, view1) -> {
                    TextView categoryId = view1.findViewById(R.id.categoryId);
                    if(categoryId == null)  return;
                    Log.d("Category", categoryId.getText().toString());
                    String id = categoryId.getText().toString();
                    categoryManager.saveCategoryId(id);
                    bottomNavigationView.setSelectedItemId(R.id.food);
                });
                recyclerView.addItemDecoration(new SpacesItemDecoration(20));
            }
            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Log.d("Home Activity", t.getMessage());
            }
        });

    }

    private void initializeData() {
        categories = new ArrayList<>();
        slides = new ArrayList<>();
        popularProducts = new ArrayList<>();
    }
}
