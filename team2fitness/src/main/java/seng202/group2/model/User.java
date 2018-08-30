package seng202.group2.model;

import java.util.ArrayList;

public class User {

    private int id;
    private String name;
    private int age;
    private float height;
    private float weight;
    ArrayList<Activity> activityList = new ArrayList<Activity>();
    ArrayList<Target> targetList = new ArrayList<Target>();


    public User(String name, int age, float height, float weight){
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public ArrayList<Activity> getActivityList() {
        return activityList;
    }

    public ArrayList<Target> getTargetList() {
        return targetList;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }
}
