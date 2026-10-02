# College Event Management System

A desktop application written in Java (Swing) for managing college events end to end: creating events, registering participants, tracking attendance, scheduling activities, and generating reports. All data is held in memory and the whole application runs from a single window.

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [Class Design](#class-design)
6. [Data Structures Used](#data-structures-used)
7. [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
8. [Requirements](#requirements)
9. [Compile and Run](#compile-and-run)
10. [User Guide](#user-guide)
11. [Input Rules and Validation](#input-rules-and-validation)
12. [Error Handling](#error-handling)
13. [Sample Walkthrough](#sample-walkthrough)
14. [Known Limitations](#known-limitations)
15. [Possible Improvements](#possible-improvements)

---

## Overview

The system models how a college runs events such as technical fests, workshops, seminars, cultural programs, and competitions. An administrator can create events with a capacity and an organizer, maintain a pool of participants, register those participants into events, mark who attended, and view summary reports.

The application is a single Swing window. A grid of buttons at the top triggers each operation, input is collected through popup dialogs, and results are printed in a scrollable text area at the bottom.

---

## Features

### Event management
- Add an event with ID, name, category, date, capacity, and organizer details
- View all events together with their schedules
- Delete an event by ID
- Add timed schedule entries (time and activity) to an event
- Sort events by date

### Participant management
- Add a participant with ID, name, email, and course
- View all participants

### Registration and attendance
- Register a participant for an event; each registration receives a unique ID starting from 1001
- Automatic checks for full events and duplicate registrations
- Cancel a registration, which frees the seat in the event
- Mark attendance against a registration ID

### Search and reports
- Search an event or a participant by ID
- Event report: total events, and for each event the capacity, registered count, and available seats
- Participant report: total participants, total registrations, and for each registration the participant, event, and Present/Absent status

### Interface
- Single window, 850 x 600, centered on screen
- 14 action buttons in a 5 x 3 grid
- Read-only monospaced output area with scrolling
- Clear button to wipe the output area

---

## Technology Stack

| Item | Detail |
|------|--------|
| Language | Java |
| GUI toolkit | Swing (`javax.swing`) with AWT layouts (`java.awt`) |
| Date handling | `java.time.LocalDate` |
| Collections | `ArrayList`, `LinkedList`, `HashMap`, `TreeMap`, arrays |
| Storage | In-memory only |
| Minimum Java version | 8 (uses lambdas and `java.time`) |

---

## Project Structure

```
javafinalproject/
├── EventManagementSystem.java
├── Event.java
├── Participant.java
└── Registration.java
```

| File | Contains | Role |
|------|----------|------|
| `EventManagementSystem.java` | `EventManagementSystem` (extends `JFrame`) | Entry point, GUI construction, all button handlers, search, sort, and reports |
| `Event.java` | `Event` (public) and `Organizer` (package-private) | Event data, capacity-bound participant array, schedule list, and organizer details |
| `Participant.java` | `Participant` | Participant data with validated constructor |
| `Registration.java` | `Registration` | Link between one participant and one event, with registration date and attendance flag |

`Organizer` is declared inside `Event.java`, so there is no separate `Organizer.java`. After compilation it still produces its own `Organizer.class`.

---

## Class Design

### Relationships

```
EventManagementSystem
   |-- has many --> Event ---------- has one --> Organizer
   |                  |-- holds up to capacity --> Participant
   |                  |-- holds --> schedule (LinkedList<String>)
   |-- has many --> Participant
   |-- has many --> Registration ---- links --> Participant + Event
```

### `Event`

| Field | Type | Description |
|-------|------|-------------|
| `eventId` | `int` | Unique positive identifier |
| `eventName` | `String` | Non-empty name |
| `category` | `String` | Free text (the dialog suggests Technical, Workshop, Seminar, Cultural, Competition) |
| `date` | `LocalDate` | Event date |
| `capacity` | `int` | Maximum number of participants, must be greater than 0 |
| `organizer` | `Organizer` | Person responsible for the event |
| `participants` | `Participant[]` | Fixed-size array created with length equal to capacity |
| `participantCount` | `int` | Number of occupied slots in the array |
| `schedule` | `LinkedList<String>` | Entries stored as `"time - activity"` |

| Method | Behavior |
|--------|----------|
| `Event(...)` | Validates ID, name, and capacity; allocates the participant array and schedule list |
| `addParticipant(Participant)` | Places the participant in the next free slot; throws `IllegalStateException` if full |
| `removeParticipant(int id)` | Finds the participant by ID, shifts later entries left to close the gap, clears the last slot, decrements the count; does nothing if the ID is not found |
| `addSchedule(String, String)` | Rejects null or blank input, otherwise appends `time + " - " + activity` |
| Getters | `getEventId`, `getEventName`, `getCategory`, `getDate`, `getCapacity`, `getOrganizer`, `getParticipants`, `getParticipantCount`, `getSchedule` |
| `toString()` | Multi-line summary: ID, name, category, date, capacity, registered count, organizer name |

### `Organizer`

| Field | Type | Description |
|-------|------|-------------|
| `organizerId` | `int` | Positive identifier |
| `name` | `String` | Required, non-empty |
| `department` | `String` | Organizer's department |

The constructor throws `IllegalArgumentException` for an ID of 0 or less or a blank name. `toString()` returns `id - name (department)`.

### `Participant`

| Field | Type | Description |
|-------|------|-------------|
| `participantId` | `int` | Positive identifier |
| `name` | `String` | Non-empty |
| `email` | `String` | Must contain `@` |
| `course` | `String` | Non-empty |

The constructor validates every field and throws `IllegalArgumentException` with a specific message. Setters exist for name, email, and course. `toString()` prints ID, name, email, and course on separate lines.

### `Registration`

| Field | Type | Description |
|-------|------|-------------|
| `registrationId` | `int` | Positive identifier |
| `participant` | `Participant` | Who registered |
| `event` | `Event` | Which event |
| `registrationDate` | `LocalDate` | Set to today's date at creation |
| `attended` | `boolean` | Starts as `false` |

| Method | Behavior |
|--------|----------|
| `markAttendance()` | Sets `attended` to `true` |
| `cancelRegistration()` | Calls `event.removeParticipant(participantId)` to free the seat |
| `isAttended()`, getters | Read access to fields |
| `toString()` | Registration ID, participant name, event name, date, and Present/Absent |

### `EventManagementSystem`

| Member | Purpose |
|--------|---------|
| `events` | `ArrayList<Event>`; display order, re-ordered by Sort Events |
| `participants` | `ArrayList<Participant>`; all known participants |
| `registrations` | `ArrayList<Registration>`; all active registrations |
| `sortedEvents` | `TreeMap<Integer, Event>`; fast lookup of an event by ID |
| `registrationMap` | `HashMap<Integer, Participant>`; registration ID to participant |
| `output` | Read-only `JTextArea` used for all results |
| `nextRegistrationId` | Counter starting at 1001 |

| Method | Purpose |
|--------|---------|
| `createGUI()` | Builds the button grid and output area and attaches lambda listeners |
| `addEvent()`, `viewEvents()`, `deleteEvent()` | Event operations |
| `addParticipant()`, `viewParticipants()` | Participant operations |
| `registerParticipant()`, `cancelRegistration()`, `markAttendance()` | Registration operations |
| `addSchedule()` | Adds a schedule entry to an event |
| `search()`, `searchEvent()`, `searchParticipant()` | Search by ID |
| `sortEvents()` | Sorts `events` by date using a comparator lambda |
| `eventReport()`, `participantReport()` | Report generation |
| `findParticipant(int)` | Linear search through the participant list |
| `showError(String)` | Shows an error dialog |
| `main(String[])` | Launches the window on the Swing event thread |

---

## Data Structures Used

| Structure | Where | Why |
|-----------|-------|-----|
| Array (`Participant[]`) | Inside each `Event` | Capacity is fixed per event, so a fixed-size array models seat limits directly |
| `LinkedList<String>` | Inside each `Event` | Schedule entries are appended in order |
| `ArrayList<Event>` | `EventManagementSystem` | Ordered, re-sortable list for display |
| `ArrayList<Participant>` | `EventManagementSystem` | Master list of participants |
| `ArrayList<Registration>` | `EventManagementSystem` | Master list of registrations |
| `TreeMap<Integer, Event>` | `EventManagementSystem` | Event lookup by ID, keys kept sorted |
| `HashMap<Integer, Participant>` | `EventManagementSystem` | Registration ID to participant mapping |

---

## OOP Concepts Demonstrated

- **Encapsulation:** all fields are private and accessed through getters and setters
- **Abstraction:** each real-world entity (event, organizer, participant, registration) is its own class
- **Association and composition:** `Event` holds an `Organizer`, `Registration` holds a `Participant` and an `Event`
- **Inheritance:** `EventManagementSystem` extends `JFrame`
- **Constructor validation:** invalid objects cannot be created
- **Exception handling:** `IllegalArgumentException`, `IllegalStateException`, `NumberFormatException`, and `DateTimeParseException` are caught and shown to the user
- **Method overriding:** `toString()` is overridden in every model class
- **Collections framework and generics:** typed lists and maps
- **Lambda expressions:** button listeners and the date comparator

---

## Requirements

- JDK 8 or higher (check with `java -version` and `javac -version`)
- A desktop environment that can display Swing windows (Windows, macOS, or Linux with a display)

---

## Compile and Run

Open a terminal (Command Prompt or PowerShell on Windows, Terminal on macOS or Linux) on your own computer and move into the project folder first.

Paste this into your terminal to compile:

```
javac Event.java Participant.java Registration.java EventManagementSystem.java
```

Paste this into the same terminal to run:

```
java EventManagementSystem
```

Compiling creates `Event.class`, `Organizer.class`, `Participant.class`, `Registration.class`, and `EventManagementSystem.class`. These are build output and should not be committed.

---

## User Guide

Every button opens one or more popup dialogs asking for input. Pressing Cancel on the first dialog of an operation aborts it quietly.

### Recommended order of use

1. Add one or more events
2. Add participants
3. Register participants into events
4. Add schedule entries
5. Mark attendance on the day of the event
6. Generate reports

### Button reference

| Button | Inputs requested | Result |
|--------|------------------|--------|
| Add Event | Event ID, name, category, date (`YYYY-MM-DD`), capacity, organizer name, organizer department | Event created and confirmed in the output area |
| View Events | None | Lists every event with its details and schedule entries |
| Delete Event | Event ID | Removes the event from the list and the lookup map |
| Add Participant | Participant ID, name, email, course | Participant created and stored |
| View Participants | None | Lists every participant |
| Register | Event ID, participant ID | Creates a registration and displays its Registration ID |
| Cancel Registration | Registration ID | Removes the registration and frees the seat |
| Mark Attendance | Registration ID | Marks the participant as present |
| Add Schedule | Event ID, time, activity | Appends `time - activity` to the event schedule |
| Search | Choice of Event or Participant, then an ID | Shows the matching record |
| Sort Events | None | Lists events as `ID \| name \| category \| date`, ordered by date |
| Event Report | None | Total events and per-event capacity, registered, and available seats |
| Participant Report | None | Total participants, total registrations, and per-registration attendance |
| Clear | None | Empties the output area |

---

## Input Rules and Validation

| Field | Rule |
|-------|------|
| Event ID | Whole number greater than 0; must not already exist |
| Event name | Cannot be empty |
| Category | Free text; no enforcement |
| Date | Must parse as `YYYY-MM-DD` |
| Capacity | Whole number greater than 0 |
| Organizer name | Cannot be empty |
| Participant ID | Whole number greater than 0; must not already exist |
| Participant name | Cannot be empty |
| Email | Must contain `@` |
| Course | Cannot be empty |
| Registration | Event and participant must exist, event must not be full, the same participant cannot register twice for the same event |
| Schedule time and activity | Neither can be empty |
| Registration ID (cancel, attendance) | Must match an existing registration |

---

## Error Handling

Operations are wrapped in `try/catch`. Any exception raised by validation or parsing is displayed in an error dialog titled "Error".

| Situation | Message shown |
|-----------|---------------|
| Non-numeric ID or capacity | The `NumberFormatException` message |
| Duplicate event ID | Event ID already exists! |
| Duplicate participant ID | Participant ID already exists! |
| Unknown event | Event not found! |
| Unknown participant | Participant not found! |
| Full event | Event is full! |
| Repeat registration | Participant already registered! |
| Unknown registration | Registration not found! |
| Bad email | Invalid email |
| Bad date | The `DateTimeParseException` message |

---

## Sample Walkthrough

1. Click **Add Event** and enter: ID `1`, name `Code Sprint`, category `Competition`, date `2026-11-15`, capacity `2`, organizer `Asha`, department `CSE`.
2. Click **Add Participant** twice: ID `101`, `Ravi`, `ravi@college.edu`, `B.Tech CSE` and ID `102`, `Meera`, `meera@college.edu`, `B.Tech IT`.
3. Click **Register**: event `1`, participant `101` returns Registration ID `1001`. Repeat for participant `102` to get `1002`.
4. Register a third participant for event `1`: the application reports **Event is full!**
5. Click **Add Schedule**: event `1`, time `10:00`, activity `Round 1`.
6. Click **Mark Attendance**: registration `1001`.
7. Click **Participant Report**: registration 1001 shows Present, registration 1002 shows Absent.
8. Click **Cancel Registration** with `1002`, then **Event Report** to see one seat available again.

---

## Known Limitations

- **No persistence.** Everything is lost when the window closes.
- **Deleting an event does not delete its registrations.** They remain in the registration list and still appear in the participant report.
- **Organizer ID equals the event ID.** The organizer is created fresh for every event and cannot be reused.
- **Attendance can be marked repeatedly** with no feedback that it was already recorded.
- **Category is not validated** against the suggested list.
- **Cancelling a later popup in a multi-step dialog** (for example the date prompt) can produce an error dialog with an empty or unhelpful message.
- **Search is by ID only.**
- **`removeParticipant` is silent** if the participant ID is not found in the event.
- **`registrationMap` is written to but never read** by any feature.
- **No past-date check.** Events can be created for dates that have already passed.

---

## Possible Improvements

- Save and load data using files, serialization, or a database such as SQLite through JDBC
- Delete related registrations when an event is deleted
- Replace the popup dialogs with proper form panels and table views (`JTable`)
- Search by name, category, or date range
- Dropdown for category and a date picker for dates
- Prevent attendance from being marked twice
- Export reports to CSV or PDF
- Separate GUI code from business logic into distinct classes
- Add unit tests for `Event`, `Participant`, and `Registration`
- Add a `.gitignore` for `*.class` and `.DS_Store`

---
