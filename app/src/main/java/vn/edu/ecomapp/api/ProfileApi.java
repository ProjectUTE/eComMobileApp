package vn.edu.ecomapp.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import vn.edu.ecomapp.dto.profile.ProfileResponse;
import vn.edu.ecomapp.dto.Customer;

public interface ProfileApi {
    @GET("profile/get/{id}")
    Call<Customer> geCustomerById(@Path("id") String id);

    @PUT("profile/update")
    @Multipart
    Call<ProfileResponse> updateProfile(@Part("id") RequestBody id,
                                        @Part MultipartBody.Part avatar,
                                        @Part("displayName") RequestBody displayName,
                                        @Part("phonenumber") RequestBody phonenumber,
                                        @Part("address") RequestBody address);
}
