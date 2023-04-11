package vn.edu.ecomapp.dto.profile;

import vn.edu.ecomapp.dto.message.MessageResponse;
import vn.edu.ecomapp.dto.Customer;

public class ProfileResponse extends MessageResponse {
    Customer data;

    public Customer getData() {
        return data;
    }

    public void setData(Customer data) {
        this.data = data;
    }
}
