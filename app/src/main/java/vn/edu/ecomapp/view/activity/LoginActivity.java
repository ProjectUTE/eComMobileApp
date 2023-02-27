package vn.edu.ecomapp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import vn.edu.ecomapp.R;
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
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage((CharSequence) "Sign in, please wait...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if(task.isSuccessful()) {
                        if(Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()) {
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
                            Toast.makeText(this, "Congratulation! You have successfully login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, CustomerPanelBottomActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialogMessage.showAlertMessage(this, "Verification failed", "You have not verified your email! Please, verify to continue...");
                        }
                    } else {
                        AlertDialogMessage.showAlertMessage(this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
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

         return  isEmailValid && isPasswordValid;
    }


}