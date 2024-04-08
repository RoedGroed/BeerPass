package GUI.Model;

import BE.Event;
import BE.User;
import BLL.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.File;
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

    public String[] divorceTimeString(String formattedDateTime) {
        String[] parts = formattedDateTime.split(" ", 2); // Splits String into an array
        String stringDate = parts[0];
        String time = parts[1];

        LocalDate date = LocalDate.parse(stringDate);

        return new String[] { date.toString(), time };
    }





}
