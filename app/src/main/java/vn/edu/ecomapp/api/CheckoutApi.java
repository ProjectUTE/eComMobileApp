package vn.edu.ecomapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.edu.ecomapp.dto.Order;
import vn.edu.ecomapp.dto.message.MessageResponse;

public interface CheckoutApi {
    @POST("checkout/save")
    Call<MessageResponse> saveOrderByCustomerId(@Body Order order);
}
