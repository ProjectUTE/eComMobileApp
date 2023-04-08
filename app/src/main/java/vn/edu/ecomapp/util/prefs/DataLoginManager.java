package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.dto.login.Login;
import vn.edu.ecomapp.util.Constants;

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
       editor.putString(Constants.LOGIN_EMAIL, dataLogin.getEmail());
       editor.putString(Constants.LOGIN_PASSWORD, dataLogin.getPassword());
       editor.putBoolean(Constants.LOGIN_IS_REMEMBER, dataLogin.getIsRemember());
       editor.commit();
   }

   public void removeDataLogin() {
        editor.remove(Constants.LOGIN_EMAIL);
        editor.remove(Constants.LOGIN_PASSWORD);
        editor.remove(Constants.LOGIN_IS_REMEMBER);
        editor.commit();
   }

   public Login getDataLogin() {
       Login instance = new Login();
       instance.setEmail(prefs.getString(Constants.LOGIN_EMAIL, null));
       instance.setPassword(prefs.getString(Constants.LOGIN_PASSWORD, null));
       instance.setRemember(prefs.getBoolean(Constants.LOGIN_IS_REMEMBER, false));
       return instance;
   }


}
