package seng202.group2.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class User {

    private int id;
    private StringProperty name;
    private IntegerProperty age;
    private DoubleProperty height;
    private DoubleProperty weight;
    private DoubleProperty bmi;

    ObservableList<Activity> activityList = FXCollections.observableArrayList();
    ObservableList<Target> targetList = FXCollections.observableArrayList();



    public User(String name, int age, double height, float weight){
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.height = new SimpleDoubleProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.bmi = new SimpleDoubleProperty();
        this.bmi.bind(this.weight.divide(this.height.multiply(this.height)));
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

    public static void main(String[] args )
    {
        User user = new User("Adam", 18, 173, 70);
        System.out.println(user.getBmi());
        user.setWeight(100);
        System.out.println(user.getBmi());

    }
}
