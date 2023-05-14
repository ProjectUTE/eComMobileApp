package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.PaymentMethodApi;
import vn.edu.ecomapp.dto.payment.PaymentMethodResponse;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.PaymentManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.PaymentAdapter;

public class SelectPaymentFragment extends Fragment {
    private final String TAG = SelectPaymentFragment.class.getName();
    TextView appBarTitle;
    ImageView backButton;
    RecyclerView rcvPayment;
    List<PaymentMethodResponse> paymentMethods;
    PaymentManager paymentManager;
    TokenManager tokenManager;
    PaymentMethodApi methodApi;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        paymentManager = PaymentManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_PAYMENT, Context.MODE_PRIVATE));
        tokenManager = TokenManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
        methodApi = RetrofitClient.createApiWithAuthAndContext(PaymentMethodApi.class, tokenManager, requireContext());
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

        methodApi.getAllPaymentMethods().enqueue(new Callback<List<PaymentMethodResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<PaymentMethodResponse>> call, @NonNull Response<List<PaymentMethodResponse>> response) {
                if(response.isSuccessful()) {
                    if (response.body() == null) return;
                    paymentMethods = response.body();
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

            @Override
            public void onFailure(@NonNull Call<List<PaymentMethodResponse>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });



    }
}