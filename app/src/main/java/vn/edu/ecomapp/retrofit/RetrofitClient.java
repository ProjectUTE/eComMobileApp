package vn.edu.ecomapp.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.137.1:8081/api/";

    public static Retrofit getRetrofit() {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS) // set thời gian kết nối tối đa là 30 giây
                .readTimeout(3000, TimeUnit.SECONDS) // set thời gian đọc dữ liệu tối đa là 30 giây
                .build();
    // thêm OkHttpClient vào Retrofit.Builder
    return new Retrofit.Builder()
         .baseUrl(BASE_URL)
         .client(okHttpClient) // Thêm OkHttpClient vào Retrofit.Builder
         .addConverterFactory(GsonConverterFactory.create())
         .build();
    }
}
