package vn.edu.ecomapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.ecomapp.dto.Order;

public interface OrderApi {
    @GET("order/get-all-by-customer-id/{id}")
    Call<List<Order>> getOrderByCustomerId(@Path("id") String id);
}
