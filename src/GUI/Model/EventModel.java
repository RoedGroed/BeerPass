package GUI.Model;

import BE.Event;
import BE.User;
import BLL.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EventModel {

    private Manager manager;
    //private Model model;


    public EventModel (){
        //this.model = Model.getInstance();
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

    public List<Event> getAllEvents() throws SQLException, IOException {
        return manager.getAllEvents();
    }

    public List<Event> getEventsForEventCo(int userID) throws SQLException, IOException {
        return manager.getEventsForEventCo(userID);
    }

    public ObservableList<User> getUsersByRole(String role){
        ObservableList<User> filteredUsers = FXCollections.observableArrayList();
        try {
            List<User> allUsers = manager.getAllUsers();
            for (User user : allUsers) {
                String userRole = user.getRole();
                if (userRole != null && userRole.equalsIgnoreCase(role)) {
                    filteredUsers.add(user);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return filteredUsers;
    }

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
