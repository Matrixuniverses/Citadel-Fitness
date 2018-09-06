package seng202.group2.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Target {

    private int id;
    private StringProperty name;
    private StringProperty dateAchieved;
    private StringProperty type;

    private DoubleProperty initialValue;
    private DoubleProperty currentValue;
    private DoubleProperty finalValue;

    private BooleanProperty completed = new SimpleBooleanProperty(false);

    public Target(String tName, String tType, Double tValue) {
        name = new SimpleStringProperty(tName);
        type = new SimpleStringProperty(tType);
        initialValue =  new SimpleDoubleProperty(tValue);
        currentValue =  new SimpleDoubleProperty(tValue);
    }

    public void updateProgress(DoubleProperty progress) {
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
