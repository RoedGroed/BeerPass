package GUI.Model;

import BE.Event;
import BE.User;
import BLL.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EventModel {

    private Manager manager;


    public EventModel (){
        //this.model = Model.getInstance();
        manager = new Manager();
    }

    public void createEvent(Event event) throws SQLException, IOException {
        manager.createEvent(event);

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

    public ObservableList<User> getUsersByRole(String role) throws SQLException, IOException {
        ObservableList<User> filteredUsers = FXCollections.observableArrayList();
        try {
            List<User> allUsers = manager.getAllUsers();
            for (User user : allUsers) {
                String userRole = user.getRole();
                if (userRole != null && userRole.equalsIgnoreCase(role)) {
                    filteredUsers.add(user);
                }
            }
        } finally {

        }
        return filteredUsers;
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

    public List<User> readAllEventCoordinators() throws SQLException, IOException {
       return manager.readAllEventCoordinators();
    }

    public List<User> readAllAssignedEventCoordinators(int eventID) throws SQLException, IOException {
       return manager.readAllAssignedEventCoordinators(eventID);
    }

    public void addCoordinator(int eventID, int userID) throws IOException, SQLException {
        manager.addCoordinator(eventID, userID);
    }

    public int getSoldTicketsCount(int eventID) throws SQLException, IOException {
        return manager.getSoldTicketsCount(eventID);

    }

    public void removeCoordinator(int eventID, int userID) throws SQLException, IOException {
        manager.removeCoordinator(eventID,userID);
    }

    //// Input Validation
    public boolean validateStringLength(String input, int maxLength) {
        return manager.validateStringLength(input, maxLength);
    }

    public boolean validateTime(String eventTime) {
        return manager.validateTime(eventTime);
    }

    /**
     * Method that validates ALL the fields in an event.
     * @param name
     * @param location
     * @param date
     * @param time
     * @param note
     * @param ticketLimit
     * @param selectedImage
     * @return
     */
    public boolean validateInputs(String name, String location, LocalDate date, String time, String note, int ticketLimit, String selectedImage) {
        if (name.isEmpty()) {
            showAlert("Name cannot be empty.");
            return false;
        } else if (!validateStringLength(name, 255)) {
            showAlert("Name cannot exceed 255 characters.");
            return false;
        }

        if (location.isEmpty()) {
            showAlert("Location cannot be empty.");
            return false;
        } else if (!validateStringLength(location, 255)) {
            showAlert("Location cannot exceed 255 characters.");
            return false;
        }

        if (date == null || time.isEmpty()) {
            showAlert("Please select a date and time for the event.");
            return false;
        }

        if (date.isBefore(LocalDate.now())) {
            showAlert("Event date cannot be in the past.");
            return false;
        }

        if (!validateTime(time)) {
            showAlert("Invalid event time format. Please enter time in HH:MM format.");
            return false;
        }

        if (!validateStringLength(note, 255)) {
            showAlert("Note cannot exceed 255 characters.");
            return false;
        }

        if (ticketLimit <= 0) {
            showAlert("Ticket limit must be greater than zero.");
            return false;
        }

        if (selectedImage == null || selectedImage.isEmpty()) {
            showAlert("Please select an image for the event.");
            return false;
        }

        return true;
    }

    public void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, content);
        alert.setTitle("Invalid Input");
        alert.showAndWait();
    }

}
