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

    public Target(String tName, Date completionDate, String tType, double initialValue, double currentValue, double finalValue ){

        this.name  = new SimpleStringProperty(tName);
        this.type = new SimpleStringProperty(tType);
        this.initialValue = new SimpleDoubleProperty(initialValue);
        this.currentValue = new SimpleDoubleProperty(currentValue);
        this.finalValue = new SimpleDoubleProperty(finalValue);
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

    public String getName() { return name.get(); }

    public void setName(String newName) { this.name.set(newName); }

    public String getType() { return type.get(); }

    public void setType(String newType) { this.type.set(newType); }

    public double getInitialValue() { return initialValue.get(); }


    public double getCurrentValue() { return currentValue.get(); }


    public double getFinalValue() { return finalValue.get(); }

    public java.util.Date getCompletionDate() { return completionDate; }

}