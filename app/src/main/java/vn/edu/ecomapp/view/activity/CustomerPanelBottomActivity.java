package vn.edu.ecomapp.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.view.fragment.CustomerAccountFragment;
import vn.edu.ecomapp.view.fragment.CustomerFoodFragment;
import vn.edu.ecomapp.view.fragment.CustomerHomeFragment;
import vn.edu.ecomapp.view.fragment.CustomerSettingsFragment;

public class CustomerPanelBottomActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_panel_bottom);
        BottomNavigationView navigationView = findViewById(R.id.customer_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        }
        return  loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, fragment).commit();
            return  true;
        }
        return  false;
    }
}