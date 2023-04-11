package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import vn.edu.ecomapp.util.constants.PrefsConstants;

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
        return prefs.getString(PrefsConstants.CATEGORY_ID, null);
   }

   public void saveCategoryId(String categoryId) {
       editor.putString(PrefsConstants.CATEGORY_ID, categoryId);
       editor.apply();
   }

   public void removeCategoryId() {
        editor.remove(PrefsConstants.CATEGORY_ID);
        editor.apply();
   }
}
