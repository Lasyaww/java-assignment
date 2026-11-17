# Assignment 1 — CO1 Report
**Student:** A. Lasya  
**Register number:** 192371090  
**Title:** Design, Implement and Validate an Object-Oriented Model for a Campus Smart-Parking System

## Problem analysis
The system accepts vehicle registration/type, user category, pass validity, entry/exit time, floor layout and charging usage. It returns an allocated floor/slot, a unique ticket, parking/charging fee, and updated occupancy. Candidate classes include Vehicle (identity and category), ParkingSlot (one occupancy position), ParkingFacility (floor layout and allocation), ParkingTicket (entry record), ParkingPass (eligibility), and a pricing/charging capability. Core invariants: a slot contains at most one vehicle; a vehicle cannot hold two active tickets; ticket numbers are unique; invalid slot coordinates are rejected; exit time cannot precede entry; valid student/staff monthly passes receive the stated free-parking benefit; EV charging is charged separately.

## Task-wise report and analysis

### Task 1 — Identify objects
Vehicle, ParkingSlot, ParkingFacility, ParkingTicket, ParkingPass and Chargeable separate identity, occupancy, allocation, visit history, eligibility and EV capability. Inputs/outputs and business rules are described above. Searching n vehicle objects by registration is O(n); parallel arrays also require O(n) search but make adding a new attribute error-prone because every parallel array must remain aligned. Both store O(n) vehicle data; objects improve cohesion and maintainability.

### Task 2 — Data representation
Use String for registration, int for floor/slot, LocalDateTime for entry/exit, long/int for elapsed minutes, and double or BigDecimal for money (BigDecimal is preferable for production currency). A jagged array represents floors with different slot counts. Integer division 95/60 gives 1, undercharging a 95-minute stay; ceiling division for nonnegative minutes is (minutes+59)/60. A flat array needs prefix offsets when floor sizes differ; a jagged array directly expresses each floor. Slot lookup is O(1) when coordinates are known; scanning for a free slot is O(total slots).

### Task 3 — Control logic
Allocation scans each floor and slot, assigns the first suitable free slot, then returns its coordinates; if none exists it returns null. Release validates both indices and occupancy before clearing. Fee calculation determines billable duration, applies the vehicle rate and user-pass rule, then adds EV energy charges. The supplied complete-code task expects allocation positions [0,0], [0,1], [1,0], then null; release results true, false, false; the freed position is reused. The scan approach is O(S) worst case for S total slots and O(1) auxiliary space. A free-slot stack/map can make allocation/release approximately O(1) but needs O(S) extra storage and careful handling of vehicle suitability.

### Task 4 — Classes and constructors
Use parameterised constructors to establish required identity, overload constructors for optional values, and constructor chaining with this(...) to avoid duplicated validation. A copy constructor is useful only when a deliberate snapshot/duplicate record is required; a live ticket should not be duplicated as a second active ticket. References live in stack frames/objects and object instances are allocated on the heap. Creating n tickets takes O(n) total time and O(n) retained memory.

### Task 5 — Static members and object life cycle
A ticket sequence shared across a facility may be static; occupancy is instance state. If each gate needs independent numbering, the counter belongs to each ParkingFacility; if ticket numbers must be globally unique across gates, use a shared ticket-number service or database sequence. A departed ticket becomes eligible for garbage collection when no reachable reference remains. Static counter storage is O(1); one counter per k facilities is O(k).

### Task 6 — Inheritance hierarchy
A Vehicle superclass can provide registration identity and common fee contract; TwoWheeler and Car specialise rates; ElectricCar extends Car and implements Chargeable. This is hierarchical plus multilevel inheritance. super(...) initialises superclass state before subclass state. Adding categories via inheritance creates a class per category; composition with VehicleType/rate strategy can make new categories data/configuration and reduce class growth. Fee lookup is O(1) in either approach; per-object storage is O(1).

### Task 7 — Override and specialise behaviour
Subclasses override ratePerHour/calculateFee with @Override. Keep registration identity final; use final methods/classes only where extension must be prohibited. equals and hashCode must use the same identity fields. A switch-based fee method is also O(1), but adding a type requires modifying the central switch and risks missing branches; overriding localises each rule.

### Task 8 — Polymorphism
Overloaded methods demonstrate compile-time polymorphism. A Vehicle[] can hold TwoWheeler, Car and ElectricCar references; runtime dispatch selects the actual object's implementation. The supplied FeeDemo expected output is TwoWheeler TN01A1 -> Rs 30.0; Car TN02B2 -> Rs 90.0; ElectricCar TN03C3 -> Rs 210.0; and true for equal registration values. The last result follows from registration-based equality across subclasses; whether this is appropriate depends on whether registration uniquely identifies a vehicle regardless of subtype.

### Task 9 — Abstraction
Vehicle should be abstract because a generic vehicle has no single concrete rate. Chargeable is a capability implemented only by vehicles that can charge. Abstract classes share state/constructors; interfaces allow unrelated classes to share a capability. Adding a charging-capable category requires implementing the interface without forcing all vehicles to support charging.

### Task 10 — Packages and access modifiers
Place domain classes in a model package and application/UI classes in an app package. Keep fields private, expose validated public methods, and use protected only for deliberate subclass extension. Package-private members should remain internal to the package. Encapsulation prevents callers from directly corrupting slot or vehicle state.

### Tasks 11–12 — Design challenge and integrated system
The design should be modified toward extensible pricing strategies and vehicle capabilities rather than a growing chain of type checks. Separate allocation, fee policy, user-pass eligibility and EV charging. The integrated implementation demonstrates jagged floors, ticket tracking, monthly/student-staff passes, visitor treatment, EV charging and additional categories. New fee rules should be added as policy implementations rather than rewriting stable vehicle classes.

## Validation and evidence
The deterministic expected outputs supplied by the assignment are recorded in `outputs/`. Actual screenshots and locally executed evidence must be added after compiling/running each task. Timing measurements must be collected on the student's machine; no benchmark values are fabricated here.
