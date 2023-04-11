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
import vn.edu.ecomapp.room.database.CartDatabase;
import vn.edu.ecomapp.room.entities.CartItem;
import vn.edu.ecomapp.room.entities.CartWithCartItem;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.view.adapter.CartAdapter;

public class CustomerCartFragment  extends Fragment {

    RecyclerView recyclerViewCart;
    TextView checkoutButton, tvtotalItems, btnPlus, btnMinus, tvProductId;
    CustomerManager customerManager;
    CartAdapter cartAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        customerManager = CustomerManager
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
        String customerId = customerManager.getCustomer().getCustomerId();
        int itemsTotal = CartDatabase.getInstance(getContext()).cartItemDao().getItemsTotal(customerId);
        tvtotalItems.setText(CurrencyFormat.VietnameseCurrency(itemsTotal));
    }

    @SuppressLint("LongLogTag")
    private void initComponents(View view) {
        checkoutButton = view.findViewById(R.id.checkoutButton);
        tvtotalItems = view.findViewById(R.id.totalItem);
        checkoutButton.setOnClickListener(view1 -> FragmentManager.nextFragment(requireActivity(), new SelectPaymentFragment()));
        cartAdapter = new CartAdapter();
        cartAdapter.setContext(getContext());
        cartAdapter.setOnItemClickListener((position, view1) -> {
            btnPlus = view1.findViewById(R.id.button_plus);
            btnMinus = view1.findViewById(R.id.button_minus);
            tvProductId = view1.findViewById(R.id.productId);
            String cartId = customerManager.getCustomer().getCustomerId();
            String productId = tvProductId.getText().toString();
            btnPlus.setOnClickListener(view2 -> {
                CartItem item = CartDatabase.getInstance(getContext()).cartItemDao()
                        .getCartItemByCartIdAndProductId(cartId, productId);
                item.setQuantity(item.getQuantity() + 1);
                item.setAmount(item.getQuantity() * item.getPrice());
                CartDatabase.getInstance(getContext()).cartItemDao()
                        .updateCartItem(item);
                loadRecyclerViewCart(view);
                loadSummary();
            });

            btnMinus.setOnClickListener(view3 -> {
                CartItem item = CartDatabase.getInstance(getContext()).cartItemDao()
                        .getCartItemByCartIdAndProductId(cartId, productId);
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    item.setAmount(item.getQuantity() * item.getPrice());
                    CartDatabase.getInstance(getContext()).cartItemDao()
                            .updateCartItem(item);
                } else if (item.getQuantity() == 1) {
                    CartDatabase.getInstance(getContext()).cartItemDao().deleteCartItem(item);
                }
                loadRecyclerViewCart(view);
                loadSummary();
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadRecyclerViewCart(View view) {
        String customerId = customerManager.getCustomer().getCustomerId();
        CartWithCartItem data = CartDatabase.getInstance(getContext()).cartDao().getCartWithCartItemByCartId(customerId);
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        cartAdapter.setLineItems(data.getCartItems());
        recyclerViewCart.setAdapter(cartAdapter);
    }
}