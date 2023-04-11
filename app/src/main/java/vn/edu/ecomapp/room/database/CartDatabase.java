package vn.edu.ecomapp.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import vn.edu.ecomapp.room.dao.CartDao;
import vn.edu.ecomapp.room.dao.CartItemDao;
import vn.edu.ecomapp.room.entities.Cart;
import vn.edu.ecomapp.room.entities.CartItem;

@Database(entities = {Cart.class, CartItem.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {
    static final String DATABASE_NAME = "coffee_shop.db";
    static CartDatabase instance;

    public static synchronized CartDatabase getInstance(Context context) {
       if(instance == null) {
           instance = Room.databaseBuilder(context.getApplicationContext(), CartDatabase.class, DATABASE_NAME)
                   .allowMainThreadQueries().build();
       }
       return instance;
   }
   public abstract CartDao cartDao();
    public abstract CartItemDao cartItemDao();
}
