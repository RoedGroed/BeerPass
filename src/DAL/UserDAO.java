package DAL;

import BE.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /*public boolean isValidUser(String username, String password) throws SQLException, IOException {
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
    }*/

    public User getUserByNameAndPassword(String username, String password) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT UserID, UserName, Password, Email, Role FROM Users WHERE UserName = ? AND Password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Consider hashing in real applications

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                                rs.getInt("UserID"),
                                rs.getString("UserName"),
                                rs.getString("Password"),
                                rs.getString("Email"),
                                rs.getString("Role")
                        );
                    }
                }
            }
        }
        return null;
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
                String password = rs.getString("password");

                User user = new User(userID, userName, password, email, role);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new SQLException("Could not retrieve users from database", e);
        }
        return allUsers;
    }


    public void deleteUser(int userId) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "DELETE FROM Users WHERE UserID = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, userId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createNewUser(String username, String password, String role, String email) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "INSERT INTO Users (Username, Password, Role, Email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, role);
                statement.setString(4, email);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateUser(User selectedUser) throws SQLException, IOException {
        String updateQuery = "UPDATE Users SET UserName = ?, Password = ?, Role = ?, Email = ? WHERE UserID = ?";
        DBConnector dbConnector = new DBConnector();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, selectedUser.getUsername());
            preparedStatement.setString(2, selectedUser.getPassword());
            preparedStatement.setString(3, selectedUser.getRole());
            preparedStatement.setString(4, selectedUser.getEmail());
            preparedStatement.setInt(5, selectedUser.getUserID());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Could not update user", ex);
        }
    }


}


