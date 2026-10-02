import java.time.LocalDate;

public class Registration {
    private int id;
    private Participant participant;
    private Event event;
    private LocalDate date = LocalDate.now();
    private boolean attended;

    public Registration(int id, Participant participant, Event event) {
        if (id <= 0 || participant == null || event == null) throw new IllegalArgumentException("Invalid registration");
        this.id = id;
        this.participant = participant;
        this.event = event;
    }

    public int getId() { return id; }
    public Participant getParticipant() { return participant; }
    public Event getEvent() { return event; }
    public boolean isAttended() { return attended; }
    public void markAttendance() { attended = true; }
    public void cancelRegistration() { event.removeParticipant(participant.getId()); }

    public String toString() {
        return "Registration ID: " + id + "\nParticipant: " + participant.getName() + "\nEvent: " + event.getName()
                + "\nDate: " + date + "\nAttendance: " + (attended ? "Present" : "Absent");
    }
}