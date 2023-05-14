package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.ProfileApi;
import vn.edu.ecomapp.dto.Customer;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.services.oauth2.GoogleAuthManager;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.constants.UrlConstants;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.activity.LoginActivity;

public class CustomerAccountFragment extends Fragment {

    TextView tvPhone, tvCustomerId;
    Button changePasswordButton, editProfileButton, logoutButton;
    TextView email, displayName;
    ImageView avatar;
    GoogleSignInClient gsc;
    GoogleSignInAccount account;
    CustomerManager customerManager;
    TokenManager tokenManager;
    Customer profile;
    ProfileApi profileApi;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        customerManager = CustomerManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CUSTOMER, Context.MODE_PRIVATE));
        tokenManager = TokenManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
    }

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
        profileApi.geCustomerById(customerManager.getCustomer().getCustomerId())
                        .enqueue(new Callback<Customer>() {
                            @Override
                            public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
                                if(response.body() == null) return;
                                profile = response.body();
                                String avatarStr = "";
                                if(profile.getAvatar() == null || profile.getAvatar().equals("")) {
                                    if(account == null) avatarStr = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
                                    else {
                                        avatarStr = Objects.requireNonNull(account.getPhotoUrl()).toString();
                                    }
                                }
                                else {
                                    avatarStr = profile.getAvatar().replace(UrlConstants.BASE_URL_LOCAL, UrlConstants.BASE_URL);
                                }

                                profile.setAvatar(avatarStr);
                                customerManager.removeCustomer();
                                customerManager.saveCustomer(profile);
                                displayName.setText(profile.getDisplayName());
                                email.setText(profile.getEmail());
                                tvCustomerId.setText(profile.getCustomerId());
                                profile.setAvatar(profile.getAvatar());
                                 Glide.with(requireContext())
                                    .load(profile.getAvatar())
                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(1000)))
                                    .into(avatar);
                            }

                            @Override
                            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {

                            }
                        });

    }

    private void initGoogleService() {
         gsc = GoogleAuthManager.getGoogleSignInClient(getActivity(), GoogleAuthManager.getGoogleSignInOptions());
         account = GoogleAuthManager.getGoogleSignInAccount(getContext());
    }

    @SuppressLint("SetTextI18n")
    private void initComponents(View view) {
         // get element

        email = view.findViewById(R.id.text_view_email);
        displayName = view.findViewById(R.id.text_view_fullname);
        avatar = view.findViewById(R.id.image_view_avatar);
        tvCustomerId = view.findViewById(R.id.customerId);
        tvPhone = view.findViewById(R.id.phone);
        changePasswordButton = view.findViewById(R.id.changePassword);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        profileApi = RetrofitClient.createApiWithAuthAndContext(ProfileApi.class, tokenManager, requireContext());

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
        customerManager.removeCustomer();
        if(gsc != null) {
            gsc.signOut().addOnCompleteListener(task -> goToLoginForm());
        }
    }

    private void goToLoginForm() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager.nextFragment(requireActivity(), fragment);
    }
}
