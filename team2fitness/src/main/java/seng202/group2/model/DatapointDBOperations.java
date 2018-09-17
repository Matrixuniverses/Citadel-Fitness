package seng202.group2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.data_functions.databaseWriter;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatapointDBOperations {


    public static ObservableList<DataPoint> getAllActivityDatapoints(int activity_id) throws SQLException {

        databaseWriter.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Datapoints WHERE activity_id = " + activity_id + " ORDER BY dp_date";
        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQueryStmt);

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
            DataPoint newDP = new DataPoint(datapointDate, dpHeartRate, dpLatitude, dpLongitude, dpAltitude);
            newDP.setId(datapointID);
            activityDatapoints.add(newDP);
        }
        databaseWriter.disconnectFromDB();
        return activityDatapoints;

    }



    public static DataPoint getDataPointFromRS(int datapoint_id) throws SQLException {
        databaseWriter.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Datapoints WHERE dp_id = " + datapoint_id;
        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQueryStmt);

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
            retrievedDataPoint = new DataPoint(datapointDate, dpHeartRate, dpLatitude, dpLongitude, dpAltitude);
            retrievedDataPoint.setId(datapointID);
        }
        databaseWriter.disconnectFromDB();
        return retrievedDataPoint;

    }


/*    public static boolean insertNewDataPoint(DataPoint datapoint, int activityID) throws SQLException {

        //If the activity doesn't exist. Return false.
        if (ActivityDBOperations.getActivityFromRS(activityID) == null) {
            return false;*/

    public static int insertNewDataPoint(DataPoint datapoint, int activityID) throws SQLException {

        //If the activity doesn't exist. Return false.
        if (ActivityDBOperations.getActivityFromRS(activityID) == null) {
            return -1;
        }
        databaseWriter.connectToDB();
        String sqlInsertStmt = "INSERT INTO Datapoints(activity_id, dp_date_string, dp_date, heart_rate, latitude, longitude, altitude) \n"
                + "VALUES(?,?,?,?,?,?,?)";

        Connection dbConn = databaseWriter.getDbConnection();

        PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStatement.setInt(1, activityID);
        pUpdateStatement.setString(2, datapoint.getDate().toString());
        pUpdateStatement.setDate(3, new java.sql.Date(datapoint.getDate().getTime()));
        pUpdateStatement.setInt(4, datapoint.getHeartRate());
        pUpdateStatement.setDouble(5, datapoint.getLatitude());
        pUpdateStatement.setDouble(6, datapoint.getLongitude());
        pUpdateStatement.setDouble(7, datapoint.getAltitude());
        pUpdateStatement.executeUpdate();
        databaseWriter.disconnectFromDB();
        return 1;


    }

/*
    public static boolean updateExistingDataPoint(DataPoint updatedDP) throws SQLException{


        ResultSet results = pUpdateStatement.getGeneratedKeys();
        results.next();
        int datapoint_id = results.getInt(1);

        databaseWriter.disconnectFromDB();
        return datapoint_id;

    }
*/

    public static boolean updateExistingDataPoint(DataPoint updatedDP) throws SQLException {

        String sqlUpdateStmt = "UPDATE Datapoints SET dp_date_string = ?, dp_date = ?, heart_rate = ?, latitude = ?, longitude = ?, altitude = ? WHERE dp_id = ?";
        if (getDataPointFromRS(updatedDP.getId()) != null) {
            databaseWriter.connectToDB();
            Connection dbConn = databaseWriter.getDbConnection();

            PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStatement.setString(1, updatedDP.getDate().toString());
            pUpdateStatement.setDate(2, new java.sql.Date(updatedDP.getDate().getTime()));
            pUpdateStatement.setInt(3, updatedDP.getHeartRate());
            pUpdateStatement.setDouble(4, updatedDP.getLatitude());
            pUpdateStatement.setDouble(5, updatedDP.getLongitude());
            pUpdateStatement.setDouble(6, updatedDP.getAltitude());
            pUpdateStatement.setInt(7, updatedDP.getId());
            pUpdateStatement.executeUpdate();

            databaseWriter.disconnectFromDB();
            return true;


        } else {
            return false;
        }
    }


    public static boolean deleteExistingDataPoint(int datapointID) throws SQLException {
        databaseWriter.connectToDB();
        String sqlDeleteStmt = "DELETE FROM Datapoints WHERE dp_id = ?";
        Connection dbConn = databaseWriter.getDbConnection();
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, datapointID);
        pDeleteStmt.executeUpdate();
        databaseWriter.disconnectFromDB();
        if (getDataPointFromRS(datapointID) == null) {
            return true;
        } else {
            return false;
        }

    }

}
