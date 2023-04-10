package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.util.Constants;

public class CategoryManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static CategoryManager INSTANCE  = null;

    public static synchronized CategoryManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new CategoryManager(prefs);
        }
        return INSTANCE;
    }
   private CategoryManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public String getCategoryId() {
        return prefs.getString(Constants.CATEGORY_ID, null);
   }

   public void saveCategoryId(String categoryId) {
       editor.putString(Constants.CATEGORY_ID, categoryId);
       editor.apply();
   }

   public void removeCategoryId() {
        editor.remove(Constants.CATEGORY_ID);
        editor.apply();
   }
}
