package BE;

public class Event {

    private int eventID;
    private String Name;
    private String Location;
    private String Time;
    private String Note;
    private int ticketLimit;
    private String imagePath;

    public Event(int eventID, String name, String location, String time, String note, int ticketLimit, String imagePath) {
        this.eventID = eventID;
        Name = name;
        Location = location;
        Time = time;
        Note = note;
        this.ticketLimit = ticketLimit;
        this.imagePath = imagePath;
    }

    public Event() {
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getTicketLimit() {
        return ticketLimit;
    }

    public void setTicketLimit(int ticketLimit) {
        this.ticketLimit = ticketLimit;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
