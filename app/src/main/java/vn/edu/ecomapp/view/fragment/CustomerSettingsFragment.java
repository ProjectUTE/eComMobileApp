package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import vn.edu.ecomapp.R;
import vn.edu.ecomapp.view.activity.MainActivity;

public class CustomerSettingsFragment extends Fragment {

    Button logoutButton;


    private  void initializeComponents(View view) {
        this.logoutButton = view.findViewById(R.id.button_logout);
    }

    private void handleLogout() {
        this.logoutButton.setOnClickListener(view -> {

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_setting, null) ;
       requireActivity().setTitle("Settings");
       return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        handleLogout();
    }
}

