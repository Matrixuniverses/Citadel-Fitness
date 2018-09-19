package seng202.group2.model;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.data_functions.DatabaseWriter;

public class ActivityDBOperations {


    /**
     * Checks whether an activity for a particular user already exists in the database at a given time in the form
     * of a java.util.Date. This function works under the assumption that a user cannot have two activities occuring
     * at exactly the same time. If an activity is found that for the user (using their user id) that has the date
     * inputted by the function the Activity that is already in the database is returned by the function.
     * This function automatically connects to and disconnects from the database.
     * @param user_id - The id of the user that the activity is for as an integer
     * @param activityDate - The date/time that will be checked as a java.util.Date
     * @return The activity in the database that has the two input parameters specified. If no such activity exists
     * a null value is returned.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static Activity getClashingActivity(int user_id, java.util.Date activityDate) throws SQLException {

        DatabaseWriter.connectToDB();
        String dateString = activityDate.toString();
        String sqlQueryStatement = "SELECT * FROM Activities WHERE user_id = " + user_id + " AND date_string = '" + dateString + "'";
        ResultSet queryResult = DatabaseWriter.executeDBQuery(sqlQueryStatement);

        Activity clashingActivity = null;

        if (queryResult.next()) {
            int activityID = queryResult.getInt("activity_id");
            String activityName = queryResult.getString("name");
            activityDate = null;
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
            clashingActivity = new Activity(activityName, activityDate, activityType, totalDistance, totalTime);
            clashingActivity.setId(activityID);

        }
        DatabaseWriter.disconnectFromDB();
        return clashingActivity;

    }


/*    public static boolean insertNewActivity(Activity activity, int user_id) throws SQLException {

        //check that the user is actually in the database
        if (UserDBOperations.getUserFromRS(user_id) == null) {
            return false;
        }*/


