package GUI.Model;

import BE.Ticket;
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
    private static Model instance;
    private Manager manager;
    private User currentUser;

    private Model( ) {
        manager = new Manager();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

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

    public ObservableList<User> getAllUsers() throws SQLException, IOException {
        ObservableList<User> Users = FXCollections.observableArrayList();
        try {
            List<User> allUsers = manager.getAllUsers();
            for (User user : allUsers) {
                Users.add(user);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Users;
    }

    public void updateUser(User selectedUser) throws SQLException, IOException {
        manager.updateUser(selectedUser);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User validateUser(String email, String password) throws SQLException, IOException {
        currentUser = manager.validateUser(email, password);
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser != null && "Admin".equals(currentUser.getRole());
    }

    public void addTicket(String ticketName, String ticketType) throws IOException {
        manager.addTicket(ticketName, ticketType);
    }
    public List<Ticket> getAllTickets() throws SQLException, IOException {
        return manager.getAllTickets();
    }
    public void linkTicketToEvent(int eventID, int ticketID) throws SQLException, IOException {
        manager.linkTicketToEvent(eventID, ticketID);
    }

}

