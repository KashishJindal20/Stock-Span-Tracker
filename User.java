// User.java
public class User {
    private String username;
    private String passwordHash;
    private StockTracker tracker;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.tracker = new StockTracker();
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public StockTracker getTracker() { return tracker; }
}
