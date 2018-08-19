package seng202.group2.Data_Analysis;

import java.lang.Math;



public class DataAnalyzer {

    /**
     * Inputs a height value in cm and a weight value in kgs and returns the calculated
     * Body Mass Index as a double.
     * @param height - The height of the person in cm
     * @param weight - The weight of the person in kg
     * @return The body mass index based on the two inputted values in km/m^2
     * @throws IllegalArgumentException if the values for height or weight are negative
     */
    public static double calcBMI(double height, double weight) {
        if (height < 0 || weight < 0) {
            throw new IllegalArgumentException("Height and weight values cannot be negative");
        }
        return weight / Math.pow(height, 2);
    }

    /**
     * Inputs a age value and a resting heart rate (beats per minute) and calculates an estimate of
     * the VO2 max based of these values.
     * @param age The age of the person in years
     * @param restingHeartRate The resting heart rate of the person in beats per minute
     * @return An estimate value of the VO2 max based off the inputted values
     * @throws IllegalArgumentException if the values for age and restingHeartRate are negative
     */
    public static double calcVO2Max_RestingHeartRate(int age, int restingHeartRate) {
        if (age < 0 || restingHeartRate < 0) {
            throw new IllegalArgumentException("age and restingHeartRate values cannot be negative");
        }
        double maxHeartRate = 208 - (0.7 * age);
        return 15.3 * (maxHeartRate/restingHeartRate);
    }

    /**
     * Implements the Rockport Fitness Walking Test for calculating an estimate of a person's VO2 max.
     * @param weight The weight of the person in kg
     * @param age The age of the person in years
     * @param isMale Set to true if the person is male
     * @param mileWalkTime The time taken for the person to walk a mile (1.6km) in minutes.
     * @param numHeartBeats The person's heart rate immediately after the walk in beats per minute.
     * @return An estimate value of the VO2 max of the person based of the inputted values using the Rockpot Fitness
     * walking test.
     * @throws IllegalArgumentException if any of the values inputted are negative
     */
    public static double calcVO2Max_PFWT(double weight, int age, boolean isMale, double mileWalkTime, int numHeartBeats) {
        if (weight < 0 || age < 0 || mileWalkTime < 0 || numHeartBeats < 0) {
            throw new IllegalArgumentException("Inputted values to the calcVO2Max_PFWT function cannot be negative");
        }
        weight = 2.2046226218 * weight; //convert weight to lbs
        if (isMale) {
            return 132.853 - (0.0769 * weight) - (0.3877 * age) + 6.315 - (3.2649 * mileWalkTime) - (0.1565 * numHeartBeats);
        } else {
            return 132.853 - (0.0769 * weight) - (0.3877 * age) - (3.2649 * mileWalkTime) - (0.1565 * numHeartBeats);
        }
    }
}


