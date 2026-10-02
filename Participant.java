public class Participant {

    private int participantId;
    private String name;
    private String email;
    private String course;

    // Constructor
    public Participant(int participantId, String name,
                       String email, String course) {

        if (participantId <= 0)
            throw new IllegalArgumentException("Invalid Participant ID");

        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");

        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email");

        if (course == null || course.trim().isEmpty())
            throw new IllegalArgumentException("Course cannot be empty");

        this.participantId = participantId;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    // Getters
    public int getParticipantId() {
        return participantId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {

        return "Participant ID: " + participantId +
                "\nName: " + name +
                "\nEmail: " + email +
                "\nCourse: " + course;
    }
}