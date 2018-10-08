

package seng202.group2.data;

import org.sqlite.SQLiteConfig;
import java.sql.*;

/**
 * The DatabaseOperations class handles all general functionality regarding to establishing a connection to and
 * maintaining the sqlite database. The database is stored in the home directory of the operating system that the
 * application has been launched on as a .db file called CitadelFitnessLocalDatabase.db and uses a JDBC driver in order
 * to be able to establish a connection with the database.
 */
public class DatabaseOperations {
    private static String dbURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessLocalDatabase.db";
    private static final String driver = "org.sqlite.JDBC";
    private static Connection dbConn = null;

    //Helper function which returns the appropriate configuration as a SQLiteConfig Object.
    //In this case foreign keys are set to true
    private static SQLiteConfig configureSQl() {
        SQLiteConfig sqlConfig = new SQLiteConfig();
        sqlConfig.enforceForeignKeys(true);
        sqlConfig.setCacheSize(100000);

        return sqlConfig;
    }


    /**
     * Establishes a connection to a sql data with the filename CitadelFitnessLocalDatabase.db which is assigned to
     * the local java.sql.Connection object. The connection is set up with the configuration that Foreign Keys in the
     * data are enforced. The data is located in the User's home directory and the data is created in this
     * directory if it does not already exist.
     * @return The Connection object that establishes a connection to the database
     * @throws SQLException if an error occurs preforming sql operations on the database.
     */
    public static Connection connectToDB() throws SQLException {
        try {
            Class.forName(driver);
            SQLiteConfig sqlConfig = configureSQl();
            dbConn = DriverManager.getConnection(dbURL, sqlConfig.toProperties());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dbConn;


    }


    /**
     * Sets a new data URL.
     * @param newDatabaseURL The new URL for the data
     */
    public static void setDatabaseURL(String newDatabaseURL) {
        dbURL = newDatabaseURL;
    }


    /**
     * Closes down the connection to the data by calling the close method on the Connection object.
     * @throws SQLException if an error occurs disconnecting from the database.
     */
    public static void disconnectFromDB() throws SQLException {
        if (dbConn != null) {
            if (!dbConn.isClosed()) {
                dbConn.close();
            }
        }

    }


    /**
     *  Creates a new data for the application if it does not already exist and creates the databases structure (tables
     *  and attributes). The data is stored in the project's directory and consists of four tables:
     *  users, activities, datapoints and targets.
     *  The Relations and attributes in the database are as follows
     *  Users
     *  <ul>
     *      <li>
     *          user_id - The unique id of a user that is stored in the database. Primary key and has the property Autoincrement.
     *      </li>
     *      <li>
     *          name - The name of the user. Text with the property not null.
     *      </li>
     *      <li>
     *          age - The age of the user. Integer with the property not null.
     *      </li>
     *      <li>
     *          height - The height of the user in cm. Real number with the property not null.
     *      </li>
     *      <li>
     *          weight - The weight of the user in kg. Real number with the property not null.
     *      </li>
     *      <li>
     *          gender - The gender of the user. Text either set as 'M','F' or left as a null value
     *      </li>
     *  </ul>
     *  Activities
     *  <ul>
     *      <li>
     *          activity_id - The unique id of an activity that is stored in the database. Primary key with the property Autoincrement.
     *      </li>
     *      <li>
     *          user_id - foreign key which references a user_id from a tuple in Users. Has the properties not null and on delete cascade.
     *      </li>
     *      <li>
     *          name - the name of the activity. Text with the property not null.
     *      </li>
     *      <li>
     *          date_string - a textual representation of the date that the activity was started on. Text with the property not null.
     *      </li>
     *      <li>
     *          date - the date that the activity was completed on represented by a long integer used to order activities by their date. Date type with property not null.
     *      </li>
     *      <li>
     *          type - the activity's type. Text with the property not null
     *      </li>
     *      <li>
     *          total_distance - the total distance traversed during the activity in metres. Real number with property not null
     *      </li>
     *      <li>
     *          total_time - the total time taken to complete the activity in minutes. Real number with property not null.
     *      </li>
     *      <li>
     *          calories_burnt - an approximation of the the total calories burnt during the exercise. Real number with property not null.
     *      </li>
     *  </ul>
     *  Datapoints
     *  <ul>
     *      <li>
     *          dp_id - The unique id of a datapoint that is stored in the database. Primary key with the property Autoincrement.
     *      </li>
     *      <li>
     *          activity_id - foreign key which references an activity_id from a tuple in Activities. Has the properties not null and on delete cascade.
     *      </li>
     *      <li>
     *          dp_date_string - a textual representation of the date/time that the data from the datapoint was recorded at. Text with the property not null.
     *      </li>
     *      <li>
     *           date - the date/time that the activity was completed on represented by a long integer used to order activities by their date. Date type with property not null.
     *      </li>
     *      <li>
     *          heart_rate - the recorded heart rate. Integer with property not null.
     *      </li>
     *      <li>
     *          latitude - the recorded latitude. Real number with property not null.
     *      </li>
     *      <li>
     *          longitude - the recorded longitude. Real number with property not null.
     *      </li>
     *      <li>
     *          altitude - the recorded altitude. Real number with property not null.
     *      </li>
     *      <li>
     *          time_delta - the change in time compared to the datapoint and the previous data point if applicable. Real number with property not null.
     *      </li>
     *      <li>
     *          dist_delta - the change in distance compared to the datapoint and the previous data point if applicable. Real number with property not null.
     *      </li>
     *  </ul>
     *  Targets
     *  <ul>
     *      <li>
     *          target_id - The unique id of the target that is stored in the database. Primary key with the property Autoincrement.
     *      </li>
     *      <li>
     *          user_id - foreign key which references a user_id from a tuple in Users. Has the properties not null and on delete cascade.
     *      </li>
     *      <li>
     *          name - the name of the target. Varchar of max length 100 with the property not null.
     *      </li>
     *      <li>
     *          date_achieved - the date the target was achieved on represented as a string. Text data type.
     *      </li>
     *      <li>
     *          type - the type of the target. Text with the property not null
     *      </li>
     *      <li>
     *          initial_value - The initial value of the set target. Real number with property not null.
     *      </li>
     *      <li>
     *          current_value - The current value of the set target. Real number with property not null.
     *      </li>
     *      <li>
     *          final_value - The value needed to be reached to meet the target. Real number with property not null.
     *      </li>
     *  </ul>
     * @throws SQLException if an sql related problem is encountered trying to set up and create tables in the database.
     */
    public static void createDatabase() throws SQLException {
        connectToDB();
        if (dbConn != null) {

            String sqlCreateUserTableStmt = "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "user_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "name text NOT NULL, \n"
                    + "age integer NOT NULL, \n"
                    + "height real NOT NULL, \n"
                    + "weight real NOT NULL, \n"
                    + "gender text NOT NULL \n"
                    + ");";

            String sqlCreateActivityTableStmt = "CREATE TABLE IF NOT EXISTS Activities (\n"
                    + "activity_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "user_id integer NOT NULL, \n"
                    + "name text NOT NULL, \n"
                    + "date_string text NOT NULL, \n"
                    + "date date NOT NULL, \n "
                    + "type text NOT NULL, \n"
                    + "total_distance real NOT NULL,"
                    + "total_time real NOT NULL,"
                    + "calories_burnt real NOT NULL,"
                    + "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE"
                    + ");";

            String sqlCreateDatapointTableStmt = "CREATE TABLE IF NOT EXISTS Datapoints (\n"
                    + "dp_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "activity_id integer NOT NULL, \n"
                    + "dp_date_string text NOT NULL, \n"
                    + "dp_date date NOT NULL, \n"
                    + "heart_rate integer NOT NULL, \n"
                    + "latitude real NOT NULL, \n"
                    + "longitude real NOT NULL, \n"
                    + "altitude real NOT NULL, \n"
                    + "time_delta real NOT NULL, \n"
                    + "dist_delta real NOT NULL, \n"
                    + "FOREIGN KEY(activity_id) REFERENCES activities(activity_id) ON DELETE CASCADE"
                    + ");";

            String sqlCreateTargetTableStmt = "CREATE TABLE IF NOT EXISTS Targets (\n"
                    + "target_id integer PRIMARY KEY AUTOINCREMENT, \n"
                    + "user_id integer NOT NULL, \n"
                    + "name text NOT NULL, \n"
                    + "date_achieved text, \n"
                    + "type text NOT NULL, \n"
                    + "initial_value real NOT NULL, \n"
                    + "current_value real NOT NULL, \n"
                    + "final_value real NOT NULL, \n"
                    + "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE\n"
                    + ");";

            //Prepare and execute each create table statement
            PreparedStatement pCreateTableStmt = dbConn.prepareStatement(sqlCreateUserTableStmt);
            pCreateTableStmt.executeUpdate();
            pCreateTableStmt = dbConn.prepareStatement(sqlCreateActivityTableStmt);
            pCreateTableStmt.executeUpdate();
            pCreateTableStmt = dbConn.prepareStatement(sqlCreateDatapointTableStmt);
            pCreateTableStmt.executeUpdate();
            pCreateTableStmt = dbConn.prepareStatement(sqlCreateTargetTableStmt);
            pCreateTableStmt.executeUpdate();
            pCreateTableStmt.close();

        }
        disconnectFromDB();
    }


    /**
     * Resets the database by deleting all the tuples from the database. If total reset is enabled then the tables
     * are also deleted from the database.
     * @param totalReset enables total reset option
     * @throws SQLException if an sql related error occurs deleting tables or tuples from the database.
     */
    public static void resetDatabase(boolean totalReset) throws SQLException {
        connectToDB();
        //If total reset drop all tables. Used for testing to account for database schema changes between versions
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
            //deleting all users will delete all records from the database.
            String sqlDeleteUsersStmt = "DELETE FROM Users";
            PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteUsersStmt);
            pDeleteStmt.executeUpdate();
            pDeleteStmt.close();

        }
        disconnectFromDB();
    }


}
