package seng202.group2.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.model.Activity;

/**
 * The ActivityDBOperations is a static class handles all sql queries and methods relating specifically to the Activities relation
 * in the database.
 */
public class ActivityDBOperations {

    /**
     * Checks the database for any activity that may contain a duplicate of the passed activity, it is assumed
     * that an activity of the same date, name and distances will be the same activity
     *
     * @param activityToCheck Activity object to compare to the database
     * @param userID         UserID of the user that the activity belongs to
     * @return True if activity is a duplicate, false if not @future null if unable to determine
     * @throws SQLException If unable to read from database
     */
    public static boolean checkDuplicateActivity(Activity activityToCheck, int userID) throws SQLException {

        Connection dbConn = DatabaseOperations.connectToDB();
        //Search for an activity that belongs to the same user that occurred at the same time as the activity to
        //be inputted into the database
        String dateString = activityToCheck.getDate().toString();
        String sqlQueryStmt = "SELECT date_string, name FROM Activities WHERE user_id = ? AND date_string =  ? ";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, userID);
        pQueryStmt.setString(2, dateString);
        ResultSet queryResult = pQueryStmt.executeQuery();

        //If the activity exists check if it has the same name
        if (queryResult.next()) {
            if (activityToCheck.getActivityName().equals(queryResult.getString("name"))) {
                //If true then we can confirm that activity can be regarded as a duplicate
                DatabaseOperations.disconnectFromDB();
                return true;
            }
        }

        //free up resources
        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();

