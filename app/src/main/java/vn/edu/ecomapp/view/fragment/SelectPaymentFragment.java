package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.dto.PaymentMethod;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.prefs.PaymentManager;
import vn.edu.ecomapp.view.adapter.PaymentAdapter;

public class SelectPaymentFragment extends Fragment {

    TextView appBarTitle;
    ImageView backButton;
    RecyclerView rcvPayment;
    List<PaymentMethod> paymentMethods;
    PaymentManager paymentManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        paymentManager = PaymentManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_PAYMENT, Context.MODE_PRIVATE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_payment, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarTitle = view.findViewById(R.id.appBarTitle);
        appBarTitle.setText("Payment method");
        rcvPayment = view.findViewById(R.id.rcvPayment);
        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(view1 -> FragmentManager.backFragment(requireActivity()));

        paymentMethods = new ArrayList<>();
        PaymentMethod p1 = new PaymentMethod("1", "Payment on delivery");
        PaymentMethod p2 = new PaymentMethod("2", "PayPal");
        paymentMethods.add(p1);
        paymentMethods.add(p2);

        PaymentAdapter adapter = new PaymentAdapter(getContext(), paymentMethods);
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvPayment.setAdapter(adapter);
        rcvPayment.setLayoutManager(layout);
        adapter.setOnItemClickListener((position, view12) ->{
            TextView tvId = view12.findViewById(R.id.tvId);
            if (tvId == null) return;
            paymentManager.savePaymentId(tvId.getText().toString());
             FragmentManager.nextFragment(requireActivity(), new CheckoutFragment());
        });
    }
}