package seng202.group2.analysis;

import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;

import java.lang.Math;
import java.awt.Desktop;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Collection of methods to calculate fitness metrics
 */
public class DataAnalyzer {

    public static void calculateDeltas(ArrayList<Activity> activitiesToUpdate) {
        for (Activity activity : activitiesToUpdate) {
            ArrayList<DataPoint> points = activity.getActivityData();
            double totalDistance = 0;
            int totalTime = 0;

            if (points.size() >= 2) {
                for (int i = 1; i < points.size(); i++) {
                    double lat1 = points.get(i - 1).getLatitude();
                    double lon1 = points.get(i - 1).getLongitude();
                    double lat2 = points.get(i).getLatitude();
                    double lon2 = points.get(i).getLongitude();
                    double dist = calcDistance(lat1, lon1, lat2, lon2);

                    Date time1 = points.get(i - 1).getDate();
                    Date time2 = points.get(i).getDate();

                    long milliDiff = time2.getTime() - time1.getTime();
                    long time = TimeUnit.SECONDS.convert(milliDiff, TimeUnit.MILLISECONDS);

                    points.get(i).setDistanceDelta(dist);
                    points.get(i).setTimeDelta(time);

                    totalDistance += dist;
                    totalTime += time;
                }

                activity.setTotalDistance(totalDistance);
                activity.setTotalTime(totalTime);
            }

        }
    }

    /**
     * Calculates the Haversine Distance between two WSG84 geodetic points
     * @param latStart - Latitude of the start point
     * @param longStart - Longitude of the start point
     * @param latEnd - Latitude of the end point
     * @param longEnd - Longitude of the end point
     * @return Distance in meters between points
     */
    public static double calcDistance(double latStart, double longStart, double latEnd, double longEnd) {
        double earthRad = 6378.137; // Radius of earth in KM
        double diffLat = latEnd * Math.PI / 180 - latStart * Math.PI / 180;
        double diffLong = longEnd * Math.PI / 180 - longStart * Math.PI / 180;
        double sineResult = Math.sin(diffLat/2) * Math.sin(diffLat/2) + Math.cos(latStart * Math.PI / 180) *
                Math.cos(latEnd * Math.PI / 180) * Math.sin(diffLong/2) * Math.sin(diffLong/2);
        double result = 2 * Math.atan2(Math.sqrt(sineResult), Math.sqrt(1-sineResult));

        return 1000 * earthRad * result;
    }

    /**
     * Takes the distance between two points as well as a time value for each point and calculates the average speed
     * between them.
     * @param distance Distance between two points in metres
     * @param timeStart Time value at the start point in seconds
     * @param timeEnd Time value at the end point in seconds
     * @return The average speed between the start and end points in m/s
     * @throws IllegalArgumentException if the start and end times are equal
     */
    public static double calcAverageSpeed(double distance, double timeStart, double timeEnd) {
        double timeTaken = timeEnd - timeStart;
        if (timeTaken == 0.0) {
            throw new IllegalArgumentException("timeStart value cannot equal timeEnd value");
        }

        return distance / timeTaken;
    }

    /**
     * Helper function takes weight in kg's and returns value in pounds. Used by other dataAnalyzer functions.
     * @param weight Weight in KG
     * @return Weight in Pounds
     */
    private static double weightKgToLbs(double weight) {
        return weight * 2.2046226218;
    }

    /**
     * Inputs a height value in cm and a weight value in kgs and returns the calculated
     * Body Mass Index as a double.
     * @param height The height of the person in cm
     * @param weight The weight of the person in kg
     * @return The body mass index based on the two inputted values in km/m^2
     * @throws IllegalArgumentException if the value for height is equal to zero
     */
    public static double calcBMI(double height, double weight) {
        if (height == 0.0) {
            throw new IllegalArgumentException("height value cannot equal zero");
        }
        height *= 0.01; //convert cm into m
        return weight / Math.pow(height, 2.0);
    }

    /**
     * Inputs a age value and a resting heart rate (beats per minute) and calculates an estimate of
     * the VO2 max based of these values.
     * @param age The age of the person in years
     * @param restingHeartRate The resting heart rate of the person in beats per minute
     * @return An estimate value of the VO2 max based off the inputted values
     * @throws IllegalArgumentException if the restingHeartRate value is not greater than zero
     */
    public static double calcVo2Max(int age, int restingHeartRate) {
        if (restingHeartRate <= 0) {
            throw new IllegalArgumentException("restingHeartRate must be greater than zero");
        }
        double maxHeartRate = 208 - (0.7 * age);
        return 15.3 * (maxHeartRate/restingHeartRate);
    }

    public static double calcAverageHR(Activity activity) {
        int totalHR = 0;
        int count = 0;

        for (DataPoint point : activity.getActivityData()) {
            totalHR += point.getHeartRate();
            count += 1;
        }

        return (double)totalHR / count;
    }

