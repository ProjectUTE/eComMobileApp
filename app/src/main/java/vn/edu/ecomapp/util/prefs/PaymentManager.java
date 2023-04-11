package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.util.constants.PrefsConstants;

public class PaymentManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static PaymentManager INSTANCE  = null;

    public static synchronized PaymentManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new PaymentManager(prefs);
        }
        return INSTANCE;
    }
   private PaymentManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public String getPaymentId() {
        return prefs.getString(PrefsConstants.PAYMENT_ID, null);
   }

   public void savePaymentId(String categoryId) {
       editor.putString(PrefsConstants.PAYMENT_ID, categoryId);
       editor.apply();
   }

   public void removePaymentId() {
        editor.remove(PrefsConstants.PAYMENT_ID);
        editor.apply();
   }
}
