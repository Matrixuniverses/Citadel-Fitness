package seng202.group2.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;

public class Target {

    private int id;
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleDoubleProperty initialValue;
    private SimpleDoubleProperty currentValue;
    private SimpleDoubleProperty finalValue;
    private Date completionDate;

    private BooleanProperty completed = new SimpleBooleanProperty(false);

    public Target(String name, String type, double initialValue, double currentValue, double finalValue, Date completionDate){
        this.name.set(name);
        this.type.set(type);
        this.initialValue.set(initialValue);
        this.currentValue.set(currentValue);
        this.finalValue.set(finalValue);
        this.completionDate = completionDate;

    }

    public Target(String tName, String tType, Double tValue, Date tDate) {
        name = new SimpleStringProperty(tName);
        type = new SimpleStringProperty(tType);
        initialValue = new SimpleDoubleProperty(tValue); // Needs to identify value to use.
        currentValue = new SimpleDoubleProperty(tValue);
        finalValue = new SimpleDoubleProperty(tValue);
        completionDate = tDate;
    }

    public void updateProgress(SimpleDoubleProperty progress) {
        currentValue = progress;
        if (currentValue.get() >= finalValue.get()) {
            completed.setValue(true);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}