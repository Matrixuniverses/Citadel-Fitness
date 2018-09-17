package seng202.group2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataManager {

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private User currentUser = new User("", 0, 0, 0);

    public void setCurrentUser(int i) {

        System.out.print("Current user:" + userList.get(i).getName() + "\n");
        currentUser.setName(userList.get(i).getName());
        currentUser.setAge(userList.get(i).getAge());
        currentUser.setWeight(userList.get(i).getWeight());
        currentUser.setHeight(userList.get(i).getHeight());
        currentUser.setName(userList.get(i).getName());
        currentUser.setActivityList(userList.get(i).getActivityList());
        currentUser.setTargetList(userList.get(i).getTargetList());

    }

    // User Data Functions

    public void createNewUser(String name, int age, double height, double weight) {
        // TODO Add Database Connection!
        User newUser = new User(name, age, height, weight);

        // newUser.setId = ..... get ID from database

        userList.add(newUser);
    }

    public void deleteUser(User user) {
        // TODO Add Database Connection!
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
        // TODO Add Database Connection!
        currentUser.addActivity(activity);
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
