package seng202.group2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.data_functions.DatabaseWriter;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class DataManager {

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private User currentUser = new User("", 0, 0,0);


    public DataManager() {
//        try {
//            for (User user : UserDBOperations.getAllUsers()) {
//                userList.add(user);
//            }
//            System.out.println(userList.size());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        userList.add(new User( "John Smith", 20, 170, 70));
        userList.add(new User( "Bob Johnson", 15, 130, 50));
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    // User Data Functions

    public void addUser(String name, int age, double height, double weight) {
        User newUser = new User(name, age, height, weight);
        try {
            DatabaseWriter.createDatabase();
            newUser.setId(UserDBOperations.insertNewUser(newUser));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userList.add(newUser);
    }

    public void deleteUser(User user) {
        try {
            DatabaseWriter.createDatabase();
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
// Activity Functions

    /**
     * adds an activity to the current user and updates the database
     * @param activity
     */
    public void addActivity(Activity activity){
        try {
            DatabaseWriter.createDatabase();
            activity.setId(ActivityDBOperations.insertNewActivity(activity, currentUser.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        currentUser.addActivity(activity);
    }

    public void addActivity(Iterable<Activity> activityList) {
        for (Activity activity : activityList) {
            addActivity(activity);
        }
    }

    /**
     * deletes an activity from the current user and updates the database
     * @param activity
     */
    public void deleteActivity(Activity activity) {
        // TODO Add Database Connection!
        currentUser.deleteActivity(activity);
    }

    public void updateActivity(Activity activity) {
        // TODO Add Database Connection!
    }




    //Getters and setters


    public ObservableList<User> getUserList() {
        return userList;
    }
}
