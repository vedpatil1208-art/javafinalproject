import java.time.LocalDate;
import java.util.LinkedList;

public class Event {

    // Event details
    private int id, capacity, count;
    private String name, category;
    private LocalDate date;
    private Organizer organizer;
    private Participant[] participants;
    private LinkedList<String> schedule = new LinkedList<>();

    // Constructor: creates an Event object
    public Event(int id, String name, String category, LocalDate date, int capacity, Organizer organizer) {
        if (id <= 0) throw new IllegalArgumentException("Invalid Event ID");
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than 0");
        if (organizer == null) throw new IllegalArgumentException("Organizer required");

        this.id = id;
        this.capacity = capacity;
        this.organizer = organizer;
        this.participants = new Participant[capacity];

        update(name, category, date);
    }

    // Updates and validates event information
    public void update(String name, String category, LocalDate date) {
        if (name == null || name.trim().isEmpty() ||
            category == null || category.trim().isEmpty() ||
            date == null)
            throw new IllegalArgumentException("Name, category and date are required");

        this.name = name;
        this.category = category;
        this.date = date;
    }

    // Getter methods
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public int getCapacity() { return capacity; }
    public int getCount() { return count; }

    // Adds a participant to the event
    public void addParticipant(Participant p) {
        if (count >= capacity)
            throw new IllegalStateException("Event is full!");

        participants[count++] = p;
    }

    // Removes a participant from the event
    public void removeParticipant(int participantId) {
        for (int i = 0; i < count; i++) {
            if (participants[i].getId() == participantId) {
                for (int j = i; j < count - 1; j++)
                    participants[j] = participants[j + 1];

                participants[--count] = null;
                return;
            }
        }
    }

    // Adds an activity to the event schedule
    public void addSchedule(String time, String activity) {
        if (time.isEmpty() || activity.isEmpty())
            throw new IllegalArgumentException("Time and activity cannot be empty");

        schedule.add(time + " - " + activity);
    }

    // Displays complete event information
    public String toString() {
        String text = "Event ID: " + id +
                "\nName: " + name +
                "\nCategory: " + category +
                "\nDate: " + date +
                "\nCapacity: " + capacity +
                "\nRegistered: " + count +
                "\nOrganizer: " + organizer +
                "\nSchedule:";

        if (schedule.isEmpty())
            text += "\n  No schedule added";

        for (String s : schedule)
            text += "\n  " + s;

        return text;
    }
}

// Stores organizer information
class Organizer {
    private int id;
    private String name, department;

    // Constructor: creates an Organizer object
    public Organizer(int id, String name, String department) {
        if (id <= 0)
            throw new IllegalArgumentException("Invalid Organizer ID");

        if (name.isEmpty() || department.isEmpty())
            throw new IllegalArgumentException("Organizer name and department required");

        this.id = id;
        this.name = name;
        this.department = department;
    }

    // Displays organizer information
    @Override
    public String toString() {
        return "Organizer ID: " + id +
                "\nName: " + name +
                "\nDepartment: " + department;
    }
}
