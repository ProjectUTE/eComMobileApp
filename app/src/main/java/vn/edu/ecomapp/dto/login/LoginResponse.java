package vn.edu.ecomapp.dto.login;

import vn.edu.ecomapp.dto.AccessToken;
import vn.edu.ecomapp.dto.message.MessageResponse;

public class LoginResponse  extends MessageResponse {
    String email;
    AccessToken token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccessToken getToken() {
        return token;
    }

    public void setToken(AccessToken accessToken) {
        this.token = accessToken;
    }

}
