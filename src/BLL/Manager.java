package BLL;

import BE.User;
import DAL.UserDAO;
import GUI.Model.Model;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Manager {
    private UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() throws SQLException, IOException {
        return userDAO.readAllUsers();
    }

    public void updateUser(User selectedUser) throws SQLException, IOException {
        userDAO.updateUser(selectedUser);
    }

    /*public User validateUser(String username, String password) throws SQLException, IOException {
        return userDAO.getUserByNameAndPassword(username, password);
    }*/

    public User validateUser(String email, String password) throws SQLException, IOException {
        User user = userDAO.getUserByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())){
            return user;
        }
        return null;
    }

        public Manager() {
            this.userDAO = new UserDAO();
        }

    public void createNewUser(String username, String password, String role, String email) throws SQLException, IOException {
        Model model = new Model(username, email, password, role);
        userDAO.createNewUser(model);
    }
}

