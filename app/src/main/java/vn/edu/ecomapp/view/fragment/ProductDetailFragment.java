package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;

import java.lang.invoke.ConstantCallSite;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.ImageApi;
import vn.edu.ecomapp.api.ProductApi;
import vn.edu.ecomapp.dto.Cart;
import vn.edu.ecomapp.model.ImagePreview;
import vn.edu.ecomapp.model.LineItem;
import vn.edu.ecomapp.model.Product;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.Random;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.constants.UrlConstants;
import vn.edu.ecomapp.util.prefs.CartManager;
import vn.edu.ecomapp.util.prefs.ProductManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.ImagePreviewAdapter;

public class ProductDetailFragment extends Fragment {

    TextView appBarTitle;
    ImageView backButton, image;
    TextView addToCartButton;
    RecyclerView imagePreviewsRecycerView;
    List<ImagePreview> imagePreviews;
    CartManager cartManager;
    ProductManager productManager;
    Cart cart;
    String productId;
    ProductApi productApi;
    ImageApi imageApi;
    TokenManager tokenManager;
    TextView tvCost, tvName, tvDetail;
    Product productDetail;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cartManager = CartManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CART, Context.MODE_PRIVATE));
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

    private LineItem getLineItem() {
        int quantity = 1;
        int price = CurrencyFormat.convertVNCurrencyToInt(tvCost.getText().toString());
        int amount = quantity * price;
        LineItem lineItem = new LineItem();
        lineItem.setId(Random.randomId("LI"));
        lineItem.setProductId(productId);
        lineItem.setName(tvName.getText().toString());
        lineItem.setQuantity(quantity);
        lineItem.setPrice(price);
        lineItem.setAmount(amount);
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
        image = view.findViewById(R.id.image);

        cart = cartManager.getCart();
        productId = productManager.getProductId();
        productApi = RetrofitClient.createApiWithAuth(ProductApi.class, tokenManager);
        imageApi = RetrofitClient.createApiWithAuth(ImageApi.class, tokenManager);
        imagePreviewsRecycerView = view.findViewById(R.id.recyclerViewImagesPreview);

        backButton.setOnClickListener(view1 -> requireActivity().getSupportFragmentManager().popBackStack());
        addToCartButton.setOnClickListener(view2 -> {
            LineItem lineItem = getLineItem();
            cart.addLineItem(lineItem);
            cartManager.saveCart(cart);
            FragmentManager.nextFragment(requireActivity(), new CustomerCartFragment());
        });

        if(productId == null || productId.equals("")) return;
        productApi.getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if(response.body() == null) return;
                productDetail = response.body();
                String cost = CurrencyFormat.VietnameseCurrency(productDetail.getNewPrice());
                tvCost.setText(cost);
                tvName.setText(productDetail.getName());
                appBarTitle.setText(productDetail.getName());
                tvDetail.setText(productDetail.getDetail());
                if(productDetail.getMainImage() == null || productDetail.getMainImage().equals("")) return;
                String imageUrlReplaced = productDetail.getMainImage()
                                .replace(UrlConstants.BASE_URL_LOCAL, UrlConstants.BASE_URL)
                                        .replace(" ", "%20");
//                Glide.with(requireContext()).load(imageUrlReplaced).into(image);
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
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