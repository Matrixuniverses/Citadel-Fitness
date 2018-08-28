package seng202.group2.controller;

import java.lang.Math;



public class DataAnalyzer {


    /*
    Helper private function to convert weight from Kilograms to Pounds.
    */
    private static double weight_Kg_to_Lbs(double weight) {
        return weight * 2.2046226218;
    }

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
        height *= 0.01; //convert cm into m
        return weight / Math.pow(height, 2.0);
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
     * @param age The age of the person in years     *
     * @param mileWalkTime The time taken for the person to walk a mile (1.6km) in minutes.
     * @param numHeartBeats The person's heart rate immediately after the walk in beats per minute.
     * @param isMale Set to true if the person is male
     * @return An estimate value of the VO2 max of the person based of the inputted values using the Rockpot Fitness
     * walking test.
     * @throws IllegalArgumentException if any of the values inputted are negative
     */
    public static double calcVO2Max_PFWT(double weight, int age, double mileWalkTime, int numHeartBeats, boolean isMale) {
        if (weight < 0 || age < 0 || mileWalkTime < 0 || numHeartBeats < 0) {
            throw new IllegalArgumentException("Inputted values to the calcVO2Max_PFWT function cannot be negative");
        }
        weight = weight_Kg_to_Lbs(weight);
        if (isMale) {
            return 132.853 - (0.0769 * weight) - (0.3877 * age) + 6.315 - (3.2649 * mileWalkTime) - (0.1565 * numHeartBeats);
        } else {
            return 132.853 - (0.0769 * weight) - (0.3877 * age) - (3.2649 * mileWalkTime) - (0.1565 * numHeartBeats);
        }
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
     * @throws IllegalArgumentException if any of the values inputted are negative
     */
    public static double calcCalories_Est(int age, double weight, double heartRate_Avg, double time, boolean isMale) {
        if (weight < 0 || age < 0 || heartRate_Avg < 0 || time < 0) {
            throw new IllegalArgumentException("Inputted values to the calcCalories_Est function cannot be negative");

        }
        weight = weight_Kg_to_Lbs(weight);
        if (isMale) {
            return (((age * 0.2017) - (weight * 0.09036) + (heartRate_Avg * 0.6309) - 55.0969) * time / 4.184);
        } else {
            return (((age * 0.074) - (weight * 0.05741) + (heartRate_Avg * 0.4472) - 20.4022) * time / 4.184);
        }
    }
}

