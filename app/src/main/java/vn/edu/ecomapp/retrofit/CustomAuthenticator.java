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
import vn.edu.ecomapp.dto.AccessToken;
import vn.edu.ecomapp.dto.login.LoginRequest;
import vn.edu.ecomapp.dto.login.LoginResponse;
import vn.edu.ecomapp.util.constants.HttpStatusCodeConstants;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.TokenManager;

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
        if(response.code() == HttpStatusCodeConstants.BAD_REQUEST) {
            String email = "doduongthaituan201102@gmail.com";
            String password = "doduongthaituan201102@gmail.com";
            int role = 1;
            boolean isGoogleLogin = true;

            AccessToken token = tokenManager.getAccessToken();
            AccessTokenApi accessTokenApi = RetrofitClient.createApi(AccessTokenApi.class);

            LoginRequest loginRequest = new LoginRequest(email, password, isGoogleLogin, role);
            Call<LoginResponse> call = accessTokenApi.refreshToken(token.getRefreshToken(), token.getAccessToken(), loginRequest);

            retrofit2.Response<LoginResponse> res = call.execute();

            if(res.isSuccessful()) {
                AccessToken newToken = res.body().getToken();
                tokenManager.saveToken(newToken);
                return response.request().newBuilder().header("Authorization", "Bearer " + newToken.getAccessToken()).build();
            }
        }
        return null;
    }
}
