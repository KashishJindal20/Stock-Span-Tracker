// UserManager.java
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>();
    private final String USER_FILE = "users.txt";

    public UserManager() {
        loadUsers();
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) return false;
        String hashed = PasswordUtil.hashPassword(password);
        User newUser = new User(username, hashed);
        users.put(username, newUser);
        saveUsers();
        return true;
    }

    public User authenticateUser(String username, String password) {
        if (!users.containsKey(username)) return null;
        String inputHash = PasswordUtil.hashPassword(password);
        User user = users.get(username);
        if (user.getPasswordHash().equals(inputHash)) {
            return user;
        }
        return null;
    }

    public void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPasswordHash());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (FileNotFoundException e) {
            // First time: no file
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }
}
