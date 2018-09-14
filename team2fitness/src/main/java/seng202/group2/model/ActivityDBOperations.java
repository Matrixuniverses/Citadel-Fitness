package seng202.group2.model;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import seng202.group2.data_functions.databaseWriter;

public class ActivityDBOperations {





    public static boolean insertNewActivity(Activity activity, int user_id) throws SQLException {

        //check that the user is actually in the database
        if (UserDBOperations.getUserFromRS(user_id) == null) {
            return false;
        }
        databaseWriter.connectToDB();


        String sqlInsertStmt = "INSERT INTO Activities(user_id,name,date_string,date,type,total_distance,total_time) \n" +
                "VALUES(?,?,?,?,?,?,?)";


        Connection dbConn = databaseWriter.getDbConnection();

        PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStatement.setInt(1,user_id);
        pUpdateStatement.setString(2,activity.getActivityName());
        pUpdateStatement.setString(3,activity.getDate().toString());
        pUpdateStatement.setDate(4,new java.sql.Date(activity.getDate().getTime()));
        pUpdateStatement.setString(5, activity.getActivityType());
        pUpdateStatement.setDouble(6, activity.getTotalDistance());
        pUpdateStatement.setDouble(7, activity.getTotalTime());
        pUpdateStatement.executeUpdate();
        databaseWriter.disconnectFromDB();
        return true;


    }


    public static ObservableList<Activity> getAllUsersActivities(int user_id) throws SQLException{

        databaseWriter.connectToDB();
        String sqlQueryStatement = "SELECT * FROM Activities WHERE user_id = "+ user_id + " ORDER BY date;";
        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQueryStatement);

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
        databaseWriter.disconnectFromDB();
        return userActivities;


    }

    public static Activity getActivityFromRS(int activity_id) throws SQLException{
        databaseWriter.connectToDB();
        String sqlQuery = "SELECT * FROM Activities WHERE activity_id = "+ activity_id + ";";
        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQuery);
        Activity retrievedActivity= null;

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
        databaseWriter.disconnectFromDB();
        return retrievedActivity;
    }


    public static boolean updateExistingActivity(Activity activity) throws SQLException{

        String sqlUpdateStmt = "UPDATE Activities SET name = ?, date_string = ?, date = ?, type = ?, total_distance = ?, total_time = ? WHERE activity_id = ?";
        if (getActivityFromRS(activity.getId()) != null) {
            databaseWriter.connectToDB();
            Connection dbConn = databaseWriter.getDbConnection();

            PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStatement.setString(1, activity.getActivityName());
            pUpdateStatement.setString(2, activity.getDate().toString());
            pUpdateStatement.setDate(3, new java.sql.Date(activity.getDate().getTime()));
            pUpdateStatement.setString(4, activity.getActivityType());
            pUpdateStatement.setDouble(5, activity.getTotalDistance());
            pUpdateStatement.setDouble(6, activity.getTotalTime());
            pUpdateStatement.setInt(7, activity.getId());
            pUpdateStatement.executeUpdate();
            databaseWriter.disconnectFromDB();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



}
