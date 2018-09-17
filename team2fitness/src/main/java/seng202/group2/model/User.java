package seng202.group2.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;

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



    public User(int id, String name, int age, double height, double weight){
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.height = new SimpleDoubleProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.bmi = new SimpleDoubleProperty();
        this.bmi.bind(this.weight.divide(this.height.multiply(this.height.divide(100))));

        totalDistance = new SimpleDoubleProperty(0);
        activityList.addListener(new ListChangeListener<Activity>() {
            @Override
            public void onChanged(Change<? extends Activity> c) {
                totalDistance.setValue(calculateTotalUserDistance()/1000);
            }
        });
    }

    private double calculateTotalUserDistance(){
        double totalDistance = 0;
        for (Activity activity : activityList){
            totalDistance += activity.getTotalDistance();
        }
        return totalDistance;
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
