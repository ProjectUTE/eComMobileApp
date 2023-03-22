package vn.edu.ecomapp.view.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.view.fragment.CustomerAccountFragment;
import vn.edu.ecomapp.view.fragment.CustomerCartFragment;
import vn.edu.ecomapp.view.fragment.CustomerFoodFragment;
import vn.edu.ecomapp.view.fragment.CustomerHomeFragment;
import vn.edu.ecomapp.view.fragment.CustomerSettingsFragment;

public class PanelActivity extends AppCompatActivity implements OnScrollListenerMain {

    BottomNavigationView bottomNavigationView;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    private void initView() {
        bottomAppBar = findViewById(R.id.bottomAppBar);
        fab = findViewById(R.id.fabCart);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setBackground(null);
        fab.setOnClickListener(view -> loadFragment(new CustomerCartFragment()));
        loadFragment(new CustomerHomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch(item.getItemId()) {
                case R.id.account:
                    fragment = new CustomerAccountFragment();
                    break;

                case  R.id.setting:
                    fragment = new CustomerSettingsFragment();
                    break;

                case  R.id.food:
                    fragment = new CustomerFoodFragment();
                    break;

                case  R.id.home:
                    fragment = new CustomerHomeFragment();
                    break;
                case  R.id.cart:
                    fragment = new CustomerCartFragment();
                    break;
            }
            return  loadFragment(fragment);
        });

        findViewById(R.id.scrollViewContainer).setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if(i1 == 0) {
                fabAndBottomAppBarShow();
            } else  {
                fabAndBottomAppBarHide();
            }
        });
    }

     private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            return  true;
        }
        return  false;
    }

    @Override
    public void fabAndBottomAppBarHide() {
        fab.hide();
        fab.setVisibility(View.VISIBLE);
        int height = bottomAppBar.getHeight();

        //Hide
        bottomAppBar.clearAnimation();
        bottomAppBar.animate().translationY(height).setDuration(200);
    }

    @Override
    public void fabAndBottomAppBarShow() {
        fab.show();
        fab.setVisibility(View.VISIBLE);
        // Show
        bottomAppBar.clearAnimation();
        bottomAppBar.animate().translationY(0).setDuration(200);
    }
}

interface OnScrollListenerMain {
    void fabAndBottomAppBarHide();
    void fabAndBottomAppBarShow();
}