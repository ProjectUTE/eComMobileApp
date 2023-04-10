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

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;

import java.util.ArrayList;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.dto.Cart;
import vn.edu.ecomapp.model.LineItem;
import vn.edu.ecomapp.util.Constants;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.prefs.CartManager;
import vn.edu.ecomapp.view.adapter.CartAdapter;

public class CustomerCartFragment  extends Fragment {

    RecyclerView recyclerViewCart;
    TextView checkoutButton, tvtotalItems, btnPlus, btnMinus, tvLineItemid;
    PayPalButton payPalButton;
    CartManager cartManager;
    Cart cart;

    String lineItemId;

    private static final  String CLIENT_ID = "ASkkqIO3hfOoZfMWh5u6wF1a3cp2jAzTaD0e1eN8yhE4CMtsJNKewg8ah7xTqhZRGoXfjaTARkQRDz75";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cartManager = CartManager
                .getInstance(requireActivity().getSharedPreferences(Constants.DATA_CART, Context.MODE_PRIVATE));
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
        PayPalCheckout.setConfig(new CheckoutConfig(
                requireActivity().getApplication(),
                CLIENT_ID,
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                 "vn.edu.ecomapp://paypalpay"
        ));
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
        payPalButton = view.findViewById(R.id.payPalButton);
        tvtotalItems = view.findViewById(R.id.totalItem);
        cart = cartManager.getCart();

        checkoutButton.setOnClickListener(view1 -> FragmentManager.nextFragment(requireActivity(), new SelectPaymentFragment()));

        payPalButton.setup(
                createOrderActions -> {
            ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
            purchaseUnits.add(
                    new PurchaseUnit.Builder()
                            .amount(
                                    new Amount.Builder()
                                            .currencyCode(CurrencyCode.USD)
                                            .value("10.00")
                                            .build()
                            )
                            .build());
            Order order = new Order(
                    OrderIntent.CAPTURE,
                    new AppContext.Builder()
                            .userAction(UserAction.PAY_NOW)
                            .build(),
                    purchaseUnits
            );
            createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
        },
                approval -> approval.getOrderActions().capture(captureOrderResult -> {
                    Log.d("CustomerOder", String.format("%s", captureOrderResult));
                })
        );

        payPalButton.setPaymentButtonEligibilityStatusChanged(paymentButtonEligibilityStatus -> Log.i("PaymentButtonEligibility", String.format("paymentButton : %s", paymentButtonEligibilityStatus)));
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