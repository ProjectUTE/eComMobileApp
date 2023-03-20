package vn.edu.ecomapp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.LoginApi;
import vn.edu.ecomapp.model.MessageDto;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.AlertDialogMessage;

public class LoginActivity extends AppCompatActivity {

//    Components
    CheckBox rememberMeCheckbox;
    TextView textViewNotYetAccount, textViewForgotPassword;
    TextInputLayout editTextEmail, editTextPassword;
    Button buttonLogin;

//    Data
    String email = "", password = "";


    FirebaseAuth firebaseAuth;

    SharedPreferences sharedPreferences;

    LoginApi loginApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSharePreferences();
        this.initializeComponents();
        this.handleTextViewClick();
        this.handleButtonLoginClick();
        this.handleGoToForgotPasswordActivity();
    }

    private void getSharePreferences() {
        this.sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
    }

    private void handleGoToForgotPasswordActivity() {
        this.textViewForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void initializeComponents() {
        this.textViewNotYetAccount = findViewById(R.id.text_view_not_yet_account);
        this.textViewForgotPassword = findViewById(R.id.text_view_forgot_password);

        // init edit text
        this.editTextEmail = findViewById(R.id.edit_text_email);
        this.editTextPassword = findViewById(R.id.edit_text_password);
        this.editTextEmail.getEditText().setText((CharSequence) sharedPreferences.getString("email", ""));
        this.editTextPassword.getEditText().setText((CharSequence) sharedPreferences.getString("password", ""));

        this.buttonLogin = findViewById(R.id.button_login);
        this.rememberMeCheckbox = findViewById(R.id.checkbox_remember_me);
        this.rememberMeCheckbox.setChecked(sharedPreferences.getBoolean("checked", false));

        firebaseAuth = FirebaseAuth.getInstance();
        loginApi = RetrofitClient.getRetrofit().create(LoginApi.class);
    }

    private void handleTextViewClick() {
        this.textViewNotYetAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void  handleButtonLoginClick() {
       this.buttonLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
               password = Objects.requireNonNull(editTextPassword.getEditText()).getText().toString();

               if(isValidate()) {
                   loginApi.loginUser(email, password).enqueue(new Callback<MessageDto>() {
                       @Override
                       public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                           if(response.body().getStatus().equals("SUCCESS")) {
                               SharedPreferences.Editor editor = sharedPreferences.edit();
                               if(rememberMeCheckbox.isChecked()) {
                                   editor.putString("email", email);
                                   editor.putString("password", password);
                                   editor.putBoolean("checked", rememberMeCheckbox.isChecked());
                                   editor.commit();
                               } else  {
                                   editor.remove("email");
                                   editor.remove("password");
                                   editor.remove("checked");
                                   editor.commit();
                               }
                               Toast.makeText(LoginActivity.this, "Congratulation! You have successfully login", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(LoginActivity.this, CustomerPanelBottomActivity.class);
                               startActivity(intent);
                               finish();
                           } else {
                               AlertDialogMessage.showAlertMessage(LoginActivity.this, "Verification failed", response.body().getMessage());
                           }
                       }

                       @Override
                       public void onFailure(Call<MessageDto> call, Throwable t) {
                           Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }
       });
    }

    private boolean isValidate() {
//        String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        editTextEmail.setError("");
        editTextPassword.setError("");
        editTextEmail.setErrorEnabled(false);
        editTextPassword.setErrorEnabled(false);

        boolean isEmailValid = false, isPasswordValid = false;

        if(TextUtils.isEmpty(email)) {
            editTextEmail.setErrorEnabled(true);
            Objects.requireNonNull(editTextEmail.getEditText()).setError("Email is required");
        } else {
            if(email.matches(emailPattern)) {
                isEmailValid = true;
            } else {
                editTextEmail.setErrorEnabled(true);
                Objects.requireNonNull(editTextEmail.getEditText()).setError("Email is not valid");
            }
        }

         if(TextUtils.isEmpty(password)) {
            editTextPassword.setErrorEnabled(true);
            Objects.requireNonNull(editTextPassword.getEditText()).setError("Password is required");
        } else  {
           isPasswordValid = true;
        }

         return  isEmailValid && isPasswordValid;
    }


}