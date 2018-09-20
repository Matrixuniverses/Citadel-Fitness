package seng202.group2.model;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.data.ActivityDBOperations;
import seng202.group2.data.DatabaseOperations;
import seng202.group2.data.UserDBOperations;

import java.sql.SQLException;

public class User {

    private int id;
    private StringProperty name;
    private IntegerProperty age;
    private DoubleProperty height;
    private DoubleProperty weight;
    private DoubleProperty bmi;
    private DoubleProperty totalDistance;

    private ObservableList<Activity> activityList = FXCollections.observableArrayList();
    private ObservableList<Target> targetList = FXCollections.observableArrayList();

    /**
     * Creates a new user and populates their data. Sets a listener to recalculate the total user distance when
     * a new activity is added to the user's activity list.
     * @param id Unique identification
     * @param name User name
     * @param age User's age(years)
     * @param height User's height (cm)
     * @param weight User's weight(kg)
     */
    public User(int id, String name, int age, double height, double weight){
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.height = new SimpleDoubleProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.bmi = new SimpleDoubleProperty();
        this.bmi.bind(this.weight.divide(this.height.divide(100).multiply(this.height.divide(100))));

        totalDistance = new SimpleDoubleProperty(0);
        activityList.addListener(new ListChangeListener<Activity>() {
            @Override
            public void onChanged(Change<? extends Activity> c) {
                totalDistance.setValue(calculateTotalUserDistance()/1000);
            }
        });
    }

    public User(String name, int age, double height, double weight){
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.height = new SimpleDoubleProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.bmi = new SimpleDoubleProperty();
        this.bmi.bind(this.weight.divide(this.height.divide(100).multiply(this.height.divide(100))));

        totalDistance = new SimpleDoubleProperty(0);
        activityList.addListener(new ListChangeListener<Activity>() {
            @Override
            public void onChanged(Change<? extends Activity> c) {
                totalDistance.setValue(calculateTotalUserDistance()/1000);
            }
        });

        setupDatabaseHandlers();
    }


    /*
     * assigns listeners to all properties that are stored in the data. When changed it updates the data.
     */
    private void setupDatabaseHandlers() {
        this.name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    UserDBOperations.updateExistingUser(User.this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        this.age.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });

        this.height.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });

        this.weight.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
    }

    public void loadDataFromDB() {
        try {
            DatabaseOperations.createDatabase();
            DatabaseOperations.connectToDB();

            activityList = ActivityDBOperations.getAllUsersActivities(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double calculateTotalUserDistance(){
        double totalDistance = 0;
        for (Activity activity : activityList){
            totalDistance += activity.getTotalDistance();
        }
        return totalDistance;
    }

    public void addActivity(Activity activity){
        double avgHR = DataAnalyzer.calcAverageHR(activity);
        int caloriesBurned = DataAnalyzer.calcCalories(this.getAge(), this.getWeight(), avgHR, activity.getTotalTime(), true);
        activity.setCaloriesBurned(caloriesBurned);
        this.activityList.add(activity);
    }

    public void deleteActivity(Activity activity) {
        this.activityList.remove(activity);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public double getHeight() {
        return height.get();
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public double getWeight() {
        return weight.get();
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public double getBmi() {
        return bmi.get();
    }

    public DoubleProperty bmiProperty() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi.set(bmi);
    }

    public ObservableList<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(ObservableList<Activity> activityList) {
        this.activityList = activityList;
    }

    public ObservableList<Target> getTargetList() {
        return targetList;
    }

    public void setTargetList(ObservableList<Target> targetList) {
        this.targetList = targetList;
    }

    public double getTotalDistance() {
        return totalDistance.get();
    }

    public DoubleProperty totalDistanceProperty() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance.set(totalDistance);
    }
}
