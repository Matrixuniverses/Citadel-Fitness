package seng202.group2.model;

import javafx.beans.property.*;
import seng202.group2.data.TargetDBOperations;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Model class for the user Targets
 */
public class Target {

    private int id;
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleDoubleProperty initialValue;
    private SimpleDoubleProperty currentValue;
    private SimpleDoubleProperty progress;
    private SimpleDoubleProperty finalValue;
    private SimpleStringProperty status;
    private Date completionDate;

    private BooleanProperty completed = new SimpleBooleanProperty(false);

    /**
     * Creates a new target with the given values, also generates the status of the target
     * @param tName Name of the target
     * @param completionDate Completion date of the target
     * @param tType Type of target
     * @param initialValue Initial value of the field of the target type when the target was created
     * @param currentValue Current value of the target
     * @param finalValue Target final value, value that is achieved by
     */
    public Target(String tName, Date completionDate, String tType, double initialValue, double currentValue, double finalValue){
        this.name  = new SimpleStringProperty(tName);
        this.type = new SimpleStringProperty(tType);
        this.initialValue = new SimpleDoubleProperty(initialValue);
        this.currentValue = new SimpleDoubleProperty(currentValue);
        this.finalValue = new SimpleDoubleProperty(finalValue);
        this.completionDate = completionDate;
        this.status = new SimpleStringProperty();
        this.progress = new SimpleDoubleProperty(calculateProgress(initialValue, currentValue, finalValue));

        // Generates the status string for the summary page
        generateStatus();
    }

    /**
     * Helper function to check the status of the target in context of the current dates relation to the target achieved
     * by field
     */
    private void generateStatus() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        Date today = cal.getTime();

        // Changes the status based on current date, target date and if the target value was reached
        if (this.progress.get() >= 1 || this.completed.get()) {
            status.set("Achieved");
            completed.set(true);
        } else if (today.after(completionDate)) {
            status.set("Not Achieved");
        } else {
            status.set("In Progress");
        }
    }

    /**
     * Updates the progress of the target with a new current value and updates the progress percentage
     * @param newCurrent New current value of the target
     */
    public void updateProgress(double newCurrent) {
        if (!this.completed.get()) {
            this.progress.set(calculateProgress(this.initialValue.get(), newCurrent, this.finalValue.get()));
            this.currentValue.set(newCurrent);

            // Generates a new status for the updated current
            generateStatus();

            try {
                // Updates target in the database
                TargetDBOperations.updateExistingTarget(this);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to calculate progress of a value from a given start and final value. Progress is a calculated by finding the
     * ratio of the differences between the current and start, with the final and start values
     * @param initialValue Starting value
     * @param currentValue Current value to calculate progress with
     * @param finalValue Final value
     * @return Double in [0,1] with progress
     */
    private double calculateProgress(double initialValue, double currentValue, double finalValue) {
        double completed = (currentValue - initialValue) / (finalValue - initialValue);
        if (completed <= 0) {
            return 0;
        } else if (completed >= 1) {
            return 1;
        }
        return completed;
    }

    /**
     * Used to display the type of target in the target list table, called by CellValueFactory during runtime
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

    /**
     * Used to display a formatted completion date in the target list table, called by CellValueFactory during runtime
     * @return Date which has been formatted into the correct format
     */
    public String getFormattedCompletionDate() {
        return new SimpleDateFormat("MMMM d, YYYY").format(this.completionDate);
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

    public String getStatus() {
        return status.get();
    }
}