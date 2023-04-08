package vn.edu.ecomapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import vn.edu.ecomapp.dto.login.LoginRequest;
import vn.edu.ecomapp.dto.login.LoginResponse;

public interface AccessTokenApi {
    @POST("refresh-token")
    Call<LoginResponse> refreshToken(@Header("Refresh-Token") String refreshToken,
                                     @Header("Authorization") String accessToken,
                                     @Body LoginRequest request);
}
