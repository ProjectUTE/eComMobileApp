package vn.edu.ecomapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.LoginApi;
import vn.edu.ecomapp.model.dto.MessageDto;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.AlertDialogMessage;

public class VerifyAccountActivity extends AppCompatActivity {
    TextInputLayout editTextEmail, editTextVerifyCode;
    Button buttonVerifyAccount;

    LoginApi loginApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        this.initializeComponents();
        this.handleVerifyAccount();
    }

    private void initializeComponents() {
        this.editTextEmail = findViewById(R.id.edit_text_email);
        this.editTextVerifyCode = findViewById(R.id.edit_text_otp_code);
        this.buttonVerifyAccount = findViewById(R.id.button_verify_account);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
//        editTextEmail.getEditText().setText(email);
        loginApi = RetrofitClient.getRetrofit().create(LoginApi.class);
    }

    private void handleVerifyAccount() {
        buttonVerifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpCode = Objects.requireNonNull(editTextVerifyCode.getEditText()).getText().toString().trim();
                loginApi.verifyAccountUser(otpCode).enqueue(new Callback<MessageDto>() {
                    @Override
                    public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                        AlertDialogMessage.showAlertMessage(VerifyAccountActivity.this, response.body().getTitle(), response.body().getMessage());
                        if(response.body().getStatus().equals("SUCCESS")){
                            Intent intent = new Intent(VerifyAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(VerifyAccountActivity.this, SignUpActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageDto> call, Throwable t) {
                        Toast.makeText(VerifyAccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}