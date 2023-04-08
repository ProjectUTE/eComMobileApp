package vn.edu.ecomapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.LoginApi;
import vn.edu.ecomapp.dto.message.MessageResponse;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.AlertDialogMessage;

public class SignUpActivity extends AppCompatActivity {

   TextView textViewAlreadyAccount;
   TextInputLayout editTextEmail, editTextPassword, editTextConfirmPassword;
   Button buttonSignUp;
   String email = "", password = "", confirmPassword = "";
   LoginApi loginApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.initializeComponents();
        this.initializeDatabase();
        this.handleTextViewClick();
        this.handleButtonSignUpClick();
    }


    private  void initializeDatabase() {
        loginApi = RetrofitClient.getRetrofit().create(LoginApi.class);
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
            if(password.equals(confirmPassword)) {
                loginApi.createAccountUser(email, password).enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                        assert response.body() != null;
                        AlertDialogMessage.showAlertMessage(SignUpActivity.this, response.body().getTitle(), response.body().getMessage());
                        if(response.body().getStatus().equals("SUCCESS")) {
                            Intent intent = new Intent(SignUpActivity.this, VerifyAccountActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                        AlertDialogMessage.showAlertMessage(SignUpActivity.this, "Sign Up Message", t.getMessage());
//                            Intent intent = new Intent(SignUpActivity.this, VerifyAccountActivity.class);
//                            intent.putExtra("email", email);
//                            startActivity(intent);
//                            finish();
                    }
                });
            } else {
                Toast.makeText(SignUpActivity.this, "Confirm the password needs to be the same as the password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid() {
        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Objects.requireNonNull(editTextEmail).setError("");
        Objects.requireNonNull(editTextPassword).setError("");
//        Objects.requireNonNull(editTextConfirmPassword).setError("");
        editTextEmail.setErrorEnabled(false);
        editTextPassword.setErrorEnabled(false);
//        editTextConfirmPassword.setErrorEnabled(false);
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

//        if(TextUtils.isEmpty(confirmPassword)) {
//            editTextConfirmPassword.setErrorEnabled(true);
//            Objects.requireNonNull(editTextConfirmPassword.getEditText()).setError("Confirm password is required");
//        }
//        else {
//            if(!TextUtils.equals(password, confirmPassword)) {
//                editTextConfirmPassword.setErrorEnabled(true);
//                Objects.requireNonNull(editTextConfirmPassword.getEditText()).setError("Confirm password doesn't match");
//            } else {
//                isValidConfirmPassword = true;
//            }
//        }
//        return  isValidEmail && isValidPassword && isValidConfirmPassword;
        return  isValidEmail && isValidPassword;
    }
}