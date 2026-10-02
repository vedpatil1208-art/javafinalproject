import java.time.LocalDate;

public class Registration {

    private int registrationId;
    private Participant participant;
    private Event event;
    private LocalDate registrationDate;
    private boolean attended;

    // Constructor
    public Registration(int registrationId,
                        Participant participant,
                        Event event) {

        if (registrationId <= 0)
            throw new IllegalArgumentException("Invalid Registration ID");

        if (participant == null)
            throw new IllegalArgumentException("Participant required");

        if (event == null)
            throw new IllegalArgumentException("Event required");

        this.registrationId = registrationId;
        this.participant = participant;
        this.event = event;
        this.registrationDate = LocalDate.now();
        this.attended = false;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public Participant getParticipant() {
        return participant;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public boolean isAttended() {
        return attended;
    }

    // Attendance
    public void markAttendance() {
        attended = true;
    }

    public void cancelRegistration() {
        event.removeParticipant(participant.getParticipantId());
    }

    @Override
    public String toString() {

        return "Registration ID: " + registrationId +
                "\nParticipant: " + participant.getName() +
                "\nEvent: " + event.getEventName() +
                "\nDate: " + registrationDate +
                "\nAttendance: " +
                (attended ? "Present" : "Absent");
    }
}