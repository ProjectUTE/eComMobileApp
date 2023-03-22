package vn.edu.ecomapp.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import vn.edu.ecomapp.R;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textViewDesignBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        hiddenTitleBar();
        setContentView(R.layout.activity_main);
        initializeComponents();
        initializeAnimate();
        handler();
    }

    private void hiddenTitleBar() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

//    Switch activity
    private  void handler() {
        int delay = 3000;
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }, delay);
    }

    private void initializeAnimate() {

        this.imageView.animate().alpha(0f).setDuration(0);
        this.textViewDesignBy.animate().alpha(0f).setDuration(0);
        this.imageView.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                textViewDesignBy.animate().alpha(1f).setDuration(800);
            }
        });

    }

    private  void initializeComponents() {
        this.imageView = findViewById(R.id.image_view);
        this.textViewDesignBy = findViewById(R.id.text_view_design_by);
    }
}