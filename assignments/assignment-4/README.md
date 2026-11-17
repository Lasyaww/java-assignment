# Java Assignment 4 - CO4

**Student:** A. Lasya  
**Register number:** 192371090  
**Course:** Programming in Java — Event-Driven GUI Design

## Task folders
- Task 1: `task-01/FeeWindow.java` — fee GUI and service separation
- Task 2: `task-02/AppointmentDesk.java` — outpatient appointment desk
- Task 3: `task-03/LibrarySearchScreen.java` — resizable library search layout
- Task 4: `task-04/DrawPad.java` — mouse events and persistent repaint
- Task 5: `task-05/Converter.java` — four event-handler styles
- Task 6: `task-06/EventRegistrationApp.java`, `task-06/schema.sql` — Swing/JDBC registration

## Report and outputs
- Design and analysis report: [report.md](report.md)
- Illustrative expected behaviour notes: [outputs/](outputs/)
- Add actual GUI screenshots and database evidence to `outputs/` after running the programs. The current text notes are not captured execution logs.

## Compile and run
Open the relevant task folder. For standalone source files use `javac FileName.java` and `java ClassName`. GUI tasks need a desktop environment. Tasks 1 and 6 require MySQL Connector/J and a configured database. Set `JAVA_DB_URL`, `JAVA_DB_USER`, and `JAVA_DB_PASSWORD`. Run `task-06/schema.sql` before Task 6.

**Java version:** Java 8+ for most AWT/Swing examples; use Java 11+ for the recommended current setup.

Only Java source code, SQL schema, report and illustrative output notes are currently included. Actual screenshots and execution measurements remain to be added after local execution.
