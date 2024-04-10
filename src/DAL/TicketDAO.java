package DAL;

import BE.Event;
import BE.Ticket;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public void addTicket(String ticketName, String ticketType) throws IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "INSERT INTO Tickets (Name, Type) VALUES (?,?)";
            try (PreparedStatement prep = conn.prepareStatement(query)) {
                prep.setString(1, ticketName);
                prep.setString(2, ticketType);
                prep.executeUpdate();
            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ticket> readAllTickets() throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        List<Ticket> tickets = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection()) {
            String query = "SELECT * FROM dbo.Tickets";
            try (PreparedStatement prep = conn.prepareStatement(query)) {
                ResultSet resultSet = prep.executeQuery();

                while(resultSet.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketID(resultSet.getInt("TicketID"));
                    ticket.setTicketName(resultSet.getString("Name"));
                    ticket.setTicketType(resultSet.getString("Type"));
                    tickets.add(ticket);
                }
            }
        }
        return tickets;
    }

    public void linkTicketToEvent(int eventID, int ticketID) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "INSERT INTO EventTickets (EventID, TicketID) VALUES (?,?)";
            try (PreparedStatement prep = conn.prepareStatement(query)) {
                prep.setInt(1, eventID);
                prep.setInt(2, ticketID);
                prep.executeUpdate();
            }
        }
    }
    public List<Ticket> getLinkedTickets(int eventID) throws SQLException, IOException {
        List<Ticket> linkedTickets = new ArrayList<>();
        String query = "SELECT * FROM EventTickets WHERE EventID =?";
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setInt(1, eventID);

            try (ResultSet resultSet = prep.executeQuery()) {
                while (resultSet.next()) {
                    int ticketID = resultSet.getInt("TicketID");
                    Ticket ticket = getTicketByID(ticketID);
                    if (ticket != null) {
                        linkedTickets.add(ticket);
                    }
                }
            }
        }
        return linkedTickets;
    }
    private Ticket getTicketByID(int ticketID) throws SQLException, IOException {
        String query = "SELECT * FROM Tickets WHERE TicketID = ?";
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, ticketID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve ticket details from the result set
                    int id = resultSet.getInt("TicketID");
                    String name = resultSet.getString("Name");
                    String type = resultSet.getString("Type");
                    // Create and return the Ticket object
                    return new Ticket(id, name, type);
                }
            }
        }
        // Return null if ticket with the given ID is not found
        return null;
    }
    public int getTicketIDByName(String ticketName) throws SQLException, IOException {
        String query = "SELECT TicketID FROM Tickets WHERE Name = ?";
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, ticketName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the ticket ID from the result set
                    return resultSet.getInt("TicketID");
                }
            }
        }
        // Return -1 if ticket with the given name is not found
        return -1;
    }

    public void removeTicketFromEvent (int eventID, int ticketID) throws IOException, SQLException {
        DBConnector dbConnector = new DBConnector();
        String query = "DELETE FROM EventTickets WHERE EventID = ? AND TicketID = ?";

        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement prep = conn.prepareStatement(query);

            prep.setInt(1, eventID);
            prep.setInt(2, ticketID);
            prep.executeUpdate();
        }
    }

    public void deleteTicket(int ticketID) throws IOException, SQLException {
        DBConnector dbConnector = new DBConnector();
        String query = "DELETE FROM Tickets WHERE TicketID = ?";

        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement prep = conn.prepareStatement(query);

            prep.setInt(1, ticketID);
            prep.executeUpdate();
        }
    }
    public void linkUserToTicket(int userID, int ticketID, int eventID) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            String query = "INSERT INTO TicketUser (UserID, TicketID, EventID) VALUES (?,?,?)";
            try (PreparedStatement prep = conn.prepareStatement(query)) {
                prep.setInt(1, userID);
                prep.setInt(2, ticketID);
                prep.setInt(3, eventID);
                prep.executeUpdate();
            }
        }
    }
}
