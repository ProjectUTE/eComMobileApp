package vn.edu.ecomapp.retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import vn.edu.ecomapp.api.AccessTokenApi;
import vn.edu.ecomapp.model.AccessToken;
import vn.edu.ecomapp.util.TokenManager;

public class CustomAuthenticator  implements Authenticator {
    private final TokenManager tokenManager;
    private static CustomAuthenticator INSTANCE;
    private CustomAuthenticator(TokenManager tokenManager){
        this.tokenManager = tokenManager;
    }
    static synchronized CustomAuthenticator getInstance(TokenManager tokenManager){
        if(INSTANCE == null){
            INSTANCE = new CustomAuthenticator(tokenManager);
        }

        return INSTANCE;
    }
    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {

        AccessToken token = tokenManager.getAccessToken();

        AccessTokenApi accessTokenApi = RetrofitClient.createApi(AccessTokenApi.class);
        Call<AccessToken> call = accessTokenApi.refreshToken(token.getRefreshToken());
        retrofit2.Response<AccessToken> res = call.execute();

        if(res.isSuccessful()){
            AccessToken newToken = res.body();
            tokenManager.saveToken(newToken);
            return response.request().newBuilder().header("Authorization", "Bearer " + res.body().getAccessToken()).build();
        }else{
            return null;
        }
    }
}
