package vn.edu.ecomapp.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.AuthApi;
import vn.edu.ecomapp.dto.signup.SignUpResponse;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.AlertDialogMessage;
import vn.edu.ecomapp.util.constants.HttpStatusConstants;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.CookieManager;

public class SignUpActivity extends AppCompatActivity {

   TextView textViewAlreadyAccount;
   TextInputLayout editTextEmail, editTextPassword, editTextConfirmPassword;
   Button buttonSignUp;
   String email = "", password = "", confirmPassword = "";
   AuthApi authApi;
   ProgressDialog pd;
   AlertDialog.Builder alBuilder;
   Gson gson = new GsonBuilder().create();
   CookieManager cookieManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pd = new ProgressDialog(SignUpActivity.this);
        pd.setCanceledOnTouchOutside(false);
        alBuilder = new AlertDialog.Builder(this);
        cookieManager = CookieManager.getInstance(getSharedPreferences(PrefsConstants.COOKIE_DATA, MODE_PRIVATE));
        initializeComponents();
        initializeDatabase();
        handleTextViewClick();
        handleButtonSignUpClick();
    }


    private  void initializeDatabase() {
        authApi = RetrofitClient.createApiWithClientTimeout(AuthApi.class);
    }

    private void initializeComponents() {
        this.textViewAlreadyAccount = findViewById(R.id.text_view_already_account);
        this.editTextEmail = findViewById(R.id.edit_text_email);
        this.editTextPassword = findViewById(R.id.edit_text_password);
        this.editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        this.buttonSignUp = findViewById(R.id.button_sign_up);
    }

    private void handleTextViewClick() {
        this.textViewAlreadyAccount.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }


    private void handleButtonSignUpClick() {
        this.buttonSignUp.setOnClickListener(v -> {
            email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
            password = Objects.requireNonNull(editTextPassword.getEditText()).getText().toString().trim();
            confirmPassword = Objects.requireNonNull(editTextConfirmPassword.getEditText()).getText().toString().trim();

            if(!isValid()) return;
            pd.setTitle("Sign Up Account");
            pd.setMessage("Account creation in progress, please wait");
            pd.show();
                authApi.createAccountUser(email, password).enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SignUpResponse> call, @NonNull Response<SignUpResponse> response) {
                        pd.dismiss();
                        if(response.isSuccessful()) {
                            // Success
                            if(response.body() == null) return;

                            // Get cookies
                           List<String> Cookielist = response.headers().values("Set-Cookie");
                           String jsessionid = (Cookielist .get(0).split(";"))[0];
                            Log.d("cookieval", jsessionid);
                            cookieManager.saveCookie(jsessionid);
                            if(response.body().getStatus().equals(HttpStatusConstants.SUCCESS)) {
                                alBuilder.setTitle(response.body().getTitle());
                                alBuilder.setMessage(response.body().getMessage());
                                alBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
                                    Intent intent = new Intent(SignUpActivity.this, VerifyAccountActivity.class);
                                    startActivity(intent);
                                })
                                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
                            }
                            return;
                        }
                        SignUpResponse signUpResponse;
                        try {
                            if(response.errorBody() == null) return;
                            response.errorBody().string();
                            signUpResponse = gson.fromJson(response.errorBody().string(), SignUpResponse.class);
                            Log.d("SIGNUP", signUpResponse.getMessage());
                            Toast.makeText(SignUpActivity.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.d("SIGNUP", e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SignUpResponse> call, @NonNull Throwable t) {
                        AlertDialogMessage.showAlertMessage(SignUpActivity.this, "Sign Up Message", t.getMessage());
                        Log.d("TAG", t.getMessage());
                    }
                });
        });
    }

    private boolean isValid() {
        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Objects.requireNonNull(editTextEmail).setError("");
        Objects.requireNonNull(editTextPassword).setError("");
        Objects.requireNonNull(editTextConfirmPassword).setError("");
        editTextEmail.setErrorEnabled(false);
        editTextPassword.setErrorEnabled(false);
        editTextConfirmPassword.setErrorEnabled(false);
        boolean isValidEmail = false, isValidPassword = false, isValidConfirmPassword = false;

        if(TextUtils.isEmpty(email)) {
            editTextEmail.setErrorEnabled(true);
            Objects.requireNonNull(editTextEmail.getEditText()).setError("Email is required");
        } else  {
            if(email.matches(emailPattern)) {
                isValidEmail = true;
            } else {
                editTextEmail.setErrorEnabled(true);
                Objects.requireNonNull(editTextEmail.getEditText()).setError("Enter a valid email");
            }
        }

        if(TextUtils.isEmpty(password)) {
            editTextPassword.setErrorEnabled(true);
            Objects.requireNonNull(editTextPassword.getEditText()).setError("Password is required");
        } else  {
            if(password.length() < 8)  {
                editTextPassword.setErrorEnabled(true);
                Objects.requireNonNull(editTextPassword.getEditText()).setError("Password is weak");
            } else {
                isValidPassword = true;
            }
        }

        if(TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setErrorEnabled(true);
            Objects.requireNonNull(editTextConfirmPassword.getEditText()).setError("Confirm password is required");
        }
        else {
            if(!TextUtils.equals(password, confirmPassword)) {
                editTextConfirmPassword.setErrorEnabled(true);
                Objects.requireNonNull(editTextConfirmPassword.getEditText()).setError("Confirm password doesn't match");
            } else {
                isValidConfirmPassword = true;
            }
        }
        return  isValidEmail && isValidPassword && isValidConfirmPassword;
    }
}