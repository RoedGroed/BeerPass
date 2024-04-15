package BE;

import java.util.UUID;

public class Ticket {
    private int ticketID;
    private String ticketName;
    private String ticketType;
    private UUID uuid;

    public Ticket(int ticketID, String ticketName, String ticketType) {
        this.ticketID = ticketID;
        this.ticketName = ticketName;
        this.ticketType = ticketType;
        this.uuid = UUID.randomUUID(); // Generate UUID
    }

    public Ticket(int ticketID, String ticketName, String ticketType, UUID uuid) {
        this.ticketID = ticketID;
        this.ticketName = ticketName;
        this.ticketType = ticketType;
        this.uuid = uuid;
    }

    public Ticket() {
        // Initialize the uuid field with a new UUID
        this.uuid = UUID.randomUUID();
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTicketData() {
        return "Ticket ID: " + ticketID + "\n" +
                "Ticket Name: " + ticketName + "\n" +
                "Ticket Type: " + ticketType + "\n" +
                "UUID: " + uuid.toString();
    }
}
