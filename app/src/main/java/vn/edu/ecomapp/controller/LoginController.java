package vn.edu.ecomapp.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.ecomapp.api.AuthApi;
import vn.edu.ecomapp.dto.AccessToken;
import vn.edu.ecomapp.dto.Customer;
import vn.edu.ecomapp.dto.login.LoginRequest;
import vn.edu.ecomapp.dto.login.LoginResponse;
import vn.edu.ecomapp.retrofit.RetrofitClient;
import vn.edu.ecomapp.room.database.CartDatabase;
import vn.edu.ecomapp.room.entities.Cart;
import vn.edu.ecomapp.util.AlertDialogMessage;
import vn.edu.ecomapp.util.constants.PrefsConstants;
import vn.edu.ecomapp.util.prefs.CustomerManager;
import vn.edu.ecomapp.util.prefs.DataLoginRequestManager;
import vn.edu.ecomapp.util.prefs.TokenManager;
import vn.edu.ecomapp.view.activity.PanelActivity;

public class LoginController {

    private final Context context;
    TokenManager tokenManager;
    CustomerManager customerManager;
    DataLoginRequestManager dataLoginManager;
    final AuthApi authApi = RetrofitClient.createApi(AuthApi.class);

    public LoginController(Context context) {
        this.context = context;
        tokenManager = TokenManager
                .getInstance(context.getSharedPreferences(PrefsConstants.DATA_ACCESS_TOKEN, Context.MODE_PRIVATE));
        customerManager = CustomerManager
                .getInstance(context.getSharedPreferences(PrefsConstants.DATA_CUSTOMER, Context.MODE_PRIVATE));
        dataLoginManager = DataLoginRequestManager
                .getInstance(context.getSharedPreferences(PrefsConstants.DATA_USER_LOGIN_REQUEST, Context.MODE_PRIVATE));
    }

    public void handleLogin(LoginRequest loginRequest) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Login Account");
        pd.setMessage("Logging in, please wait");
        pd.show();
        authApi.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                pd.dismiss();
                if(response.body() != null) {
                    AccessToken accessToken = response.body().getToken();
                    if(accessToken != null) {
                        tokenManager.saveToken(accessToken);
                    }
                    Customer customer = new Customer();
                    customer.setCustomerId(response.body().getCustomerId());
                    customerManager.saveCustomer(customer);
                    dataLoginManager.saveDataLoginRequest(loginRequest);
                    createCartIfNotExits(response.body().getCustomerId());

                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    goToPanelActivity();
                } else {
                    String title = "Login failed";
                    String message = "Please check your email, password";
                    AlertDialogMessage.showAlertMessage(context, title, message);
                }
            }

            private void createCartIfNotExits(String customerId) {
                boolean isExist = CartDatabase.getInstance(context).cartDao().cartIsExits(customerId);
                if(isExist) {
                    Log.d("LOGIN", "EXITS");
                    return;
                }

                Log.d("LOGIN", "NOT EXITS");
                Cart cart = new Cart();
                cart.setId(customerId);
                CartDatabase.getInstance(context).cartDao().insertCart(cart);
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.d("LOGIN", t.getMessage());
            }
        });
    }

      private void goToPanelActivity() {
          Intent myIntent = new Intent(context, PanelActivity.class);
          context.startActivity(myIntent);
    }
}
