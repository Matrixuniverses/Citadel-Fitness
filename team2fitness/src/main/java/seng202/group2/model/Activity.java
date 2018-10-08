package seng202.group2.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.data.ActivityDBOperations;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is activity model class responsible for manipulating data required for activities
 * @author Seng202team2
 * @version 1.1
 */
public class Activity {
    private int id;
    private SimpleStringProperty activityName;
    private SimpleStringProperty activityType;
    private SimpleDoubleProperty totalTime;
    private SimpleDoubleProperty totalDistance;
    private SimpleDoubleProperty caloriesBurned;
    private SimpleDoubleProperty averageHR;
    private SimpleDoubleProperty vo2MAX;
    private Date activityDate;
    private ObservableList<DataPoint> activityData = FXCollections.observableArrayList();
    private boolean manualEntry = false;

    // Used for the import activities conformation screen
    private Image statusImage;
    private SimpleBooleanProperty checked = new SimpleBooleanProperty();

    /**
     * Initialises an activity with only name as input, this is used for the Dataparser where a blank activity has
     * items read into it. This is also used to read an activit into the database
     * @param activityName name of the activity
     */
    public Activity(String activityName) {
        this.activityName = new SimpleStringProperty(activityName);
        this.activityType = new SimpleStringProperty("Run");
        this.totalTime = new SimpleDoubleProperty(10);
        this.totalDistance = new SimpleDoubleProperty(0);
        this.caloriesBurned = new SimpleDoubleProperty(0);
        this.averageHR = new SimpleDoubleProperty(0);
        this.activityDate = new Date(0);
        this.vo2MAX = new SimpleDoubleProperty(0);

        // Change listener that recalculates the VO2MAX
        activityData.addListener(new ListChangeListener<DataPoint>() {
            @Override
            public void onChanged(Change<? extends DataPoint> c) {
                calcVo2Max();
            }
        });

        // Sets up database operations that are fired when a change in the activity data is detected
        setupDatabaseHandlers();
    }

    /**
     * This sets name, date, type, time and distance variables for an activity. This is used when inserting manual activities
     * @param activityName name of the activity
     * @param activityDate date of the activity
     * @param activityType type of the activity
     * @param totalTime total time of the activity
     * @param totalDistance total distance of the activity
     */
    public Activity(String activityName, Date activityDate, String activityType, Double totalTime, Double totalDistance) {
        this.activityName = new SimpleStringProperty(activityName);
        this.activityType = new SimpleStringProperty(activityType);
        this.activityDate = activityDate;
        this.totalTime = new SimpleDoubleProperty(Double.valueOf((new DecimalFormat("#.#")).format(totalTime)));
        this.totalDistance = new SimpleDoubleProperty(Double.valueOf((new DecimalFormat("#.##")).format(totalDistance)));
        this.averageHR = new SimpleDoubleProperty(0);
        this.caloriesBurned = new SimpleDoubleProperty(0);
        this.vo2MAX = new SimpleDoubleProperty(0);

        // Change listener that recalculates VO2MAX
        activityData.addListener(new ListChangeListener<DataPoint>() {
            @Override
            public void onChanged(Change<? extends DataPoint> c) {
                calcVo2Max();
            }
        });

        // Activities created using this constructor will always be manual
        manualEntry = true;

        // Creates the database handlers for updating database upon activity property change
        setupDatabaseHandlers();
    }


