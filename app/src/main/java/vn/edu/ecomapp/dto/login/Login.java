package vn.edu.ecomapp.dto.login;

public class Login {
    String email, password;
    boolean isRemember;

    public  Login() {}

    public Login(String email, String password, boolean isRemember) {
        this.email = email;
        this.password = password;
        this.isRemember = isRemember;
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

    public boolean getIsRemember() {
        return isRemember;
    }

    public void setRemember(boolean remember) {
        isRemember = remember;
    }
}
