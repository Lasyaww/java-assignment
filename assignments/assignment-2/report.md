# Assignment 2 — CO2 Report
**Student:** A. Lasya  
**Register number:** 192371090  
**Course:** Programming in Java — Strings and Collections

## Q1 — Building text under load
String is immutable, so repeated concatenation in a loop creates intermediate strings and can approach O(m²) copying for m appended pieces. StringBuilder mutates one buffer and is the normal single-threaded choice; StringBuffer synchronises methods for shared mutable use. The assignment asks for contrasting workloads and repeated nanoTime/memory observations. Use StringBuilder for per-ticket text and a thread-safe logger/queue or StringBuffer for a genuinely shared log. StringJoiner is suitable for delimiter-separated fields. Actual measurements belong in outputs and must be collected locally.

## Q2 — Collection choice and performance
ArrayList offers O(1) indexed access and amortised O(1) append, but front/middle insertions and removals are O(n). LinkedList has O(1) insertion/removal once a node is reached, but indexed access/search is O(n) and node overhead is higher. ArrayDeque is generally preferred for queue/deque operations at both ends, with amortised O(1) operations. Benchmark with repeated runs and identical workloads; one-shot nanoTime measurements are noisy.

## Q3 — equals/hashCode contract
Equal objects must have equal hash codes. HashSet uses hashCode to select a bucket and equals to resolve collisions. If equals compares registration number but hashCode is inherited/object-identity based, logically duplicate students may occupy separate buckets. Override both using the same stable identity field. Expected corrected unique-student set size is 2 for two unique registration numbers.

## Q4 — Map comparison
HashMap provides average O(1) get/put and no iteration-order guarantee. LinkedHashMap provides average O(1) operations and preserves insertion order with additional linked-entry memory. TreeMap provides O(log n) get/put and sorted-key iteration. Use HashMap for fast lookup, LinkedHashMap for insertion-order reports, and TreeMap for sorted medicine-code reports. Benchmarks should separate population, lookup and traversal.

## Q5 — Generics and Comparator
A generic Leaderboard<T> stores any comparable domain type while Comparator<? super T> supplies a ranking policy. Sorting n entries costs O(n log n) time; copying the list costs O(n) extra space. A comparator can order by points descending, goal difference descending and name ascending, keeping ranking rules outside the Team model.

## Q6 — PriorityQueue triage
PriorityQueue orders patients by lower severity number first, then earlier arrival time. Insertion and removal are O(log n), peek is O(1), and storage is O(n). A stable arrival counter resolves equal-severity ties. Do not rely on iteration order of PriorityQueue; repeatedly poll to obtain priority order.

## Q7 — Queue redesign
Repeatedly sorting a waiting list before each service is inefficient: each sort costs O(n log n), repeated across n patients can approach O(n² log n). A PriorityQueue maintains order incrementally: n inserts and n polls cost O(n log n) total, with O(n) storage. An ExecutorService is not required for priority ordering; it addresses concurrent task execution, a separate concern. Compare identical generated patient data and record actual timings.

## Validation and evidence
The programs are included in task folders. Timing outputs are machine/JVM dependent and must be captured from actual repeated runs. Expected behaviour notes in `outputs/` are illustrative, not recorded benchmark results.
