package seng202.group2.model;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import seng202.group2.data.DataManager;
import seng202.group2.data.TargetDBOperations;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Target {

    private int id;
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleDoubleProperty initialValue;
    private SimpleDoubleProperty currentValue;
    private SimpleDoubleProperty progress;
    private SimpleDoubleProperty finalValue;
    private Date completionDate;

    private BooleanProperty completed = new SimpleBooleanProperty(false);

    public Target(String tName, Date completionDate, String tType, double initialValue, double currentValue, double finalValue){
        this.name  = new SimpleStringProperty(tName);
        this.type = new SimpleStringProperty(tType);
        this.initialValue = new SimpleDoubleProperty(initialValue);
        this.currentValue = new SimpleDoubleProperty(currentValue);
        this.finalValue = new SimpleDoubleProperty(finalValue);
        this.completionDate = completionDate;
        this.progress = new SimpleDoubleProperty(calculateProgress(initialValue, currentValue, finalValue));
    }


    public void updateProgress(double newCurrent) {
        this.progress.set(calculateProgress(this.initialValue.get(), newCurrent, this.finalValue.get()));
        this.currentValue.set(newCurrent);
        try {
            TargetDBOperations.updateExistingTarget(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double calculateProgress(double initialValue, double currentValue, double finalValue) {
        System.out.println(initialValue + " "  + currentValue + " " + finalValue);
        double completed = (currentValue - initialValue) / (finalValue - initialValue);
        if (completed <= 0) {
            return 0;
        } else if (completed >= 1) {
            return 1;
        }
        return completed;
    }

    /**
     * Used to display the type of target in the target list table (Called by javafx at runtime)
     * @return String with the formatted type
     */
    public String getFormattedType() {
        String type = this.type.get();

        switch(type){
            case "Total Distance (m)": return "Total Distance";
            case "Target Weight (kg)": return "Target Weight";
            case "Average Speed (m/s)": return "Average Speed";
        }
        return null;
    }

    public String getFormattedCompletionDate() {
        return new SimpleDateFormat("MMMM d, YYYY").format(this.completionDate);
    }

    public String getFormattedProgress() {
        String percentStr;
        double progress = this.progress.get();
        if (progress == 1) {
            percentStr = "COMPLETED";
        } else {
            percentStr = Integer.toString((int)Math.round(progress * 100)) + "%";
        }
        return percentStr;
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

    public void setName(String name) {
        this.name.set(name);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public double getInitialValue() {
        return initialValue.get();
    }

    public void setInitialValue(double initialValue) {
        this.initialValue.set(initialValue);
    }

    public double getCurrentValue() {
        return currentValue.get();
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue.set(currentValue);
    }

    public double getFinalValue() {
        return finalValue.get();
    }

    public SimpleDoubleProperty finalValueProperty() {
        return finalValue;
    }

    public void setFinalValue(double finalValue) {
        this.finalValue.set(finalValue);
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty progressProperty() {
        return progress;
    }
}