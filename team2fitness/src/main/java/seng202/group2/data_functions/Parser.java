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
    private Activity currentActivity;

    /**
     * Creates a new parser object and reads location and fitness information from CSV file
     *
     * @param file Given file object to read data from
     * @throws FileFormatException If any error occurs in reading or parsing
     */
    public Parser(File file) throws FileFormatException {
        try {
            FileReader readFile = new FileReader(file);
            CSVReader readCSV = new CSVReader(readFile);

            readLines(readCSV);
            generateMetrics();
            databaseWrite();

        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new FileFormatException(null, "File not found");
            } else {
                throw new FileFormatException(null, "Unreadable file");
            }

        }

    }


    private void readLine(String line[]) {

    }


    /**
     * Reads each line and creates an activity, filled with raw data
     *
     * @param readCSV Object containing CSV file read from disk
     * @throws IOException         If unreadable file on disk
     * @throws FileFormatException If invalid line is encountered, allows controller to report line to user
     */
    private void readLines(CSVReader readCSV) throws IOException, FileFormatException {
        String[] line;

        while ((line = readCSV.readNext()) != null) {
            int lineLen = line.length;

            if (lineLen == 0) {
                // Blank line
                continue;
            } else if (lineLen != 6) {
                throw new FileFormatException(line, "Line contains unexpected data fields");
            }

            if (line[0].equals("#start")) {
                currentActivity = new Activity("Unnamed");
                if (!line[1].equals("")) {
                    currentActivity.setActivityName(line[1]);
                }
                activitiesRead.add(currentActivity);
                continue;
            }

            Date pointDate = checkDateTimeFormat(line[0], line[1]);
            if (pointDate == null) {
                malformedLines.add(line);
                throw new FileFormatException(line, "Incorrect date format");
            }

            try {
                int heart = Integer.parseInt(line[2]);
                double lat = Double.parseDouble(line[3]);
                double lon = Double.parseDouble(line[4]);
                double alt = Double.parseDouble(line[5]);

                currentActivity.addDataPoint(new DataPoint(pointDate, heart, lat, lon, alt));

            } catch (NumberFormatException e) {
                throw new FileFormatException(line, "Invalid numerical input");
            }
        }

    }

    /**
     * Checks, given two string representing date and time, that the passed strings are of the correct CSV format
     *
     * @param date Textual date
     * @param time Textual time
     * @return DateFormat object representing the current DateTime of the passed strings
     */
    private Date checkDateTimeFormat(String date, String time) {
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
            Parser testParser = new Parser(new File("seng202group2/team2fitness/src/main/java/seng202/group2/development_code/data/malformedData.csv"));
            ArrayList<Activity> test = testParser.getActivitiesRead();

            for (Activity activity : test) {
                System.out.println(activity.getActivityName());
                System.out.println(activity.getTotalDistance());
            }
        } catch (FileFormatException e) {
            e.printStackTrace();
        }

    }
}
