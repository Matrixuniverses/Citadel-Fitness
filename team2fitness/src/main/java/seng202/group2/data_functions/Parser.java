package seng202.group2.data_functions;

import com.opencsv.CSVReader;
import seng202.group2.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

// TODO - Need to get multithreading working

/**
 * Parser designed to read a CSV file for activity data.
 */
public class Parser {
    private ArrayList<MalformedLine> malformedLines = new ArrayList<>();
    private ArrayList<Activity> activitiesRead = new ArrayList<>();
    private int user_id = -1;


    /**
     * Creates a new parser object, reading location and fitness information into Activities and Datapoints
     *
     * @param file Given file object to read data from
     * @throws FileFormatException If the passed file is not valid for reading
     */
    public Parser(File file) throws FileFormatException {
        try {
            if (file == null) {
                throw new FileFormatException("File is null");
            }

            FileReader readFile = new FileReader(file);
            CSVReader readCSV = new CSVReader(readFile);

            String name = file.getName();
            String extension = name.substring(name.lastIndexOf(".") + 1);

            if (!extension.equals("csv")) {
                throw new FileFormatException("Incorrect file format");
            }

            readLines(readCSV);
            generateMetrics(this.activitiesRead);
            // Needs to be written to after the user has validated their data?
            // databaseWrite(this.activitiesRead, );


        } catch (FileNotFoundException e) {
            throw new FileFormatException("File not found");
        } catch (IOException e) {
            throw new FileFormatException("Unreadable file");
        }

    }

    /**
     * Creates a new parser object, reading location and fitness information, tied to the passed user, into a database
     * @param file CSV file to read
     * @param user User to read the file into
     * @throws FileFormatException If passed file is not valid for reading
     */
    public Parser(File file, int user) throws FileFormatException {
        this(file);
        this.user_id = user;
        databaseWrite(this.activitiesRead, this.user_id);
    }

    /**
     * Creates a new Datapoint for each line, whilst checking the values are as expected
     *
     * @param line            Containing the CSV line to read data from
     * @param fields          Number of fields per line
     * @param currentActivity Current activity that the line should belong to
     * @return Datapoint containing parsed line, null if no line could be parsed
     */
    public DataPoint readLine(String line[], int fields, Activity currentActivity) {
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

            return new DataPoint(date, heart, lat, lon, alt);

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

        while ((line = readCSV.readNext()) != null) {
            if (line.length >= 2) {
                if (line[0] != null && line[0].equals("#start")) {
                    currentActivity = new Activity("Unnamed");
                    activitiesRead.add(currentActivity);

                    if (line[1] != null && !line[1].equals("")) {
                        currentActivity.setActivityName(line[1]);
                    }

                } else {
                    DataPoint point = readLine(line, fields, currentActivity);
                    if (point != null) {
                        currentActivity.addDataPoint(point);
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

    /**
     * Creates time, distance and speed metrics for each data point and activity
     */
    private static void generateMetrics(ArrayList<Activity> activities) {
        for (Activity activity : activities) {
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

    private static void databaseWrite(ArrayList<Activity> activities, int user_id) throws IllegalArgumentException {
        // Send data to database when the functionality is implemented
        try {
            if (user_id == -1) {
                throw new IllegalArgumentException("Cannot find user to parse activities to!");
            }

            DatabaseWriter.connectToDB();
            DatabaseWriter.createDatabase();

            for (Activity activity : activities) {
                int activityId = ActivityDBOperations.insertNewActivity(activity, user_id);
                for (DataPoint point : activity.getActivityData()) {
                    DatapointDBOperations.insertNewDataPoint(point, activityId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
     * @return True if line is valid, false if not
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
            Integer.parseInt(line[2]); // HeartRate check
            Double.parseDouble(line[3]); // Latitude check
            Double.parseDouble(line[4]); // Longitude check
            Double.parseDouble(line[5]); // Altitude check
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static void main(String[] args) {
        try {
            Parser testParser = new Parser(new File("team2fitness/src/test/java/seng202/group2/testData/latLongBroken.csv"), 1);

            ArrayList<Activity> test = testParser.getActivitiesRead();

            DatabaseWriter.createDatabase();
            UserDBOperations.insertNewUser(new User(1, "test", 24, 180, 80));
            ActivityDBOperations.insertNewActivity(test.get(0), 1);
            for (Activity activity : test) {
                System.out.println(activity.getActivityName());
                System.out.println(activity.getTotalDistance());
            }
        } catch (FileFormatException e) {
            System.out.println(new File(".").getAbsolutePath());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
