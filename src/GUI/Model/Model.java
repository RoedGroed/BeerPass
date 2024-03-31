package GUI.Model;

import BE.User;
import BLL.Manager;
import DAL.DBConnector;
import DAL.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Model {
    private String role;


    private UserDAO userDAO;
    private Manager manager;

    public Model( ) {
        userDAO = new UserDAO();
        manager = new Manager();
    }

    public boolean isValidUser(String username, String password) throws SQLException, IOException {
        return userDAO.isValidUser(username, password);
    }

    public ObservableList<User>getUsersByRole(String role){
            ObservableList<User> filteredUsers = FXCollections.observableArrayList();
            try {
                List<User> allUsers = manager.getAllUsers();
                for (User user : allUsers) {
                    if (user.getRole().equalsIgnoreCase(role)) {
                        filteredUsers.add(user);
                    }
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace(); //håndter med alert
            }
            return filteredUsers;
        }

}
