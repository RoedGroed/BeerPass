package BE;

public class Ticket {
    private int ticketID;
    private String ticketName;
    private String ticketType;

    public Ticket(int ticketID, String ticketName, String ticketType) {
        this.ticketID = ticketID;
        this.ticketName = ticketName;
        this.ticketType = ticketType;
    }

    public Ticket() {

    }
    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
}
