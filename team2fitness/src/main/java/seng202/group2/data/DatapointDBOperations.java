package seng202.group2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.model.DataPoint;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * The DatapointDBOperations is a static class handles all sql queries and methods relating specifically to the Datapoints relation
 * in the database.
 */
public class DatapointDBOperations {

    //Helper function to retrieve an Observable list of Datapoints from a resultSet
    private static ObservableList<DataPoint> getDatapointsFromResultSet(ResultSet rs) throws SQLException{

        ObservableList<DataPoint> activityDatapoints = FXCollections.observableArrayList();

        // Iterate through each of the results and create a datapoint for them
        while (rs.next()) {
            int datapointID = rs.getInt(1);
            java.util.Date datapointDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

            // Checked exception handling
            try {
                datapointDate = dateFormatter.parse(rs.getString(3));
            } catch (ParseException e) {
                System.err.println("Unable to parse date");
                e.printStackTrace();
                continue;
            }

            // No type checking required as this is done before insert into database
            int dpHeartRate = rs.getInt(5);
            double dpLatitude = rs.getDouble(6);
            double dpLongitude = rs.getDouble(7);
            double dpAltitude = rs.getDouble(8);
            double dpTimeDelta = rs.getDouble(9);
            double dpDistDelta = rs.getDouble(10);

            // Create the new Datapoint for this result in the result set
            DataPoint newDP = new DataPoint(datapointDate, dpHeartRate, dpLatitude, dpLongitude, dpAltitude);
            newDP.setTimeDelta(dpTimeDelta);
            newDP.setDistanceDelta(dpDistDelta);
            newDP.setId(datapointID);

            activityDatapoints.add(newDP);
        }
        return  activityDatapoints;
    }


    /**
     * Queries the data for all data points that belong to the Activity represented by an inputted Activity id. The
     * result of the query is returned from the function in the form of an ObservableList. If the activity id is invalid
     * or the activity has no datapoints in the data an empty ObservableList is returned. The Datapoints in the
     * ObservableList are sorted in order by their date from oldest to most recent.
     * This function automatically connects to and disconnects from the data.
     * @param activityID The id of the activity that's datapoints are being queried for as an integer.
     * @return an ObservableList of the Datapoints that are returned by the query.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static ObservableList<DataPoint> getAllActivityDatapoints(int activityID) throws SQLException {
        // Executes the query that gets all datapoints with activityID as the foreign key
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Datapoints WHERE activity_id = ? ORDER BY dp_date";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, activityID);

        ResultSet queryResult = pQueryStmt.executeQuery();

        // Datapoints are all read into an observable list
        ObservableList<DataPoint> activityDatapoints = getDatapointsFromResultSet(queryResult);

        // Terminate database connection
        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();
        return activityDatapoints;

    }


    /**
     * Queries the data for a particular datapoint specified by the datapoint_id. If a Datapoint is found in the
     * data with a matching ID it is returned by the function. If the Query fails to find a Datapoint with the
     * specified ID or the ID is invalid a null value is returned by the function.
     * This function automatically connects to and disconnects from the data.
     * @param datapointID The ID of the data point being queried for as an integer
     * @return The Datapoint that has been quered for from the data if the datapoint_id exists, null otherwise.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static DataPoint getDataPointFromDB(int datapointID) throws SQLException {
        // Executes prepared statement
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Datapoints WHERE dp_id = ?";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, datapointID);

        ResultSet queryResult = pQueryStmt.executeQuery();

        // The query should only get one Datapoint but extra checking has been implemented in case of database
        // access issues
        DataPoint retrievedDataPoint = null;
        ObservableList<DataPoint> retrievedDatapoints = getDatapointsFromResultSet(queryResult);
        if (retrievedDatapoints.size() > 0) {
            retrievedDataPoint = retrievedDatapoints.get(0);
        }

        // Terminate database connection
        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();

        return retrievedDataPoint;

    }

    /**
     * Queries the database and returns the average heart rate for all datapoints that are linked to a particular activity
     * identified by an inputted activity. If the activity has no datapoints in the database a value of -1 is returned.
     * @param activityID The id of the activity that the average heart rate will be calculated for.
     * @return the average heart rate of the activity as an n Interger. -1 if the activity has no datapoints in the database.
     * @throws SQLException If an sql related error occurs while connecting to or querying the database.
     */
    public static int getAverageHR(int activityID) throws SQLException {
        // Using SQL to get the average HR was faster than using Java methods
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT avg(heart_rate) FROM Datapoints WHERE activity_id = ?";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, activityID);

        ResultSet queryResult = pQueryStmt.executeQuery();

        // Average HR of -1 is essential a fail return code, caught in the controller classes
        int averageHR = -1;
        if (queryResult.next()) {
            averageHR = queryResult.getInt(1);
        }

        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();

        return averageHR;
    }