    /**
     * Implements a function that calculates a general estimate of the amount of calories burned during physical exercise
     * based on a persons age, weight, average heard rate while exercising, time spent exercising and their gender. The
     * function is based of the equation supplied from The Journal of Sports Sciences.
     * @see <a href="http://fitnowtraining.com/2012/01/formula-for-calories-burned/">http://fitnowtraining.com/2012/01/formula-for-calories-burned/</a>
     * @param age The age of the person in years
     * @param weight The weight of the person in Kg
     * @param heartRate_Avg The average heart rate of the person during the physical exercise
     * @param time The time the person spent exercising
     * @param isMale Set to true if the person is male
     * @return An estimate value of the amount of Calories burned by the person during the physical exercise
     * @throws IllegalArgumentException if the resulted calories burned is a negative value
     */
    public static double calcCalories(int age, double weight, double heartRate_Avg, double time, boolean isMale, double height) {
        double result;
        int mets = 590;

        weight = weightKgToLbs(weight);
        if (isMale) {
            result = 66.5 + (13.75 * weight) + (5.003 * height)-(6.775 * age);
            result = (result * mets)/24;
            result = (result * time)/60;
            result = result / 100;
            //result = (((age * 0.2017) - (weight * 0.09036) + (heartRate_Avg * 0.6309) - 55.0969) * time / 4.184);
        } else {
            result = 655.1 + (9.563 * weight) + (1.85 * height)-(4.676 * age);
            result = (result * mets)/24;
            result = (result * time)/60;
            result = result / 100;
            //result = (((age * 0.074) - (weight * 0.05741) + (heartRate_Avg * 0.4472) - 20.4022) * time / 4.184);
        }
        if (result < 0) {
            throw new IllegalArgumentException("Calories burned cannot be negative");
        }

        return result;
    }

    /**
     * Implements a function that calculates if a user has Tachycardia based on a persons age and resting heart rate.
     * @param age The age of the person in years.
     * @param restingHR The resting heart rate of a person in beats per minute.
     * @return A boolean value, true if person has Tachycardia, false if person does not have Tachycardia.
     */
    public static boolean hasTachycardia(int age, int restingHR) {
        if (age > 15 && restingHR > 100 || age >= 12 && restingHR > 119 || age >= 8 && restingHR > 130 ||
                age >= 5 && restingHR > 133 || age < 5 && restingHR > 137) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Implements a function that calculates if a user has Bradycardia based on a persons age and resting heart rate.
     * @param age The age of the person in years.
     * @param restingHR The resting heart rate of a person in beats per minute.
     * @return A boolean value, true if person has Bradycardia, false if person does not have Bradycardia.
     */
    public static boolean hasBradycardia(int age, int restingHR) {
        if (age < 15 && restingHR <= 60 || age < 18 && restingHR <= 55 || age >= 18 && restingHR <= 50) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Implements a function which checks if a user is at risk of cardiovascular mortality health issues.
     * @param age The age of the user in years.
     * @param restingHR The resting heart rate of the user in beats per minute.
     * @return A boolean value, true if the user is at risk, false if the user is not at risk.
     */
    public static boolean cardiovascularMortalityProne(int age, int restingHR) {
        if (age >= 18 && restingHR > 83) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Helper Function which generates a valid search term substring for the generated Google Search URL
     * @param searchTerm the inputted search term as a String
     * @return A valid URL substring
     * @throws IllegalArgumentException if a invalid character is found in the inputted search Term
     */
    private static String genValidURLSearchTerm(String searchTerm) {

        ArrayList<Integer> formatPositions = new ArrayList<>();
        for (int i = 0; i < searchTerm.length(); i++) {
            if (!(Character.isDigit(searchTerm.charAt(i)) || Character.isLetter(searchTerm.charAt(i)))) {

                String strChr = Character.toString(searchTerm.charAt(i));
                if (!(strChr.equals(" "))) {
                    throw new IllegalArgumentException("Search term contains the unexpected character " + strChr);

                } else {
                    formatPositions.add(i);
                }

            }
        }
        String validURLSearchTerm;

        if (formatPositions.size() > 0) {

            StringBuffer sTBuff = new StringBuffer(searchTerm);
            for (int j : formatPositions) {
                if (Character.toString(searchTerm.charAt(j)).equals(" ")) {
                    sTBuff.replace(j,j+1,"+");
                }

            }
            validURLSearchTerm = sTBuff.toString();

        } else {
            validURLSearchTerm = searchTerm;
        }

        return validURLSearchTerm;
    }

    /**
     * Uses the java Desktop API to open up the google search results for an inputted term in the default web browser
     * of the system that the application is running on. If the helper function throws an IllegalArgumentException the
     * opens up the google search page and prints out a message to acknowledge the error.
     * @param searchTerm The text that the google search results will be for.     *
     */
    public static void webSearch_Google(String searchTerm) {

        String googleURL;

        try {
            googleURL = "https://www.google.com/search?q=" + genValidURLSearchTerm(searchTerm);
        } catch (IllegalArgumentException e) {
            //print error message and open up google search
            System.out.println("Found invalid character in searchTerm: " + searchTerm);
            System.out.println("Cannot create Google Search URL correctly");
            googleURL = "https://www.google.com/search?q=";
        }

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(googleURL));
            } else {
                System.out.println("Java desktop integration is not supported on the current operating system.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
