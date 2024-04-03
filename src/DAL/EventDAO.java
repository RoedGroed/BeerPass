package DAL;

import BE.Event;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventDAO {

    public Event createEvent(Event event) throws SQLException, IOException {
        DBConnector dbConnector = new DBConnector();
        try(Connection conn = dbConnector.getConnection()) {
            String sql = "INSERT INTO Event (Name,Location,Time,Note,TicketLimit,ImagePath) VALUES (?,?,?,?,?,?)";
            try(PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1,event.getName());
                preparedStatement.setString(2,event.getLocation());
                preparedStatement.setString(3,event.getTime());
                preparedStatement.setString(4,event.getNote());
                preparedStatement.setInt(5,event.getTicketLimit());
                preparedStatement.setString(6, event.getImagePath());

                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
            e.printStackTrace();
            }
        }
        return event;
    }

    public void deleteEvent(){
        // Delete Event.
    }

    public void updateEvents(){}

    public void readAllEvent(){}

    public void readSomeOfTheEventMaybeIDontKnowForSureButItCouldBeOfUseWithTicketsAndUsersMaybe (){}

}
