# LunaCLI - Command Line Interface for Database Management

LunaCLI is a command-line tool designed to interact with POSTGRESQL. It allows users to connect, execute SQL commands, and configure connection settings dynamically.

## Features
- Connect to PostgreSQL databases with custom credentials.
- Change database connection settings (username, password, database name, and port).
- Execute SQL commands directly from the CLI.
- Supports dynamic port configuration.
- Supports various database operations like transactions, table management, and schema creation.
- Create an entity and clone it to connect to the terminal.
- Display query results in table format.
- History command
- Schedule a query
- Execute SQL file
- Export table data to CSV
- Multiple Query Execution
- Retrieve results in sequential order, improving efficiency for batch operations.
- Save frequently used SQL commands for quick access. (Snippets)
- Prometheus Support for Query Monitoring
  
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
 luna connect postgresql username:postgres password:postgres database:managify
```
![cnn](https://github.com/user-attachments/assets/3aa3ec69-b005-4f13-8d6e-8062adf6a757)



### Changing the Database Port
To change the port dynamically, use:
```sh
 luna port:5433
```
After changing the port, restart LunaCLI for changes to take effect.

### Executing SQL Commands
Once connected, you can execute SQL queries directly:
```sh
 luna select-from users
```
![asdasd](https://github.com/user-attachments/assets/e1d87971-5edd-4071-8728-13141e58d557)


### Displaying Connection Info
To view current connection settings:
```sh
 luna info
```
This will output details like database name, port, and connection status.

![Ekran görüntüsü 2025-02-23 011859](https://github.com/user-attachments/assets/ee821bf6-e62d-4519-9f55-0d082cd9e5cc)



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
- history: Display all past commands
- schedule command:<query> delay:<delay> unit:<unit>
- export command:<query> filepath:<filepath>
- execute filepath:<sqlFile>
```

## Development & Contribution
To run the project without packaging:
```sh
 mvn exec:java -Dexec.mainClass="org.cli.Main"
```

Feel free to contribute by submitting pull requests or reporting issues.

## License
This project is licensed under the MIT License.


