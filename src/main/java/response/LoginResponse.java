package response;


import model.User;

public class LoginResponse extends Response {

    public User user;
    public String auth;

    public LoginResponse(String reason) {
        super(false, reason);
    }

    public LoginResponse(boolean success, User user) {
        super(success);
        this.user = user;
    }

    public LoginResponse(User user, boolean success, String message) {
        super(success, message);
        this.user = user;
    }

    public LoginResponse(boolean success) {
        super(success);
    }

    public LoginResponse(User user){
        super(true);
        this.user = user;
    }

    public LoginResponse(boolean success, String message) {
        super(success, message);
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
