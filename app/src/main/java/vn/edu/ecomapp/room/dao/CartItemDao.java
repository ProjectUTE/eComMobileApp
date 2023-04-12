package vn.edu.ecomapp.room.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import vn.edu.ecomapp.room.entities.CartItem;

@Dao
public interface CartItemDao {
    @Insert
    void insertCartItem(CartItem cartItem);

    @Update
    void updateCartItem(CartItem cartItem);

    @Query("SELECT * FROM cart_item WHERE cart_item.cartId = :cartId AND cart_item.productId = :productId")
    CartItem getCartItemByCartIdAndProductId(String cartId, String productId);

    @Delete
    void deleteCartItem(CartItem cartItem);

   @Query("SELECT SUM(amount) as ItemsTotal FROM cart_item WHERE cart_item.cartId = :cartId")
    int getItemsTotal(String cartId);

    @Query("SELECT COUNT(*) FROM cart_item WHERE cart_item.cartId = :cartId")
    int getItemsCount(String cartId);
}

