# Assignment 5 - CO5 Report
**Programming in Java — Data Persistence Engineering: Files and Databases**

**Student:** A. Lasya  
**Register number:** 192371090

## Part A — Persistence design and pseudocode

### A1 — Bus-pass file format
A delimited record stores passId, registerNo, name, routeNo and amount in a fixed field order. A production format should use a proper CSV escaping rule (quote fields containing commas, quotes or line breaks); a simple split-on-comma implementation is only a teaching example. A binary DataOutputStream format can write each field in order using writeUTF for strings, writeInt for route and writeDouble for amount. Include a format version and record length if long-term compatibility is required.

**Add:** open BufferedWriter in append mode, encode one complete record, write newline, close safely.  
**Find:** open BufferedReader, read each line, parse/validate fields, skip malformed records, compare pass ID and return matching record.  
**List by route:** scan each line, validate, parse route and emit matching rows.  
Missing file: report no stored passes or create it for add. Damaged line: record its line number and skip it. Interrupted write: write a temporary file and atomically replace the original for whole-file rewrites; append-only writes should use validation/recovery and backups.

A RandomAccessFile with fixed-size records supports O(1) access by known record index, but searching by pass ID still needs an index or O(n) scan. Fixed-size fields waste padding space when values are shorter than the reserved field width.

### A2 — Buffered I/O
A 50 MB byte-at-a-time implementation can make roughly 50 million read calls at the Java API level. Buffering reads blocks (for example 8 KB), reducing underlying read operations to roughly fileSize/bufferSize, while the scan remains O(fileSize) time. A buffer may offer little benefit when the source is already memory-backed or data is consumed only once in very small amounts. Files.readAllLines uses O(fileSize) memory; streaming uses O(bufferSize) additional memory.

### A3 — DAO pattern
StudentDB, CourseDB and MarksDB repeat connection handling, SQL execution, ResultSet mapping and resource closing. A generic Dao<T,K> can declare insert, findById, findAll, update and delete; StudentDao supplies SQL and maps Student fields to student table columns. Keeping SQL in DAO classes localises schema changes. It does not change the asymptotic database operation cost; it improves maintainability.

### A4 — Serialization
Mark the live JDBC Connection transient because it is not durable application state and cannot be meaningfully restored from an object stream. Passwords should not be serialized in plaintext; store a secure credential reference or hash-based authentication data instead. Serializable object graphs recursively include reachable serializable objects. A stable serialVersionUID permits compatible class evolution; a newly added field in an older stream is assigned its Java default when compatible, while incompatible class changes can cause InvalidClassException.

CSV/JSON is more interoperable and easier to inspect but needs explicit schema/version handling and parsing. Java serialization is convenient for object graphs but couples stored data to Java class definitions and is unsafe for untrusted input unless carefully filtered.

### A5 — Transactions and SQL injection
For a wallet transfer, disable auto-commit, debit the source only if its balance is sufficient, credit the destination, then commit. On any failure, rollback. Two independent auto-commit updates can leave money debited but not credited. PreparedStatement binds user ID and password as data, preventing input from changing the SQL structure. Concatenated SQL can be manipulated by quote characters and boolean expressions.

## Part B — Implementation and investigation

### B1 — File store and line counter
PassStore implements append, find and damaged-row skipping. BufferedLineCounter counts line-feed bytes with an 8 KB buffer. The expected search complexity is O(n) for n records; buffered line counting is O(fileSize) time and O(bufferSize) additional memory. The provided CSV demonstration is intentionally simple and does not fully implement quoted CSV escaping.

### B2 — Adversarial login
LoginSecurityDemo contrasts concatenated Statement SQL with PreparedStatement. Run only against a local test database created for this exercise. Do not test against production or third-party systems. The demonstration requires a login_test table with user_id and user_password columns. Parameterized input remains data and cannot append SQL predicates.

### B3 — JDBC batch processing
PassLoader uses PreparedStatement batches of 1,000 rows in one transaction. It commits only after all batches succeed and rolls back on SQL/runtime failure. Run schema.sql first. Compare Statement, individual PreparedStatement and batch PreparedStatement using the same generated input sizes on the same machine; record the actual elapsed time rather than inventing benchmark results.

### B4 — Migration and metadata
PassMigration validates five-field input rows, records invalid lines in rejected-passes.txt, loads valid rows transactionally and reports database/driver/table metadata. A shared relational database is the appropriate production choice for concurrent counters, centralised search, uniqueness constraints and transaction safety across campuses. Per-campus serialized files require synchronisation, merging and cross-campus search and are harder to keep consistent.

## Verification and evidence
Java source files and SQL schema are committed. Actual timing tables, database screenshots, and failure-injection evidence must be captured after running against the student's local MySQL installation. The output notes are illustrative expected behaviour and are not presented as measured results.
