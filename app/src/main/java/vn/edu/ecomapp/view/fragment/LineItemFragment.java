package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.R;
import vn.edu.ecomapp.api.LineItemApi;
import vn.edu.ecomapp.dto.LineItem;
import vn.edu.ecomapp.dto.lineitem.LineItemResponse;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.OrderManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.adapter.LineItemAdapter;

public class LineItemFragment extends Fragment {

    TextView appBarTitle;
    ImageView backButton;
    RecyclerView lineItemRecyclerView;
    List<LineItemResponse> lineItems;
    LineItemApi lineItemApi;
    OrderManager orderManager;
    TokenManager tokenManager;
    ProgressDialog pd;

    private void initData() {
        lineItems = new ArrayList<>();
        String orderId = orderManager.getOrderId();
        if(orderId.isEmpty()) {
            return;
        }
        pd.setTitle("Order detail");
        pd.setMessage("Loading data, please wait");
        pd.show();
        lineItemApi.getLineItemByOrderId(orderId).enqueue(new Callback<List<LineItemResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<LineItemResponse>> call, @NonNull Response<List<LineItemResponse>> response) {
                pd.dismiss();
                lineItems = response.body();
                if(lineItems == null) {
                    Log.d("LINEITEM", "NULL");
                    return;
                } else {
                    Log.d("LINEITEM", Integer.toString(lineItems.size()));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    lineItemRecyclerView.setLayoutManager(linearLayoutManager);
                    LineItemAdapter lineItemAdapter = new LineItemAdapter(getContext(), lineItems);
                    lineItemRecyclerView.setAdapter(lineItemAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<LineItemResponse>> call, Throwable t) {

            }
        });
    }

    private  void initializeComponents(View view) {
        lineItemRecyclerView = view.findViewById(R.id.lineItemRecyclerView);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tokenManager = TokenManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
        orderManager = OrderManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_ORDER, Context.MODE_PRIVATE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line_item, container, false);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        pd = new ProgressDialog(getContext());
        pd.setCanceledOnTouchOutside(false);
        lineItemApi = RetrofitClient.createApiWithAuthAndContext(LineItemApi.class, tokenManager, requireContext());
        appBarTitle = view.findViewById(R.id.appBarTitle);
        appBarTitle.setText("Order detail");
        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(view1 -> FragmentManager.backFragment(requireActivity()));
        initData();
    }

}