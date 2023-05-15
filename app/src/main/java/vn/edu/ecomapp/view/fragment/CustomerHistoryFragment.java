package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.OrderApi;
import vn.edu.ecomapp.dto.Order;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.OrderManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.OrderAdapter;

public class CustomerHistoryFragment extends Fragment {

    RecyclerView orderRecyclerView;
    List<Order> orders;
    OrderApi orderApi;
    TokenManager tokenManager;
    CustomerManager customerManager;
    OrderManager orderManager;
    ProgressDialog pd;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tokenManager = TokenManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
        customerManager = CustomerManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CUSTOMER, Context.MODE_PRIVATE));
        orderManager = OrderManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_ORDER, Context.MODE_PRIVATE));

    }

    private  void initializeComponents(View view) {
        orderRecyclerView = view.findViewById(R.id.orderRecyclerView);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_history, null) ;
       requireActivity().setTitle("Settings");
       return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        pd = new ProgressDialog(getContext());
        pd.setCanceledOnTouchOutside(false);
        orders = new ArrayList<>();
        orderApi = RetrofitClient.createApiWithAuthAndContext(OrderApi.class, tokenManager, requireContext());
        loadOrderRecyclerView();

    }

    private void loadOrderRecyclerView() {
        String customerId = customerManager.getCustomer().getCustomerId();
        pd.setTitle("My Orders");
        pd.setMessage("Loading data, please wait");
        pd.show();
        orderApi.getOrderByCustomerId(customerId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                pd.dismiss();
                Log.d("Order", "Success");
                if(response.body() == null) return;
                orders = response.body();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                orderRecyclerView.setLayoutManager(linearLayoutManager);
                OrderAdapter orderAdapter = new OrderAdapter(requireContext(), orders);
                orderRecyclerView.setAdapter(orderAdapter);
                orderAdapter.setOnItemClickListener((position, view) -> {
                    TextView tvOrderId = view.findViewById(R.id.orderIdValue);
                    if(tvOrderId == null) return;
                    orderManager.saveOrderId(tvOrderId.getText().toString());
                    FragmentManager.nextFragment(requireActivity(), new LineItemFragment());
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                pd.dismiss();
                Log.d("Order", t.getMessage());
            }
        });
    }

}

