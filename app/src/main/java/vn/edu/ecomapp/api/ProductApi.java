package vn.edu.ecomapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.edu.ecomapp.model.Product;

public interface ProductApi {
    @GET
    Call<List<Product>> getProducts();

    @GET
    Call<Product> getProductByCategoryId();
}
