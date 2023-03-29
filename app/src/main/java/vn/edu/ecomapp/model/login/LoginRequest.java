package vn.edu.ecomapp.model.login;

public class LoginRequest {
    String email, password, isGoogleLogin, picture, displayName;
    int role;
    public int getRole() {
        return role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsGoogleLogin() {
        return isGoogleLogin;
    }

    public void setIsGoogleLogin(String isGoogleLogin) {
        this.isGoogleLogin = isGoogleLogin;
    }
}
