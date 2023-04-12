package vn.edu.ecomapp.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import vn.edu.ecomapp.dto.login.LoginRequest;
import vn.edu.ecomapp.dto.login.LoginResponse;
import vn.edu.ecomapp.dto.message.MessageResponse;
import vn.edu.ecomapp.dto.signup.SignUpResponse;

public interface AuthApi {
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @FormUrlEncoded
    @POST("sign-up-account")
    Call<SignUpResponse> createAccountUser(@Field("to-email") String toEmail, @Field("password") String password);

    @POST("sign-up-account/verify-account")
    @Multipart
    Call<MessageResponse> verifyAccountUser(@Header("Cookie") String cookie, @Part("otp-code") RequestBody otpCode);
}
