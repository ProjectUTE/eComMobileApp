package vn.edu.ecomapp.services.payment;

import android.app.Activity;
import android.content.Intent;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

public class PayPalManager {

    private static final  String PAYPAL_CLIENT_ID = "ASkkqIO3hfOoZfMWh5u6wF1a3cp2jAzTaD0e1eN8yhE4CMtsJNKewg8ah7xTqhZRGoXfjaTARkQRDz75";
    public static PayPalConfiguration getPayPalConfiguration() {
        return new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);
    }

    public static Intent getIntentPayPal(Activity activity, PayPalPayment payment) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, getPayPalConfiguration());
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        return intent;
    }
}
