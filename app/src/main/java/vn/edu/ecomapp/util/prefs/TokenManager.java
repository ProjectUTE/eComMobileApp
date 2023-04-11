package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.dto.AccessToken;
import vn.edu.ecomapp.util.constants.PrefsConstants;

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
       editor.putString(PrefsConstants.ACCESS_TOKEN, token.getAccessToken());
       editor.putString(PrefsConstants.REFRESH_TOKEN, token.getRefreshToken());
       editor.commit();
   }

   public void removeToken() {
        editor.remove(PrefsConstants.ACCESS_TOKEN);
        editor.remove(PrefsConstants.REFRESH_TOKEN);
        editor.commit();
   }

   public AccessToken getAccessToken() {
       AccessToken instance = new AccessToken();
       instance.setAccessToken(prefs.getString(PrefsConstants.ACCESS_TOKEN, null));
       instance.setRefreshToken((prefs.getString(PrefsConstants.REFRESH_TOKEN, null)));
       return instance;
   }


}
