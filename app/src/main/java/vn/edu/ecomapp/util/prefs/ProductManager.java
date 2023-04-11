package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.util.constants.PrefsConstants;

public class ProductManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static ProductManager INSTANCE  = null;

    public static synchronized ProductManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new ProductManager(prefs);
        }
        return INSTANCE;
    }
   private ProductManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public String getProductId() {
        return prefs.getString(PrefsConstants.PRODUCT_ID, null);
   }

   public void saveProductId(String id) {
       editor.putString(PrefsConstants.PRODUCT_ID, id);
       editor.apply();
   }

   public void removeProductId() {
        editor.remove(PrefsConstants.PRODUCT_ID);
        editor.apply();
   }
}
