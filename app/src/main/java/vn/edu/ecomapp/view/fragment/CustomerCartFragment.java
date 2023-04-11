package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
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

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.dto.Cart;
import vn.edu.ecomapp.model.LineItem;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.prefs.CartManager;
import vn.edu.ecomapp.view.adapter.CartAdapter;

public class CustomerCartFragment  extends Fragment {

    RecyclerView recyclerViewCart;
    TextView checkoutButton, tvtotalItems, btnPlus, btnMinus, tvLineItemid;
    CartManager cartManager;
    Cart cart;

    String lineItemId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cartManager = CartManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CART, Context.MODE_PRIVATE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_customer_cart, null) ;
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        loadRecyclerViewCart(view);
        loadSummary();
    }

    private void loadSummary() {
        if(cart == null) return;
        tvtotalItems.setText(CurrencyFormat.VietnameseCurrency(cart.getTotalItems()));
    }

    @SuppressLint("LongLogTag")
    private void initComponents(View view) {
        checkoutButton = view.findViewById(R.id.checkoutButton);
        tvtotalItems = view.findViewById(R.id.totalItem);
        cart = cartManager.getCart();
        checkoutButton.setOnClickListener(view1 -> FragmentManager.nextFragment(requireActivity(), new SelectPaymentFragment()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateCart() {
        cart = cartManager.getCart();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void loadRecyclerViewCart(View view) {
        if(cart == null) return;
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        CartAdapter cartAdapter = new CartAdapter(getContext(), cart.getLineItems());
        recyclerViewCart.setAdapter(cartAdapter);
        cartAdapter.setOnItemClickListener((position, view1) -> {
            btnPlus = view1.findViewById(R.id.button_plus);
            btnMinus = view1.findViewById(R.id.button_minus);
            tvLineItemid = view1.findViewById(R.id.lineItemId);
            lineItemId = tvLineItemid.getText().toString();
            btnPlus.setOnClickListener(view2 -> {
               Log.d("CART", "Plus button");
               if(cart == null) return;
               LineItem itemInCart = null;
               LineItem itemExtra = new LineItem();
               for (LineItem i : cart.getLineItems()) {
                    if(i.getId().equals(lineItemId)) {
                        itemInCart = i;
                        break;
                    }
               }
               if(itemInCart == null) return;
                int quantity = 1;
                int price = itemInCart.getPrice();
                int total = quantity * price;
                itemExtra.setId(lineItemId);
                itemExtra.setQuantity(1);
                itemExtra.setProductId(itemInCart.getProductId());
                itemExtra.setPrice(price);
                itemExtra.setAmount(total);
                cart.addLineItem(itemExtra);
                cartManager.saveCart(cart);
                cartAdapter.notifyDataSetChanged();
                updateCart();
                loadSummary();
            });

            btnMinus.setOnClickListener(view3 -> {
                Log.d("CART", "Minus button");
                cart.removeLineItem(lineItemId);
                cartAdapter.notifyDataSetChanged();
                updateCart();
                loadSummary();
            });
        });
    }
}