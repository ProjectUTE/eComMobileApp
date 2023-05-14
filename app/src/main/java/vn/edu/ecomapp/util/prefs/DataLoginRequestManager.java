package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.dto.login.LoginRequest;
import vn.edu.ecomapp.util.constants.PrefsConstants;

public class DataLoginRequestManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static DataLoginRequestManager INSTANCE  = null;

   public static synchronized DataLoginRequestManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new DataLoginRequestManager(prefs);
        }
        return INSTANCE;
    }
   private DataLoginRequestManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public void saveDataLoginRequest(LoginRequest dataLogin) {
       if(dataLogin == null) return;
       editor.putString(PrefsConstants.LOGIN_EMAIL, dataLogin.getEmail());
       editor.putString(PrefsConstants.LOGIN_PASSWORD, dataLogin.getPassword());
       editor.putBoolean(PrefsConstants.IS_GOOGLE_LOGIN, dataLogin.getIsGoogleLogin());
       editor.putInt(PrefsConstants.ROLE, dataLogin.getRole());
       editor.commit();
   }

   public void removeDataLoginRequest() {
        editor.remove(PrefsConstants.LOGIN_EMAIL);
        editor.remove(PrefsConstants.LOGIN_PASSWORD);
        editor.remove(PrefsConstants.IS_GOOGLE_LOGIN);
       editor.remove(PrefsConstants.ROLE);
       editor.commit();
   }

   public LoginRequest getDataLoginRequest() {
       LoginRequest instance = new LoginRequest();
       instance.setEmail(prefs.getString(PrefsConstants.LOGIN_EMAIL, null));
       instance.setPassword(prefs.getString(PrefsConstants.LOGIN_PASSWORD, null));
       instance.setIsGoogleLogin(prefs.getBoolean(PrefsConstants.IS_GOOGLE_LOGIN, false));
       instance.setRole(prefs.getInt(PrefsConstants.ROLE, 1));
       return instance;
   }


}
