package seng202.group2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.model.DataPoint;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatapointDBOperations {


    /**
     * Queries the data for all data points that belong to the Activity represented by an inputted Activity id. The
     * result of the query is returned from the function in the form of an ObservableList. If the activity id is invalid
     * or the activity has no datapoints in the data an empty ObservableList is returned. The Datapoints in the
     * ObservableList are sorted in order by their date from oldest to most recent.
     * This function automatically connects to and disconnects from the data.
     * @param activity_id The id of the activity that's datapoints are being queried for as an integer.
     * @return an ObservableList of the Datapoints that are returned by the query.
     * @throws SQLException If an error occurs when handling the sql operations on the data.
     */
    public static ObservableList<DataPoint> getAllActivityDatapoints(int activity_id) throws SQLException {
        DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Datapoints WHERE activity_id = " + activity_id + " ORDER BY dp_date";
        ResultSet queryResult = DatabaseOperations.executeDBQuery(sqlQueryStmt);

        ObservableList<DataPoint> activityDatapoints = FXCollections.observableArrayList();

        while (queryResult.next()) {
            int datapointID = queryResult.getInt("dp_id");
            java.util.Date datapointDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

            try {
                datapointDate = dateFormatter.parse(queryResult.getString("dp_date_string"));
            } catch (ParseException e) {
                System.out.println("Unable to parse date");
                e.printStackTrace();
            }

            int dpHeartRate = queryResult.getInt("heart_rate");
            double dpLatitude = queryResult.getDouble("latitude");
            double dpLongitude = queryResult.getDouble("longitude");
            double dpAltitude = queryResult.getDouble("altitude");
            double dpTimeDelta = queryResult.getDouble("time_delta");
            double dpDistDelta = queryResult.getDouble("dist_delta");

            DataPoint newDP = new DataPoint(datapointDate, dpHeartRate, dpLatitude, dpLongitude, dpAltitude);
            newDP.setTimeDelta(dpTimeDelta);
            newDP.setDistanceDelta(dpDistDelta);
            newDP.setId(datapointID);

            activityDatapoints.add(newDP);
        }
        DatabaseOperations.disconnectFromDB();
        return activityDatapoints;

    }


    /**
     * Queries the data for a particular datapoint specified by the datapoint_id. If a Datapoint is found in the
     * data with a matching ID it is returned by the function. If the Query fails to find a Datapoint with the
     * specified ID or the ID is invalid a null value is returned by the function.
     * This function automatically connects to and disconnects from the data.
     * @param datapoint_id The ID of the data point being queried for as an integer
     * @return The Datapoint that has been quered for from the data if the datapoint_id exists, null otherwise.
     * @throws SQLException If an error occurs when handling the sql operations on the data.
     */
    public static DataPoint getDataPointFromRS(int datapoint_id) throws SQLException {
        DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Datapoints WHERE dp_id = " + datapoint_id;
        ResultSet queryResult = DatabaseOperations.executeDBQuery(sqlQueryStmt);

        DataPoint retrievedDataPoint = null;
        if (queryResult.next()) {
            int datapointID = queryResult.getInt("dp_id");
            java.util.Date datapointDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

            try {
                datapointDate = dateFormatter.parse(queryResult.getString("dp_date_string"));
            } catch (ParseException e) {
                System.out.println("Unable to parse date");
                e.printStackTrace();
            }

            int dpHeartRate = queryResult.getInt("heart_rate");
            double dpLatitude = queryResult.getDouble("latitude");
            double dpLongitude = queryResult.getDouble("longitude");
            double dpAltitude = queryResult.getDouble("altitude");
            double dpTimeDelta = queryResult.getDouble("time_delta");
            double distDelta = queryResult.getDouble("dist_delta");

            retrievedDataPoint = new DataPoint(datapointDate, dpHeartRate, dpLatitude, dpLongitude, dpAltitude);
            retrievedDataPoint.setTimeDelta(dpTimeDelta);
            retrievedDataPoint.setDistanceDelta(distDelta);
            retrievedDataPoint.setId(datapointID);
        }

        DatabaseOperations.disconnectFromDB();

        return retrievedDataPoint;

    }


/*    public static boolean insertNewDataPoint(DataPoint datapoint, int activityID) throws SQLException {

        //If the activity doesn't exist. Return false.
        if (ActivityDBOperations.getActivityFromDB(activityID) == null) {
            return false;*/

    /**
     *Inserts a new Datapoint into the data for a given Activity denoted by the activityID parameter. The Datapoint
     *does not need to have been assigned a datapoint id before being used in this function. If the activity_id is invalid
     *the function returns an error value of -1. Else the function returns with the code 1.
     * @param datapoint The Datapoint which will to inserted into the data.
     * @param activityID The id of the activity that the Datapoint belongs to as an Integer.
     * @return An int value based on the result of the insertion: 1 for success, -1 for error
     * @throws SQLException If an error occurs when handling the sql operations on the data.
     */
    public static int insertNewDataPoint(DataPoint datapoint, int activityID) throws SQLException {

        //If the activity doesn't exist. Return false.
        if (ActivityDBOperations.getActivityFromDB(activityID) == null) {
            return -1;
        }
        DatabaseOperations.connectToDB();
        String sqlInsertStmt = "INSERT INTO Datapoints(activity_id, dp_date_string, dp_date, heart_rate, latitude, "
                + "longitude, altitude, time_delta, dist_delta) \n"
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        Connection dbConn = DatabaseOperations.getDbConnection();

        PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStatement.setInt(1, activityID);
        pUpdateStatement.setString(2, datapoint.getDate().toString());
        pUpdateStatement.setDate(3, new java.sql.Date(datapoint.getDate().getTime()));
        pUpdateStatement.setInt(4, datapoint.getHeartRate());
        pUpdateStatement.setDouble(5, datapoint.getLatitude());
        pUpdateStatement.setDouble(6, datapoint.getLongitude());
        pUpdateStatement.setDouble(7, datapoint.getAltitude());
        pUpdateStatement.setDouble(8, datapoint.getTimeDelta());
        pUpdateStatement.setDouble(9, datapoint.getDistanceDelta());
        pUpdateStatement.executeUpdate();
        DatabaseOperations.disconnectFromDB();
        return 1;


    }

/*
    public static boolean updateExistingDataPoint(DataPoint updatedDP) throws SQLException{


        ResultSet results = pUpdateStatement.getGeneratedKeys();
        results.next();
        int datapoint_id = results.getInt(1);

        DatabaseOperations.disconnectFromDB();
        return datapoint_id;

    }
*/


    /**
     * Updates a datapoint that is already in the data. Returns a boolean value based on whether the update operation
     * has been created successfully. A value of false is returned if the Datapoint is not present in the data. That is
     * there is no Datapoint already in the data that has the same datapoint id as the Datapoint that is to be updated.
     * The inputted Activity must have an assigned id.
     * This function automatically connects to and disconnects from the data.
     * @param updatedDP The Datapoint which will have its recorded updated in the data providing that the Datapoint
     *                  already exists in the Database.
     * @return true if the datapoint is updated successfully, false if the Datapoint was not found in the data.
     * @throws SQLException If an error occurs when handling the sql operations on the data.
     */
    public static boolean updateExistingDataPoint(DataPoint updatedDP) throws SQLException {

        String sqlUpdateStmt = "UPDATE Datapoints SET dp_date_string = ?, dp_date = ?, heart_rate = ?, latitude = ?, longitude = ?, altitude = ? WHERE dp_id = ?";
        if (getDataPointFromRS(updatedDP.getId()) != null) {
            DatabaseOperations.connectToDB();
            Connection dbConn = DatabaseOperations.getDbConnection();

            PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStatement.setString(1, updatedDP.getDate().toString());
            pUpdateStatement.setDate(2, new java.sql.Date(updatedDP.getDate().getTime()));
            pUpdateStatement.setInt(3, updatedDP.getHeartRate());
            pUpdateStatement.setDouble(4, updatedDP.getLatitude());
            pUpdateStatement.setDouble(5, updatedDP.getLongitude());
            pUpdateStatement.setDouble(6, updatedDP.getAltitude());
            pUpdateStatement.setInt(7, updatedDP.getId());
            pUpdateStatement.executeUpdate();

            DatabaseOperations.disconnectFromDB();
            return true;


        } else {
            return false;
        }
    }


    /**
     * Removes an existing datapoint from the data. Returns true if the datapoint with the inputted id no longer exists
     * in the data, false otherwise.
     * This function automatically connects to and disconnects from the data.
     * @param datapointID The ID of the datapoint to be removed from the data.
     * @return true if a datapoint with the inputted id no longer exists in the data, false otherwise.
     * @throws SQLException If an error occurs when handling the sql operations on the data.
     */
    public static boolean deleteExistingDataPoint(int datapointID) throws SQLException {
        DatabaseOperations.connectToDB();
        String sqlDeleteStmt = "DELETE FROM Datapoints WHERE dp_id = ?";
        Connection dbConn = DatabaseOperations.getDbConnection();
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, datapointID);
        pDeleteStmt.executeUpdate();
        DatabaseOperations.disconnectFromDB();
        if (getDataPointFromRS(datapointID) == null) {
            return true;
        } else {
            return false;
        }

    }

}