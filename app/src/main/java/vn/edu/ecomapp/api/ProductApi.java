package vn.edu.ecomapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.ecomapp.dto.Product;

public interface ProductApi {
    @GET("product/get-detail-product-by-id/{id}")
    Call<Product> getProductById(@Path("id") String id);

    @GET("product/get-product-by-category-id")
    Call<List<Product>> getProductByCategoryId(@Query("id") String categoryId, @Query("pageNo") String pageNo);
}
