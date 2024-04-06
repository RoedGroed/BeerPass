package DAL;

import BE.Event;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

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
            catch (SQLException e) {
            e.printStackTrace();
            }
        }
        return event;
    }

    public void deleteEvent(Event event) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try(Connection conn = dbConnector.getConnection()) {
            String sql = "DELETE FROM Events WHERE EventID = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)){

                preparedStatement.setInt(1,event.getEventID());
                preparedStatement.executeUpdate();
            }
        }
    }

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

            preparedStatement.executeUpdate();
        }
    }

    public List<Event> readAllEvents() throws SQLException, IOException{
        DBConnector dbConnector = new DBConnector();
        List<Event> events = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection()) {
            String sql = "SELECT * FROM dbo.Events";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Event event = new Event();
                    event.setName(resultSet.getString("Name"));
                    event.setLocation(resultSet.getString("Location"));
                    event.setTime(resultSet.getString("Time"));
                    event.setNote(resultSet.getString("Note"));
                    event.setTicketLimit(resultSet.getInt("TicketLimit"));
                    event.setImagePath(resultSet.getString("ImagePath"));
                    events.add(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return events;
    }

    public void readSomeOfTheEventMaybeIDontKnowForSureButItCouldBeOfUseWithTicketsAndUsersMaybe (){}

}
