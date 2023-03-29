package vn.edu.ecomapp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import java.util.List;

import vn.edu.ecomapp.R;
import vn.edu.ecomapp.model.Product;
import vn.edu.ecomapp.view.adapter.CartAdapter;

public class CustomerCartFragment  extends Fragment {

    RecyclerView recyclerViewCart;
    List<Product> products;

    Button checkoutButton;
    PayPalButton payPalButton;
    private static final  String CLIENT_ID = "ASkkqIO3hfOoZfMWh5u6wF1a3cp2jAzTaD0e1eN8yhE4CMtsJNKewg8ah7xTqhZRGoXfjaTARkQRDz75";

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
        initializeData();
        loadRecyclerViewCart(view);
    }

    @SuppressLint("LongLogTag")
    private void initComponents(View view) {
        checkoutButton = view.findViewById(R.id.checkoutButton);
        payPalButton = view.findViewById(R.id.payPalButton);
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

//        checkoutButton.setOnClickListener(view2 -> {
//            String amounts = "100";
//            String currency = "USD";
//            String title = "Checkout";
//            PayPalPayment payment = new PayPalPayment(new BigDecimal(amounts), currency, title, PayPalPayment.PAYMENT_INTENT_SALE);
//            Intent intent = PayPalManager.getIntentPayPal(requireActivity(), payment);
//            startActivityForResult(intent, REQUEST_CODE);
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_CODE) {
//            if(resultCode == Activity.RESULT_OK) {
//                PaymentConfirmation config = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if(config != null) {
//                    try {
//                        String paymentDetails = config.toJSONObject().toString(4);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(requireActivity(), "Payment has been canceled", Toast.LENGTH_SHORT).show();
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Toast.makeText(requireActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
//            if(resultCode == Activity.RESULT_OK) {
//                PayPalAuthorization authorization = data
//                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
//                try {
//                    Log.d("CustomerCart", authorization.toJSONObject().toString());
//                }catch (Exception e) {
//                    Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    private void loadRecyclerViewCart(View view) {
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        recyclerViewCart.setHasFixedSize(true);
        CartAdapter cartAdapter = new CartAdapter(getContext(), products);
        recyclerViewCart.setAdapter(cartAdapter);
    }

    private void initializeData() {
        this.products = new ArrayList<>();
        this.products.add(new Product("Coffee", 14000));
        this.products.add(new Product("Coffee 1", 15000));
        this.products.add(new Product("Coffee 2", 16000));
        this.products.add(new Product("Coffee 3", 17000));
        this.products.add(new Product("Coffee 4", 18000));
    }
}