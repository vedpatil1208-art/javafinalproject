public class Participant {
    private int id;
    private String name, email, course;

    public Participant(int id, String name, String email, String course) {
        if (id <= 0) throw new IllegalArgumentException("Invalid Participant ID");
        this.id = id;
        update(name, email, course);
    }

    public void update(String name, String email, String course) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        if (course == null || course.trim().isEmpty()) throw new IllegalArgumentException("Course cannot be empty");
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public String toString() {
        return "Participant ID: " + id + "\nName: " + name + "\nEmail: " + email + "\nCourse: " + course;
    }
}
