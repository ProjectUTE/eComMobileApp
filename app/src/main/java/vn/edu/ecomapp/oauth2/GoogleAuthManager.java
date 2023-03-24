package vn.edu.ecomapp.oauth2;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class GoogleAuthManager {

    private static final  String SERVER_CLIENT_ID = "861387057426-7rn94er3poopkos8cv8gfefcg6crmkla.apps.googleusercontent.com";


    public static GoogleSignInClient getGoogleSignInClient(Activity activity, GoogleSignInOptions gso) {
        return GoogleSignIn.getClient(activity, gso);
    }


    public static GoogleSignInOptions getGoogleSignInOptions() {
        return  new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(SERVER_CLIENT_ID)
                .requestServerAuthCode(SERVER_CLIENT_ID)
                .requestEmail()
                .build();
    }

    public static GoogleSignInAccount getGoogleSignInAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }
}
