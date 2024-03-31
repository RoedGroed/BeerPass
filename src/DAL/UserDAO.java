package DAL;

import BE.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean isValidUser(String username, String password) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT * FROM Users WHERE UserName = ? AND Password = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurred
        }
    }

    public List<User> readAllUsers() throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        List<User> allUsers = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Users;";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int userID = rs.getInt("UserID");
                String userName = rs.getString("UserName");
                String email = rs.getString("Email");
                String role = rs.getString("Role");

                User user = new User(userID, userName, null, email, role);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new SQLException("Could not retrieve users from database", e);
        }
        return allUsers;
    }


    public boolean deleteUser(int userId) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "DELETE FROM Users WHERE UserID = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, userId);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0; // Return true if a row was deleted
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurred
        }
    }
    public boolean createNewUser(String username, String password, String role, String email) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "INSERT INTO Users (Username, Password, Role, Email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, role);
                statement.setString(4, email);

                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0; // Return true if a row was inserted
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurred
        }
    }
}


