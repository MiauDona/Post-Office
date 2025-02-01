package miau.dona;

public class User {
    private static int id = 0;
    private final String username;
    private boolean admin = false;
    
    public User(String username, boolean admin) {
        id++;
        this.username = username;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }
}
    