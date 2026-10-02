import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.*;

public class EventManagementSystem extends JFrame {

    // Main collections used to store system data
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Participant> participants = new ArrayList<>();
    private ArrayList<Registration> registrations = new ArrayList<>();

    // Maps used for quick lookup
    private HashMap<Integer, Participant> registrationMap = new HashMap<>();
    private HashMap<Integer, Organizer> organizers = new HashMap<>();
    private TreeMap<Integer, Event> eventMap = new TreeMap<>();

    // Area used to display output
    private JTextArea output = new JTextArea();

    // First registration ID
    private int nextRegId = 1001;

    // Custom exception used when the user cancels an input
    private static class Cancelled extends RuntimeException { }


    // Constructor: creates the main application window
    public EventManagementSystem() {

        setTitle("College Event Management System");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creates a grid with 7 rows and 3 columns for buttons
        JPanel p = new JPanel(new GridLayout(7, 3, 8, 8));

        // Add Organizer
        button(p, "Add Organizer", () -> addOrganizer());

        // View all organizers
        button(p, "View Organizers", () -> viewOrganizers());

        // Event management
        button(p, "Add Event", () -> addEvent());
        button(p, "View Events", () -> showEvents("All events:"));
        button(p, "Update Event", () -> updateEvent());
        button(p, "Delete Event", () -> deleteEvent());

        // Participant management
        button(p, "Add Participant", () -> addParticipant());
        button(p, "View Participants", () -> viewParticipants());
        button(p, "Update Participant", () -> updateParticipant());
        button(p, "Delete Participant", () -> deleteParticipant());

        // Registration and attendance
        button(p, "Register", () -> register());
        button(p, "Cancel Registration", () -> cancelRegistration());
        button(p, "Mark Attendance", () -> markAttendance());

        // Schedule and searching
        button(p, "Add Schedule",
                () -> getEvent("Event ID:")
                        .addSchedule(ask("Time (e.g. 10:00):"), ask("Activity:")));

        button(p, "Search Event",
                () -> show(getEvent("Event ID to search:").toString()));

        button(p, "Search Participant",
                () -> show(getParticipant("Participant ID to search:").toString()));

        // Sorting options
        button(p, "Sort by Date", () -> {
            events.sort((a, b) -> a.getDate().compareTo(b.getDate()));
            showEvents("Sorted by date:");
        });

        button(p, "Sort by Category", () -> {
            events.sort((a, b) -> a.getCategory().compareToIgnoreCase(b.getCategory()));
            showEvents("Sorted by category:");
        });

        // Reports
        button(p, "Event Report", () -> eventReport());
        button(p, "Participant Report", () -> participantReport());

        // Clears the output area
        button(p, "Clear", () -> show(""));

        // Configure output area
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Add buttons at the top and output area in the center
        add(p, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }


    // Creates a button and connects it to an action
    private void button(JPanel panel, String name, Runnable action) {
        JButton b = new JButton(name);
        b.addActionListener(e -> run(action));
        panel.add(b);
    }


    // Handles errors and cancelled operations
    private void run(Runnable action) {
        try {
            action.run();

        } catch (Cancelled ex) {
            return;

        } catch (NumberFormatException ex) {
            error("Please enter a valid number");

        } catch (DateTimeParseException ex) {
            error("Date must be in YYYY-MM-DD format");

        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }


    // Displays an error message
    private void error(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }


    // Displays text in the output area
    private void show(String text) {
        output.setText(text);
    }


    // Takes integer input from the user
    private int askInt(String msg) {
        return Integer.parseInt(ask(msg));
    }


    // Takes text input using a popup
    private String ask(String msg) {
        String text = JOptionPane.showInputDialog(this, msg);

        // If user presses Cancel
        if (text == null)
            throw new Cancelled();

        return text.trim();
    }


    // Checks whether an object exists
    private void check(Object item, String msg) {
        if (item == null)
            throw new IllegalArgumentException(msg);
    }


    // Finds an event using its ID
    private Event getEvent(String msg) {
        Event e = eventMap.get(askInt(msg));

        check(e, "Event not found!");

        return e;
    }


    // Searches participants by ID
    private Participant findParticipant(int id) {
        for (Participant p : participants)
            if (p.getId() == id)
                return p;

        return null;
    }


    // Gets a participant and checks if it exists
    private Participant getParticipant(String msg) {
        Participant p = findParticipant(askInt(msg));

        check(p, "Participant not found!");

        return p;
    }


    // Finds a registration using its ID
    private Registration getRegistration(String msg) {
        int id = askInt(msg);

        for (Registration r : registrations)
            if (r.getId() == id)
                return r;

        throw new IllegalArgumentException("Registration not found!");
    }


    // Removes a registration from all required collections
    private void removeRegistration(Registration r) {
        r.cancelRegistration();
        registrations.remove(r);
        registrationMap.remove(r.getId());
    }


    // Adds a new organizer
    private void addOrganizer() {
        int id = askInt("Organizer ID:");

        if (organizers.containsKey(id))
            throw new IllegalArgumentException("Organizer ID already exists!");

        organizers.put(
                id,
                new Organizer(id, ask("Name:"), ask("Department:"))
        );

        show("Organizer added.");
    }


    // Displays all organizers
    private void viewOrganizers() {
        show(organizers.isEmpty() ? "No organizers found." : "");

        for (Organizer o : organizers.values())
            output.append(o + "\n");
    }


    // Creates and stores a new event
    private void addEvent() {
        int id = askInt("Event ID:");

        if (eventMap.containsKey(id))
            throw new IllegalArgumentException("Event ID already exists!");

        String name = ask("Event name:");
        String category = ask(
                "Category (Technical / Workshop / Seminar / Cultural / Competition):"
        );

        LocalDate date = LocalDate.parse(
                ask("Date (YYYY-MM-DD):")
        );

        int capacity = askInt("Capacity:");

        Organizer organizer = organizers.get(
                askInt("Organizer ID:")
        );

        check(organizer, "Organizer not found! Add the organizer first.");

        Event event = new Event(
                id,
                name,
                category,
                date,
                capacity,
                organizer
        );

        events.add(event);
        eventMap.put(id, event);

        show("Event added.");
    }


    // Displays all events
    private void showEvents(String title) {
        show(events.isEmpty() ? "No events available." : title + "\n");

        for (Event e : events)
            output.append("-----------------------------\n" + e + "\n");
    }


    // Updates an existing event
    private void updateEvent() {
        Event e = getEvent("Event ID to update:");

        e.update(
                ask("New name:"),
                ask("New category:"),
                LocalDate.parse(ask("New date (YYYY-MM-DD):"))
        );

        show("Event updated.");
    }


    // Deletes an event and its related registrations
    private void deleteEvent() {
        Event e = getEvent("Event ID to delete:");

        for (Registration r : new ArrayList<>(registrations))
            if (r.getEvent() == e)
                removeRegistration(r);

        events.remove(e);
        eventMap.remove(e.getId());

        show("Event deleted.");
    }


    // Adds a new participant
    private void addParticipant() {
        int id = askInt("Participant ID:");

        if (findParticipant(id) != null)
            throw new IllegalArgumentException("Participant ID already exists!");

        participants.add(
                new Participant(
                        id,
                        ask("Name:"),
                        ask("Email:"),
                        ask("Course:")
                )
        );

        show("Participant added.");
    }


    // Displays all participants
    private void viewParticipants() {
        show(participants.isEmpty() ? "No participants found." : "");

        for (Participant p : participants)
            output.append("-----------------------------\n" + p + "\n");
    }


    // Updates participant information
    private void updateParticipant() {
        getParticipant("Participant ID to update:")
                .update(
                        ask("New name:"),
                        ask("New email:"),
                        ask("New course:")
                );

        show("Participant updated.");
    }


    // Deletes a participant and related registrations
    private void deleteParticipant() {
        Participant p = getParticipant("Participant ID to delete:");

        for (Registration r : new ArrayList<>(registrations))
            if (r.getParticipant() == p)
                removeRegistration(r);

        participants.remove(p);

        show("Participant deleted.");
    }


    // Registers a participant for an event
    private void register() {
        Event event = getEvent("Event ID:");
        Participant p = getParticipant("Participant ID:");

        // Prevents duplicate registration
        for (Registration r : registrations)
            if (r.getEvent() == event && r.getParticipant() == p)
                throw new IllegalArgumentException(
                        "Participant already registered!"
                );

        // Add participant to event
        event.addParticipant(p);

        // Create a new registration
        registrations.add(
                new Registration(nextRegId, p, event)
        );

        // Store registration ID with participant
        registrationMap.put(nextRegId, p);

        show("Registration successful!\nRegistration ID: " + nextRegId++);
    }


    // Cancels an existing registration
    private void cancelRegistration() {
        removeRegistration(
                getRegistration("Registration ID:")
        );

        show("Registration cancelled.");
    }


    // Marks a registered participant as present
    private void markAttendance() {
        Registration r = getRegistration("Registration ID:");

        if (r.isAttended())
            throw new IllegalStateException("Attendance already marked!");

        r.markAttendance();

        show("Attendance marked for " + r.getParticipant().getName());
    }


    // Generates an event report
    private void eventReport() {
        show(
                "========== EVENT REPORT ==========\n" +
                "Total Events: " + events.size() + "\n"
        );

        for (Event e : events)
            output.append(
                    "\nEvent: " + e.getName() +
                    " (" + e.getCategory() + ", " + e.getDate() + ")" +
                    "\nCapacity: " + e.getCapacity() +
                    "\nRegistered: " + e.getCount() +
                    "\nAvailable Seats: " +
                    (e.getCapacity() - e.getCount()) +
                    "\n-----------------------------\n"
            );
    }


    // Generates a participant report
    private void participantReport() {
        show(
                "======= PARTICIPANT REPORT =======\n" +
                "Total Participants: " + participants.size() +
                "\nTotal Registrations: " + registrations.size() +
                "\n"
        );

        for (Registration r : registrations)
            output.append(
                    "\n" + r +
                    "\n-----------------------------\n"
            );
    }


    // Main method: starts the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new EventManagementSystem().setVisible(true)
        );
    }
}