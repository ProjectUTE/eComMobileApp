package vn.edu.ecomapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.ecomapp.model.ImagePreview;

public interface ImageApi {
    @GET("image/get-all-image-by-product-id/{id}")
    Call<List<ImagePreview>> getImagesByProductId(@Path("id") String id);
}
