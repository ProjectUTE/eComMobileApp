package vn.edu.ecomapp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import vn.edu.ecomapp.R;

public class SignUpActivity extends AppCompatActivity {

   TextView textViewAlreadyAccount;
   TextInputLayout editTextEmail, editTextPassword, editTextConfirmPassword;
   Button buttonSignUp;

   String email = "", password = "", confirmPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.initializeComponents();
        this.handleTextViewClick();
        this.handleButtonSignUpClick();
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
        this.buttonSignUp.setOnClickListener(view -> {
           email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
           password = Objects.requireNonNull(editTextPassword.getEditText()).getText().toString();
           confirmPassword = Objects.requireNonNull(editTextConfirmPassword.getEditText()).getText().toString();

           if(isValid()) {
               final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
               progressDialog.setCancelable(false);
               progressDialog.setCanceledOnTouchOutside(false);
               progressDialog.setMessage("Registration in progress, please wait...");
                progressDialog.show();
                // Call API
               CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                   @Override
                   public void onTick(long l) {

                   }

                   @Override
                   public void onFinish() {
                       progressDialog.dismiss();
                       Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                       startActivity(intent);
                       finish();
                   }
               };
               countDownTimer.start();
           }
        });
    }

    private boolean isValid() {
        String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
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