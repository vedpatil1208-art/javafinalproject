import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.*;

public class EventManagementSystem extends JFrame {

    // ArrayList for events
    private ArrayList<Event> events = new ArrayList<>();

    // ArrayList for participants
    private ArrayList<Participant> participants = new ArrayList<>();

    // HashMap: Registration ID -> Participant
    private HashMap<Integer, Participant> registrationMap =
            new HashMap<>();

    // TreeMap: Event ID -> Event
    private TreeMap<Integer, Event> sortedEvents =
            new TreeMap<>();

    // Registration storage
    private ArrayList<Registration> registrations =
            new ArrayList<>();

    private JTextArea output;

    private int nextRegistrationId = 1001;

    public EventManagementSystem() {

        setTitle("College Event Management System");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {

        JPanel panel = new JPanel(new GridLayout(5, 3, 10, 10));

        JButton addEventButton =
                new JButton("Add Event");

        JButton viewEventButton =
                new JButton("View Events");

        JButton deleteEventButton =
                new JButton("Delete Event");

        JButton addParticipantButton =
                new JButton("Add Participant");

        JButton viewParticipantButton =
                new JButton("View Participants");

        JButton registerButton =
                new JButton("Register");

        JButton cancelButton =
                new JButton("Cancel Registration");

        JButton attendanceButton =
                new JButton("Mark Attendance");

        JButton scheduleButton =
                new JButton("Add Schedule");

        JButton searchButton =
                new JButton("Search");

        JButton sortButton =
                new JButton("Sort Events");

        JButton reportButton =
                new JButton("Event Report");

        JButton participantReportButton =
                new JButton("Participant Report");

        JButton clearButton =
                new JButton("Clear");

        panel.add(addEventButton);
        panel.add(viewEventButton);
        panel.add(deleteEventButton);

        panel.add(addParticipantButton);
        panel.add(viewParticipantButton);
        panel.add(registerButton);

        panel.add(cancelButton);
        panel.add(attendanceButton);
        panel.add(scheduleButton);

        panel.add(searchButton);
        panel.add(sortButton);
        panel.add(reportButton);

        panel.add(participantReportButton);
        panel.add(clearButton);

        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(output);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions

        addEventButton.addActionListener(e -> addEvent());

        viewEventButton.addActionListener(e -> viewEvents());

        deleteEventButton.addActionListener(e -> deleteEvent());

        addParticipantButton.addActionListener(e -> addParticipant());

        viewParticipantButton.addActionListener(e -> viewParticipants());

        registerButton.addActionListener(e -> registerParticipant());

        cancelButton.addActionListener(e -> cancelRegistration());

        attendanceButton.addActionListener(e -> markAttendance());

        scheduleButton.addActionListener(e -> addSchedule());

        searchButton.addActionListener(e -> search());

        sortButton.addActionListener(e -> sortEvents());

        reportButton.addActionListener(e -> eventReport());

        participantReportButton.addActionListener(
                e -> participantReport());

        clearButton.addActionListener(e -> output.setText(""));
    }

    // ---------------- ADD EVENT ----------------

    private void addEvent() {

        try {

            String idText = JOptionPane.showInputDialog(
                    this, "Enter Event ID:");

            if (idText == null) return;

            int id = Integer.parseInt(idText);

            if (sortedEvents.containsKey(id)) {
                throw new IllegalArgumentException(
                        "Event ID already exists!");
            }

            String name = JOptionPane.showInputDialog(
                    this, "Enter Event Name:");

            String category = JOptionPane.showInputDialog(
                    this,
                    "Enter Category\nTechnical / Workshop / Seminar / Cultural / Competition:");

            String dateText = JOptionPane.showInputDialog(
                    this, "Enter Date (YYYY-MM-DD):");

            LocalDate date = LocalDate.parse(dateText);

            String capacityText = JOptionPane.showInputDialog(
                    this, "Enter Capacity:");

            int capacity = Integer.parseInt(capacityText);

            String organizerName = JOptionPane.showInputDialog(
                    this, "Enter Organizer Name:");

            String department = JOptionPane.showInputDialog(
                    this, "Enter Organizer Department:");

            Organizer organizer =
                    new Organizer(id, organizerName, department);

            Event event = new Event(
                    id,
                    name,
                    category,
                    date,
                    capacity,
                    organizer
            );

            events.add(event);
            sortedEvents.put(id, event);

            output.append(
                    "\nEvent added successfully!\n"
            );

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ---------------- VIEW EVENTS ----------------

    private void viewEvents() {

        output.setText("");

        if (events.isEmpty()) {
            output.setText("No events available.");
            return;
        }

        for (Event event : events) {

            output.append("-----------------------------\n");

            output.append(event.toString());

            output.append("\nSchedules:\n");

            if (event.getSchedule().isEmpty()) {
                output.append("No schedule added.\n");
            } else {

                for (String schedule :
                        event.getSchedule()) {

                    output.append(schedule + "\n");
                }
            }
        }
    }

    // ---------------- DELETE EVENT ----------------

    private void deleteEvent() {

        try {

            String input = JOptionPane.showInputDialog(
                    this, "Enter Event ID to delete:");

            if (input == null) return;

            int id = Integer.parseInt(input);

            Event event = sortedEvents.get(id);

            if (event == null) {
                throw new IllegalArgumentException(
                        "Event not found!");
            }

            events.remove(event);
            sortedEvents.remove(id);

            output.setText(
                    "Event deleted successfully."
            );

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ---------------- ADD PARTICIPANT ----------------

    private void addParticipant() {

        try {

            String idText = JOptionPane.showInputDialog(
                    this, "Enter Participant ID:");

            if (idText == null) return;

            int id = Integer.parseInt(idText);

            if (findParticipant(id) != null) {

                throw new IllegalArgumentException(
                        "Participant ID already exists!");
            }

            String name = JOptionPane.showInputDialog(
                    this, "Enter Name:");

            String email = JOptionPane.showInputDialog(
                    this, "Enter Email:");

            String course = JOptionPane.showInputDialog(
                    this, "Enter Course:");

            Participant participant =
                    new Participant(id, name, email, course);

            participants.add(participant);

            output.setText(
                    "Participant added successfully."
            );

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ---------------- VIEW PARTICIPANTS ----------------

    private void viewParticipants() {

        output.setText("");

        if (participants.isEmpty()) {

            output.setText("No participants found.");
            return;
        }

        for (Participant p : participants) {

            output.append("-----------------------------\n");
            output.append(p.toString());
            output.append("\n");
        }
    }

    // ---------------- REGISTER ----------------

    private void registerParticipant() {

        try {

            String eventText = JOptionPane.showInputDialog(
                    this, "Enter Event ID:");

            if (eventText == null) return;

            int eventId = Integer.parseInt(eventText);

            Event event = sortedEvents.get(eventId);

            if (event == null) {

                throw new IllegalArgumentException(
                        "Event not found!");
            }

            String participantText =
                    JOptionPane.showInputDialog(
                            this, "Enter Participant ID:");

            if (participantText == null) return;

            int participantId =
                    Integer.parseInt(participantText);

            Participant participant =
                    findParticipant(participantId);

            if (participant == null) {

                throw new IllegalArgumentException(
                        "Participant not found!");
            }

            // Check capacity
            if (event.getParticipantCount()
                    >= event.getCapacity()) {

                throw new IllegalStateException(
                        "Event is full!");
            }

            // Prevent duplicate registration
            for (Registration r : registrations) {

                if (r.getEvent().getEventId() == eventId &&
                    r.getParticipant().getParticipantId()
                            == participantId) {

                    throw new IllegalArgumentException(
                            "Participant already registered!");
                }
            }

            event.addParticipant(participant);

            Registration registration =
                    new Registration(
                            nextRegistrationId,
                            participant,
                            event
                    );

            registrations.add(registration);

            // HashMap
            registrationMap.put(
                    nextRegistrationId,
                    participant
            );

            output.setText(
                    "Registration successful!\n" +
                    "Registration ID: " +
                    nextRegistrationId
            );

            nextRegistrationId++;

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ---------------- CANCEL REGISTRATION ----------------

    private void cancelRegistration() {

        try {

            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter Registration ID:");

            if (input == null) return;

            int id = Integer.parseInt(input);

            Registration found = null;

            for (Registration r : registrations) {

                if (r.getRegistrationId() == id) {

                    found = r;
                    break;
                }
            }

            if (found == null) {

                throw new IllegalArgumentException(
                        "Registration not found!");
            }

            found.cancelRegistration();

            registrations.remove(found);

            registrationMap.remove(id);

            output.setText(
                    "Registration cancelled successfully."
            );

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ---------------- ATTENDANCE ----------------

    private void markAttendance() {

        try {

            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter Registration ID:");

            if (input == null) return;

            int id = Integer.parseInt(input);

            Registration found = null;

            for (Registration r : registrations) {

                if (r.getRegistrationId() == id) {

                    found = r;
                    break;
                }
            }

            if (found == null) {

                throw new IllegalArgumentException(
                        "Registration not found!");
            }

            found.markAttendance();

            output.setText(
                    "Attendance marked successfully for "
                    + found.getParticipant().getName()
            );

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ---------------- SCHEDULE ----------------

    private void addSchedule() {

        try {

            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter Event ID:");

            if (input == null) return;

            int eventId = Integer.parseInt(input);

            Event event = sortedEvents.get(eventId);

            if (event == null) {

                throw new IllegalArgumentException(
                        "Event not found!");
            }

            String time = JOptionPane.showInputDialog(
                    this, "Enter Time:");

            String activity = JOptionPane.showInputDialog(
                    this, "Enter Activity:");

            event.addSchedule(time, activity);

            output.setText(
                    "Schedule added successfully."
            );

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ---------------- SEARCH ----------------

    private void search() {

        String[] options = {
                "Search Event",
                "Search Participant"
        };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Select Search Type",
                "Search",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {

            searchEvent();

        } else if (choice == 1) {

            searchParticipant();
        }
    }

    private void searchEvent() {

        try {

            String input = JOptionPane.showInputDialog(
                    this, "Enter Event ID:");

            if (input == null) return;

            int id = Integer.parseInt(input);

            Event event = sortedEvents.get(id);

            if (event == null) {

                throw new IllegalArgumentException(
                        "Event not found!");
            }

            output.setText(event.toString());

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    private void searchParticipant() {

        try {

            String input = JOptionPane.showInputDialog(
                    this, "Enter Participant ID:");

            if (input == null) return;

            int id = Integer.parseInt(input);

            Participant p = findParticipant(id);

            if (p == null) {

                throw new IllegalArgumentException(
                        "Participant not found!");
            }

            output.setText(p.toString());

        } catch (Exception ex) {

            showError(ex.getMessage());
        }
    }

    // ---------------- SORT ----------------

    private void sortEvents() {

        events.sort(
                (e1, e2) ->
                        e1.getDate().compareTo(e2.getDate())
        );

        output.setText(
                "Events sorted by date:\n\n"
        );

        for (Event event : events) {

            output.append(
                    event.getEventId() +
                    " | " +
                    event.getEventName() +
                    " | " +
                    event.getCategory() +
                    " | " +
                    event.getDate() +
                    "\n"
            );
        }
    }

    // ---------------- EVENT REPORT ----------------

    private void eventReport() {

        output.setText(
                "========== EVENT REPORT ==========\n\n"
        );

        output.append(
                "Total Events: " +
                events.size() + "\n\n"
        );

        for (Event event : events) {

            output.append(
                    "Event: " +
                    event.getEventName() +
                    "\n"
            );

            output.append(
                    "Category: " +
                    event.getCategory() +
                    "\n"
            );

            output.append(
                    "Date: " +
                    event.getDate() +
                    "\n"
            );

            output.append(
                    "Capacity: " +
                    event.getCapacity() +
                    "\n"
            );

            output.append(
                    "Registered: " +
                    event.getParticipantCount() +
                    "\n"
            );

            output.append(
                    "Available Seats: " +
                    (event.getCapacity()
                    - event.getParticipantCount()) +
                    "\n"
            );

            output.append(
                    "-----------------------------\n"
            );
        }
    }

    // ---------------- PARTICIPANT REPORT ----------------

    private void participantReport() {

        output.setText(
                "======= PARTICIPANT REPORT =======\n\n"
        );

        output.append(
                "Total Participants: " +
                participants.size() +
                "\n"
        );

        output.append(
                "Total Registrations: " +
                registrations.size() +
                "\n\n"
        );

        for (Registration r : registrations) {

            output.append(
                    "Registration ID: " +
                    r.getRegistrationId() +
                    "\n"
            );

            output.append(
                    "Participant: " +
                    r.getParticipant().getName() +
                    "\n"
            );

            output.append(
                    "Event: " +
                    r.getEvent().getEventName() +
                    "\n"
            );

            output.append(
                    "Attendance: " +
                    (r.isAttended()
                    ? "Present"
                    : "Absent") +
                    "\n"
            );

            output.append(
                    "-----------------------------\n"
            );
        }
    }

    // ---------------- FIND PARTICIPANT ----------------

    private Participant findParticipant(int id) {

        for (Participant p : participants) {

            if (p.getParticipantId() == id) {
                return p;
            }
        }

        return null;
    }

    // ---------------- ERROR HANDLING ----------------

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ---------------- MAIN ----------------

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            EventManagementSystem system =
                    new EventManagementSystem();

            system.setVisible(true);
        });
    }
}