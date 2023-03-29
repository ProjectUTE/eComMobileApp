package vn.edu.ecomapp.model;

public class Order {
    private String id;
    private String date;
    private String address;
    private String status;

   public  Order() {}

    public Order(String id, String date, String address, String status, String paymentMethod, String quantityProduct, String totalPrice) {
        this.id = id;
        this.date = date;
        this.address = address;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.quantityProduct = quantityProduct;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(String quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    private String paymentMethod;
    private String quantityProduct;
    private String totalPrice;
}
