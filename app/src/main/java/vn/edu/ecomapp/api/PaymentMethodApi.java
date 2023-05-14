package vn.edu.ecomapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.edu.ecomapp.dto.payment.PaymentMethodResponse;

public interface PaymentMethodApi {
    @GET("payment-method/get-all")
    Call<List<PaymentMethodResponse>> getAllPaymentMethods();
}
