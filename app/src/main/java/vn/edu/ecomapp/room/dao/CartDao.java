package vn.edu.ecomapp.room.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import vn.edu.ecomapp.room.entities.Cart;
import vn.edu.ecomapp.room.entities.CartWithCartItem;

@Dao
public interface CartDao {
   @Insert
   void insertCart(Cart cart);

   @Query("SELECT * FROM cart WHERE cart.id = :id")
   @Transaction
   public CartWithCartItem getCartWithCartItemByCartId(String id);

   @Query("SELECT EXISTS(SELECT * FROM cart WHERE cart.id = :id)")
   boolean cartIsExits(String id);
}
