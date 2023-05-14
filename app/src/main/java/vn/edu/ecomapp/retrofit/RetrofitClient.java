package vn.edu.ecomapp.retrofit;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.ecomapp.util.constants.UrlConstants;
import vn.edu.ecomapp.util.prefs.TokenManager;

public class RetrofitClient {
    private static final String BASE_URL = UrlConstants.BASE_URL + "/api/";
    private final static OkHttpClient client = buildClient();
    private final static Retrofit retrofit = buildRetrofit();

    private static OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()

                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request.Builder builder1 = request.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Connection", "close");
                    request = builder1.build();
                    return  chain.proceed(request);
                });
        return builder.build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private static Retrofit buildRetrofit() {
        return  new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(RetrofitClient.client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // Create api without authorization
    public static <T> T createApi(Class<T> service){
        return retrofit.create(service);
    }

    // Create api with authorization
    public static <T> T createApiWithAuth(Class<T> service, final TokenManager tokenManager) {
        OkHttpClient newClient = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            newClient = client.newBuilder().addInterceptor(chain -> {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                String accessToken = tokenManager.getAccessToken().getAccessToken();
                if(accessToken != null){
                    builder.addHeader("Authorization", "Bearer " + accessToken);
                }
                request = builder.build();
                return chain.proceed(request);
            }).authenticator(CustomAuthenticator.getInstance(tokenManager, null)).build();
        }
        Retrofit newRetrofit = retrofit.newBuilder().client(Objects.requireNonNull(newClient)).build();
        return newRetrofit.create(service);
    }

      public static <T> T createApiWithClientTimeout(Class<T> service) {
         OkHttpClient newClient = client.newBuilder()
                 .connectTimeout(60, TimeUnit.SECONDS)
                 .readTimeout(60, TimeUnit.SECONDS)
                 .writeTimeout(60, TimeUnit.SECONDS)
                 .build();
        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
        return newRetrofit.create(service);
    }

    public static <T> T createApiWithAuthAndContext(Class<T> service, final TokenManager tokenManager, Objects context) {
        OkHttpClient newClient = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            newClient = client.newBuilder().addInterceptor(chain -> {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                String accessToken = tokenManager.getAccessToken().getAccessToken();
                if(accessToken != null){
                    builder.addHeader("Authorization", "Bearer " + accessToken);
                }
                request = builder.build();
                return chain.proceed(request);
            }).authenticator(CustomAuthenticator.getInstance(tokenManager,context)).build();
        }
        Retrofit newRetrofit = retrofit.newBuilder().client(Objects.requireNonNull(newClient)).build();
        return newRetrofit.create(service);
    }

}
