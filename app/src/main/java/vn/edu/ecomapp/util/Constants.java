package vn.edu.ecomapp.util;

public class Constants {

    public static final String BASE_URL = "https://8275-58-187-186-208.ngrok-free.app".trim();
    public static final String BASE_URL_LOCAL = "http://localhost:8080".trim();
    // Config data user login
    public static final String DATA_USER_LOGIN = "DATA_USER_LOGIN";
    public static final String LOGIN_EMAIL = "LOGIN_EMAIL";
    public static final String LOGIN_PASSWORD = "LOGIN_PASSWORD";
    public static final String LOGIN_IS_REMEMBER = "LOGIN_IS_REMEMBER";
    // Config AccessToken
    public static final String DATA_ACCESS_TOKEN = "DATA_ACCESS_TOKEN";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    // Config user info
    public static final String DATA_CUSTOMER = "DATA_CUSTOMER";
    public static final String CUSTOMER_JSON = "CUSTOMER_JSON";


    // Cart data
    public static final String DATA_CART = "DATA_CART";
    public static final String CART_JSON = "CART_JSON";

    // Order
    public static final String DATA_ORDER = "DATA_ORDER";
    public static final String ORDER_ID = "ORDER_ID";


    // Role
    public static final int USER_ADMIN = 0;
    public static final int USER_CUSTOMER = 1;

    // HttpStatus
    public static final int OK = 200;
    public static final int BAD_REQUEST = 401;
    public static final int NOT_FOUND = 404;

    // Method
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";

    // Category
    public static final String DATA_CATEGORY = "DATA_CATEGORY";
    public static final String CATEGORY_ID = "CATEGORY_ID";

    // Product
   public static final String  PRODUCT_ID = "PRODUCT_ID";
    public static final String  DATA_PRODUCT = "DATA_PRODUCT";

}
