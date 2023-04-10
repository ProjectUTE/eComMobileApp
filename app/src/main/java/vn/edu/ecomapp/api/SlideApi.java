package vn.edu.ecomapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.edu.ecomapp.model.Slide;

public interface SlideApi {
    @GET("slide/get-all")
    Call<List<Slide>> getAllSlide();
}
