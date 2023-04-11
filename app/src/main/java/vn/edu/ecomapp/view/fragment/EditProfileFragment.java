package vn.edu.ecomapp.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.ProfileApi;
import vn.edu.ecomapp.dto.profile.ProfileResponse;
import vn.edu.ecomapp.model.Customer;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.RealPathUtil;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.TokenManager;


public class EditProfileFragment extends Fragment {

    TextView appBarTitle;
    ImageView backButton;
    ImageView ivAvatar;
    Button btnUpload, btnUpdate;
    TextInputLayout tylEmail, tylDisplayName, tylPhonenumber;
    String customerId = "", email = "", displayName = "", phonenumber = "";
    CustomerManager customerManager;
    Customer profile;
    Uri mUri;
    private static final int REQUEST_CODE = 1;
    ProfileApi profileApi;
    TokenManager tokenManager;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appBarTitle = view.findViewById(R.id.appBarTitle);
        appBarTitle.setText("Edit profile");
        tylDisplayName = view.findViewById(R.id.displayName);
        tylEmail = view.findViewById(R.id.email);
        tylPhonenumber = view.findViewById(R.id.phonenumber);
        ivAvatar = view.findViewById(R.id.avatar);
        btnUpload = view.findViewById(R.id.uploadAvatar);
        btnUpdate = view.findViewById(R.id.update);
        backButton = view.findViewById(R.id.backButton);
        profileApi = RetrofitClient.createApiWithAuth(ProfileApi.class, tokenManager);

        profile = customerManager.getCustomer();
        Objects.requireNonNull(tylPhonenumber.getEditText()).setText(profile.getPhonenumber());
        Objects.requireNonNull(tylEmail.getEditText()).setText(profile.getEmail());
        Objects.requireNonNull(tylDisplayName.getEditText()).setText(profile.getDisplayName());
        Glide.with(requireContext())
            .load(profile.getAvatar())
            .apply(RequestOptions.bitmapTransform(new RoundedCorners(500)))
            .into(ivAvatar);

        btnUpload.setOnClickListener(view1 -> checkPermission(requireContext()));
        btnUpdate.setOnClickListener(view2 -> updateProfile());
        backButton.setOnClickListener(view1 -> FragmentManager.backFragment(requireActivity()));

    }

    private void updateProfile() {
        customerId = profile.getCustomerId();
        email = profile.getEmail();
        displayName = Objects.requireNonNull(tylDisplayName.getEditText()).getText().toString();
        phonenumber = Objects.requireNonNull(tylPhonenumber.getEditText()).getText().toString();
        String address = "null";

        String FORM_DATA = "multipart/form-data";
        RequestBody rbId = RequestBody.create(MediaType.parse(FORM_DATA), customerId);
        RequestBody rbPhonenumber = RequestBody.create(MediaType.parse(FORM_DATA), phonenumber);
        RequestBody rbDisplayName = RequestBody.create(MediaType.parse(FORM_DATA), displayName);
        RequestBody rbAddress = RequestBody.create(MediaType.parse(FORM_DATA), address);
        if(mUri == null)  {
            Toast.makeText(getContext(), "Please, Upload new avatar", Toast.LENGTH_SHORT).show();
            return;
        }
        String IMAGE_PATH = RealPathUtil.getRealPath(requireContext(), mUri);
        File file = new File(IMAGE_PATH);
        RequestBody rbAvatar = RequestBody.create(MediaType.parse(FORM_DATA), file);
        MultipartBody.Part partBodyAvatar = MultipartBody.Part.createFormData("avatar", file.getName(), rbAvatar);


        profileApi.updateProfile(rbId, partBodyAvatar, rbDisplayName, rbPhonenumber, rbAddress)
                .enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileResponse> call, @NonNull Response<ProfileResponse> response) {
                assert response.body() != null;
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                FragmentManager.backFragment(requireActivity());
            }

            @Override
            public void onFailure(@NonNull Call<ProfileResponse> call, @NonNull Throwable t) {
                Log.d("UPDATE profile", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
     private void checkPermission(Context context) {
        if(context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissions(permissions(), REQUEST_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));
    }

      public static String[] permissions() {
        String[] p;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) p = storage_permissions_33;
        else p = storage_permissions;
        return p;
    }
    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("Profile Tag", "OnActivityResult");
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            ivAvatar.setImageBitmap(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri));
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("UPLOAD", e.getMessage());
                        }
                    }
                }
            }
    );

      public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
    };


}