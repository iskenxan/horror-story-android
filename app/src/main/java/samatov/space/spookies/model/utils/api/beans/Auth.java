package samatov.space.spookies.model.utils.api.beans;

import samatov.space.spookies.model.utils.api.middleware.AuthMiddleware;

public class Auth extends AuthMiddleware {
    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
