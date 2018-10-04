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
    private ArrayList<Activity> activitiesRead;

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

            //TODO - Look at this (might not need to ignore all files of the incorrect format)
            if (!extension.equals("csv")) {
                throw new FileFormatException("Incorrect file format");
            }

            this.activitiesRead = new ArrayList<>();
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

        if (lineLength != fields) {
            malformedLines.add(new MalformedLine(line, currentActivity, "Unexpected number of fields"));
            return null;
        }

        Date date = checkDateTimeFormat(line[0], line[1]);
        if (date == null) {
            malformedLines.add(new MalformedLine(line, currentActivity, "Incorrect date format"));
            return null;
        }

        try {
            int heart = Integer.parseInt(line[2]);
            double lat = Double.parseDouble(line[3]);
            double lon = Double.parseDouble(line[4]);
            double alt = Double.parseDouble(line[5]);

            if (lat >= -90 && lat <= 90 && lon >= -180 && lat <= 180) {
                return new DataPoint(date, heart, lat, lon, alt);
            } else {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            malformedLines.add(new MalformedLine(line, currentActivity, "Incorrect numerical format"));
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

        while ((line = readCSV.readNext()) != null) {
            if (line.length >= 2) {
                if (line[0] != null && line[0].equals("#start")) {
                    currentActivity.setAverageHR((double) totalHR / HRCounts);
                    currentActivity = new Activity("Unnamed");
                    activitiesRead.add(currentActivity);
                    totalHR = 0;
                    HRCounts = 0;

                    if (line[1] != null && !line[1].equals("")) {
                        currentActivity.setActivityName(line[1]);
                    }

                } else {
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

    /**
     * Checks a passed line to see if it is valid or not
     *
     * @param line Line to be checked
     * @return True if line is valid, False if not
     */
    public static boolean isValidLine(String[] line) {
        int lineLength = line.length;

        // Default number of fields that should be contained in the line
        if (lineLength != 6) {
            return false;
        }

        // Checks date
        Date date = checkDateTimeFormat(line[0], line[1]);
        if (date == null) {
            return false;
        }

        // Checks only the data type of the line fields
        try {
            // The return values of these variables is not used, they are dummy calls designed to throw an
            // exception upon an invalid value being passed
            Integer.parseInt(line[2]); // HeartRate check
            Double.parseDouble(line[3]); // Latitude check
            Double.parseDouble(line[4]); // Longitude check
            Double.parseDouble(line[5]); // Altitude check
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
