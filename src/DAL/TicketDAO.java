package DAL;

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
}
