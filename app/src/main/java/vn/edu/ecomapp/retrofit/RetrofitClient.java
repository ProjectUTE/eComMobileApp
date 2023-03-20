package vn.edu.ecomapp.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.4:8080/api/";
//    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//            .callTimeout(5, TimeUnit.SECONDS)
//            .connectTimeout(2, TimeUnit.MINUTES)
//            .readTimeout(3, TimeUnit.MINUTES)
//            .writeTimeout(3, TimeUnit.MINUTES);
    public static Retrofit getRetrofit() {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        if(retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(client)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // set thời gian kết nối tối đa là 30 giây
                .readTimeout(30, TimeUnit.SECONDS) // set thời gian đọc dữ liệu tối đa là 30 giây
                .build();

       retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient) // thêm OkHttpClient vào Retrofit.Builder
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
