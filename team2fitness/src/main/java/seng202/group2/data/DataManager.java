package seng202.group2.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.model.Activity;
import seng202.group2.model.Target;
import seng202.group2.model.User;
import java.sql.SQLException;

/**
 * DataManager is a Singleton class that provides support for accessing userdata and updating userdata throughout all
 * the controller classes
 */
public class DataManager {
    // Important user data lists
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObjectProperty<User> currentUser = new SimpleObjectProperty<User>(new User("", 0, 0,0,"Male"));

    // Health Warning Booleans
    private BooleanProperty newHealthWarning = new SimpleBooleanProperty(false);
    private BooleanProperty hasTachycardia = new SimpleBooleanProperty(true);
    private BooleanProperty hasCardiovascular = new SimpleBooleanProperty(true);
    private BooleanProperty hasBradycardia = new SimpleBooleanProperty(true);

    private static DataManager dataManager = new DataManager();

    public static DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Upon creation the DataManager object will preload all user data from the database to prevent possible slowdowns
     * when changing the user.
     */
    public DataManager() {
        try {
            // Adding all users to the User list, read from database
            userList.addAll(UserDBOperations.getAllUsers());
            for (User user : userList) {
                // Adding all activities to the Activity list of the current User, read from database
                user.getActivityList().addAll(ActivityDBOperations.getAllUsersActivities(user.getId()));

                // For each activity of a user, the Datapoints are loaded from database
                for (Activity activity : user.getActivityList()) {
                    activity.getActivityData().addAll(DatapointDBOperations.getAllActivityDatapoints(activity.getId()));
                    activity.setCaloriesBurned(DataAnalyzer.calcCalories(user, activity));
                }
                // Adding all targets to the Target list of the current User, read from database
                user.getTargetList().addAll(TargetDBOperations.getAllUserTargets(user.getId()));

                // Cannot use the addAll() method as each target needs to have a listener added to the users data
                for (Target target : user.getTargetList()) {
                    listenTarget(target, user);
                }
            }

            // Database error
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * When the user is changed upon logout/ login the health warnings are cleared from the health warning list, and
     * the current user is changed
     * @param currentUser User to switch to
     */
    public void setCurrentUser(User currentUser) {
        resetWarnings();
        this.currentUser.set(currentUser);
    }

    /**
     * Adds a new User to the User list and writes to the database, this method assumes that error detection on
     * invalid fields has already been completed
     * @param name Name of user
     * @param age Age of user
     * @param height Height of user
     * @param weight Weight of user
     * @param gender Gender of user
     */
    public void addUser(String name, int age, double height, double weight, String gender) {
        User newUser = new User(name, age, height, weight, gender);
        try {
            // Prevents an attempt at adding a user to a non existent database, the tables will not be recreated
            // if there already exists a database as the SQL create statements are of 'CREATE TABLE IF NOT EXISTS'
            DatabaseOperations.createDatabase();

            // Upon write to the database, the UserID is generated
            newUser.setId(UserDBOperations.insertNewUser(newUser));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userList.add(newUser);
    }

    /**
     * Deletes a user from the database
     * @param user User to delete
     */
    public void deleteUser(User user) {
        try {
            // Calling the create database prevents an error that was occurring where a user was being created, then
            // when deleted shortly thereafter, a crash was occurring due to the tables still being created in the
            // background. This was a very difficult bug to try and fix
            DatabaseOperations.createDatabase();
            UserDBOperations.deleteExistingUser(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userList.remove(user);
    }


    /**
     * Adds an activity to the data, this is first done by checking if an activity of the same name and date/ time
     * already exists for the user, if it does not then it is added to the data and to the users current
     * activity list
     * @param activity Activity to be added to data
     */
    public void addActivity(Activity activity){
        try {
            // Do not add duplicate activities to the database
            if (!ActivityDBOperations.checkDuplicateActivity(activity, currentUser.get().getId())) {
                activity.setId(ActivityDBOperations.insertNewActivity(activity, currentUser.get().getId()));

                // Using the database operation that uses manual DB transactions to increase write performance
                DatapointDBOperations.insertDataPointList(activity.getActivityData(), activity.getId());
                currentUser.get().addActivity(activity);

                // Only need to check health warnings on activity import
                checkHealthWarnings(activity);
            }
            // Database error
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds a list of activities to the database
     * @param activityList List of activities to add
     */
    public void addActivities(Iterable<Activity> activityList) {
        for (Activity activity : activityList) {
            addActivity(activity);
        }
    }

    /**
     * deletes an activity from the current user and updates the data
     * @param activity Activity to delete
     */
    public void deleteActivity(Activity activity) {
        try {
            ActivityDBOperations.deleteExistingActivity(activity.getId());
            currentUser.get().deleteActivity(activity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets up change listeners on user data that will update activities when user properties changes
     * @param target Target that the listener should update
     * @param user User to attach the listener to
     */
    private void listenTarget(Target target, User user) {
        switch(target.getType()) {
            // Only have 3 target types, if there were more a proper CONST class style would be used
            case "Target Weight (kg)":
                user.weightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        target.updateProgress((double) newValue);
                    }
                });
                break;

            case "Average Speed (m/s)":
                user.avgSpeedProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        target.updateProgress((double) newValue);
                    }
                });
                break;

            case "Total Distance (m)":
                user.totalDistanceProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        target.updateProgress((double) newValue);
                    }
                });
                break;
        }
    }

    /**
     * Adds a target to the Target list, writes to database and attaches change listeners to the user properties
     * @param target Target to add
     */
    public void addTarget(Target target){
        // Add the target to the target list
        currentUser.get().getTargetList().add(target);
        // Listen to the user properties
        listenTarget(target, currentUser.get());
        try {
            // Add the target to the database
            target.setId(TargetDBOperations.insertNewTarget(target, currentUser.get().getId()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes a target from the Target list, the listeners attached to the user properties are automatically
     * removed by Java garbage collection
     * @param target Target to delete
     */
    public void deleteTarget(Target target) {
        // Delete target from User Target list
        currentUser.get().getTargetList().remove(target);
        try {
            // Delete target from the database
            TargetDBOperations.deleteExistingTarget(target.getId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks an activity for health warnings by calling the warning checkers in the DataAnalyzer
     * @param activity Activity to check health warnings for
     */
    private void checkHealthWarnings(Activity activity) {
        if (DataAnalyzer.hasTachycardia(currentUser.get().getAge(), (int) activity.getMinHR())) {
            hasTachycardia.set(true);
            newHealthWarning.setValue(true);
        }
        if (DataAnalyzer.hasBradycardia(currentUser.get().getAge(), (int) activity.getMinHR())) {
            hasBradycardia.set(true);
            newHealthWarning.setValue(true);
        }
        if (DataAnalyzer.cardiovascularMortalityProne(currentUser.get().getAge(), (int) activity.getMinHR())) {
            hasCardiovascular.set(true);
            newHealthWarning.setValue(true);
        }
    }

    /**
     * Helper function to remove existing health warnings upon logout so another user does not get notified of
     * health warnings that do not belong to them
     */
    private void resetWarnings(){
        hasBradycardia.set(false);
        hasCardiovascular.set(false);
        hasTachycardia.set(false);
        newHealthWarning.setValue(false);
    }

    public boolean isHasTachycardia() {
        return hasTachycardia.get();
    }

    public BooleanProperty hasTachycardiaProperty() {
        return hasTachycardia;
    }

    public boolean isHasCardiovascular() {
        return hasCardiovascular.get();
    }

    public BooleanProperty hasCardiovascularProperty() {
        return hasCardiovascular;
    }

    public boolean isHasBradycardia() {
        return hasBradycardia.get();
    }

    public BooleanProperty hasBradycardiaProperty() {
        return hasBradycardia;
    }

    public boolean isNewHealthWarning() {
        return newHealthWarning.get();
    }

    public BooleanProperty newHealthWarningProperty() {
        return newHealthWarning;
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public ObservableList<Activity> getActivityList() {
        return currentUser.get().getActivityList();
    }

    public ObservableList<Target> getTargetList() {
        return currentUser.get().getTargetList();
    }

    public ObservableList<User> getUserList() {
        return userList;
    }
}
