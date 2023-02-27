package vn.edu.ecomapp.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.util.AlertDialogMessage;

public class SignUpActivity extends AppCompatActivity {

   TextView textViewAlreadyAccount;
   TextInputLayout editTextEmail, editTextPassword, editTextConfirmPassword;
   Button buttonSignUp;
   String email = "", password = "", confirmPassword = "";
   FirebaseAuth firebaseAuth;
   DatabaseReference databaseReference;

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
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance("https://ecomappbe-e99b7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
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

              // Check email exist
               firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                   progressDialog.dismiss();
                  if (Objects.requireNonNull(task.getResult().getSignInMethods()).size() == 0){
                      // email not exits
                      firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task1 -> {
                          if(task1.isSuccessful()) {
                              // Create User table
                              String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                              final HashMap<String, String> hashMapUser = new HashMap<>();
                              hashMapUser.put("role", "customer");
                              databaseReference.child("Users").child(userId).setValue(hashMapUser).addOnCompleteListener(task2 -> {
                                  if(task2.isSuccessful()) {
                                      // Create customer table
                                      final  HashMap<String, String> customerInfo = new HashMap<>();
                                      customerInfo.put("email", email);
                                      customerInfo.put("password", password);
                                      databaseReference.child("Customers").child(userId).setValue(customerInfo).addOnCompleteListener(task3 -> {
                                            if(task3.isSuccessful()) {
                                                // Send email verification
                                               firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task4 -> {
                                                    if(task4.isSuccessful())  {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                                        builder.setCancelable(false);
                                                        builder.setTitle("Registration success");
                                                        builder.setMessage("You have registered. Please, Verify your email to continue");
                                                        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                                                            dialogInterface.dismiss();
                                                            // Switch to Login form
                                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        });
                                                        builder.create().show();
                                                    } else {
                                                        AlertDialogMessage.showAlertMessage(this, "Send email failed", Objects.requireNonNull(task4.getException()).getMessage());
                                                    }
                                               });
                                            } else {
                                                AlertDialogMessage.showAlertMessage(this, "Create customer failed", Objects.requireNonNull(task3.getException()).getMessage());
                                            }
                                      });
                                  } else  {
                                      AlertDialogMessage.showAlertMessage(this, "Create user failed", Objects.requireNonNull(task2.getException()).getMessage());
                                  }
                              });
                          } else {
                              AlertDialogMessage.showAlertMessage(this, "Authentication failed", Objects.requireNonNull(task1.getException()).getMessage());
                          }
                      });
                  }else {
                      // email exits
                      AlertDialogMessage.
                              showAlertMessage(this, "Registration failed!", "Email already exits. Please, choose another email");
                  }
               });

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