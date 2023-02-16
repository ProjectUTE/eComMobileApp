package vn.edu.ecomapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import vn.edu.ecomapp.R;

public class MainMenuActivity extends AppCompatActivity {


//    Components
    Button buttonLogin, buttonSignUp;
    ConstraintLayout constraintLayoutMainMenu;

//    Animations
     Animation zoomIn, zoomOut, move;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiddenTitleBar();
        setContentView(R.layout.activity_main_menu);
        this.initializeComponents();
        this.initializeAnimation();
//        this.setLayoutAnimation();
        this.handleLoginButtonClick();
        this.handleSignUpButtonClick();
    }

    private void initializeComponents() {
        this.buttonLogin = findViewById(R.id.button_login);
        this.buttonSignUp = findViewById(R.id.button_sign_up);
        this.constraintLayoutMainMenu = findViewById(R.id.constraint_layout_main_menu);
    }

    private void initializeAnimation() {
        this.zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        this.zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
    }

      private void hiddenTitleBar() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setLayoutAnimation() {
        this.constraintLayoutMainMenu.setAnimation(this.zoomIn);
        this.constraintLayoutMainMenu.setAnimation(this.zoomOut);
        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                constraintLayoutMainMenu.setAnimation(zoomIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                constraintLayoutMainMenu.setAnimation(zoomOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void handleLoginButtonClick() {
       this.buttonLogin.setOnClickListener(view -> {
           Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
           intent.putExtra("Home", "Login");
           startActivity(intent);
           finish();
       });
    }

    private void handleSignUpButtonClick() {
        this.buttonSignUp.setOnClickListener(view -> {
           Intent intent = new Intent(MainMenuActivity.this, SignUpActivity.class);
           intent.putExtra("Home", "Sign Up");
           startActivity(intent);
           finish();
       });
    }

}