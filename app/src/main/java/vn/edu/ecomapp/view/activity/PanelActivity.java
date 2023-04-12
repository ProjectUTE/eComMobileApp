package vn.edu.ecomapp.view.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.room.database.CartDatabase;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.view.fragment.CustomerAccountFragment;
import vn.edu.ecomapp.view.fragment.CustomerCartFragment;
import vn.edu.ecomapp.view.fragment.CustomerFoodFragment;
import vn.edu.ecomapp.view.fragment.CustomerHistoryFragment;
import vn.edu.ecomapp.view.fragment.CustomerHomeFragment;

public class PanelActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    CustomerManager customerManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        customerManager = CustomerManager
                .getInstance(getSharedPreferences(PrefsConstants.DATA_CUSTOMER, MODE_PRIVATE));
        initView();
        String cartId = customerManager.getCustomer().getCustomerId();
        int count = CartDatabase.getInstance(PanelActivity.this).cartItemDao().getItemsCount(cartId);
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.cart);
        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.orange));
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(count);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    private void initView() {
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setBackground(null);
        loadFragment(new CustomerHomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch(item.getItemId()) {
                case  R.id.home:
                    fragment = new CustomerHomeFragment();
                    break;
                case  R.id.food:
                    fragment = new CustomerFoodFragment();
                    break;
                case R.id.cart:
                   fragment = new CustomerCartFragment();
                   break;
                case  R.id.history:
                    fragment = new CustomerHistoryFragment();
                    break;
                case R.id.account:
                    fragment = new CustomerAccountFragment();
                    break;
            }
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
