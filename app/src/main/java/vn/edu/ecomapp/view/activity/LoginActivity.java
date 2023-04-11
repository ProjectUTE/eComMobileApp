package vn.edu.ecomapp.view.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.controller.LoginController;
import vn.edu.ecomapp.dto.login.Login;
import vn.edu.ecomapp.dto.login.LoginRequest;
import vn.edu.ecomapp.services.oauth2.GoogleAuthManager;
import vn.edu.ecomapp.util.constants.DrawablePositionConstants;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.constants.RoleCodeConstants;
import vn.edu.ecomapp.util.prefs.DataLoginManager;

public class LoginActivity extends AppCompatActivity {

    final int REQUEST_CODE = 123;
    // Google
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;

//    Components
    ConstraintLayout googleLoginButton;
    CheckBox rememberMeCheckbox;
    TextView textViewNotYetAccount, textViewForgotPassword;
    TextInputLayout editTextEmail, editTextPassword;
    Button buttonLogin;

//    Data
    String email = "", password = "";
    boolean isGoogleLogin = false;
    DataLoginManager dataLoginManager;
    LoginController loginController;
    ProgressDialog pd;


    private void setIsGoogleLogin(boolean value) {
        isGoogleLogin = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        dataLoginManager = DataLoginManager
                .getInstance(getSharedPreferences(PrefsConstants.DATA_USER_LOGIN, MODE_PRIVATE));
        googleSignInOptions = GoogleAuthManager.getGoogleSignInOptions();
        googleSignInClient = GoogleAuthManager.getGoogleSignInClient(this, googleSignInOptions);
        loginController = new LoginController(this);
        initializeComponents();
    }

    private void loginWithGoogle() {
//            Hiện lên popup chọn account
        Intent signInIntent = googleSignInClient.getSignInIntent();
//            Sau khi đã lựa chọn account
        startActivityForResult(signInIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                loginWithEmailAndPassword();
            }catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeComponents() {
        textViewNotYetAccount = findViewById(R.id.text_view_not_yet_account);
        textViewForgotPassword = findViewById(R.id.text_view_forgot_password);
        rememberMeCheckbox = findViewById(R.id.checkbox_remember_me);
        buttonLogin = findViewById(R.id.button_login);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        googleLoginButton = findViewById(R.id.googleButton);

        // Auto fill input
        Login dataLogin = dataLoginManager.getDataLogin();
        Objects.requireNonNull(editTextEmail.getEditText()).setText(dataLogin.getEmail());
        Objects.requireNonNull(editTextPassword.getEditText()).setText(dataLogin.getPassword());
        rememberMeCheckbox.setChecked(dataLogin.getIsRemember());
        // listen event
        textViewNotYetAccount.setOnClickListener(view -> goToSignUpForm());
        textViewForgotPassword.setOnClickListener(view -> goToForgotPasswordForm());
        googleLoginButton.setOnClickListener(view -> {
            setIsGoogleLogin(true);
            loginWithGoogle();
        });
        buttonLogin.setOnClickListener(v -> {
            if(googleSignInClient != null) googleSignInClient.signOut();
            setIsGoogleLogin(false);
            loginWithEmailAndPassword();
        });
    }

    private void goToSignUpForm() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToForgotPasswordForm() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    private void  loginWithEmailAndPassword() {
           if(!isGoogleLogin) {
               email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
               password = Objects.requireNonNull(editTextPassword.getEditText()).getText().toString();
               boolean isRemember = rememberMeCheckbox.isChecked();
               if (isValidate()) {
                   if (rememberMeCheckbox.isChecked()) {
                       Login dataLogin = new Login(email, password, isRemember);
                       dataLoginManager.saveDataLogin(dataLogin);
                   } else {
                       dataLoginManager.removeDataLogin();
                   }
               }
           } else {
                GoogleSignInAccount account = GoogleAuthManager.getGoogleSignInAccount(this);
                if(account != null) {
                    email = account.getEmail();
                    password = email;
                }
           }

//               Call controller
           LoginRequest loginRequest = new LoginRequest(email, password, isGoogleLogin, RoleCodeConstants.USER_CUSTOMER);
           loginController.handleLogin(loginRequest);
       }

 private boolean isValidate() {
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