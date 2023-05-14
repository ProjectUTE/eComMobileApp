package vn.edu.ecomapp.dto.payment;

public class PaymentMethodResponse {
    private String id, name, available;

    public PaymentMethodResponse(String id, String name, String available) {
        this.id = id;
        this.name = name;
        this.available = available;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
