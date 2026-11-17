# Assignment 4 - CO4 Report
**Programming in Java — Event-Driven GUI Design**

**Student:** A. Lasya  
**Register number:** 192371090

## Task 1 — GUI design review
The original design couples UI, validation and database access in one event handler. It compares strings with `==`, does not validate empty or malformed input, blocks the event-dispatch thread during database work and `Thread.sleep`, uses SQL concatenation, provides no meaningful receipt action, and does not handle database exceptions or connection cleanup robustly. The revised design separates `FeeWindow` from `FeeService`, validates before persistence, uses `PreparedStatement` and try-with-resources, and gives each control a focused handler. For a larger application, payment should run in a `SwingWorker` so the UI remains responsive.

**Listener alternative:** Separate listeners or Swing Actions avoid a chain of up to k command comparisons; dispatch is effectively direct for the registered source. k listener/action objects require O(k) additional space. A shared Action is preferable when the same command is used by buttons, menus and shortcuts.

## Task 2 — Appointment desk
| Source | Event | Listener | Action |
|---|---|---|---|
| Department Choice | ItemEvent | ItemListener | Refresh doctor choices |
| Book button | ActionEvent | ActionListener | Validate fields and book |
| Window close | WindowEvent | WindowAdapter | Confirm before closing during booking |

Containment: Frame → form Panel (GridLayout) → patient ID, department, doctor and slot controls; status label and Book button. Doctor data is kept in a Map from department to doctor list. A map lookup is O(1) average; storing d departments and m total doctors takes O(d+m) space. Fetching each change from a database adds network/query latency each time and requires no full in-memory doctor map.

## Task 3 — Library layout
The search row uses FlowLayout, the outer frame uses BorderLayout, the centre results region expands, and the navigation controls use FlowLayout. Nested containers preserve the fixed-size search and navigation rows while allowing the centre to grow. A GridBagLayout alternative offers flexible constraints but has more configuration; absolute positioning does not adapt reliably to resizing. Layout computation is proportional to the number of components in the containers; nested panels add a small O(c) container/component space overhead.

## Task 4 — Drawing pad
The original handler misspells `mouseClicked` as `mouseclicked`, so it does not override the listener method. Direct drawing with `getGraphics()` is transient and disappears after repaint/expose/minimise-restore. The corrected version stores click coordinates and draws them in `paint()`; `@Override` makes a misspelled method a compile-time error. Repainting n points costs O(n); an off-screen image makes repaint a width×height image copy, but uses O(width×height) pixel storage rather than O(n) points.

## Task 5 — Event handling styles
The Converter source includes frame-listener, named-inner-class, anonymous-class and lambda examples. The button and Enter key invoke conversion; valid values are formatted to two decimal places and invalid input is reported. A frame listener is straightforward for a tiny form but couples the frame to the listener interface. A named inner class is reusable and readable; an anonymous class is local but verbose; a lambda is concise for a single-method interface. A shared Action or method reference scales best when buttons, menu items and keyboard shortcuts must trigger the same operation.

## Task 6 — Campus event registration
The implementation separates Swing UI, service validation and JDBC DAO work. Database reads and registration run in SwingWorker, keeping JDBC work off the event-dispatch thread. The SQL schema enforces uniqueness for (student_id, event_name), preventing duplicate registrations even when concurrent requests race. Filtering uses a parameterized query.

For 100,000 rows, loading everything at once increases initial latency and memory to O(n). Paging with LIMIT/OFFSET bounds the displayed data and memory to O(pageSize), though large offsets may become slower; keyset pagination is a further improvement for very large tables.

## Verification and evidence
The source files and schema are committed. GUI screenshots, MySQL execution evidence, and real measurements must be captured after running on a desktop with a configured MySQL Connector/J driver and database. The companion output notes are illustrative expected behaviour, not claimed execution logs.
