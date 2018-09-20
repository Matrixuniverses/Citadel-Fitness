package seng202.group2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.data.ActivityDBOperations;
import seng202.group2.data.DatabaseOperations;
import seng202.group2.data.DatapointDBOperations;
import seng202.group2.data.UserDBOperations;
import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;
import seng202.group2.model.User;

import java.sql.SQLException;

public class DataManager {

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private User currentUser = new User("", 0, 0,0);


    public DataManager() {
        try {
            userList.addAll(UserDBOperations.getAllUsers());
            // System.out.println(String.format("[INFO] Users loaded: %d", userList.size()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        userList.add(new User( "John Smith", 20, 170, 70));
//        userList.add(new User( "Bob Johnson", 15, 130, 50));
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        try {
            user.getActivityList().addAll(ActivityDBOperations.getAllUsersActivities(user.getId()));
            for (Activity activity : user.getActivityList()) {
                activity.getActivityData().addAll(DatapointDBOperations.getAllActivityDatapoints(activity.getId()));
                System.out.println(activity.getTotalDistance());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addUser(String name, int age, double height, double weight) {
        User newUser = new User(name, age, height, weight);
        try {
            DatabaseOperations.createDatabase();
            newUser.setId(UserDBOperations.insertNewUser(newUser));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userList.add(newUser);
    }

    public void deleteUser(User user) {
        try {
            DatabaseOperations.createDatabase();
            UserDBOperations.deleteExistingUser(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userList.remove(user);
    }

    public void changeUserWeight(int newWeight) {
        // TODO Add Database Connection!
        currentUser.setWeight(newWeight);
    }

    public void changeUserName(String newName) {
        // TODO Add Database Connection!
        currentUser.setName(newName);
    }

    public void changeUserHeight(int newWeight) {
        // TODO Add Database Connection!
        currentUser.setHeight(newWeight);
    }

    public void changeUserName(int newWeight) {
        // TODO Add Database Connection!
        currentUser.setWeight(newWeight);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Adds an activity to the data, this is first done by checking if an activity of the same name and date/ time
     * already exists for the user, if it does not then it is added to the data and to the users current
     * activity list
     * @param activity Activity to be added to data
     */
    public void addActivity(Activity activity){
        try {
            DatabaseOperations.createDatabase();
            if (!ActivityDBOperations.checkDuplicateActivity(activity, currentUser.getId())) {
                activity.setId(ActivityDBOperations.insertNewActivity(activity, currentUser.getId()));
                for (DataPoint datapoint : activity.getActivityData()) {
                    datapoint.setId(DatapointDBOperations.insertNewDataPoint(datapoint, activity.getId()));
                }
                currentUser.addActivity(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addActivities(Iterable<Activity> activityList) {
        for (Activity activity : activityList) {
            addActivity(activity);
        }
    }

    /**
     * deletes an activity from the current user and updates the data
     * @param activity
     */
    public void deleteActivity(Activity activity) {
        // TODO Add Database Connection!
        currentUser.deleteActivity(activity);
    }

    public void updateActivity(Activity activity) {
        // TODO Add Database Connection!
    }

    public ObservableList<Activity> getActivityList() {
        return currentUser.getActivityList();
    }




    //Getters and setters


    public ObservableList<User> getUserList() {
        return userList;
    }
}