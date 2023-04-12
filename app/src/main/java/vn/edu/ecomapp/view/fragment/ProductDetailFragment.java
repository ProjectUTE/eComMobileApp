package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.ImageApi;
import vn.edu.ecomapp.api.ProductApi;
import vn.edu.ecomapp.dto.ImagePreview;
import vn.edu.ecomapp.dto.Product;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.room.database.CartDatabase;
import vn.edu.ecomapp.room.entities.CartItem;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.Random;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.ProductManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.ImagePreviewAdapter;

public class ProductDetailFragment extends Fragment {

    BottomNavigationView bottomNavigationView;

    TextView appBarTitle;
    ImageView backButton, image;
    TextView addToCartButton;
    RecyclerView imagePreviewsRecycerView;
    List<ImagePreview> imagePreviews;
    ProductManager productManager;
    String productId, cartId;
    ProductApi productApi;
    ImageApi imageApi;
    TokenManager tokenManager;
    TextView tvCost, tvName, tvDetail;
    Product productDetail;
    CustomerManager customerManager;
    ProgressDialog pd;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       customerManager = CustomerManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CUSTOMER, Context.MODE_PRIVATE));
        productManager = ProductManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_PRODUCT, Context.MODE_PRIVATE));
        tokenManager = TokenManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    private CartItem getCartItem() {
        int quantity = 1;
        if(tvCost.getText().toString().equals(""))  {
            Log.d("CHECK", "NULL");
        } else {
            Log.d("CHECK", "NOT NULL");
        }
        int price = CurrencyFormat.convertVNCurrencyToInt(tvCost.getText().toString());
        int amount = quantity * price;
        CartItem lineItem = new CartItem();
        lineItem.setId(Random.randomId("LI"));
        lineItem.setProductId(productId);
        lineItem.setName(tvName.getText().toString());
        lineItem.setQuantity(quantity);
        lineItem.setPrice(price);
        lineItem.setAmount(amount);
        lineItem.setCartId(cartId);
        return lineItem;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCost = view.findViewById(R.id.cost);
        tvName = view.findViewById(R.id.productName);
        tvDetail = view.findViewById(R.id.productDescription);
        backButton = view.findViewById(R.id.backButton);
        appBarTitle = view.findViewById(R.id.appBarTitle);
        addToCartButton = view.findViewById(R.id.addToCartButton);
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav);
        imagePreviewsRecycerView = view.findViewById(R.id.recyclerViewImagesPreview);
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.cart);
        productApi = RetrofitClient.createApiWithAuth(ProductApi.class, tokenManager);
        imageApi = RetrofitClient.createApiWithAuth(ImageApi.class, tokenManager);

        image = view.findViewById(R.id.image);
        pd = new ProgressDialog(getContext());
        pd.setCanceledOnTouchOutside(false);
        productId = productManager.getProductId();
        cartId = customerManager.getCustomer().getCustomerId();

        backButton.setOnClickListener(view1 -> requireActivity().getSupportFragmentManager().popBackStack());
        addToCartButton.setOnClickListener(view2 -> {
            CartItem item = CartDatabase.getInstance(getContext()).cartItemDao()
                    .getCartItemByCartIdAndProductId(cartId, productId);
            if(item != null) {
                // Update
                Log.d("PD", "Update");
                CartItem cartItem =
                        CartDatabase.getInstance(getContext()).cartItemDao()
                                .getCartItemByCartIdAndProductId(cartId, productId);
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setAmount(cartItem.getQuantity() * cartItem.getPrice());
                CartDatabase.getInstance(getContext()).cartItemDao().updateCartItem(cartItem);
            } else {
                // Create
                Log.d("PD", "CREATE");
                CartDatabase.getInstance(getContext()).cartItemDao().insertCartItem(getCartItem());
                int count = CartDatabase.getInstance(getContext()).cartItemDao().getItemsCount(cartId);
                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(count);
            }
            bottomNavigationView.setSelectedItemId(R.id.cart);
        });

        if(productId == null || productId.equals("")) return;
        pd.show();
        productApi.getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                pd.dismiss();
                if(response.body() == null) return;
                productDetail = response.body();
                String cost = CurrencyFormat.VietnameseCurrency(productDetail.getNewPrice());
                tvCost.setText(cost);
                tvName.setText(productDetail.getName());
                appBarTitle.setText(productDetail.getName());
                tvDetail.setText(productDetail.getDetail());
//                if(productDetail.getMainImage() == null || productDetail.getMainImage().equals("")) return;
//                String imageUrlReplaced = productDetail.getMainImage()
//                                .replace(UrlConstants.BASE_URL_LOCAL, UrlConstants.BASE_URL)
//                                        .replace(" ", "%20");
////                Glide.with(requireContext()).load(imageUrlReplaced).into(image);
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                pd.dismiss();
                Log.d("Get product detail", t.getMessage());
            }
        });
        loadImagePreviews();
    }
    private void loadImagePreviews() {
        imageApi.getImagesByProductId(productId).enqueue(new Callback<List<ImagePreview>>() {
            @Override
            public void onResponse(@NonNull Call<List<ImagePreview>> call, @NonNull Response<List<ImagePreview>> response) {
               if(response.body() == null)  return;
               imagePreviews = response.body();
               ImagePreviewAdapter adapter = new ImagePreviewAdapter(imagePreviews, getContext());
               GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 3);
               imagePreviewsRecycerView.setLayoutManager(gridLayoutManager);
               imagePreviewsRecycerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<ImagePreview>> call, @NonNull Throwable t) {
                Log.d("Image preview", t.getMessage());
            }
        });

    }
}