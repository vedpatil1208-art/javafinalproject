import java.time.LocalDate;
import java.util.LinkedList;

// Event class
public class Event {

    private int eventId;
    private String eventName;
    private String category;
    private LocalDate date;
    private int capacity;
    private Organizer organizer;

    // Array used for event capacity
    private Participant[] participants;
    private int participantCount;

    // LinkedList for event schedule
    private LinkedList<String> schedule;

    // Constructor
    public Event(int eventId, String eventName, String category,
                 LocalDate date, int capacity, Organizer organizer) {

        if (eventId <= 0)
            throw new IllegalArgumentException("Invalid Event ID");

        if (eventName == null || eventName.trim().isEmpty())
            throw new IllegalArgumentException("Event name cannot be empty");

        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be greater than 0");

        this.eventId = eventId;
        this.eventName = eventName;
        this.category = category;
        this.date = date;
        this.capacity = capacity;
        this.organizer = organizer;

        // Array
        this.participants = new Participant[capacity];

        // LinkedList
        this.schedule = new LinkedList<>();
    }

    // Getters
    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public Participant[] getParticipants() {
        return participants;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public LinkedList<String> getSchedule() {
        return schedule;
    }

    // Add participant
    public void addParticipant(Participant participant) {

        if (participantCount >= capacity) {
            throw new IllegalStateException("Event capacity is full!");
        }

        participants[participantCount] = participant;
        participantCount++;
    }

    // Remove participant
    public void removeParticipant(int participantId) {

        for (int i = 0; i < participantCount; i++) {

            if (participants[i].getParticipantId() == participantId) {

                for (int j = i; j < participantCount - 1; j++) {
                    participants[j] = participants[j + 1];
                }

                participants[participantCount - 1] = null;
                participantCount--;

                return;
            }
        }
    }

    // Add schedule
    public void addSchedule(String time, String activity) {

        if (time == null || activity == null ||
            time.trim().isEmpty() || activity.trim().isEmpty()) {

            throw new IllegalArgumentException("Invalid schedule");
        }

        schedule.add(time + " - " + activity);
    }

    @Override
    public String toString() {

        return "Event ID: " + eventId +
                "\nName: " + eventName +
                "\nCategory: " + category +
                "\nDate: " + date +
                "\nCapacity: " + capacity +
                "\nRegistered: " + participantCount +
                "\nOrganizer: " + organizer.getName();
    }
}


// Organizer class
class Organizer {

    private int organizerId;
    private String name;
    private String department;

    public Organizer(int organizerId, String name, String department) {

        if (organizerId <= 0)
            throw new IllegalArgumentException("Invalid Organizer ID");

        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Organizer name required");

        this.organizerId = organizerId;
        this.name = name;
        this.department = department;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return organizerId + " - " + name + " (" + department + ")";
    }
}