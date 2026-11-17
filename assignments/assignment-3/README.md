# Java Assignment 3 — CO3

**Student:** A. Lasya  
**Register number:** 192371090  
**Course:** Programming in Java — Exception Handling and Multithreading  
**Java:** Java 11+ recommended

## Task-wise source files
- Q1: `task-01/ATMWithdrawal.java`
- Q2: `task-02/StorageDemo.java`
- Q3: `task-03/SalesReport.java`
- Q4: `task-04/ScannerStations.java`
- Q5: `task-05/SeatBooking.java`
- Q6: `task-06/TokenCounter.java`
- Q7: `task-07/ResultProcessor.java`

## Report and outputs
- Detailed analysis: [report.md](report.md)
- Expected behaviour and test notes: [outputs/](outputs/)
- Add actual screenshots, test tables, thread-order observations, debugger traces and timing measurements after running locally.

## Compile and run
Open a task folder and run:

```bash
javac FileName.java
java ClassName
```

Q1 reads a withdrawal amount from standard input. Q3 creates sample store files in the working directory. Q7 creates sample score files and processes them with thread pools of 1, 2, 4 and 8.

The CO3 PDF specifically asks for repeated thread-order experiments, race-condition comparisons, deadlock prevention evidence and pool-size timings. These must be recorded from actual execution.
