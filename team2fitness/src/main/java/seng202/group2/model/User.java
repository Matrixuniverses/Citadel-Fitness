package seng202.group2.model;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.data.UserDBOperations;
import java.sql.SQLException;

/**
 * Model class for a user
 */
public class User {

    private int id;
    private StringProperty name;
    private StringProperty gender;
    private IntegerProperty age;
    private DoubleProperty height;
    private DoubleProperty weight;
    private DoubleProperty bmi;
    private DoubleProperty totalDistance;
    private DoubleProperty totalTime;
    private DoubleProperty avgSpeed;

    private ObservableList<Activity> activityList = FXCollections.observableArrayList();
    private ObservableList<Target> targetList = FXCollections.observableArrayList();



    /**
     * Creates a new user and populates their data. Sets a listener to recalculate the total user distance when
     * a new activity is added to the user's activity list. This will be used when no unique ID is known (e.g. after
     * user creation)
     *
     * @param name the users name
     * @param age the users age
     * @param height the users height
     * @param weight the users weight
     */
    public User(String name, int age, double height, double weight, String gender){
        this.name = new SimpleStringProperty(name);
        this.gender = new SimpleStringProperty(gender);
        this.age = new SimpleIntegerProperty(age);
        this.height = new SimpleDoubleProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.bmi = new SimpleDoubleProperty();
        this.bmi.bind(this.weight.divide(this.height.divide(100).multiply(this.height.divide(100))));

        totalDistance = new SimpleDoubleProperty(0);
        totalTime = new SimpleDoubleProperty(0);
        avgSpeed = new SimpleDoubleProperty(0);

        // Change listener that will re-calculate the user total distance and time, used for targets
        activityList.addListener(new ListChangeListener<Activity>() {
            @Override
            public void onChanged(Change<? extends Activity> c) {
                totalDistance.set(calculateTotalUserDistance());
                totalTime.set(calculateTotalUserTime());
                avgSpeed.set(totalDistance.get() / totalTime.get());
            }
        });

        // Creates new event listeners that update the database when the users properties are changed
        setupDatabaseHandlers();
    }

    /**
     * Creates a new user with with a given ID, this will be used a unique ID for the user is known (e.g. when
     * loaded from database)
     *
     * @param id Unique identification
     * @param name User name
     * @param age User's age(years)
     * @param height User's height (cm)
     * @param weight User's weight(kg)
     */
    public User(int id, String name, int age, double height, double weight, String gender) {
        this(name, age, height, weight, gender);
        this.id = id;
    }

    /**
     * assigns listeners to all properties that are stored in the data. When changed it updates the data.
     */
    private void setupDatabaseHandlers() {
        this.name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    // Update the name in the database
                    UserDBOperations.updateExistingUser(User.this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        this.age.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    // Update age in the database
                    UserDBOperations.updateExistingUser(User.this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        this.height.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    // Update height in the database
                    UserDBOperations.updateExistingUser(User.this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        this.weight.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    // Update weight in the database
                    UserDBOperations.updateExistingUser(User.this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This returns the total distance the user has traveled
     * @return double totalDistance
     */
    private double calculateTotalUserDistance(){
        double totalDistance = 0;
        for (Activity activity : activityList){
            totalDistance += activity.getTotalDistance();
        }
        return totalDistance;
    }

    /**
     * This returns the total time the user has spent travelling
     * @return Double containing total time
     */
    private double calculateTotalUserTime() {
        double totalTime = 0;
        for (Activity activity : activityList) {
            totalTime += activity.getTotalTime();
        }
        return totalTime;
    }

    /**
     * This calculated the average heart rate and calories for the activity and then adds the activity to the activityList
     * @param activity activity object
     */
    public void addActivity(Activity activity){
        //double avgHR = DataAnalyzer.calcAverageHR(activity);
        double caloriesBurned = DataAnalyzer.calcCalories(this, activity);
        activity.setCaloriesBurned(caloriesBurned);
        this.activityList.add(activity);
    }

    /**
     * This removes an activity from the activity list
     * @param activity activity Object
     */
    public void deleteActivity(Activity activity) {
        this.activityList.remove(activity);
    }

    /**
     * This returns the id of the user
     * @return returns int id
     */
    public int getId() {
        return id;
    }

    /**
     * This sets the id for the user
     * @param id id of the user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This returns the name of the user with type String
     * @return returns String name
     */
    public String getName() {
        return name.get();
    }

    /**
     * This returns the name of the user with type StringProperty
     * @return returns StringProperty name
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * This sets the name of the user -Type String
     * @param name name of the user
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * This returns the age of the user with type Integer
     * @return returns int age
     */
    public int getAge() {
        return age.get();
    }

    /**
     * This returns the age of the user with type IntegerProperty
     * @return returns IntegerProperty age
     */
    public IntegerProperty ageProperty() {
        return age;
    }

    /**
     * This sets the age of the user -type int
     * @param age age of the user
     */
    public void setAge(int age) {
        this.age.set(age);
    }

    /**
     * This returns the height of the user -type Double
     * @return returns double height
     */
    public double getHeight() {
        return height.get();
    }

    /**
     * This returns the height of the user -type DoubleProperty
     * @return returns DoubleProperty height
     */
    public DoubleProperty heightProperty() {
        return height;
    }

    /**
     * This sets the height of the user -type double
     * @param height height of the user
     */
    public void setHeight(double height) {
        this.height.set(height);
    }

    /**
     * This returns the weight of the user -type Double
     * @return returns double weight
     */
    public double getWeight() {
        return weight.get();
    }

    /**
     * This returns the weight of the user -type DoubleProperty
     * @return returns DoubleProperty weight
     */
    public DoubleProperty weightProperty() {
        return weight;
    }

    /**
     * This sets the weight of the user -type Double
     * @param weight weight of the user
     */
    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getGender() {
        return this.gender.get();
    }

    /**
     * This returns the current BMI of the user -type Double
     * @return returns double bmi
     */
    public double getBmi() {
        return bmi.get();
    }

    /**
     * This returns current BMI of the user -type DoubleProperty
     * @return returns DoubleProperty bmi
     */
    public DoubleProperty bmiProperty() {
        return bmi;
    }

    /**
     * This returns a list of the current users activities
     * @return returns Activity activityList
     */
    public ObservableList<Activity> getActivityList() {
        return activityList;
    }

    /**
     * Returns a list of the activities that have been completed by the user that have been loaded
     * in from a csv file.
     * @return returns an ObservableList of the user's activities that have not be manually entered in by the user.
     */
    public ObservableList<Activity> getNonManualActivityList() {
        ObservableList<Activity> nonManualActivities = FXCollections.observableArrayList();
        for (Activity activity : activityList) {
            if (!(activity.isManualEntry())) {
                nonManualActivities.add(activity);
            }
        }
        return nonManualActivities;
    }

    /**
     * This returns the current users list of targets
     * @return returns Target targetList
     */
    public ObservableList<Target> getTargetList() {
        return targetList;
    }

    /**
     * This returns the users total distance -type Double
     * @return returns double totalDistance
     */
    public double getTotalDistance() {
        return totalDistance.get();
    }

    /**
     * This returns the users total distance -type DoubleProperty
     * @return returns DoubleProperty totalDistance
     */
    public DoubleProperty totalDistanceProperty() {
        return totalDistance;
    }

    public DoubleProperty avgSpeedProperty() {
        return avgSpeed;
    }
}
