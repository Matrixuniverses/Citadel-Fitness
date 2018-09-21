package seng202.group2.data;

import org.sqlite.SQLiteConfig;

import java.sql.*;

public class DatabaseOperations {
    private static String dbURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessLocalDatabase.db";
    private static final String driver = "org.sqlite.JDBC";
    private static Connection dbConn = null;

    private static SQLiteConfig configureSQl() {
        SQLiteConfig sqlConfig = new SQLiteConfig();
        sqlConfig.enforceForeignKeys(true);
        return sqlConfig;
    }

    /**
     * Establishes a connection to a sql data with the filename CitadelFitnessLocalDatabase.db which is assigned to
     * the local java.sql.Connection object. The connection is set up with the configuration that Foreign Keys in the
     * data are enforced. The data is located in the User's home directory and the data is created in this
     * directory if it does not already exist.
     * @throws SQLException if an error occurs preforming sql operations on the data.
     */
    public static void connectToDB() throws SQLException {
        try {
            Class.forName(driver);
            SQLiteConfig sqlConfig = configureSQl();
            dbConn = DriverManager.getConnection(dbURL, sqlConfig.toProperties());
            sqlConfig.apply(dbConn);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets a new data URL.
     * @param newDatabaseURL The new URL for the data
     */
    public static void setDatabaseURL(String newDatabaseURL) {
        dbURL = newDatabaseURL;
    }




    /**
     *
     * @return the Connection object that is linked to the data if not null.
     */
    public static Connection getDbConnection() {
        return dbConn;
    }

    /**
     * Closes down the connection to the data by setting the data Connection object to null.
     * @throws SQLException if an error occurs disconnecting from the data
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
     *  Creates a new data for the application if it does not already exist and creates the databases structure (tables
     *  and attributes). The data is stored in the project's directory and consists of four tables:
     *  users, activities, datapoints and targets.     *
     * @throws SQLException if an sql related problem is encountered trying to set up the data.
     */
    public static void createDatabase() throws SQLException {
        connectToDB();
        if (dbConn != null) {
            String sqlEnableForeignKeyDelete = "PRAGMA foreign_keys = ON;";

            String sqlCreateUserTable = "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "user_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "name varchar(100) NOT NULL, \n"
                    + "age integer NOT NULL, \n"
                    + "height real NOT NULL, \n"
                    + "weight real NOT NULL, \n"
                    + "gender varchar(2) \n"
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
                    + "calories_burnt real NOT NULL,"
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
                    + "time_delta real NOT NULL, \n"
                    + "dist_delta real NOT NULL, \n"
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

            executeSQLStatement(sqlEnableForeignKeyDelete, dbConn);
            executeSQLStatement(sqlCreateUserTable, dbConn);
            executeSQLStatement(sqlCreateActivityTable, dbConn);
            executeSQLStatement(sqlCreateDatapointTable, dbConn);
            executeSQLStatement(sqlCreateTargetTable, dbConn);

        }
        disconnectFromDB();
    }

    public static void resetDatabase(boolean totalReset) throws SQLException {
        connectToDB();
        if (totalReset) {
            String sqlDropUserTableStmt = "DROP TABLE IF EXISTS Users";
            String sqlDropActivityTableStmt = "DROP TABLE IF EXISTS Activities";
            String sqlDropDatapointsTableStmt = "DROP TABLE IF EXISTS Datapoints";
            String sqlDropTargetsTableStmt = "DROP TABLE IF EXISTS Targets";
            PreparedStatement pDropTableStmt = dbConn.prepareStatement(sqlDropUserTableStmt);
            pDropTableStmt.executeUpdate();
            pDropTableStmt = dbConn.prepareStatement(sqlDropActivityTableStmt);
            pDropTableStmt.executeUpdate();
            pDropTableStmt = dbConn.prepareStatement(sqlDropDatapointsTableStmt);
            pDropTableStmt.executeUpdate();
            pDropTableStmt = dbConn.prepareStatement(sqlDropTargetsTableStmt);
            pDropTableStmt.executeUpdate();

        } else {

            String sqlDeleteUsersStmt = "DELETE FROM Users";
            PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteUsersStmt);
            pDeleteStmt.executeUpdate();

        }
        disconnectFromDB();
    }

    /**
     * Executes a inputted sql query (as a string) and returns the result set from the query
     * @param queryStmt A string representation of a sql query statement
     * @return The result set from querying the data.
     * @throws SQLException if an error occurs querying the data.
     */
    public static ResultSet executeDBQuery(String queryStmt) throws SQLException {
        Statement dbStmt = dbConn.createStatement();

        return dbStmt.executeQuery(queryStmt);
    }

    public static void executeDBUpdate(String updateStmt) throws SQLException {

        executeSQLStatement(updateStmt, dbConn);

    }





}
