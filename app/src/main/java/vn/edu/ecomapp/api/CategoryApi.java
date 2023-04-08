package vn.edu.ecomapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import vn.edu.ecomapp.model.Category;

public interface CategoryApi {
    @GET("category/get-all")
    Call<List<Category>> getCategories();

    @GET("category/find-by-id")
    Call<Category> getCategoryById();
}
