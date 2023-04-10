package vn.edu.ecomapp.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.LineItem;
import vn.edu.ecomapp.util.Constants;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.prefs.CartManager;
import vn.edu.ecomapp.view.adapter.CheckoutAdapter;

public class CheckoutFragment extends Fragment {

    CartManager cartManager;
    List<LineItem> lineItems;
    RecyclerView rcvCheckout;

    TextView appBarTitle;
    ImageView backButton;
    TextView placeOrderBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cartManager = CartManager
                .getInstance(requireActivity().getSharedPreferences(Constants.DATA_CART, Context.MODE_PRIVATE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        placeOrderBtn = view.findViewById(R.id.checkoutButton);
        rcvCheckout = view.findViewById(R.id.rcvItems);
        appBarTitle = view.findViewById(R.id.appBarTitle);
        appBarTitle.setText("Place Order");
        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(view1 -> FragmentManager.backFragment(requireActivity()));
        placeOrderBtn.setVisibility(View.INVISIBLE);

        if(cartManager == null) return;
        lineItems = cartManager.getCart().getLineItems();
        CheckoutAdapter adapter = new CheckoutAdapter(getContext(), lineItems);
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvCheckout.setAdapter(adapter);
        rcvCheckout.setLayoutManager(layout);
    }
}