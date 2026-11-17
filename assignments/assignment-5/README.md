# Java Assignment 5 - CO5

Source: Assignment CO5 - Data Persistence Engineering: Files and Databases.

## Task-wise source files
- A1/B1: `task-01/PassStore.java` - CSV pass store, add and find; skips malformed rows.
- A2/B1: `task-01/BufferedLineCounter.java` - buffered line counter.
- A3: DAO<T,K> design is specified as a design/pseudocode task in the PDF; no standalone Java implementation is prescribed.
- A4: serialization/transient/versioning is a design task in the PDF.
- A5/B2: `task-02/LoginSecurityDemo.java` - SQL concatenation versus PreparedStatement.
- B3: `task-03/PassLoader.java`, `PassLoaderDemo.java`, `schema.sql` - transactional batch insert.
- B4: `task-04/PassMigration.java` - validated CSV migration and JDBC metadata.

## Run
Use Java 11 or later. JDBC tasks require MySQL Connector/J and environment variables `JAVA_DB_URL`, `JAVA_DB_USER`, and `JAVA_DB_PASSWORD`. Run `task-03/schema.sql` before PassLoaderDemo. LoginSecurityDemo expects a local `login_test(user_id, user_password)` table. PassMigration expects five CSV fields: passId,regNo,name,route,amount.

Compile a standalone class with `javac FileName.java` and run with `java ClassName`. Compile PassLoader and PassLoaderDemo together.

Only source code and SQL schema are included. No output screenshots or measurement files are included.