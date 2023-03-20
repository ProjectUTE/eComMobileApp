package vn.edu.ecomapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import vn.edu.ecomapp.model.MessageDto;

public interface LoginApi {
    @FormUrlEncoded
    @POST("login")
    Call<MessageDto> loginUser(@Field("to-email") String toEmail, @Field("password") String password);

    @FormUrlEncoded
    @POST("sign-up-account")
    Call<MessageDto> createAccountUser(@Field("to-email") String toEmail, @Field("password") String password);

    @FormUrlEncoded
    @POST("sign-up-account/verify-account")
    Call<MessageDto> verifyAccountUser(@Field("otp-code") String otpCode);
}
