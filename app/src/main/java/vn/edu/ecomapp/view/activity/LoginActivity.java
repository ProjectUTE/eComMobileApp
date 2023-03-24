package vn.edu.ecomapp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.LoginApi;
import vn.edu.ecomapp.oauth2.GoogleAuthManager;
import vn.edu.ecomapp.retrofit.RetrofitClient;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getName();

    // Google
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;

//    Components
    ImageView googleLoginButton;
    CheckBox rememberMeCheckbox;
    TextView textViewNotYetAccount, textViewForgotPassword;
    TextInputLayout editTextEmail, editTextPassword;
    Button buttonLogin;

//    Data
    String email = "", password = "";
    SharedPreferences sharedPreferences;
    LoginApi loginApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
     // Get data = {username, password} stored
        getSharePreferences();
//        Init API
        initGoogleService();
        initLoginApi();

//        Init Component
        initializeComponents();

//        Go to another activity if user click
        goToForgotPasswordForm();
        goToSignUpForm();

        // Get data = {username, password} stored
        getSharePreferences();

//        Handle Login => Normal or login with google Service
        loginNormal();
        loginWithGoogle();

    }

    private void loginWithGoogle() {
        googleLoginButton.setOnClickListener(view -> {
//            Hiện lên popup chọn account
            Intent signInIntent = googleSignInClient.getSignInIntent();
//            Sau khi đã lựa chọn account
            final int requestCode = 1000;
            startActivityForResult(signInIntent, requestCode);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                goToPanelActivity();
            }catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initLoginApi() {
        loginApi = RetrofitClient.getRetrofit().create(LoginApi.class);
    }

    private void initGoogleService() {
        googleSignInOptions = GoogleAuthManager.getGoogleSignInOptions();
        googleSignInClient = GoogleAuthManager.getGoogleSignInClient(this, googleSignInOptions);
    }

    private void getSharePreferences() {
        this.sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
    }

    private void initializeComponents() {
        textViewNotYetAccount = findViewById(R.id.text_view_not_yet_account);
        textViewForgotPassword = findViewById(R.id.text_view_forgot_password);
        rememberMeCheckbox = findViewById(R.id.checkbox_remember_me);
        buttonLogin = findViewById(R.id.button_login);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        googleLoginButton = findViewById(R.id.googleButton);

//        Get Data
        Objects.requireNonNull(this.editTextEmail.getEditText()).setText(sharedPreferences.getString("email", ""));
        Objects.requireNonNull(this.editTextPassword.getEditText()).setText(sharedPreferences.getString("password", ""));
//        if(sharedPreferences.contains("checked")) {
//            this.rememberMeCheckbox.setChecked(sharedPreferences.getBoolean("checked", false));
//        }

    }

    private void goToSignUpForm() {
        this.textViewNotYetAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void goToForgotPasswordForm() {
        this.textViewForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void goToPanelActivity() {
        Intent intent = new Intent(LoginActivity.this, PanelActivity.class);
        startActivity(intent);
        finish();
    }

    private void  loginNormal() {
       this.buttonLogin.setOnClickListener(v -> {
           email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
           password = Objects.requireNonNull(editTextPassword.getEditText()).getText().toString();

           if(isValidate()) {
               SharedPreferences.Editor editor = sharedPreferences.edit();
                           if(rememberMeCheckbox.isChecked()) {
                               editor.putString("email", email);
                               editor.putString("password", password);
                               editor.putBoolean("checked", rememberMeCheckbox.isChecked());
                           } else  {
                               editor.remove("email");
                               editor.remove("password");
                               editor.remove("checked");
                           }
                           editor.apply();

                           // Go to home activity
                           Toast.makeText(LoginActivity.this, "Congratulation! You have successfully login", Toast.LENGTH_SHORT).show();
                            goToPanelActivity();
//               loginApi.loginUser(email, password).enqueue(new Callback<MessageDto>() {
//                   @Override
//                   public void onResponse(@NonNull Call<MessageDto> call, @NonNull Response<MessageDto> response) {
//                       assert response.body() != null;
//                       if(response.body().getStatus().equals("SUCCESS")) {
//                           SharedPreferences.Editor editor = sharedPreferences.edit();
//                           if(rememberMeCheckbox.isChecked()) {
//                               editor.putString("email", email);
//                               editor.putString("password", password);
//                               editor.putBoolean("checked", rememberMeCheckbox.isChecked());
//                           } else  {
//                               editor.remove("email");
//                               editor.remove("password");
//                               editor.remove("checked");
//                           }
//                           editor.apply();
//
//                           // Go to home activity
//                           Toast.makeText(LoginActivity.this, "Congratulation! You have successfully login", Toast.LENGTH_SHORT).show();
//                           Intent intent = new Intent(LoginActivity.this, CustomerPanelBottomActivity.class);
//                           startActivity(intent);
//                           finish();
//                       } else {
//                           AlertDialogMessage.showAlertMessage(LoginActivity.this, "Verification failed", response.body().getMessage());
//                       }
//                   }
//
//                   @Override
//                   public void onFailure(@NonNull Call<MessageDto> call, @NonNull Throwable t) {
//                       Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                       Log.d(TAG, t.getMessage());
//                   }
//               });
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