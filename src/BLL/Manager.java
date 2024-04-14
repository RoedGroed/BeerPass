package BLL;

import BE.Event;
import BE.Ticket;
import BE.User;
import DAL.EventDAO;
import DAL.TicketDAO;
import DAL.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Manager {
    private UserDAO userDAO = new UserDAO();
    private EventDAO eventDAO = new EventDAO();
    private TicketDAO ticketDAO = new TicketDAO();

    public List<User> getAllUsers() throws SQLException, IOException {
        return userDAO.readAllUsers();
    }

    public void updateUser(User selectedUser) throws SQLException, IOException {
        userDAO.updateUser(selectedUser);
    }


    public User validateUser(String email, String password) throws SQLException, IOException {
        User user = userDAO.getUserByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())){
            return user;
        }
        return null;
    }

        public Manager() {
            this.userDAO = new UserDAO();
        }

    public void createNewUser(String username, String password, String role, String email) throws SQLException, IOException {
        User user = new User(username, password, email);
        user.setRole(role);
        userDAO.createNewUser(user);
    }

    ///EVENT MANAGEMENT///

    public Event createEvent (Event selectedEvent) throws IOException, SQLException {
        return eventDAO.createEvent(selectedEvent);
    }

    public void deleteEvent(Event selectedEvent) throws SQLException, IOException {
        eventDAO.deleteEvent(selectedEvent.getEventID());
    }

    public void updateEvents(Event selectedEvent) throws SQLException, IOException {
        eventDAO.updateEvents(selectedEvent);
    }

    public Manager(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public List<Event> getAllEvents() throws SQLException, IOException {
        return eventDAO.readAllEvents();
    }

    public List<Event> getEventsForEventCo(int userId) throws SQLException, IOException {
        return eventDAO.readEventsForCoordinator(userId);
    }
    public void addTicket(String ticketName, String ticketType) throws IOException {
        ticketDAO.addTicket(ticketName, ticketType);
    }
    public List<Ticket> getAllTickets() throws SQLException, IOException {
        return ticketDAO.readAllTickets();
    }
    public void linkTicketToEvent(int eventID, int ticketID) throws SQLException, IOException {
        ticketDAO.linkTicketToEvent(eventID, ticketID);
    }
    public List<Ticket> getLinkedTickets(int eventID) throws SQLException, IOException {
        return ticketDAO.getLinkedTickets(eventID);
    }
    public void removeTicketFromEvent(int eventID, int ticketID) throws SQLException, IOException {
        ticketDAO.removeTicketFromEvent(eventID, ticketID);
    }
    public int getTicketIDByName(String selectedTicket) throws SQLException, IOException {
        return ticketDAO.getTicketIDByName(selectedTicket);
    }
    public void deleteTicket(int ticketID) throws SQLException, IOException {
        ticketDAO.deleteTicket(ticketID);
    }
    public void linkTicketToUser(int userID, int ticketID, int eventID) throws SQLException, IOException {
        ticketDAO.linkUserToTicket(userID, ticketID, eventID);
    }

    public List<User> readAllEventCoordinators() throws SQLException, IOException {
        return userDAO.readAllEventCoordinators();
    }
    public List<User> readAllAssignedEventCoordinators(int eventID) throws SQLException, IOException {
       return userDAO.getAssignedEventCoordinators(eventID);
    }

    public void addCoordinator(int eventID, int userID) throws IOException, SQLException {
        userDAO.addCoordinator(eventID,userID);
    }

    public void removeCoordinator(int eventID, int userID) throws SQLException, IOException {
        userDAO.removeCoordinator(eventID,userID);
    }

    public void deleteUser(int userID) throws SQLException, IOException {
        userDAO.deleteUser(userID);
    }

    ///// INPUT VALIDATION /////


    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean validateEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static boolean validateStringLength(String input, int maxLength) {
        return input != null && input.length() <= maxLength;

    }


    static final String TIME_REGEX = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";

    public static boolean validateTime(String eventTime) {
        // Replace any of the decimal dividers with ":"
        String validatedTime = eventTime.replaceAll("[.,;-]", ":");
        return validatedTime.matches(TIME_REGEX);
    }




    public int getSoldTicketsCount(int eventID) throws SQLException, IOException {
       return eventDAO.getSoldTicketsCount(eventID);
    }

}

