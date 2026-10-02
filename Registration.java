import java.time.LocalDate;

public class Registration {

    // Registration details
    private int id;
    private Participant participant;
    private Event event;
    private LocalDate date = LocalDate.now();
    private boolean attended;

    // Constructor: creates a registration
    public Registration(int id, Participant participant, Event event) {
        if (id <= 0 || participant == null || event == null) throw new IllegalArgumentException("Invalid registration");
        this.id = id;
        this.participant = participant;
        this.event = event;
    }

    // Getter methods
    public int getId() { return id; }
    public Participant getParticipant() { return participant; }
    public Event getEvent() { return event; }
    public boolean isAttended() { return attended; }

    // Marks the participant as attended
    public void markAttendance() { attended = true; }

    // Cancels registration and removes participant from event
    public void cancelRegistration() { event.removeParticipant(participant.getId()); }

    // Displays registration information
    public String toString() {
        return "Registration ID: " + id + "\nParticipant: " + participant.getName() + "\nEvent: " + event.getName()
                + "\nDate: " + date + "\nAttendance: " + (attended ? "Present" : "Absent");
    }
}
