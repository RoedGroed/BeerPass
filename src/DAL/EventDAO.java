package DAL;

import BE.Event;
import BE.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    /**
     * Creating an event in the database
     * @param event the event being created
     */
    public Event createEvent(Event event) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try(Connection conn = dbConnector.getConnection()) {
            String sql = "INSERT INTO Events (Name,Location,Time,Note,TicketLimit,ImagePath) VALUES (?,?,?,?,?,?)";
            try(PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, event.getName());
                preparedStatement.setString(2, event.getLocation());
                preparedStatement.setString(3, event.getTime());
                preparedStatement.setString(4, event.getNote());
                preparedStatement.setInt(5, event.getTicketLimit());
                preparedStatement.setString(6, event.getImagePath());

                preparedStatement.executeUpdate();
            }
        }
        return event;
    }

    /**
     * A method for updating the event in the database
     * @param selectedEvent the event being updated
     */

    public void updateEvents(Event selectedEvent) throws SQLException, IOException{
        String updateQuery = "UPDATE Events SET Name = ?, Location = ?, Time = ?, Note = ?, TicketLimit = ?, ImagePath = ? WHERE EventID = ?";
        DBConnector dbConnector = new DBConnector();
        try (Connection connection = dbConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)){

            preparedStatement.setString(1, selectedEvent.getName());
            preparedStatement.setString(2, selectedEvent.getLocation());
            preparedStatement.setString(3, selectedEvent.getTime());
            preparedStatement.setString(4, selectedEvent.getNote());
            preparedStatement.setInt(5, selectedEvent.getTicketLimit());
            preparedStatement.setString(6, selectedEvent.getImagePath());
            preparedStatement.setInt(7, selectedEvent.getEventID());

            preparedStatement.executeUpdate();
        }
    }

    /**
     * A method which retrieves the information about all events in the database
     */

    public List<Event> readAllEvents() throws SQLException, IOException{
        DBConnector dbConnector = new DBConnector();
        List<Event> events = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection()) {
            String sql = "SELECT * FROM dbo.Events";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Event event = new Event();
                    event.setEventID(resultSet.getInt("EventID"));
                    event.setName(resultSet.getString("Name"));
                    event.setLocation(resultSet.getString("Location"));
                    event.setTime(resultSet.getString("Time"));
                    event.setNote(resultSet.getString("Note"));
                    event.setTicketLimit(resultSet.getInt("TicketLimit"));
                    event.setImagePath(resultSet.getString("ImagePath"));
                    events.add(event);
                }
            }
        }
        return events;
    }

    /**
     * Retrieving the events information that is linked to an Event Coordinator
     * @param userId the id of the event coordinator
     */
    public List<Event> readEventsForCoordinator(int userId) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        List<Event> events = new ArrayList<>();

        String sql = "SELECT e.* FROM Events e JOIN Event_Coordinator_assignment a ON e.EventID = a.EventID WHERE a.UserID = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Event event = new Event();
                    event.setEventID(resultSet.getInt("EventID"));
                    event.setName(resultSet.getString("Name"));
                    event.setLocation(resultSet.getString("Location"));
                    event.setTime(resultSet.getString("Time"));
                    event.setNote(resultSet.getString("Note"));
                    event.setTicketLimit(resultSet.getInt("TicketLimit"));
                    event.setImagePath(resultSet.getString("ImagePath"));
                    events.add(event);
                }
            }
        }
        return events;
    }



    ///// UTILITY /////

    /**
     * Deletes an event and its associated tickets from the database
     * @param eventId the event being deleted
     */
    public void deleteEvent(int eventId) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection()) {
            deleteEventTickets(conn, eventId);
            deleteTheEvent(conn, eventId);
        }
    }

    /**
     * A method which deletes the tickets associated to a specific event
     * @param eventId
     */
    private void deleteEventTickets(Connection conn, int eventId) throws SQLException {
        String deleteTicketsSql = "DELETE FROM EventTickets WHERE EventID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteTicketsSql)) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
        }
    }

    /**
     * A method which deletes a specific event
     */
    private void deleteTheEvent(Connection conn, int eventId) throws SQLException {
        String deleteEventSql = "DELETE FROM Events WHERE EventID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteEventSql)) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
        }
    }

    /**
     * Method which get the number of total tickets sold for specific event
     * @param eventId the event
     */
    public int getSoldTicketsCount(int eventId) throws SQLException, IOException {
        String sql = "SELECT COUNT(*) AS sold_tickets_count " +
                "FROM TicketUser " +
                "WHERE EventID = ?";
            DBConnector dbConnector = new DBConnector();
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement prep = conn.prepareStatement(sql)) {
            prep.setInt(1, eventId);
            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("sold_tickets_count");
                } else {
                    return 0; // No tickets sold for the event
                }
            }
        }
    }

}
