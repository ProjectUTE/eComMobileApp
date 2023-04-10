package vn.edu.ecomapp.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import vn.edu.ecomapp.R;

public class FragmentManager  {
    public static void nextFragment(FragmentActivity requireActivity, Fragment fragment) {
        if(requireActivity == null) return;
        requireActivity.getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
             .replace(R.id.fragmentContainer, fragment, null)
             .addToBackStack(null)
             .commit();
    }

    public static void backFragment(FragmentActivity requireActivity) {
        if(requireActivity == null) return;
        requireActivity.getSupportFragmentManager().popBackStack();
    }


}
