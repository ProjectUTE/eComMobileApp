package vn.edu.ecomapp.view.activity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.view.fragment.CustomerAccountFragment;
import vn.edu.ecomapp.view.fragment.CustomerCartFragment;
import vn.edu.ecomapp.view.fragment.CustomerFoodFragment;
import vn.edu.ecomapp.view.fragment.CustomerHistoryFragment;
import vn.edu.ecomapp.view.fragment.CustomerHomeFragment;

public class PanelActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    Integer menuItemSelected = R.id.home;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        initView();

        fab.setOnClickListener(view -> {
            // Set change appearance
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
            fab.getDrawable().mutate().setTint(getResources().getColor(R.color.white));
            loadFragment(new CustomerCartFragment());
            if(menuItemSelected != null) {
                bottomNavigationView.getMenu().findItem(menuItemSelected).setCheckable(false);
            }
        });

        fab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BadgeDrawable badgeDrawable = BadgeDrawable.create(PanelActivity.this);
                badgeDrawable.setNumber(2);
                //Important to change the position of the Badge
                badgeDrawable.setHorizontalOffset(30);
                badgeDrawable.setVerticalOffset(20);

                fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    private void initView() {
        fab = findViewById(R.id.fabCart);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setBackground(null);

        loadFragment(new CustomerHomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch(item.getItemId()) {
                case  R.id.home:
                    menuItemSelected = R.id.home;
                    fragment = new CustomerHomeFragment();
                    break;
                case  R.id.food:
                    menuItemSelected = R.id.food;
                    fragment = new CustomerFoodFragment();
                    break;
                case  R.id.history:
                    menuItemSelected = R.id.history;
                    fragment = new CustomerHistoryFragment();
                    break;
                case R.id.account:
                    menuItemSelected = R.id.account;
                    fragment = new CustomerAccountFragment();
                    break;
            }
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fab.getDrawable().mutate().setTint(getResources().getColor(R.color.orange));
            if(menuItemSelected != null)
                bottomNavigationView.getMenu().findItem(menuItemSelected).setCheckable(true);
            return  loadFragment(fragment);
        });
    }

     private boolean loadFragment(Fragment fragment) {
        if(fragment == null) return false;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,
                        R.anim.enter_right_to_left, R.anim.exit_right_to_left)
            .replace(R.id.fragmentContainer, fragment, null)
            .addToBackStack(null)
            .commit();
        return  true;
    }
}
