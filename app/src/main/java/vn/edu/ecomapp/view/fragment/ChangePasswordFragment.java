package vn.edu.ecomapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import vn.edu.ecomapp.R;

public class ChangePasswordFragment extends Fragment {

    private Toolbar toolbar;
    TextView appBarTitle;

    ImageView backButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       initComponents(view);
    }

    private void initComponents(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        backButton = view.findViewById(R.id.backButton);

        appBarTitle = view.findViewById(R.id.appBarTitle);
        appBarTitle.setText("Change password");
        backButton.setOnClickListener(view1 -> {
//            FragmentManager.nextFragment(requireActivity(), new CustomerAccountFragment(), R.id.fragmentContainer, null);
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }

}