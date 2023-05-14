package vn.edu.ecomapp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.AuthApi;
import vn.edu.ecomapp.dto.ForgotPasswordResponse;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.AlertDialogMessage;
import vn.edu.ecomapp.util.constants.HttpStatusConstants;

public class ForgotPasswordActivity extends AppCompatActivity {

    private final String TAG = ForgotPasswordActivity.class.getName();
   TextInputLayout editTextEmail;
   TextView goToLoginButton;
   Button sendEmailButton;
   String email;

   AuthApi authApi;

   ProgressDialog pd;


    private  void initializeComponents() {
        this.editTextEmail = findViewById(R.id.edit_text_email);
        this.goToLoginButton = findViewById(R.id.goToLoginButton);
        this.sendEmailButton = findViewById(R.id.button_send_email);
        email = "";

        // Handle event
        goToLoginButton.setOnClickListener(view -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        authApi = RetrofitClient.createApi(AuthApi.class);
        pd = new ProgressDialog(ForgotPasswordActivity.this);
        pd.setCanceledOnTouchOutside(false);
        initializeComponents();
        handleSendEmailButtonClick();
    }

    private void handleSendEmailButtonClick() {
        this.sendEmailButton.setOnClickListener(view -> {
            email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
            Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
            if(email.isEmpty()) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            String FORM_DATA = "multipart/form-data";
            RequestBody rbEmail = RequestBody.create(MediaType.parse(FORM_DATA), email);
            pd.setTitle("Forgot Password");
            pd.setMessage("Sending email, please wait");
            pd.show();
            authApi.sendEmailToResetPassword(rbEmail).enqueue(new Callback<ForgotPasswordResponse>() {
                @Override
                public void onResponse(@NonNull Call<ForgotPasswordResponse> call, @NonNull Response<ForgotPasswordResponse> response) {
                    pd.dismiss();
                    if(response.body() == null) {
                        AlertDialogMessage.showAlertMessage(ForgotPasswordActivity.this, "Forgot Password", "Body null");
                        return;
                    }
                    AlertDialogMessage.showAlertMessage(ForgotPasswordActivity.this, response.body().getTitle(), response.body().getMessage());
                }
                @Override
                public void onFailure(@NonNull Call<ForgotPasswordResponse> call, @NonNull Throwable t) {
                    pd.dismiss();
                    Log.d(TAG, t.getMessage());
                    AlertDialogMessage.showAlertMessage(ForgotPasswordActivity.this, "Forgot Password", "Error occur");
                }
            });

        });
    }
}