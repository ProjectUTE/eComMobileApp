package vn.edu.ecomapp.dto.login;

public class LoginRequest {
    String email, password;
    boolean isGoogleLogin;
    int role;

    public LoginRequest(String email, String password, boolean isGoogleLogin, int role) {
        this.email = email;
        this.password = password;
        this.isGoogleLogin = isGoogleLogin;
        this.role = role;
    }

    public LoginRequest() {}

    public int getRole() {
        return role;
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

    public Boolean getIsGoogleLogin() {
        return isGoogleLogin;
    }

    public void setIsGoogleLogin(boolean isGoogleLogin) {
        this.isGoogleLogin = isGoogleLogin;
    }
}
