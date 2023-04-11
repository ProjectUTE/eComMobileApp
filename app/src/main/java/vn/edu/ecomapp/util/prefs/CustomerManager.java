package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import vn.edu.ecomapp.dto.Customer;
import vn.edu.ecomapp.util.constants.PrefsConstants;

public class CustomerManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static CustomerManager INSTANCE  = null;

   final Gson gson = new Gson();

   public static synchronized CustomerManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new CustomerManager(prefs);
        }
        return INSTANCE;
    }
   private CustomerManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

   public void saveCustomer(Customer customer) {
       if(customer == null) return;
       String customerJSON = gson.toJson(customer);
       editor.putString(PrefsConstants.CUSTOMER_JSON, customerJSON);
       editor.apply();
   }

   public void removeCustomer() {
        editor.remove(PrefsConstants.CUSTOMER_JSON);
        editor.apply();
   }

   public Customer getCustomer() {
       String customerJSON = prefs.getString(PrefsConstants.CUSTOMER_JSON, null);
       return gson.fromJson(customerJSON, Customer.class);
   }


}
