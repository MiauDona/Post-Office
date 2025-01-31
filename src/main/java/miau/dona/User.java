package miau.dona;

public class User {
    private int id = 0;
    private String username;
    private int age;
    private boolean admin = false;
    
    public User(String username, int age, boolean admin) {
        this.id = incrementId();
        this.username = username;
        this.age = age;
        this.admin = admin;
        
        
    }
    
    public User(String username, boolean admin) {
        this.username = username;
        this.admin = admin;
    }
    
    private int incrementId() {
        id++;
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
    