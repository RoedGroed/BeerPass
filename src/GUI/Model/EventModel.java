package GUI.Model;

import BE.Event;
import BLL.Manager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class EventModel {

    private Manager manager;

    public EventModel (){
        manager = new Manager();
    }

    public void createEvent(Event event) throws SQLException, IOException {
        Event createdEvent = manager.createEvent(event);

        // Update the event beings shown here also maybe, such it updates realtime.
    }

    public void deleteEvent(Event selectedEvent) throws SQLException, IOException {
        manager.deleteEvent(selectedEvent);
    }

    public void updateEvent(Event selectedEvent) throws SQLException, IOException {
        manager.updateEvents(selectedEvent);
    }

    public void readAllEvents(){}

    private void reformatTime(){

    }

    /**
     * Method to format time to a string, so it fits into database.
     * @param timeDate
     * @param timeStart
     * @return
     */
    public String formatTimeToString(LocalDate timeDate, String timeStart){
        String formatDate = timeDate != null ? timeDate.toString() : "";

        return formatDate + " " + timeStart;
    }

    /* TODO: Lav en metode der skiller Time String, og laver første del til en LocalDate igen.
        og sender resten af String videre i tfEventTtime.
        lav et boolean flag, det checker om time og date er blevet ændret, hvis den er det, så skal den køre

    */


}
