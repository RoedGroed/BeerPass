package GUI.Model;

import BE.Event;
import BLL.Manager;

import java.io.IOException;
import java.sql.SQLException;

public class EventModel {

    private Manager manager;

    public EventModel (){
        manager = new Manager();
    }

    public void createEvent(Event event) throws SQLException, IOException {
        Event createdEvent = manager.createEvent(event);

        // Update the event beings shown here also maybe, such it updates realtime.
    }

    public void deleteEvent(){
        // Delete Event.
    }

    public void updateEvent(){}

    public void readAllEvents(){}

}
