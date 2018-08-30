package seng202.group2.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class User {

    private int id;
    private StringProperty name;
    private IntegerProperty age;
    private FloatProperty height;
    private FloatProperty weight;
    ObservableList<Activity> activityList = FXCollections.observableArrayList();
    ObservableList<Target> targetList = FXCollections.observableArrayList();


    public User(String name, int age, float height, float weight){
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.height = new SimpleFloatProperty(height);
        this.weight = new SimpleFloatProperty(weight);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public Integer getAge() {
        return age.get();
    }

    public Float getHeight() {
        return height.get();
    }

    public Float getWeight() {
        return weight.get();
    }

    public ObservableList<Activity> getActivityList() {
        return activityList;
    }

    public ObservableList<Target> getTargetList() {
        return targetList;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }
}
