package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import vn.edu.ecomapp.model.Order;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.OrderAdapter;

public class CustomerHistoryFragment extends Fragment {

    RecyclerView orderRecyclerView;
    List<Order> orders;
    OrderApi orderApi;
    TokenManager tokenManager;
    CustomerManager customerManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tokenManager = TokenManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
        customerManager = CustomerManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CUSTOMER, Context.MODE_PRIVATE));

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
        orders = new ArrayList<>();
        orderApi = RetrofitClient.createApiWithAuth(OrderApi.class, tokenManager) ;
        loadOrderRecyclerView();

    }

    private void loadOrderRecyclerView() {
        String customerId = customerManager.getCustomer().getCustomerId();
        orderApi.getOrderByCustomerId(customerId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                Log.d("Order", "Success");
                if(response.body() == null) return;
                orders = response.body();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                orderRecyclerView.setLayoutManager(linearLayoutManager);
                OrderAdapter orderAdapter = new OrderAdapter(getContext(), orders);
                orderRecyclerView.setAdapter(orderAdapter);
                orderAdapter.setOnItemClickListener((position, view) -> FragmentManager.nextFragment(requireActivity(), new LineItemFragment()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                Log.d("Order", t.getMessage());
            }
        });
    }

}

