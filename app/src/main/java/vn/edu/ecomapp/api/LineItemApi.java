package vn.edu.ecomapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.ecomapp.dto.LineItem;
import vn.edu.ecomapp.dto.Order;
import vn.edu.ecomapp.dto.lineitem.LineItemResponse;

public interface LineItemApi {
    @GET("line-item/get-all-by-order-id/{id}")
    Call<List<LineItemResponse>> getLineItemByOrderId(@Path("id") String id);
}
