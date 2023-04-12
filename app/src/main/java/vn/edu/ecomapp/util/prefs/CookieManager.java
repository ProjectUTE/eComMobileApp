package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.util.constants.PrefsConstants;

public class CookieManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static CookieManager INSTANCE  = null;

    public static synchronized CookieManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new CookieManager(prefs);
        }
        return INSTANCE;
    }
   private CookieManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public String getCookie() {
        return prefs.getString(PrefsConstants.COOKIE_ID, null);
   }

   public void saveCookie(String cookie) {
       editor.putString(PrefsConstants.COOKIE_ID, cookie);
       editor.apply();
   }

   public void removeCookie() {
        editor.remove(PrefsConstants.COOKIE_ID);
        editor.apply();
   }
}
