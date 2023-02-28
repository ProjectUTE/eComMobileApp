package vn.edu.ecomapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.util.AlertDialogMessage;

public class ForgotPasswordActivity extends AppCompatActivity {

   TextInputLayout editTextEmail;
   Button sendEmailButton;
   String email;

   FirebaseAuth firebaseAuth;

    private  void initializeComponents() {
        this.editTextEmail = findViewById(R.id.edit_text_email);
        this.sendEmailButton = findViewById(R.id.button_send_email);
        email = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeComponents();
        handleSendEmailButtonClick();
    }

    private void handleSendEmailButtonClick() {
        this.sendEmailButton.setOnClickListener(view -> {
            email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
            Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    AlertDialogMessage.showAlertMessage(this, "Send email success", "email has sent, please change your password");
                } else {
                    AlertDialogMessage.showAlertMessage(this, "Send email failed", Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        });
    }
}