package vn.edu.ecomapp.room.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CartWithCartItem {
    @Embedded private Cart cart;
    @Relation(
            parentColumn = "id",
            entityColumn = "cartId"
    )
    private List<CartItem> cartItems;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