        return false;
    }

    //helper function to get an observable list of activities from a ResultSet object
    private static ObservableList<Activity> getResultSetActivities(ResultSet queryResult) throws SQLException{

        ObservableList<Activity> collectedActivities = FXCollections.observableArrayList();

        //loop through each record
        while(queryResult.next()) {

            int activityID = queryResult.getInt(1);
            String activityName = queryResult.getString(3);

            //Parsing date string to convert it into a java.util.Date object.
            Date activityDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
            try {
                activityDate = dateFormatter.parse(queryResult.getString(4));
            } catch (ParseException e) {
                System.err.println("Unable to parse date");
                e.printStackTrace();
            }

            String activityType = queryResult.getString(6);
            double totalDistance = queryResult.getDouble(7);
            double totalTime = queryResult.getDouble(8);

            Activity newActivity = new Activity(activityName, activityDate, activityType, totalTime, totalDistance);
            newActivity.setAverageHR(DatapointDBOperations.getAverageHR(activityID));
            newActivity.setId(activityID);
            newActivity.setCaloriesBurned(queryResult.getDouble(9));

            //check to see whether the activity has datapoints stored in the database which determines whether the
            //activity was manually entered or not
            if (DatapointDBOperations.getAllActivityDatapoints(activityID).size() > 0) {
                newActivity.setManualEntry(false);
            }
            collectedActivities.add(newActivity);
        }

        return collectedActivities;
    }

    /**
     * Queries the database for all activities inclusively between two inputted dates. The activities are are returned as
     * an ObservableList where the Activities are ordered by the date that they were completed on.
     * @param minDate the minimum bound for the date query as a sql.Date.
     * @param maxDate the maximum bound for the date query as a sql.Date.
     * @param UserID the id of the user that the activities are for.
     * @return An ObservableList containing all the activities returned by the query ordered by the date the activity was
     * completed in ascending order.
     * @throws SQLException if an sql related error occurs while querying the database.
     */
    public static ObservableList<Activity> getActivitiesBetweenDates(java.sql.Date minDate, java.sql.Date maxDate, int UserID) throws SQLException {

        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Activities WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date";

        //Create and execute a prepared statement
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, UserID);
        pQueryStmt.setDate(2, minDate);
        pQueryStmt.setDate(3, maxDate);
        ResultSet queryResult = pQueryStmt.executeQuery();

        ObservableList<Activity> collectedActivities = getResultSetActivities(queryResult);

        //free resources
        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();
        return collectedActivities;


    }

    /**
     * Creates a new JavaFX ObservableList containing all of the users activities in the database
     *
     * @param userID UserID of the activities to read from database
     * @return Observable List of the Users activities
     * @throws SQLException If unable to read/ write from/ to database
     */
    public static ObservableList<Activity> getAllUsersActivities(int userID) throws SQLException {

        //queries the database all activities for a user and orders the result set by the date the activity was
        //completed on
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Activities WHERE user_id = ? ORDER BY date;";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, userID);
        ResultSet queryResult = pQueryStmt.executeQuery();

        ObservableList<Activity> userActivities = getResultSetActivities(queryResult);

        //free resources
        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();

        return userActivities;
    }

    /**
     * Gets a specific activity from the database that matches the activityID
     * @param activityID ActivityID of the activity to get from the database
     * @return Activity retrieved from database
     * @throws SQLException If unable to read from database
     */
    public static Activity getActivityFromDB(int activityID) throws SQLException {

        //executes a query that retrieves an activity from the database with a specified id.
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Activities WHERE activity_id = ?;";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, activityID);
        ResultSet queryResult = pQueryStmt.executeQuery();


        Activity retrievedActivity = null;
        //if the returned list has a length greater than 0 it must contain the retrieved activity object.
        ObservableList<Activity> retrievedActivities = getResultSetActivities(queryResult);
        if (retrievedActivities.size() > 0) {
            retrievedActivity = retrievedActivities.get(0);
        }

        //freeing resources
        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();

        return retrievedActivity;
    }

    /**
     * Inserts a new activity into the database, if the user exists in the database
     *
     * @param activity Activity to insert
     * @param userID UserID of the user that the activity will be written to
     * @return ActivityID of the inserted activity, -1 if the user is not in the database
     * @throws SQLException If unable to read/ write from/ to database
     */
    public static int insertNewActivity(Activity activity, int userID) throws SQLException {
        // Check the user exists in the database
        if (UserDBOperations.getUserFromDB(userID) == null) {
            return -1;
        }

        Connection dbConn = DatabaseOperations.connectToDB();

        String sqlInsertStmt = "INSERT INTO Activities(user_id,name,date_string,date,type,total_distance,total_time,calories_burnt) \n" +
                "VALUES(?,?,?,?,?,?,?,?)";


        //Created and execute prepared statement
        PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStmt.setInt(1, userID);
        pUpdateStmt.setString(2, activity.getActivityName());
        pUpdateStmt.setString(3, activity.getDate().toString());
        pUpdateStmt.setDate(4, new java.sql.Date(activity.getDate().getTime()));
        pUpdateStmt.setString(5, activity.getActivityType());
        pUpdateStmt.setDouble(6, activity.getTotalDistance());
        pUpdateStmt.setDouble(7, activity.getTotalTime());
        pUpdateStmt.setDouble(8, activity.getCaloriesBurned());
        pUpdateStmt.executeUpdate();

        ResultSet results = pUpdateStmt.getGeneratedKeys();
        results.next();

        int activity_id = results.getInt(1);

        //freeing resources
        pUpdateStmt.close();
        DatabaseOperations.disconnectFromDB();
        return activity_id;

    }



    /**
     * Updates an existing activity in database to match passed activity
     * @param activity Activity to update database with
     * @return True if activity to update exists in the database and has been successfully updated, False otherwise
     * @throws SQLException If unable to read/ write from/ to database
     */
    public static boolean updateExistingActivity(Activity activity) throws SQLException {
        String sqlUpdateStmt = "UPDATE Activities SET name = ?, date_string = ?, date = ?, type = ?, total_distance = ?, total_time = ?, calories_burnt = ? WHERE activity_id = ?";

        //Check for whether the activity currently exists in the database
        if (getActivityFromDB(activity.getId()) != null) {

            Connection dbConn = DatabaseOperations.connectToDB();

            //create and execute prepared statement
            PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStmt.setString(1, activity.getActivityName());
            pUpdateStmt.setString(2, activity.getDate().toString());
            pUpdateStmt.setDate(3, new java.sql.Date(activity.getDate().getTime()));
            pUpdateStmt.setString(4, activity.getActivityType());
            pUpdateStmt.setDouble(5, activity.getTotalDistance());
            pUpdateStmt.setDouble(6, activity.getTotalTime());
            pUpdateStmt.setDouble(7, activity.getCaloriesBurned());
            pUpdateStmt.setInt(8, activity.getId());
            pUpdateStmt.executeUpdate();

            //free resources
            pUpdateStmt.close();
            DatabaseOperations.disconnectFromDB();

            return true;

        } else {

            return false;
        }
    }

    /**
     * Deletes activity in the database with the given activity ID.
     * This method automatically connects to and disconnects from the database.
     * @param activityID ActivityID of activity to delete
     * @return True if activity to delete exists in database and has been deleted, False otherwise
     * @throws SQLException If unable to read/ write from/ to database
     */
    public static boolean deleteExistingActivity(int activityID) throws SQLException {
        Connection dbConn = DatabaseOperations.connectToDB();

        String sqlDeleteStmt = "DELETE FROM Activities WHERE activity_id = ?";


        //creates and executes a prepared statement
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, activityID);
        pDeleteStmt.executeUpdate();

        DatabaseOperations.disconnectFromDB();

        //check for whether the activity still exists in the database
        return (getActivityFromDB(activityID) == null);
    }
}
