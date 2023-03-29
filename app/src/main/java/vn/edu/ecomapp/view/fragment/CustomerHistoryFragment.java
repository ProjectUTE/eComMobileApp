package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.Order;
import vn.edu.ecomapp.view.adapter.OrderAdapter;
import vn.edu.ecomapp.view.adapter.listener.OnItemClickListener;

public class CustomerHistoryFragment extends Fragment {

    RecyclerView orderRecyclerView;
    List<Order> orders;

    private void initData() {
        orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
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
        initData();
        loadOrderRecyclerView();
    }

    private void loadOrderRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderRecyclerView.setLayoutManager(linearLayoutManager);
        OrderAdapter orderAdapter = new OrderAdapter(getContext(), orders);
        orderRecyclerView.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener((position, view) -> goToLineItemsFragment(new LineItemFragment()));
//        orderRecyclerView.addItemDecoration(new SpacesItemDecoration(20));
    }
    private void goToLineItemsFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
             .replace(R.id.fragmentContainer, fragment, "changePasswordFragment")
             .addToBackStack(null)
             .commit();
    }

}