    /**
     * Helper function that creates event listeners to each of the editable activity properties that will update the
     * database when the property is changed. This method will assume that error checking has already been performed
     * by the controller classes, thus will always write to the database
     */
    private void setupDatabaseHandlers() {
        // Name is editable
        activityName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    ActivityDBOperations.updateExistingActivity(Activity.this);
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // Editable type
        activityType.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    ActivityDBOperations.updateExistingActivity(Activity.this);
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * Inputs a age value and a resting heart rate (beats per minute) and calculates an estimate of
     * the VO2 max based of these values.
     * @return An estimate value of the VO2 max based off the inputted values
     * @throws IllegalArgumentException if the restingHeartRate value is not greater than zero
     */
    public void calcVo2Max() {
        double max = getMaxHR();
        double min = getMinHR();
        if (min < 0) {
            throw new IllegalArgumentException("restingHeartRate must be greater than zero");
        }
        double maxHeartRate = 15 * (max/min);
        this.vo2MAX.set(maxHeartRate);
    }

    // IMPORTANT - These getters are used by the CellValueFactories created in TableViews during JavaFX RUNTIME. They
    // are not used in code, but deleting them will break all the custom table formats

    public SimpleBooleanProperty checkedProperty() {
        return checked;
    }

    /**
     * Returns nicely formatted date
     * @return Date in the format of: 'January 1, 1970'
     */
    public String getShortFormattedDate() {
        return new SimpleDateFormat("d-MM-YYYY").format(this.activityDate);
    }

    /**
     * Returns nicely formatted time separated into time quantities
     * @return Time in the format of: '1h 20m 30s'
     */
    public String getFormattedTotalTime() {
        double sec = this.totalTime.get();
        return String.format("%.0fh %.0fm %.0fs", sec / 3600, Math.floor((sec % 3600) / 60), (sec % 60));
    }

    /**
     * Returns nicely formatted distance, (m) for distances less than 1.5km (km) else
     * @return Distance in the format of: '400m' or '1.6km'
     */
    public String getFormattedTotalDistance() {
        if (this.totalDistance.get() >= 1500) {
            return String.format("%.2fkm", this.totalDistance.get() / 1e3);
        } else {
            return String.format("%.0fm", this.totalDistance.get());
        }
    }

    /**
     * Returns nicely formatted date
     * @return Date in the format of: 'January 1, 1970'
     */
    public String getFormattedDate() {
        return new SimpleDateFormat("MMMM d, YYYY").format(this.activityDate);
    }


    // Normal getters and setters

    /**
     * Sets the total time in seconds as formatted to 1dp
     * @param totalTime Time to be formatted and set
     */
    public void setTotalTime(int totalTime) {
        this.totalTime.set(Double.valueOf((new DecimalFormat("#.#")).format(totalTime)));
    }

    /**
     * Sets the total time in meters as formatted to 2dp
     * @param totalDistance Distance to be formatted and set
     */
    public void setTotalDistance(double totalDistance) {
        this.totalDistance.set(Double.valueOf((new DecimalFormat("#.##")).format(totalDistance)));
    }

    /**
     * This adds a data point to the activityData
     * @param toAdd data point waiting to be added to activityData
     */
    public void addDataPoint(DataPoint toAdd) {
        activityData.add(toAdd);
    }

    /**
     * This returns the activity name as a String
     * @return returns String activityName
     */
    public String getActivityName() {
        return activityName.get();
    }

    /**
     * This returns the activity name as a SimpleStringProperty
     * @return returns SimpleStringProperty activityName
     */
    public SimpleStringProperty activityNameProperty() {
        return activityName;
    }

    /**
     * This sets the name of the activity
     * @param activityName name of the activity
     */
    public void setActivityName(String activityName) {
        this.activityName.set(activityName);
    }

    /**
     * This returns the type of activity as a String
     * @return returns String activityType
     */
    public String getActivityType() {
        return activityType.get();
    }

    /**
     * This returns the type of activity as a SimpleStringProperty
     * @return returns SimpleStringProperty activityType
     */
    public SimpleStringProperty activityTypeProperty() {
        return activityType;
    }

    /**
     * This sets the type of the activity
     * @param activityType the type of activity
     */
    public void setActivityType(String activityType) {
        this.activityType.set(activityType);
    }

    /**
     * This returns the total time of aDouble.valueOf((new DecimalFormat("#.##")).format(totalDistance))n activity as a double
     * @return returns double totalTime
     */
    public double getTotalTime() {
        return totalTime.get();
    }

    /**
     * This returns the total time of an activity as a SimpleDoubleProperty
     * @return returns SimpleDoubleProperty totalTime
     */
    public SimpleDoubleProperty totalTimeProperty() {
        return totalTime;
    }

    public double getTotalDistance() {
        return totalDistance.get();
    }

    /**
     * This returns the total distance of the activity as a SimpleDoubleProperty
     * @return returns SimpleDoubleProperty totalDistance
     */
    public SimpleDoubleProperty totalDistanceProperty() {
        return totalDistance;
    }

    public double getCaloriesBurned() {
        return caloriesBurned.get();
    }

    /**
     * This returns the calories burned during the activity as a SimpleDoubleProperty
     * @return returns SimpleDoubleProperty caloriesBurned
     */
    public SimpleDoubleProperty caloriesBurnedProperty() {
        return caloriesBurned;
    }

    /**
     * This sets the caloriesBurned for the activity
     * @param caloriesBurned calories burned during the activity
     */
    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned.set(caloriesBurned);
    }

    /**
     * This returns the activityData as an arrayList of data points
     * @return returns ArrayList<DataPoint> activityData
     */
    public ObservableList<DataPoint> getActivityData() {
        return activityData;
    }

    /**
     * This returns the id of the activity
     * @return returns int id
     */
    public int getId() {
        return id;
    }

    /**
     * This sets the id of the activity
     * @param id id of the activity
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This returns the average heart rate over the activity as a double
     * @return returns double averageHR
     */
    public double getAverageHR() {
        return averageHR.get();
    }

    /**
     * This returns the average heart rate over the activity as a SimpleDoubleProperty
     * @return returns SimpleDoubleProperty averageHR
     */
    public SimpleDoubleProperty averageHRProperty() {
        return averageHR;
    }

    /**
     * This sets the average heart rate for the activity
     * @param averageHR average heart rate during the activity
     */
    public void setAverageHR(double averageHR) {
        this.averageHR.set(averageHR);
    }

    /**
     * This returns the date of the first element of activityData with the
     * data type Date if the activity was entered manually
     *
     * If the activity was imported then Date activityDate is returned
     * @return returns Date activityDate
     */
    public Date getDate() {
        if (!manualEntry) {
            activityDate = activityData.get(0).getDate();
            return activityData.get(0).getDate();
        } else {
            return activityDate;
        }
    }

    /**
     * Retrieves the minimum heart rate value from an an activities datapoints.
     * @return the minimum heart rate value (in Beats Per Minute).
     */
    public double getMinHR(){
        double min = 1000;
        for (int i = 0; i< activityData.size(); i++){
            if (activityData.get(i).getHeartRate() <= min){
                min = activityData.get(i).getHeartRate();
            }
        }
        return min;

    }

    /**
     * Retrieves the maximum heart rate value from an an activities datapoints.
     * @return the maximum heart rate value (in Beats Per Minute).
     */
    public double getMaxHR(){
        double max = 0;
        for (int i = 0; i < activityData.size(); i++){
            if (activityData.get(i).getHeartRate() >= max){
                max = activityData.get(i).getHeartRate();
            }
        }
        return max;
    }

    public void setManualEntry(boolean isManualEntry) {
        manualEntry = isManualEntry;
    }

    public boolean isManualEntry() {return manualEntry; }

    public SimpleDoubleProperty vo2MaxProperty(){
        return vo2MAX;
    }

    public Image getStatusImage() {
        return this.statusImage;
    }

    public void setStatusImage(Image statusImage) {
        this.statusImage = statusImage;
    }

    public Boolean getChecked() {
        return this.checked.get();
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }
}