    /**
     * Inserts a new Activity into the database for a given User denoted by their user id. The Activity does not need
     * to have been assigned an activity id before being used in this function. If the user_id is invalid the function
     * returns an error value of -1. Else the function returns the assigned activity id that the activity has been
     * assigned to from the database.
     * This function automatically connects to and disconnects from the database.
     * @param activity The Activity that will be inserted into the database.
     * @param user_id The id of the user that the activity is for.
     * @return The activity id of the activity assigned by the database. -1 otherwise if the user id is invalid.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static int insertNewActivity(Activity activity, int user_id) throws SQLException {

        //check that the user is actually in the database
        if (UserDBOperations.getUserFromRS(user_id) == null) {
            return -1;

        }

        DatabaseWriter.connectToDB();


        String sqlInsertStmt = "INSERT INTO Activities(user_id,name,date_string,date,type,total_distance,total_time) \n" +
                "VALUES(?,?,?,?,?,?,?)";


        Connection dbConn = DatabaseWriter.getDbConnection();

        PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStatement.setInt(1, user_id);
        pUpdateStatement.setString(2, activity.getActivityName());
        pUpdateStatement.setString(3, activity.getDate().toString());
        pUpdateStatement.setDate(4, new java.sql.Date(activity.getDate().getTime()));
        pUpdateStatement.setString(5, activity.getActivityType());
        pUpdateStatement.setDouble(6, activity.getTotalDistance());
        pUpdateStatement.setDouble(7, activity.getTotalTime());
        pUpdateStatement.executeUpdate();
        //return true;

        ResultSet results = pUpdateStatement.getGeneratedKeys();
        results.next();
        int activity_id = results.getInt(1);

        DatabaseWriter.disconnectFromDB();
        return activity_id;

    }


    /**
     * Queries the database for a list of all the Activities that belong to a specific User denoted by their user_id
     * which is returned in the form of an ObservableList. If the user_id is invalid or the User has no Activities
     * stored in the database then the returned ObservableList will be empty. The Activities in the ObservableList are
     * ordered by their date from oldest to most recent.
     * This function automatically connects to and disconnects from the database.
     * @param user_id The id of the User that the Activities belong to.
     * @return An ObservableList containing the Activities belonging to the user ordered by date from oldest to most recent.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static ObservableList<Activity> getAllUsersActivities(int user_id) throws SQLException {

        DatabaseWriter.connectToDB();
        String sqlQueryStatement = "SELECT * FROM Activities WHERE user_id = " + user_id + " ORDER BY date;";
        ResultSet queryResult = DatabaseWriter.executeDBQuery(sqlQueryStatement);

        ObservableList<Activity> userActivities = FXCollections.observableArrayList();

        while (queryResult.next()) {
            int activityID = queryResult.getInt("user_id");
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
            Activity newActivity = new Activity(activityName, activityDate, activityType, totalDistance, totalTime);
            userActivities.add(newActivity);


        }
        DatabaseWriter.disconnectFromDB();
        return userActivities;
    }


    /**
     * Queries the database for a particular activity specified by the activity_id. If an Activity is found in the database
     * with a matching ID it is returned by the function. If the Query fails to find an Activity with the specified ID
     * or the ID is invalid a null value is returned by the function.
     * This function automatically connects to and disconnects from the database.
     * @param activity_id The id of the activity the will be searched for in the database.
     * @return The Activity that corresponds to the activity id if it exists in the table, null otherwise.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static Activity getActivityFromRS(int activity_id) throws SQLException {
        DatabaseWriter.connectToDB();
        String sqlQuery = "SELECT * FROM Activities WHERE activity_id = " + activity_id + ";";
        ResultSet queryResult = DatabaseWriter.executeDBQuery(sqlQuery);
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
        DatabaseWriter.disconnectFromDB();
        return retrievedActivity;
    }


    /**
     * Updates an activity that is already in the database. Returns a boolean value based on whether the update operation
     * has been created successfully. A value of false is returned if the Activity is not present in the database. That is
     * there is no Activity already in the database that has the same activity id as the Activity being inputted into the
     * database. The inputted Activity must have an assigned id.
     * This function automatically connects to and disconnects from the database.
     * @param activity The Activity class object that will have its record updated in the database providing that it
     *                 already exists.
     * @return true if the update operation is preformed without errors. False if the activity could not be located in
     * the database.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static boolean updateExistingActivity(Activity activity) throws SQLException {

        String sqlUpdateStmt = "UPDATE Activities SET name = ?, date_string = ?, date = ?, type = ?, total_distance = ?, total_time = ? WHERE activity_id = ?";
        if (getActivityFromRS(activity.getId()) != null) {
            DatabaseWriter.connectToDB();
            Connection dbConn = DatabaseWriter.getDbConnection();

            PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStatement.setString(1, activity.getActivityName());
            pUpdateStatement.setString(2, activity.getDate().toString());
            pUpdateStatement.setDate(3, new java.sql.Date(activity.getDate().getTime()));
            pUpdateStatement.setString(4, activity.getActivityType());
            pUpdateStatement.setDouble(5, activity.getTotalDistance());
            pUpdateStatement.setDouble(6, activity.getTotalTime());
            pUpdateStatement.setInt(7, activity.getId());
            pUpdateStatement.executeUpdate();
            DatabaseWriter.disconnectFromDB();
            return true;
        } else {
            return false;
        }
    }


    /**
     * Removes an existing activity from the database. Returns true if the activity no longer exists in the database,
     * false otherwise.
     * This function automatically connects to and disconnects from the database.
     * @param activityID The id of the activity that will be deleted from the database as an integer
     * @return true if the Activity no longer exists in the database, false otherwise.
     * @throws SQLException If an error occurs when handling the sql operations on the database.
     */
    public static boolean deleteExistingActivity(int activityID) throws SQLException {
        DatabaseWriter.connectToDB();
        String sqlDeleteStmt = "DELETE FROM Activities WHERE activity_id = ?";
        Connection dbConn = DatabaseWriter.getDbConnection();
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, activityID);
        pDeleteStmt.executeUpdate();
        DatabaseWriter.disconnectFromDB();
        if (getActivityFromRS(activityID) == null) {
            return true;
        } else {
            return false;
        }

    }

    /*public static void main(String[] args) {
        try {
            Activity test = getActivityFromRS(3);
            test.setActivityName("An Activity");
            System.out.println(updateExistingActivity(test));
            ObservableList<Activity> retrievedUsers = ActivityDBOperations.getAllUsersActivities(1);
            if (retrievedUsers != null) {
                for (Activity activity : retrievedUsers) {
                    System.out.println(activity.getId() + " " + activity.getActivityName() + " " + activity.getDate());

                }
            }
            deleteExistingActivity(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
