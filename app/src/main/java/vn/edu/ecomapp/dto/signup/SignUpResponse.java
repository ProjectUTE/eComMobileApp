package vn.edu.ecomapp.dto.signup;

import vn.edu.ecomapp.dto.message.MessageResponse;

public class SignUpResponse extends MessageResponse {
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
