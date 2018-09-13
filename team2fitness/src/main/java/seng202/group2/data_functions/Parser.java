package seng202.group2.data_functions;

import com.opencsv.CSVReader;
import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

// TODO - Need to get multithreading working

/**
 * Parser designed to read a CSV file for activity data
 */
public class Parser {
    private ArrayList<String[]> malformedLines = new ArrayList<String[]>();
    private ArrayList<Activity> activitiesRead = new ArrayList<Activity>();

    /**
     * Creates a new parser object and reads location and fitness information from CSV file
     *
     * @param file Given file object to read data from
     * @throws FileFormatException If any error occurs in reading or parsing
     */
    public Parser(File file) throws FileFormatException {
        try {
            if (file == null) {
                throw new IllegalArgumentException("Passed file is null");
            } else {
                String name = file.getName();
                String extension = name.substring(name.lastIndexOf("."));
                if (!extension.equals("csv")) {
                    throw new FileFormatException(null, "Incorrect file format");
                }
            }

            FileReader readFile = new FileReader(file);
            CSVReader readCSV = new CSVReader(readFile);

            readLines(readCSV);
            generateMetrics();
            databaseWrite();

        } catch (FileNotFoundException e) {
            throw new FileFormatException(null, "File not found");
        } catch (IOException e) {
            throw new FileFormatException(null, "Unreadable file");
        }

    }


    public static DataPoint validPoint(String line[], int fields) throws FileFormatException {
        int lineLength = line.length;

        if (lineLength != fields) {
            throw new FileFormatException(line, "Line contains unexpected number of fields");
        }

        Date date = checkDateTimeFormat(line[0], line[1]);
        if (date == null) {
            throw new FileFormatException(line, "Line has invalid Date/ Time format");
        }

        try {
            int heart = Integer.parseInt(line[2]);
            double lat = Double.parseDouble(line[3]);
            double lon = Double.parseDouble(line[4]);
            double alt = Double.parseDouble(line[5]);

            return new DataPoint(date, heart, lat, lon, alt);

        } catch (NumberFormatException e) {
            throw new FileFormatException(line, "Line has invalid numerical format");
        }
    }


    /**
     * Reads lines contained in CSVReader object, and creates activities populated with data points
     * @param readCSV Object containing CSV file read from disk
     * @throws IOException         If unreadable file on disk
     * @throws FileFormatException If invalid line is encountered, allows controller to report line to user
     */
    private void readLines(CSVReader readCSV) throws IOException, FileFormatException {
        Activity currentActivity = new Activity("Unnamed");
        String[] line;
        int fields = 6;

        while ((line = readCSV.readNext()) != null) {

            if (line.length >= 2) {
                if (line[0] != null && line[0].equals("#start")) {
                    if (line[1] != null && !line[1].equals("")) {
                        currentActivity.setActivityName(line[1]);

                    } else {
                        currentActivity.setActivityName("Unnamed");
                    }
                    activitiesRead.add(currentActivity);

                } else {
                    DataPoint validLine = validPoint(line, fields);
                    currentActivity.addDataPoint(validLine);
                }
            }
        }
    }


    /**
     * Checks, given two string representing date and time, that the passed strings are of the correct CSV format
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

    /**
     * Creates time, distance and speed metrics for each data point and activity
     */
    private void generateMetrics() {
        for (Activity activity : activitiesRead) {
            ArrayList<DataPoint> points = activity.getActivityData();
            double totalDistance = 0;
            int totalTime = 0;

            if (points.size() >= 2) {
                for (int i = 1; i < points.size(); i++) {
                    double lat1 = points.get(i - 1).getLatitude();
                    double lon1 = points.get(i - 1).getLongitude();
                    double lat2 = points.get(i).getLatitude();
                    double lon2 = points.get(i).getLongitude();
                    double dist = DataAnalyzer.calcDistance(lat1, lon1, lat2, lon2);

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

    private void databaseWrite() {

    }


    public ArrayList<Activity> getActivitiesRead() {
        return activitiesRead;
    }


    public static void main(String[] args) {
        try {
            Parser testParser = new Parser(new File("team2fitness/src/main/java/seng202/group2/development_code/data/broken1.csv"));
            ArrayList<Activity> test = testParser.getActivitiesRead();

            for (Activity activity : test) {
                System.out.println(activity.getActivityName());
                System.out.println(activity.getTotalDistance());
            }
        } catch (FileFormatException e) {
            System.out.println(new File(".").getAbsolutePath());
            e.printStackTrace();
        }

    }
}