    /**
     *Inserts a new Datapoint into the data for a given Activity denoted by the activityID parameter. The Datapoint
     *does not need to have been assigned a datapoint id before being used in this function. If the activity_id is invalid
     *the function returns an error value of -1. Else the function returns with the code 1.
     * @param datapoint The Datapoint which will to inserted into the data.
     * @param activityID The id of the activity that the Datapoint belongs to as an Integer.
     * @return An int value based on the result of the insertion: 1 for success, -1 for error
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static int insertNewDataPoint(DataPoint datapoint, int activityID) throws SQLException {

        //If the activity doesn't exist. Return false.
        if (ActivityDBOperations.getActivityFromDB(activityID) == null) {
            return -1;
        }
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlInsertStmt = "INSERT INTO Datapoints(activity_id, dp_date_string, dp_date, heart_rate, latitude, "
                + "longitude, altitude, time_delta, dist_delta) \n"
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        // Execute prepared statement
        PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStmt.setInt(1, activityID);
        pUpdateStmt.setString(2, datapoint.getDate().toString());
        pUpdateStmt.setDate(3, new java.sql.Date(datapoint.getDate().getTime()));
        pUpdateStmt.setInt(4, datapoint.getHeartRate());
        pUpdateStmt.setDouble(5, datapoint.getLatitude());
        pUpdateStmt.setDouble(6, datapoint.getLongitude());
        pUpdateStmt.setDouble(7, datapoint.getAltitude());
        pUpdateStmt.setDouble(8, datapoint.getTimeDelta());
        pUpdateStmt.setDouble(9, datapoint.getDistanceDelta());
        pUpdateStmt.executeUpdate();

        // IDs for model types are automatically generated by SQLite
        ResultSet result = pUpdateStmt.getGeneratedKeys();
        result.next();
        int datapointID = result.getInt(1);

        // Terminate database connection
        pUpdateStmt.close();
        DatabaseOperations.disconnectFromDB();
        return datapointID;


    }


    /**
     * Updates a datapoint that is already in the data. Returns a boolean value based on whether the update operation
     * has been created successfully. A value of false is returned if the Datapoint is not present in the data. That is
     * there is no Datapoint already in the data that has the same datapoint id as the Datapoint that is to be updated.
     * The inputted Activity must have an assigned id.
     * This function automatically connects to and disconnects from the data.
     * @param updatedDP The Datapoint which will have its recorded updated in the data providing that the Datapoint
     *                  already exists in the Database.
     * @return true if the datapoint is updated successfully, false if the Datapoint was not found in the data.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static boolean updateExistingDataPoint(DataPoint updatedDP) throws SQLException {
        // Updates all values for the given Datapoint ID, easier than checking what one was changed
        String sqlUpdateStmt = "UPDATE Datapoints SET dp_date_string = ?, dp_date = ?, heart_rate = ?, latitude = ?, longitude = ?, altitude = ? WHERE dp_id = ?";
        if (getDataPointFromDB(updatedDP.getId()) != null) {

            Connection dbConn = DatabaseOperations.connectToDB();

            // Execute prepared statement
            PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStmt.setString(1, updatedDP.getDate().toString());
            pUpdateStmt.setDate(2, new java.sql.Date(updatedDP.getDate().getTime()));
            pUpdateStmt.setInt(3, updatedDP.getHeartRate());
            pUpdateStmt.setDouble(4, updatedDP.getLatitude());
            pUpdateStmt.setDouble(5, updatedDP.getLongitude());
            pUpdateStmt.setDouble(6, updatedDP.getAltitude());
            pUpdateStmt.setInt(7, updatedDP.getId());
            pUpdateStmt.executeUpdate();

            pUpdateStmt.close();
            DatabaseOperations.disconnectFromDB();

            return true;
            // Basic error checking, boolean returns were used in development but serve as extra error checking
        } else {
            return false;
        }
    }

    /**
     * Inserts all passed datapoints into database using a SQL manual transaction, this significantly improved DB performance
     * @param points Datapoints to write to database
     * @param activityID ActivityID to attach the datapoints to
     * @throws SQLException If unable to write to database
     */
    public static ArrayList<Integer> insertDataPointList(ObservableList<DataPoint> points, int activityID) throws SQLException{
        // Execute prepared statement and begin the manual transaction
        Connection dbConn = DatabaseOperations.connectToDB();
        Statement stmt = dbConn.createStatement();
        stmt.execute("BEGIN TRANSACTION;");
        PreparedStatement prep = dbConn.prepareStatement("INSERT INTO Datapoints(activity_id, dp_date_string, dp_date, heart_rate, latitude, "
                + "longitude, altitude, time_delta, dist_delta) VALUES(?,?,?,?,?,?,?,?,?)");
        ArrayList<Integer>  primaryKeys = new ArrayList<Integer>();

        // Adding all statement executions to the current transaction
        for (DataPoint datapoint : points) {
            prep.setInt(1, activityID);
            prep.setString(2, datapoint.getDate().toString());
            prep.setDate(3, new java.sql.Date(datapoint.getDate().getTime()));
            prep.setInt(4, datapoint.getHeartRate());
            prep.setDouble(5, datapoint.getLatitude());
            prep.setDouble(6, datapoint.getLongitude());
            prep.setDouble(7, datapoint.getAltitude());
            prep.setDouble(8, datapoint.getTimeDelta());
            prep.setDouble(9, datapoint.getDistanceDelta());
            prep.executeUpdate();

            // Getting the automatically generated IDs
            ResultSet rs = prep.getGeneratedKeys();
            primaryKeys.add(rs.getInt(1));
        }

        // Terminate database connection and commit the transaction to database
        prep.close();
        stmt.execute("END TRANSACTION;");
        stmt.close();
        DatabaseOperations.disconnectFromDB();
        return primaryKeys;
    }


    /**
     * Removes an existing datapoint from the data. Returns true if the datapoint with the inputted id no longer exists
     * in the data, false otherwise.
     * This function automatically connects to and disconnects from the data.
     * @param datapointID The ID of the datapoint to be removed from the data.
     * @return true if a datapoint with the inputted id no longer exists in the data, false otherwise.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static boolean deleteExistingDataPoint(int datapointID) throws SQLException {
        // Execute prepared statement
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlDeleteStmt = "DELETE FROM Datapoints WHERE dp_id = ?";

        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, datapointID);
        pDeleteStmt.executeUpdate();

        pDeleteStmt.close();
        DatabaseOperations.disconnectFromDB();

        // Boolean return was used for development, serves as extra error checking
        return (getDataPointFromDB(datapointID) == null);
    }

}
