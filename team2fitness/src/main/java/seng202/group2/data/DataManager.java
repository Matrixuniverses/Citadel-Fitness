package seng202.group2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;
import seng202.group2.model.User;
import java.sql.SQLException;

public class DataManager {
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private User currentUser = new User("", 0, 0,0, "");


    public DataManager() {
        try {
            userList.addAll(UserDBOperations.getAllUsers());
            try {
                for (User user : userList) {
                    user.getActivityList().addAll(ActivityDBOperations.getAllUsersActivities(user.getId()));
                    for (Activity activity : user.getActivityList()) {
                        activity.getActivityData().addAll(DatapointDBOperations.getAllActivityDatapoints(activity.getId()));
                        activity.setCaloriesBurned(DataAnalyzer.calcCalories(user, activity));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("[INFO] Users loaded: %d", userList.size()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void addUser(String name, int age, double height, double weight, String gender) {
        User newUser = new User(name, age, height, weight, gender);
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
                DatapointDBOperations.insertDataPointList(activity.getActivityData(), activity.getId());
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
        try {
            ActivityDBOperations.deleteExistingActivity(activity.getId());
            currentUser.deleteActivity(activity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
