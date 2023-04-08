package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.dto.AccessToken;
import vn.edu.ecomapp.util.Constants;

public class TokenManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static TokenManager INSTANCE  = null;

   public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }
   private TokenManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public void saveToken(AccessToken token) {
       if(token == null) return;
       editor.putString(Constants.ACCESS_TOKEN, token.getAccessToken());
       editor.putString(Constants.REFRESH_TOKEN, token.getRefreshToken());
       editor.commit();
   }

   public void removeToken() {
        editor.remove(Constants.ACCESS_TOKEN);
        editor.remove(Constants.REFRESH_TOKEN);
        editor.commit();
   }

   public AccessToken getAccessToken() {
       AccessToken instance = new AccessToken();
       instance.setAccessToken(prefs.getString(Constants.ACCESS_TOKEN, null));
       instance.setRefreshToken((prefs.getString(Constants.REFRESH_TOKEN, null)));
       return instance;
   }


}
