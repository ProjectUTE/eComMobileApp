package vn.edu.ecomapp.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.api.LoginApi;
import vn.edu.ecomapp.dto.AccessToken;
import vn.edu.ecomapp.dto.login.LoginRequest;
import vn.edu.ecomapp.dto.login.LoginResponse;
import vn.edu.ecomapp.model.Customer;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.util.AlertDialogMessage;
import vn.edu.ecomapp.util.Constants;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.activity.PanelActivity;

public class LoginController {

    private final Context context;
    TokenManager tokenManager;
    CustomerManager customerManager;
    final  LoginApi loginApi = RetrofitClient.createApi(LoginApi.class);

    public LoginController(Context context) {
        this.context = context;
        tokenManager = TokenManager.getInstance(context.getSharedPreferences(Constants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
        customerManager = CustomerManager.getInstance(context.getSharedPreferences(Constants.DATA_CUSTOMER, Context.MODE_PRIVATE));
    }

    public void handleLogin(LoginRequest loginRequest) {
        loginApi.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if(response.body() != null) {
                    AccessToken accessToken = response.body().getToken();
                    if(accessToken != null) {
                        tokenManager.saveToken(accessToken);
                    }
                    Customer customer = new Customer();
                    customer.setCustomerId(response.body().getCustomerId());
                    customerManager.saveCustomer(customer);
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    goToPanelActivity();
                } else {
                    String title = "Login failed";
                    String message = "Please check your email, password";
                    AlertDialogMessage.showAlertMessage(context, title, message);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {

            }
        });
    }

      private void goToPanelActivity() {
          Intent myIntent = new Intent(context, PanelActivity.class);
          context.startActivity(myIntent);
    }
}
