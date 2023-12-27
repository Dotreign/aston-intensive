package context;

public class UserAuthContext {

    private static UserAuthContext instance;
    private String username;
    private boolean isAuthenticated = false;

    private UserAuthContext() {

    }

    public static UserAuthContext getInstance() {
        if (instance == null) {
            instance = new UserAuthContext();
        }
        return instance;
    }

    public void authenticate(String username) {
        this.username = username;
        isAuthenticated = true;
    }

    public String getAuthenticatedUsername() {
        return username;
    }
    public boolean checkIsAuthenticated() {
        return isAuthenticated;
    }

}
