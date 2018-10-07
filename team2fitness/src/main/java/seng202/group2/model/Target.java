package seng202.group2.model;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import seng202.group2.data.DataManager;

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
    }

    public Target(String tName, String tType, Double tValue, Date tDate) {
        name = new SimpleStringProperty(tName);
        type = new SimpleStringProperty(tType);
        initialValue = new SimpleDoubleProperty(80); // Needs to identify value to use.
        currentValue = new SimpleDoubleProperty(85);
        finalValue = new SimpleDoubleProperty(90);
        progress = new SimpleDoubleProperty(currentValue.get() / finalValue.get());
        completionDate = tDate;
    }

    public void updateProgress(double progress) {
        currentValue.set(progress);
        if (currentValue.get() >= finalValue.get()) {
            completed.setValue(true);
        }
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



    public String format(Double val) {
        String type = this.type.get();
        String formatted = "";

        if (type.equals("Total Distance (m)")) {
            formatted = Integer.toString((int)Math.round(val)) + " m";
        } else if (type.equals("Target Weight (kg)")) {
            if (val == 0.0) {
                formatted = "0 kg";
            } else {

                formatted = Double.toString(Math.round(val * 10.0) / 10.0) + " kg";
            }
        } else if (type.equals("Average Speed (m/s)")) {
            if (val == 0.0) {
                formatted = "0 m/s";
            } else {
                formatted = Double.toString(Math.round(val * 10.0) / 10.0) + " m/s";
            }
        }

        return formatted;
    }

    public String getFormattedInitialValue() {
        double initVal = this.initialValue.get();
        return format(initVal);
    }

    public String getFormattedCurrentValue() {
        double currVal = this.currentValue.get();
        return format(currVal);
    }

    public String getFormattedFinalValue() {
        double finVal = this.finalValue.get();
        String formatted = format(finVal);

        return formatted;
    }

    public String getFormattedCompletionDate() {
        return new SimpleDateFormat("MMMM d, YYYY").format(this.completionDate);
    }

    public String getFormattedStatus() {
        String percentStr;
        if (this.completed.get()) {
            percentStr = "COMPLETED";
        } else {
            int percentVal = (int) Math.round(Math.abs((this.currentValue.get() - this.initialValue.get()) / (this.finalValue.get() - this.initialValue.get())) * 100.0);
            percentStr = Integer.toString(percentVal) + "%";
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
        return currentValue;
    }
}