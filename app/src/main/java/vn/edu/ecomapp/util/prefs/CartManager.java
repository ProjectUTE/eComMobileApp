package vn.edu.ecomapp.util.prefs;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import vn.edu.ecomapp.dto.Cart;
import vn.edu.ecomapp.model.LineItem;
import vn.edu.ecomapp.util.Constants;

public class CartManager {
   private final SharedPreferences prefs;
   private final SharedPreferences.Editor editor;
   private static CartManager INSTANCE  = null;

   private final Cart cart = getCart();

   private  final Gson gson = new Gson();

   public static synchronized CartManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new CartManager(prefs);
        }
        return INSTANCE;
    }
   private CartManager(SharedPreferences prefs) {
       this.prefs = prefs;
       this.editor = prefs.edit();
   }

    private Cart getCart() {
       String cartJSON = prefs.getString(Constants.DATA_CART, null);
       if(cartJSON == null || cartJSON.isEmpty()) {
          return new Cart();
       }
       return gson.fromJson(cartJSON, Cart.class);
   }

   public void saveCart() {
       if(cart == null) return;
       String cartJSON = gson.toJson(cart);
       editor.putString(Constants.DATA_CART, cartJSON);
       editor.apply();
   }

   public void removeCart() {
        editor.remove(Constants.DATA_CART);
        editor.apply();
   }

   public void addCartItem(LineItem lineItem) {
       if (lineItem == null) return;
       if (cart == null) return;

       boolean isExits = false;
       int position = -1;
       for(int i = 0; i < cart.getLineItems().size(); i++) {
           LineItem item = cart.getLineItems().get(i);
            if(item.getId().equals(lineItem.getId())) {
                isExits = true;
                position = i;
                break;
            }
       }

       if(isExits) {
           // Change quantity, amount
           // Update itemsTotal, total

       } else {
           // add new lineitem
           // check amount
           int amount = lineItem.getPrice() * lineItem.getQuantity();
           if(amount != lineItem.getAmount()) lineItem.setAmount(amount);
           cart.getLineItems().add(lineItem);
       }

       updateCart();
       saveCart();
   }

   public void removeCartItem(String lineItemId) {
        if (lineItemId == null) return;
        if (cart == null) return;

       boolean isExits = false;
       int position = -1;
       for(int i = 0; i < cart.getLineItems().size(); i++) {
           LineItem item = cart.getLineItems().get(i);
            if(item.getId().equals(lineItemId)) {
                isExits = true;
                position = i;
                break;
            }
       }

       if(isExits) {
//           Remove cartItem
           cart.getLineItems().remove(position);
           updateCart();
           saveCart();
       }
   }

   private void updateCart() {
        // Update itemsTotal
       int itemsTotal = 0;
       for(LineItem item : cart.getLineItems()) {
           int amount = item.getPrice() * item.getQuantity();
           if(amount != item.getAmount())  item.setAmount(amount);
           itemsTotal += item.getAmount();
       }
       cart.setItemsTotal(itemsTotal);

       // Update total
       int total = cart.getItemsTotal() + cart.getDelivery();
       cart.setTotal(total);
   }
}
