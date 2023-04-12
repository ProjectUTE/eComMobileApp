package vn.edu.ecomapp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.AuthApi;
import vn.edu.ecomapp.dto.message.MessageResponse;
import vn.edu.ecomapp.dto.signup.SignUpResponse;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.AlertDialogMessage;
import vn.edu.ecomapp.util.constants.HttpStatusConstants;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.CookieManager;

public class VerifyAccountActivity extends AppCompatActivity {
    TextInputLayout editTextVerifyCode;
    Button buttonVerifyAccount;
    AuthApi authApi;
    ProgressDialog pd;

    Gson gson = new GsonBuilder().create();
    CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        pd = new ProgressDialog(VerifyAccountActivity.this);
        pd.setCanceledOnTouchOutside(false);
        cookieManager = CookieManager.getInstance(getSharedPreferences(PrefsConstants.COOKIE_DATA, MODE_PRIVATE));
        initializeComponents();
        handleVerifyAccount();
    }

    private void initializeComponents() {
        editTextVerifyCode = findViewById(R.id.edit_text_otp_code);
        buttonVerifyAccount = findViewById(R.id.button_verify_account);
        authApi = RetrofitClient.createApiWithClientTimeout(AuthApi.class);
    }

    private void handleVerifyAccount() {
        buttonVerifyAccount.setOnClickListener(v -> {
            pd.setTitle("Verify Account");
            pd.setMessage("Checking OTP, please wait");
            String otpCode = Objects.requireNonNull(editTextVerifyCode.getEditText()).getText().toString().trim();
            if(otpCode.equals("")) {
                editTextVerifyCode.getEditText().setError("Please enter your OTP code");
                return;
            }

            String FORM_DATA = "multipart/form-data";
            RequestBody rbOTP = RequestBody.create(MediaType.parse(FORM_DATA), otpCode);
            pd.show();
            String cookie = cookieManager.getCookie();
            Log.d("Verify", cookie);
            authApi.verifyAccountUser(cookie, rbOTP).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                    pd.dismiss();
                    if(response.isSuccessful()) {
                        if(response.body() == null) return;
                        AlertDialogMessage.showAlertMessage(VerifyAccountActivity.this, response.body().getTitle(), response.body().getMessage());
                        Intent intent;
                        if(response.body().getStatus().equals(HttpStatusConstants.SUCCESS)){
                            intent = new Intent(VerifyAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        return;
                    }
                    SignUpResponse signUpResponse;
                    try {
                        if(response.errorBody() == null) return;
                        signUpResponse = gson.fromJson(response.errorBody().string(), SignUpResponse.class);
                        Log.d("Verify Account", signUpResponse.getMessage());
                        Toast.makeText(VerifyAccountActivity.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.d("Verify Account", e.getMessage());
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                    pd.dismiss();
                    Toast.makeText(VerifyAccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Verify Account", t.getMessage());
                }
            });
        });
    }
}