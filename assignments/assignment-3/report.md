# Assignment 3 — CO3 Report
**Student:** A. Lasya  
**Register number:** 192371090  
**Course:** Programming in Java — Exception Handling and Multithreading

## Q1 — Bank ATM failure handling
Potential failures include nonnumeric input (InputMismatchException), negative amount (IllegalArgumentException), insufficient balance (custom checked exception), missing card-reader file (IOException), and serious JVM Errors. Catch specific exceptions before broader types; finally executes during normal completion and exception propagation, except abnormal JVM termination. Five tests should cover valid withdrawal, letters, negative value, amount over balance and missing file. An error-code design avoids stack-trace construction for expected failures but pushes explicit checks and status handling to callers; normal exception creation/throwing can be expensive due to stack-trace capture.

## Q2 — User-defined exceptions and propagation
The portal needs meaningful domain exceptions for closed exam window, unregistered course and oversized/unavailable submission storage. Checked exceptions force callers to handle or declare expected business/storage failures. The storage layer should wrap IOException as SubmissionStorageException while preserving the cause. The supplied expected output is:
Saved answer01.pdf
Error: Could not store answer02.pdf
Cause: Disk quota exceeded
Without passing the cause to the constructor, getCause() is null and the original storage reason is lost. Exception stack traces require memory proportional to captured stack depth.

## Q3 — Resource safety and debugging
The report generator opens multiple readers and a writer. Manual close in finally can leak earlier resources if a later open/read/write fails before all close paths are reached. Try-with-resources closes successfully opened resources in reverse declaration order and retains secondary close failures as suppressed exceptions. Processing f files with total B bytes is O(B) time; try-with-resources bounds simultaneously open handles to the resources in the active scope rather than accumulating leaked handles. It does not itself solve partial report semantics, corrupt input validation or atomic publication; write to a temporary output and move it into place after success.

## Q4 — Thread creation and life cycle
Implement each scanner station once by extending Thread and once by implementing Runnable. start() schedules a new thread; calling run() directly is an ordinary method call on the current thread. sleep() pauses the current thread, isAlive() observes liveness, and join() waits for completion. Output order is scheduler-dependent; correctness must not depend on a particular order. For n scripts and p worker threads, idealised processing time is roughly total work/p plus scheduling overhead. A fixed pool bounds thread count and stack-memory use.

## Q5 — Cinema seat-booking race
The check-then-set sequence must be atomic. Without synchronisation, two threads can both observe a free seat and both mark it booked. A synchronized method locks the booking object for the whole method; a synchronized block can lock only the critical section. Expected completed result for 100 users competing for 10 seats is Seats sold: 10. AtomicBoolean.compareAndSet, ReentrantLock or ConcurrentHashMap.putIfAbsent are alternatives; each stores O(s) seat state for s seats.

## Q6 — Deadlock and producer-consumer
Deadlock requires mutual exclusion, hold-and-wait, no pre-emption and circular wait. Locking wallet accounts in a consistent global order removes circular wait. For the bounded token counter, wait() must be called while holding the monitor and inside a while loop to recheck the condition after wakeup. notifyAll() wakes all waiters so each can recheck its predicate; sleep() does not release a monitor, while wait() does. The buffer capacity is five, so its count must remain in [0,5].

## Q7 — Fault-tolerant concurrent result processing
Independent files can be processed concurrently. Use ExecutorService with a bounded pool, Callable per file and Future to collect each outcome. Catch per-file failures inside the task so one corrupt/missing file does not cancel the entire batch. A concurrent collection can store outcomes safely; final summary counts successes and failures. Measure pool sizes 1, 2, 4 and 8 using the same input set. More threads may not help when work is I/O-bound, CPU-bound, or limited by disk throughput. For N files and p workers, ideal work time approaches total work/p; memory includes O(p) worker stacks and O(N) task/result metadata.

## Validation and evidence
The source programs are in Q1–Q7 folders. Expected deterministic behaviour is noted under `outputs/`. Actual ten-run thread ordering, race-condition comparisons, deadlock demonstrations, debug traces and pool-size timing results must be captured by executing the programs locally. No measured values are invented in this report.
