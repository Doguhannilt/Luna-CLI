# LunaCLI - Command Line Interface for Database Management

LunaCLI is a command-line tool designed to interact with POSTGRESQL. It allows users to connect, execute SQL commands, and configure connection settings dynamically.

## Features
- Connect to PostgreSQL databases with custom credentials.
- Change database connection settings (username, password, database name, and port).
- Execute SQL commands directly from the CLI.
- Supports dynamic port configuration.
- Supports various database operations like transactions, table management, and schema creation.
- Create an entity and clone it to connect to the terminal.

## **Some features that I've been working on**  

LunaCLI is evolving! We are excited to introduce the following upcoming features to enhance usability and efficiency:  

### **📊 Display Query Results in Table Format**  
- Query results will now be displayed in a structured table format for better readability.  
- Example:  
  ```
  +----+---------+-----------------+
  | ID |  Name   | Email           |
  +----+---------+-----------------+
  |  1 |  Alice  | alice@mail.com  |
  |  2 |  Bob    | bob@mail.com    |
  +----+---------+-----------------+
  ```  

### **📜 History Command**  
- View previously executed commands with the `history` command.  
- Example:  
  ```
  $ history
  1. select-from users
  2. insert-into orders (1, "Laptop", 5000)
  3. update users set name="John" where id=2
  ```
- Easily re-execute a command using `!<command_number>`.  
  ```
  $ !2  # Runs "insert-into orders (1, 'Laptop', 5000)"
  ```

### **⏳ Scheduled Command Execution**  
- Schedule recurring SQL commands to run automatically.  
- Example:  
  ```
  $ schedule run "delete-from logs where created_at < NOW() - interval '7 days'" daily at 02:00
  ```
- Supports scheduling backups and routine maintenance tasks.  

### **📂 Execute SQL Files**  
- Run `.sql` files directly from LunaCLI.  
- Example:  
  ```
  $ execute-file schema.sql
  ```
- Ideal for batch execution of SQL scripts.  

### **📤 Export Table Data to JSON & CSV**  
- Save query results as JSON or CSV files.  
- Example:  
  ```
  $ export users format:json
  ```
  ```
  $ export orders format:csv
  ```

### **📈 Basic Graph Support**  
- Visualize query results with simple terminal-based graphs.  
- Example:  
  ```
  $ visualize select count(*) from orders group by category
  ```
- Helps in quick data analysis and trend identification.  



## Prerequisites
Before running LunaCLI, ensure you have the following installed:
- **Java 17 or higher**
- **Maven** (for building the project)
- **PostgreSQL** (for database connectivity)

## Installation
Clone the repository and build the project using Maven:
```sh
 git clone https://github.com/doguhannilt/LunaCLI.git
 cd LunaCLI
 mvn clean package
```

## Usage

### Running the CLI
To start the LunaCLI, execute:
```sh
 java -jar LunaCLI.jar
```

### Connecting to a Database (PostgresSQL)
Use the following command to connect:
```sh
 luna connect postgresql username:yourUser password:yourPassword database:yourDatabase
```
Example:
```sh
 luna connect postgresql username:admin password:1234 database:mydb
```
![postgresqlconnection](https://github.com/user-attachments/assets/5a310268-b399-4044-9b3b-fc5cc1dfc089)


### Changing the Database Port
To change the port dynamically, use:
```sh
 luna port:5433
```
After changing the port, restart LunaCLI for changes to take effect.

### Executing SQL Commands
Once connected, you can execute SQL queries directly:
```sh
 SELECT * FROM users;
```
![select-from](https://github.com/user-attachments/assets/a772bf09-4840-4463-b3d6-91ce8e8d1fa7)

### Displaying Connection Info
To view current connection settings:
```sh
 luna info
```
This will output details like database name, port, and connection status.

![lunainfo](https://github.com/user-attachments/assets/420848d6-5ba2-4b61-86f8-faed8d6c0e0f)



### Available Commands
```sh
- begin-transaction: Start a new transaction.
- commit: Commit the current transaction.
- rollback: Rollback the current transaction.
- call-procedure <procedure_name>: Call a stored procedure.
- call-function <function_name>: Call a function.
- create-table <table_name> <columns>: Create a new table.
- drop-table <table_name>: Drop a table.
- create-schema <schema_name>: Create a new schema.
- insert-into <table_name> <values>: Insert data into a table.
- select-from <table_name> [condition]: Select data from a table.
- update <table_name> <set_clause> [condition]: Update data in a table.
- delete-from <table_name> [condition]: Delete data from a table.
- backup-database <file_path>: Backup the database.
- restore-database <file_path>: Restore the database.
- help: Show this help message.
- save username:<username> password:<password> database:<database> | Save User
- load users | Display all users
- force user:<EntityId> | Get user by Id
- clone user:<EntityId> | Connect a cloned user
```

## Development & Contribution
To run the project without packaging:
```sh
 mvn exec:java -Dexec.mainClass="org.cli.Main"
```

Feel free to contribute by submitting pull requests or reporting issues.

## License
This project is licensed under the MIT License.


