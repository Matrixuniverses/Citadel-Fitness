package seng202.group2.data;

import com.opencsv.CSVReader;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.model.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * DataParser designed to read a CSV file for activity data.
 */
public class DataParser {
    private ArrayList<MalformedLine> malformedLines = new ArrayList<>();
    private ArrayList<Activity> activitiesRead = new ArrayList<>();

    /**
     * Creates a new parser object, reading location and fitness information into Activities and Datapoints
     *
     * @param file Given file object to read data from
     * @throws FileFormatException If the passed file is not valid for reading
     */
    public DataParser(File file) throws FileFormatException {
        try {
            if (file == null) {
                throw new FileFormatException("File is null");
            } else if (file.isDirectory()) {
                throw new FileFormatException("Please select a file");
            }

            FileReader readFile = new FileReader(file);
            CSVReader readCSV = new CSVReader(readFile);

            String name = file.getName();
            String extension = name.substring(name.lastIndexOf(".") + 1);

            // Checking if the extension is CSV, if not file parsing halted
            if (!extension.equals("csv")) {
                throw new FileFormatException("Incorrect file format");
            }

            // Reads lines from CSV and checks validity
            readLines(readCSV);

            // Updating the activities read by calculating distances for each datapoint and activity
            DataAnalyzer.calculateDeltas(this.activitiesRead);


        } catch (FileNotFoundException e) {
            throw new FileFormatException("File not found");
        } catch (IOException e) {
            throw new FileFormatException("Unreadable file");
        }
    }

    /**
     * Creates a new Datapoint for each line, whilst checking the values are as expected
     *
     * @param line Containing the CSV line to read data from
     * @param fields Number of fields per line
     * @param currentActivity Current activity that the line should belong to
     * @return Datapoint containing parsed line, null if no line could be parsed
     */
    private DataPoint readLine(String line[], int fields, Activity currentActivity) {
        int lineLength = line.length;
        String malformedMessage = "";

        //field num check
        if (lineLength != fields) {
            malformedMessage += "Unexpected number of Fields: Expected " + fields + ", got " + lineLength + "\n";
        }

        //Date format check
        Date date = checkDateTimeFormat(line[0], line[1]);
        if (date == null) {

            if (line[0].trim().equals("")) {
                line[0] = "No Value";
            }

            if (line[1].trim().equals("")) {
                line[1] = "No Value";
            }
            malformedMessage += "Date Format Error: Expected dd/MM/yyyy, HH:mm:ss format, got " + line[0] +  ", " + line[1] + " \n";

        }

        int heart = 0;
        double lat = 0.0, lon = 0.0, alt = 0.0;

        //Heart rate check
        try {
            heart = Integer.parseInt(line[2]);
            if (heart < 20 || heart > 300) {
                malformedMessage += "Unexpected Heart Rate: Expected heart rate between 20 and 300, got " + heart + "\n";
            }
        } catch (NumberFormatException e) {
            malformedMessage += "Heart Rate Format Error: Incorrect numerical format + \n";
        }

        //latitude check
        try {
            lat = Double.parseDouble(line[3]);
            if (lat < -90 || lat > 90) {
                malformedMessage += "Unexpected Latitude: Expected latitude between -90 and 90, got " + lat + "\n";
            }
        } catch (NumberFormatException e) {
            malformedMessage += "Latitude Format Error: Incorrect numerical format + \n";
        }

        //longitude check
        try {
            lon = Double.parseDouble(line[4]);
            if (lon < -180 || lon > 180) {
                malformedMessage += "Unexpected Longitude: Expected longitude between -180 and 180, got " + lon + "\n";
            }
        } catch (NumberFormatException e) {
            malformedMessage += "Longitude Format Error: Incorrect numerical format + \n";
        }

        //altitude check
        try {
            alt = Double.parseDouble(line[5]);
            if (alt < -1500 || alt > 9000) {
                malformedMessage += "Unexpected Altitude: Expected longitude between -1500 and 9000, got " + alt + "\n";
            }
        } catch (NumberFormatException e) {
            malformedMessage += "Altitude Format Error: Incorrect numerical format + \n";
        }

        //If nothing is added to the malformed Message then the datapoint is valid
        if (malformedMessage.length() == 0) {
            return new DataPoint(date, heart, lat, lon, alt);
        } else {
            malformedLines.add(new MalformedLine(line, currentActivity, malformedMessage));
            return null;
        }

    }


    /**
     * Reads lines contained in CSVReader object, and creates activities populated with data points. If there is no
     * 'activity start' delimiter '#start' then the following data points are discarded until an 'activity start'
     * delimiter is found
     *
     * @param readCSV Object containing CSV file read from disk
     * @throws IOException If unreadable file on disk
     */
    private void readLines(CSVReader readCSV) throws IOException {
        Activity currentActivity = new Activity("Unnamed");
        String[] line;
        int fields = 6;
        int totalHR = 0;
        int HRCounts = 0;

        // Iterate through all lines of the CSV
        while ((line = readCSV.readNext()) != null) {
            // Line length needs to be more than 2 in order to get an activity name
            if (line.length >= 2) {
                // Activity found
                if (line[0] != null && line[0].equals("#start")) {
                    currentActivity.setAverageHR((double) totalHR / HRCounts);
                    currentActivity = new Activity("Unnamed");
                    activitiesRead.add(currentActivity);
                    // Start the average heart rate calculation for the given activity
                    totalHR = 0;
                    HRCounts = 0;

                    // Unnamed activity found
                    if (line[1] != null && !line[1].equals("")) {
                        currentActivity.setActivityName(line[1]);
                    }

                    // Datapoint found
                } else {
                    // Reads a Datapoint line and checks for validity, if the line is invalid the returned point is null
                    // Continue iterating through the loop
                    DataPoint point = readLine(line, fields, currentActivity);
                    if (point != null) {
                        currentActivity.addDataPoint(point);
                        totalHR += point.getHeartRate();
                        HRCounts += 1;
                    }
                }
            }
        }
    }


    /**
     * Checks, given two string representing date and time, that the passed strings are of the correct format
     *
     * @param date Textual date
     * @param time Textual time
     * @return DateFormat object representing the current DateTime of the passed strings
     */
    private static Date checkDateTimeFormat(String date, String time) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy,HH:mm:ss", Locale.ENGLISH);
        try {
            return dateFormatter.parse(date + "," + time);
        } catch (ParseException e) {
            return null;
        }
    }


    public ArrayList<Activity> getActivitiesRead() {
        return this.activitiesRead;
    }

    public ArrayList<MalformedLine> getMalformedLines() {
        return this.malformedLines;
    }
}
