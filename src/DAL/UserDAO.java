package DAL;

import BE.User;
import GUI.Model.Model;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.mindrot.jbcrypt.BCrypt;

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

    /*public User getUserByNameAndPassword(String username, String password) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT UserID, UserName, Password, Email, Role FROM Users WHERE UserName = ? AND Password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

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
    }*/

    public User getUserByEmail(String email) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT UserID, UserName, Password, Email, Role FROM Users WHERE Email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);

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

    public void createNewUser(User user) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
            String query = "INSERT INTO Users (Username, Password, Role, Email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, user.getUsername());
                //statement.setString(2, password);
                statement.setString(2, hashedPassword);
                statement.setString(3, user.getRole());
                statement.setString(4, user.getEmail());
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

            String hashedPassword = BCrypt.hashpw(selectedUser.getPassword(), BCrypt.gensalt(10));

            preparedStatement.setString(1, selectedUser.getUsername());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, selectedUser.getRole());
            preparedStatement.setString(4, selectedUser.getEmail());
            preparedStatement.setInt(5, selectedUser.getUserID());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Could not update user", ex);
        }
    }

    /**
     * Method to read all Event coordinators.
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public List<User> readAllEventCoordinators() throws SQLException, IOException {
        List<User> allUsers = readAllUsers();
        List<User> eventCoordinators = new ArrayList<>();
        for (User user : allUsers) {
            if ("Event Coordinator".equals(user.getRole())) {
                eventCoordinators.add(user);
            }
        }
        return eventCoordinators;
    }

    /**
     * Get all the assigned EC to the specific Event.
     * @param eventId
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public List<User> getAssignedEventCoordinators(int eventId) throws SQLException, IOException {
        List<User> assignedCoordinators = new ArrayList<>();
        DBConnector dbConnector = new DBConnector();
        String sql = "SELECT u.UserID, u.UserName, u.Email FROM Users u " +
                     "INNER JOIN Event_Coordinator_assignment ec ON u.UserID = ec.UserID " +
                     "WHERE ec.EventID = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int userID = rs.getInt("UserID");
                    String userName = rs.getString("UserName");
                    String email = rs.getString("Email");

                    User user = new User(userID, userName, email);
                    assignedCoordinators.add(user);
                }
            }
        }
        return assignedCoordinators;
    }

    public void addCoordinator(int EventID, int userID) throws IOException, SQLException {
        DBConnector dbConnector = new DBConnector();
        String sql = "INSERT INTO Event_Coordinator_assignment (EventID, UserID) VALUES (?, ?)";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, EventID);
            ps.setInt(2, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Could not add coordinator to this event",e);
        }
    }

    public void removeCoordinator(int EventID, int userID) throws IOException, SQLException {
        DBConnector dbConnector = new DBConnector();
        String sql = "DELETE FROM Event_Coordinator_assignment WHERE EventID = ? AND UserID = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, EventID);
            ps.setInt(2, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Could not remove coordinator to this event",e);
        }
    }

}



