package GUI.Model;

import BE.User;
import BLL.Manager;

import DAL.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Model {
    private String role;
    private String username;
    private String email;
    private String password;


    //private UserDAO userDAO;
    private Manager manager;
    private User currentUser;

    public Model( ) {
        //userDAO = new UserDAO();
        manager = new Manager();
    }

    /*public boolean isValidUser(String username, String password) throws SQLException, IOException {
        return userDAO.isValidUser(username, password);
    }*/

    public ObservableList<User>getUsersByRole(String role){
        ObservableList<User> filteredUsers = FXCollections.observableArrayList();
        try {
            List<User> allUsers = manager.getAllUsers();
            for (User user : allUsers) {
                String userRole = user.getRole();
                if (userRole != null && userRole.equalsIgnoreCase(role)) {
                    filteredUsers.add(user);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return filteredUsers;
    }

    public void updateUser(User selectedUser) throws SQLException, IOException {
        manager.updateUser(selectedUser);
    }

    public List<User> getAllUsers() throws SQLException, IOException {
        return manager.getAllUsers();
    }



    public User validateUser(String email, String password) throws SQLException, IOException {
        currentUser = manager.validateUser(email, password);
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser != null && "Admin".equals(currentUser.getRole());
    }

    public Model(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
