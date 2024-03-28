package GUI.Model;

import DAL.DBConnector;
import DAL.UserDAO;

import java.io.IOException;
import java.sql.SQLException;

public class Model {
    private UserDAO userDAO;

    public Model( ) {
        userDAO = new UserDAO();
    }

    public boolean isValidUser(String username, String password) throws SQLException, IOException {
        return userDAO.isValidUser(username, password);
    }
}
