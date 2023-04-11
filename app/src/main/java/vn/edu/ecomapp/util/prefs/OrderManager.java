package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.util.constants.PrefsConstants;

public class OrderManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static OrderManager INSTANCE  = null;

    public static synchronized OrderManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new OrderManager(prefs);
        }
        return INSTANCE;
    }
   private OrderManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public String getOrderId() {
        return prefs.getString(PrefsConstants.ORDER_ID, null);
   }

   public void saveOrderId(String categoryId) {
       editor.putString(PrefsConstants.ORDER_ID, categoryId);
       editor.apply();
   }

   public void removeOrderId() {
        editor.remove(PrefsConstants.ORDER_ID);
        editor.apply();
   }
}
