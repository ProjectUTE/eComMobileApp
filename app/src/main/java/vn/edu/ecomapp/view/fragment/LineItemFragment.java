package vn.edu.ecomapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.dto.LineItem;
import vn.edu.ecomapp.view.adapter.LineItemAdapter;

public class LineItemFragment extends Fragment {

    RecyclerView lineItemRecyclerView;
    List<LineItem> lineItems;

    private void initData() {
        lineItems = new ArrayList<>();
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
        lineItems.add(new LineItem());
    }

    private  void initializeComponents(View view) {
        lineItemRecyclerView = view.findViewById(R.id.lineItemRecyclerView);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        initData();
        loadLineItemRecyclerView();
    }

    private void loadLineItemRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lineItemRecyclerView.setLayoutManager(linearLayoutManager);
        LineItemAdapter lineItemAdapter = new LineItemAdapter(getContext(), lineItems);
        lineItemRecyclerView.setAdapter(lineItemAdapter);
    }
}