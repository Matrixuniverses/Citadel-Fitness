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

public class ActivityDBOperations {

    /**
     * Checks the database for any activity that may contain a duplicate of the passed activity, it is assumed
     * that an activity of the same date, name and distances will be the same activity
     *
     * @param activityToCheck Activity object to compare to the database
     * @param user_id         UserID of the user that the activity belongs to
     * @return True if activity is a duplicate, false if not @future null if unable to determine
     * @throws SQLException If unable to read from database
     */
    public static boolean checkDuplicateActivity(Activity activityToCheck, int user_id) throws SQLException {
        DatabaseOperations.connectToDB();

        // TODO - Check if the distances for the comparable activities are within a specific range

        String dateString = activityToCheck.getDate().toString();
        String sqlQueryStatement = "SELECT date_string, name FROM Activities WHERE user_id = "
                + user_id + " AND date_string = '" + dateString + "'";
        ResultSet queryResult = DatabaseOperations.executeDBQuery(sqlQueryStatement);

        if (queryResult.next()) {
            if (activityToCheck.getActivityName().equals(queryResult.getString("name"))) {
                DatabaseOperations.disconnectFromDB();
                return true;
            }
        }

        DatabaseOperations.disconnectFromDB();

        return false;
    }

    /**
     * Inserts a new activity into the database, if the user exists in the database
     *
     * @param activity Activity to insert
     * @param user_id  UserID of the user that the activity will be written to
     * @return ActivityID of the inserted activity, -1 if the user is not in the database
     * @throws SQLException If unable to read/ write from/ to database
     */
    public static int insertNewActivity(Activity activity, int user_id) throws SQLException {
        // Check the user exists in the database
        if (UserDBOperations.getUserFromRS(user_id) == null) {
            return -1;
        }

        DatabaseOperations.connectToDB();

        String sqlInsertStmt = "INSERT INTO Activities(user_id,name,date_string,date,type,total_distance,total_time) \n" +
                "VALUES(?,?,?,?,?,?,?)";

        Connection dbConn = DatabaseOperations.getDbConnection();

        PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStatement.setInt(1, user_id);
        pUpdateStatement.setString(2, activity.getActivityName());
        pUpdateStatement.setString(3, activity.getDate().toString());
        pUpdateStatement.setDate(4, new java.sql.Date(activity.getDate().getTime()));
        pUpdateStatement.setString(5, activity.getActivityType());
        pUpdateStatement.setDouble(6, activity.getTotalDistance());
        pUpdateStatement.setDouble(7, activity.getTotalTime());
        pUpdateStatement.executeUpdate();

        ResultSet results = pUpdateStatement.getGeneratedKeys();
        results.next();

        int activity_id = results.getInt(1);

        DatabaseOperations.disconnectFromDB();
        DatabaseOperations.disconnectFromDB();
        return activity_id;

    }

    /**
     * Creates a new JavaFX ObservableList containing all of the users activities in the database
     *
     * @param user_id UserID of the activities to read from database
     * @return Observable List of the Users activities
     * @throws SQLException If unable to read/ write from/ to database
     */
    public static ObservableList<Activity> getAllUsersActivities(int user_id) throws SQLException {
        //TODO - Change this to avoid duplicate code

        DatabaseOperations.connectToDB();
        String sqlQueryStatement = "SELECT * FROM Activities WHERE user_id = " + user_id + " ORDER BY date;";
        ResultSet queryResult = DatabaseOperations.executeDBQuery(sqlQueryStatement);

        ObservableList<Activity> userActivities = FXCollections.observableArrayList();

        while (queryResult.next()) {
            int activityID = queryResult.getInt("activity_id");
            String activityName = queryResult.getString("name");
            Date activityDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

            try {
                activityDate = dateFormatter.parse(queryResult.getString("date_string"));
            } catch (ParseException e) {
                System.err.println("Unable to parse date");
                e.printStackTrace();
            }

            String activityType = queryResult.getString("type");
            double totalDistance = queryResult.getDouble("total_distance");
            double totalTime = queryResult.getDouble("total_time");

            Activity newActivity = new Activity(activityName, activityDate, activityType, totalTime, totalDistance);
            newActivity.setId(activityID);
            userActivities.add(newActivity);
        }

        DatabaseOperations.disconnectFromDB();

        return userActivities;
    }

    /**
     * Gets a specific activity from the database that matches the activityID
     * @param activity_id ActivityID of the activity to get from the database
     * @return Activity retrieved from database
     * @throws SQLException If unable to read from database
     */
    public static Activity getActivityFromDB(int activity_id) throws SQLException {
        DatabaseOperations.connectToDB();
        String sqlQuery = "SELECT * FROM Activities WHERE activity_id = " + activity_id + ";";
        ResultSet queryResult = DatabaseOperations.executeDBQuery(sqlQuery);
        Activity retrievedActivity = null;

        if (queryResult.next()) {
            int activityID = queryResult.getInt("activity_id");
            String activityName = queryResult.getString("name");
            java.util.Date activityDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

            try {
                activityDate = dateFormatter.parse(queryResult.getString("date_string"));
            } catch (ParseException e) {
                System.out.println("Unable to parse date");
                e.printStackTrace();
            }

            String activityType = queryResult.getString("type");
            double totalDistance = queryResult.getDouble("total_distance");
            double totalTime = queryResult.getDouble("total_time");
            retrievedActivity = new Activity(activityName, activityDate, activityType, totalDistance, totalTime);
            retrievedActivity.setId(activityID);

        }

        DatabaseOperations.disconnectFromDB();

        return retrievedActivity;
    }

    /**
     * Updates an existing activity in database to match passed activity
     * @param activity Activity to update database with
     * @return True if activity to update exists in the database and has been successfully updated, False otherwise
     * @throws SQLException If unable to read/ write from/ to database
     */
    public static boolean updateExistingActivity(Activity activity) throws SQLException {
        String sqlUpdateStmt = "UPDATE Activities SET name = ?, date_string = ?, date = ?, type = ?, total_distance = ?, total_time = ? WHERE activity_id = ?";

        if (getActivityFromDB(activity.getId()) != null) {
            DatabaseOperations.connectToDB();
            Connection dbConn = DatabaseOperations.getDbConnection();

            PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStatement.setString(1, activity.getActivityName());
            pUpdateStatement.setString(2, activity.getDate().toString());
            pUpdateStatement.setDate(3, new java.sql.Date(activity.getDate().getTime()));
            pUpdateStatement.setString(4, activity.getActivityType());
            pUpdateStatement.setDouble(5, activity.getTotalDistance());
            pUpdateStatement.setDouble(6, activity.getTotalTime());
            pUpdateStatement.setInt(7, activity.getId());
            pUpdateStatement.executeUpdate();

            DatabaseOperations.disconnectFromDB();

            return true;

        } else {

            return false;
        }
    }

    /**
     * Deletes activity in the database with the given activity ID
     * @param activityID ActivityID of activity to delete
     * @return True if activity to delete exists in database and has been deleted, False otherwise
     * @throws SQLException If unable to read/ write from/ to database
     */
    public static boolean deleteExistingActivity(int activityID) throws SQLException {
        DatabaseOperations.connectToDB();

        String sqlDeleteStmt = "DELETE FROM Activities WHERE activity_id = ?";

        Connection dbConn = DatabaseOperations.getDbConnection();
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, activityID);
        pDeleteStmt.executeUpdate();
        DatabaseOperations.disconnectFromDB();

        if (getActivityFromDB(activityID) == null) {
            return true;
        } else {
            return false;
        }

    }
}