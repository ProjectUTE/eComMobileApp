package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import vn.edu.ecomapp.model.Customer;
import vn.edu.ecomapp.util.Constants;

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
       editor.putString(Constants.CUSTOMER_JSON, customerJSON);
       editor.apply();
   }

   public void removeCustomer() {
        editor.remove(Constants.CUSTOMER_JSON);
        editor.apply();
   }

   public Customer getCustomer() {
       String customerJSON = prefs.getString(Constants.CUSTOMER_JSON, null);
       return gson.fromJson(customerJSON, Customer.class);
   }


}
