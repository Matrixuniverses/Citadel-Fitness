package seng202.group2.data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.model.Activity;
import seng202.group2.model.HealthWarning;
import seng202.group2.model.Target;
import seng202.group2.model.User;
import java.sql.SQLException;

public class DataManager {

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObjectProperty<User> currentUser = new SimpleObjectProperty<User>(new User("", 0, 0,0,"Male"));
    private ObservableList<HealthWarning> healthWarnings = FXCollections.observableArrayList();

    private static DataManager dataManager = new DataManager();

    public static DataManager getDataManager() {
        return dataManager;
    }

    public DataManager() {
        try {
            userList.addAll(UserDBOperations.getAllUsers());
            for (User user : userList) {
                user.getActivityList().addAll(ActivityDBOperations.getAllUsersActivities(user.getId()));
                for (Activity activity : user.getActivityList()) {
                    activity.getActivityData().addAll(DatapointDBOperations.getAllActivityDatapoints(activity.getId()));
                    activity.setCaloriesBurned(DataAnalyzer.calcCalories(user, activity));
                }
                user.getTargetList().addAll(TargetDBOperations.getAllUserTargets(user.getId()));

                // Cannot use the addAll() method as each target needs to have a listener added to the users data
                for (Target target : user.getTargetList()) {
                    listenTarget(target, user);
                }
            }
            System.out.println(String.format("[INFO] Users loaded: %d", userList.size()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser.set(currentUser);
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



    public User getCurrentUser() {
        return currentUser.get();
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
            if (!ActivityDBOperations.checkDuplicateActivity(activity, currentUser.get().getId())) {
                activity.setId(ActivityDBOperations.insertNewActivity(activity, currentUser.get().getId()));
                DatapointDBOperations.insertDataPointList(activity.getActivityData(), activity.getId());
                currentUser.get().addActivity(activity);

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



    public ObservableList<Activity> getActivityList() {
        return currentUser.get().getActivityList();
    }

    private void listenTarget(Target target, User user) {
        switch(target.getType()) {
            case "Target Weight (kg)":
                System.out.println("beans");
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

    public void addTarget(Target target){
        listenTarget(target, currentUser.get());
        currentUser.get().getTargetList().add(target);
        try {
            target.setId(TargetDBOperations.insertNewTarget(target, currentUser.get().getId()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteTarget(Target target) {
        currentUser.get().getTargetList().remove(target);
        try {
            TargetDBOperations.deleteExistingTarget(target.getId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<Target> getTargetList() {
        return currentUser.get().getTargetList();
    }

    public ObservableList<User> getUserList() {
        return userList;
    }
}
