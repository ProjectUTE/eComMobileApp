package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.services.oauth2.GoogleAuthManager;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.view.activity.LoginActivity;

public class CustomerAccountFragment extends Fragment {

    Button changePasswordButton, editProfileButton, logoutButton;
    TextView email, displayName;
    ImageView avatar;

    GoogleSignInClient gsc;
    GoogleSignInAccount account;


     @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_account, null) ;
       return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        initGoogleService();
        loadInfo();
    }

    private void loadInfo() {
         if(account != null) {
             email.setText(account.getEmail());
             displayName.setText(account.getDisplayName());
        Uri photoUrl = account.getPhotoUrl();
        if(photoUrl != null)
            Glide.with(requireContext())
                    .load(photoUrl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
                    .into(avatar);
         }
    }

    private void initGoogleService() {
         gsc = GoogleAuthManager.getGoogleSignInClient(getActivity(), GoogleAuthManager.getGoogleSignInOptions());
         account = GoogleAuthManager.getGoogleSignInAccount(getContext());
    }

    private void initComponents(View view) {
         // get element
        email = view.findViewById(R.id.text_view_email);
        displayName = view.findViewById(R.id.text_view_fullname);
        avatar = view.findViewById(R.id.image_view_avatar);
        changePasswordButton = view.findViewById(R.id.changePassword);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);

//         Handle click event
         changePasswordButton.setOnClickListener(view1 -> {
             ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
             loadFragment(changePasswordFragment);
         });

         editProfileButton.setOnClickListener(view2 -> {
             EditProfileFragment editProfileFragment = new EditProfileFragment();
             loadFragment(editProfileFragment);
         });

         logoutButton.setOnClickListener(view3 -> signOut());
    }

    private void signOut() {
        if(gsc != null)
            gsc.signOut().addOnCompleteListener(task -> goToLoginForm());
    }

    private void goToLoginForm() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager.nextFragment(requireActivity(), fragment, R.id.fragmentContainer, null);
    }
}
