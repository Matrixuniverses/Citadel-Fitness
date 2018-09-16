package seng202.group2.data_functions;

import org.sqlite.SQLiteConfig;

import java.sql.*;

public class databaseWriter {
    private static String dbURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessLocalDatabase.db";
    private static final String driver = "org.sqlite.JDBC";
    private static Connection dbConn = null;

    /**
     * Establishes a connection to a sql database with the filename CitadelFitnessLocalDatabase.db which is assigned to
     * the local java.sql.Connection object. The connection is set up with the configuration that Foreign Keys in the
     * database are enforced. The database is located in the User's home directory and the database is created in this
     * directory if it does not already exist.
     * @throws SQLException if an error occurs preforming sql operations on the database.
     */
    public static void connectToDB() throws SQLException {
        try {
            Class.forName(driver);
            SQLiteConfig sqlConfig = new SQLiteConfig();
            sqlConfig.enforceForeignKeys(true);
            dbConn = DriverManager.getConnection(dbURL, sqlConfig.toProperties());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     *
     * @return the Connection object that is linked to the database if not null.
     */
    public static Connection getDbConnection() {
        return dbConn;
    }

    /**
     * Closes down the connection to the database by setting the database Connection object to null.
     * @throws SQLException if an error occurs disconnecting from the database
     */
    public static void disconnectFromDB() throws SQLException {
        if (dbConn != null) {
            if (!dbConn.isClosed()) {
                dbConn.close();
            }
        }

    }

    private static void executeSQLStatement(String sqlStmt, Connection dbConn) throws SQLException {

        Statement newStmt = dbConn.createStatement();
        newStmt.execute(sqlStmt);
    }


    /**
     *  Creates a new database for the application if it does not already exist and creates the databases structure (tables
     *  and attributes). The database is stored in the project's directory and consists of four tables:
     *  users, activities, datapoints and targets.
     * @return true if the function runs without error
     * @throws SQLException if an sql related problem is encountered trying to set up the database.
     */
    public static boolean createDatabase() throws SQLException {
        connectToDB();
        boolean success = false;
        if (dbConn != null) {
            String sqlCreateUserTable = "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "user_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "name varchar(100) NOT NULL, \n"
                    + "age integer NOT NULL, \n"
                    + "height real NOT NULL, \n"
                    + "weight real NOT NULL \n"
                    + ");";

            String sqlCreateActivityTable = "CREATE TABLE IF NOT EXISTS Activities (\n"
                    + "activity_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "user_id integer NOT NULL, \n"
                    + "name varchar(100) NOT NULL, \n"
                    + "date_string varchar(100) NOT NULL, \n"
                    + "date date NOT NULL, \n "
                    + "type varchar(100) NOT NULL, \n"
                    + "total_distance real NOT NULL,"
                    + "total_time real NOT NULL,"
                    + "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE"
                    + ");";

            String sqlCreateDatapointTable = "CREATE TABLE IF NOT EXISTS Datapoints (\n"
                    + "dp_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "activity_id integer NOT NULL, \n"
                    + "dp_date_string varchar(100) NOT NULL, \n"
                    + "dp_date date NOT NULL, \n"
                    + "heart_rate integer NOT NULL, \n"
                    + "latitude real NOT NULL, \n"
                    + "longitude real NOT NULL, \n"
                    + "altitude real NOT NULL, \n"
                    + "FOREIGN KEY(activity_id) REFERENCES activities(activity_id) ON DELETE CASCADE"
                    + ");";

            String sqlCreateTargetTable = "CREATE TABLE IF NOT EXISTS Targets (\n"
                    + "target_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "user_id integer NOT NULL, \n"
                    + "name varchar(100) NOT NULL, \n"
                    + "date_achieved varchar(100), \n"
                    + "type varchar(100) NOT NULL, \n"
                    + "initial_value real NOT NULL, \n"
                    + "current_value real NOT NULL, \n"
                    + "final_value real NOT NULL, \n"
                    + "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE\n"
                    + ");";


            executeSQLStatement(sqlCreateUserTable, dbConn);
            executeSQLStatement(sqlCreateActivityTable, dbConn);
            executeSQLStatement(sqlCreateDatapointTable, dbConn);
            executeSQLStatement(sqlCreateTargetTable, dbConn);


            success = true;


        }
        disconnectFromDB();

        return success;

    }

    /**
     * Executes a inputted sql query (as a string) and returns the result set from the query
     * @param queryStmt A string representation of a sql query statement
     * @return The result set from querying the database.
     * @throws SQLException if an error occurs querying the database.
     */
    public static ResultSet executeDBQuery(String queryStmt) throws SQLException {
        Statement dbStmt = dbConn.createStatement();

        return dbStmt.executeQuery(queryStmt);
    }

    public static void executeDBUpdate(String updateStmt) throws SQLException {

        executeSQLStatement(updateStmt, dbConn);

    }





}
