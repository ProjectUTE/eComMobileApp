package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.dto.login.Login;
import vn.edu.ecomapp.util.constants.PrefsConstants;

public class DataLoginManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static DataLoginManager INSTANCE  = null;

   public static synchronized DataLoginManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new DataLoginManager(prefs);
        }
        return INSTANCE;
    }
   private DataLoginManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public void saveDataLogin(Login dataLogin) {
       if(dataLogin == null) return;
       editor.putString(PrefsConstants.LOGIN_EMAIL, dataLogin.getEmail());
       editor.putString(PrefsConstants.LOGIN_PASSWORD, dataLogin.getPassword());
       editor.putBoolean(PrefsConstants.LOGIN_IS_REMEMBER, dataLogin.getIsRemember());
       editor.commit();
   }

   public void removeDataLogin() {
        editor.remove(PrefsConstants.LOGIN_EMAIL);
        editor.remove(PrefsConstants.LOGIN_PASSWORD);
        editor.remove(PrefsConstants.LOGIN_IS_REMEMBER);
        editor.commit();
   }

   public Login getDataLogin() {
       Login instance = new Login();
       instance.setEmail(prefs.getString(PrefsConstants.LOGIN_EMAIL, null));
       instance.setPassword(prefs.getString(PrefsConstants.LOGIN_PASSWORD, null));
       instance.setRemember(prefs.getBoolean(PrefsConstants.LOGIN_IS_REMEMBER, false));
       return instance;
   }


}
