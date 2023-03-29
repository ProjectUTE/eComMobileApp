package vn.edu.ecomapp.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import vn.edu.ecomapp.R;

public class FragmentManager  {
    public static void nextFragment(FragmentActivity requireActivity, Fragment fragment, int fragmentContainer, String tag) {
        if(requireActivity == null) return;
        if(tag == null) tag = "";
        requireActivity.getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
             .replace(fragmentContainer, fragment, tag)
             .addToBackStack(null)
             .commit();
    }

}
