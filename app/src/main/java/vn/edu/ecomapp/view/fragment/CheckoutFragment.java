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
import vn.edu.ecomapp.room.database.CartDatabase;
import vn.edu.ecomapp.room.entities.CartWithCartItem;
import vn.edu.ecomapp.util.CurrencyFormat;
import vn.edu.ecomapp.util.FragmentManager;
import vn.edu.ecomapp.util.constants.PaymentCodeConstants;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.PaymentManager;
import vn.edu.ecomapp.view.adapter.CheckoutAdapter;

public class CheckoutFragment extends Fragment {

    RecyclerView rcvCheckout;
    TextView tvItemTotal, tvDelivery, tvTotal;
    TextView appBarTitle;
    ImageView backButton;
    TextView placeOrderBtn;
    PayPalButton payPalButton;
    PaymentManager paymentManager;
    CustomerManager customerManager;
    String cartId;
    private static final  String CLIENT_ID = "ASkkqIO3hfOoZfMWh5u6wF1a3cp2jAzTaD0e1eN8yhE4CMtsJNKewg8ah7xTqhZRGoXfjaTARkQRDz75";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        customerManager = CustomerManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_CUSTOMER, Context.MODE_PRIVATE));
        paymentManager = PaymentManager
                .getInstance(requireActivity().getSharedPreferences(PrefsConstants.DATA_PAYMENT, Context.MODE_PRIVATE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }


    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        placeOrderBtn = view.findViewById(R.id.checkoutButton);
        rcvCheckout = view.findViewById(R.id.rcvItems);
        appBarTitle = view.findViewById(R.id.appBarTitle);
        appBarTitle.setText("Place Order");
        backButton = view.findViewById(R.id.backButton);
        payPalButton = view.findViewById(R.id.payPalButton);
        tvItemTotal = view.findViewById(R.id.totalItem);
        tvDelivery = view.findViewById(R.id.delivery);
        tvTotal = view.findViewById(R.id.total);
        cartId = customerManager.getCustomer().getCustomerId();

        backButton.setOnClickListener(view1 -> FragmentManager.backFragment(requireActivity()));
        if(paymentManager.getPaymentId().equals(PaymentCodeConstants.PAYMENT_CASH)) {
            payPalButton.setVisibility(View.INVISIBLE);
        } else {
            placeOrderBtn.setVisibility(View.INVISIBLE);
        }
        setUpPayPal();
       CartWithCartItem item = CartDatabase.getInstance(getContext()).cartDao().getCartWithCartItemByCartId(cartId);
        CheckoutAdapter adapter = new CheckoutAdapter(getContext(), item.getCartItems());
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvCheckout.setAdapter(adapter);
        rcvCheckout.setLayoutManager(layout);
        loadSummary();
    }
    private void loadSummary() {
        int itemsTotal = CartDatabase.getInstance(getContext()).cartItemDao().getItemsTotal(cartId);
        tvItemTotal.setText(CurrencyFormat.VietnameseCurrency(itemsTotal));
        tvDelivery.setText(CurrencyFormat.VietnameseCurrency(0));
        tvTotal.setText(CurrencyFormat.VietnameseCurrency(itemsTotal));
    }

    @SuppressLint("LongLogTag")
    private void setUpPayPal() {

      PayPalCheckout.setConfig(new CheckoutConfig(
            requireActivity().getApplication(),
            CLIENT_ID,
            Environment.SANDBOX,
            CurrencyCode.USD,
            UserAction.PAY_NOW,
             "vn.edu.ecomapp://paypalpay"
        ));
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


}