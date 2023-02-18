package vn.edu.ecomapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import vn.edu.ecomapp.R;

public class LoginActivity extends AppCompatActivity {

//    Components
    TextView textViewNotYetAccount;
    TextInputLayout editTextEmail, editTextPassword;
    Button buttonLogin;

//    Data
    String email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.initializeComponents();
        this.handleTextViewClick();
        this.handleButtonLoginClick();
    }

    private void initializeComponents() {
        this.textViewNotYetAccount = findViewById(R.id.text_view_not_yet_account);
        this.editTextEmail = findViewById(R.id.edit_text_email);
        this.editTextPassword = findViewById(R.id.edit_text_password);
        this.buttonLogin = findViewById(R.id.button_login);
    }

    private void handleTextViewClick() {
        this.textViewNotYetAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleButtonLoginClick() {
       this.buttonLogin.setOnClickListener(view -> {
           email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
           password = Objects.requireNonNull(editTextPassword.getEditText()).getText().toString();

           if(isValidate()) {
                Intent intent = new Intent(LoginActivity.this, CustomerPanelBottomActivity.class);
                startActivity(intent);
                finish();
           }
       });
    }

    private boolean isValidate() {
        String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
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

         // Call API

         return  isEmailValid && isPasswordValid;

    }


}