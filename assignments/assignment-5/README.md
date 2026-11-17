# Java Assignment 5 - CO5

**Student:** A. Lasya  
**Register number:** 192371090  
**Course:** Programming in Java — Data Persistence Engineering: Files and Databases

## Task files
- A1/B1: `task-01/PassStore.java` — text-file bus-pass store
- A2/B1: `task-01/BufferedLineCounter.java` — buffered line counter
- A3: DAO design described in report
- A4: serialization design described in report
- A5/B2: `task-02/LoginSecurityDemo.java` — unsafe SQL and PreparedStatement comparison
- B3: `task-03/PassLoader.java`, `PassLoaderDemo.java`, `schema.sql` — transactional JDBC batch insert
- B4: `task-04/PassMigration.java` — migration and DatabaseMetaData inspection

## Report and outputs
- Design, pseudocode and engineering analysis: [report.md](report.md)
- Illustrative expected behaviour notes: [outputs/](outputs/)
- Add actual benchmark measurements, database result captures and failure/rollback evidence to `outputs/` after executing locally. The text notes are not measured execution logs.

## Compile and run
Use Java 11 or later. JDBC tasks require MySQL Connector/J and environment variables `JAVA_DB_URL`, `JAVA_DB_USER`, and `JAVA_DB_PASSWORD`. Run `task-03/schema.sql` before PassLoaderDemo. LoginSecurityDemo requires a local `login_test(user_id, user_password)` table. PassMigration expects CSV rows with five fields: passId,regNo,name,route,amount.

Compile standalone files with `javac FileName.java`, then run with `java ClassName`. Compile `PassLoader.java` and `PassLoaderDemo.java` together.

Only Java source code, SQL schema, report and illustrative output notes are currently included. Actual execution evidence and benchmark measurements remain to be added after local execution.
