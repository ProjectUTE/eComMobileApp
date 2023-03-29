package vn.edu.ecomapp.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import vn.edu.ecomapp.model.AccessToken;

public interface AccessTokenApi {
    @POST("/token/refresh")
    @FormUrlEncoded
    Call<AccessToken> refreshToken(@Field("refresh_token") String refreshToken);
}
