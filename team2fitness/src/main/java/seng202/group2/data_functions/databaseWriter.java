package seng202.group2.data_functions;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.*;

public class databaseWriter {


    private static String dbURL = "jdbc:sqlite:" + System.getProperty("user.dir") + "\\CitadelFitnessLocalDatabase.db";

    private static Connection dbConn = null;


    public static void connectToDB() throws SQLException {
        dbConn = DriverManager.getConnection(dbURL);

    }

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
     * Creates a new database for the application if it does not already exist. The database is stored in the project's
     * directory and consists of four tables: users, activities, datapoints and targets.
     * @return true if the method runs without encountering errors
     */
    public static boolean createDatabase() throws SQLException{
        boolean success = false;
        if (dbConn != null) {
            String sqlCreateUserTable = "CREATE TABLE IF NOT EXISTS users (\n"
                    + "user_id integer PRIMARY KEY, \n"
                    + "name varchar(20) NOT NULL, \n"
                    + "age integer NOT NULL, \n"
                    + "height real NOT NULL, \n"
                    + "weight real NOT NULL \n"
                    + ");";

            String sqlCreateActivityTable = "CREATE TABLE IF NOT EXISTS activities (\n"
                    + "activity_id integer PRIMARY KEY, \n"
                    + "user_id integer NOT NULL, \n"
                    + "name varchar(100) NOT NULL, \n"
                    + "type varchar(20) NOT NULL, \n"
                    + "FOREIGN KEY(user_id) REFERENCES users(user_id)"
                    + ");";

            String sqlCreateDatapointTable = "CREATE TABLE IF NOT EXISTS datapoints (\n"
                    + "dp_id integer PRIMARY KEY, \n"
                    + "activity_id integer NOT NULL, \n"
                    + "dp_date datetime NOT NULL, \n"
                    + "heart_rate integer NOT NULL, \n"
                    + "latitude real NOT NULL, \n"
                    + "longitude real NOT NULL, \n"
                    + "altitude real NOT NULL, \n"
                    + "FOREIGN KEY(activity_id) REFERENCES activities(activity_id)"
                    + ");";

            String sqlCreateTargetTable = "CREATE TABLE IF NOT EXISTS targets (\n"
                    + "target_id integer PRIMARY KEY, \n"
                    + "user_id integer NOT NULL, \n"
                    + "name varchar(100) NOT NULL, \n"
                    + "date_achieved date, \n"
                    + "type varchar(20) NOT NULL, \n"
                    + "initial_value real NOT NULL, \n"
                    + "current_value real NOT NULL, \n"
                    + "final_value real NOT NULL, \n"
                    + "FOREIGN KEY(user_id) REFERENCES users(user_id) \n"
                    + ");";



            executeSQLStatement(sqlCreateUserTable, dbConn);
            executeSQLStatement(sqlCreateActivityTable, dbConn);
            executeSQLStatement(sqlCreateDatapointTable, dbConn);
            executeSQLStatement(sqlCreateTargetTable, dbConn);


            success = true;



        }






        return success;

    }

    public static ResultSet executeDBQuery(String queryStmt) {
        ResultSet rSet = null;
        try {
            Statement dbStmt = dbConn.createStatement();
            rSet = dbStmt.executeQuery(queryStmt);

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return rSet;
    }



    //for testing

    /*
    public static void main(String[] args) {
        try {
            connectToDB();
            boolean result = createDatabase();
            if (result) {
                System.out.println("Database created successfully!");
                disconnectFromDB();
            } else {
                System.out.println("Failed to create Database!");
                disconnectFromDB();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    */



}
