package vn.edu.ecomapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import vn.edu.ecomapp.dto.login.LoginRequest;
import vn.edu.ecomapp.dto.login.LoginResponse;
import vn.edu.ecomapp.dto.message.MessageResponse;

public interface LoginApi {
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @FormUrlEncoded
    @POST("signup")
    Call<MessageResponse> createAccountUser(@Field("to-email") String toEmail, @Field("password") String password);

    @FormUrlEncoded
    @POST("signup/verify")
    Call<MessageResponse> verifyAccountUser(@Field("otp-code") String otpCode);
}
