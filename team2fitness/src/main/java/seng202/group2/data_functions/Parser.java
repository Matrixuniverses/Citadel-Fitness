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


public class Parser {
    private ArrayList<String[]> malformedLines = new ArrayList<String[]>();
    private ArrayList<Activity> activitiesRead = new ArrayList<Activity>();
    private Activity currentActivity;

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
     * Calculates the Haversine distance between two given WSG84 points
     *
     * @param latitude1  Latitude of first point
     * @param latitude2  Latitude of second point
     * @param longitude1 Longitude of first point
     * @param longitude2 Logitude of second point
     * @return The distance between the two given points in meters
     */
    private double haversineDistance(double latitude1, double latitude2, double longitude1, double longitude2) {
        final double radius = 6.3781 * Math.pow(10, 6);

        double deltaLat = Math.toRadians(latitude2 - latitude1);
        double deltaLon = Math.toRadians(longitude2 - longitude1);

        double hav = Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(latitude1) * Math.cos(latitude2);
        double invHav = 2 * Math.asin(Math.sqrt(hav));

        return invHav * radius;
    }

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
                    double dist = haversineDistance(lat1, lat2, lon1, lon2);

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


    public Parser(File file) throws FileFormatException {
        try {
            FileReader readFile = new FileReader(file);
            CSVReader readCSV = new CSVReader(readFile);

            String[] line;

            while ((line = readCSV.readNext()) != null) {

                if (line[0].equals("#start") && !line[1].equals("")) {
                    currentActivity = new Activity(line[1]);
                    activitiesRead.add(currentActivity);
                    line = readCSV.readNext();

                } else if (line[0].equals("#start")) {
                    currentActivity = new Activity("Unnamed");
                    activitiesRead.add(currentActivity);
                    line = readCSV.readNext();
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

            generateMetrics();

        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new FileFormatException(null, "File not found");
            } else {
                throw new FileFormatException(null, "Unreadable file");
            }

        }

    }

    public ArrayList<Activity> getActivitiesRead() {
        return activitiesRead;
    }
}


/*    public static void main(String[] args) {
        try{
            Parser testParser = new Parser("C:\\Users\\Sam Shankland\\IdeaProjects\\seng202group2\\team2fitness\\src\\main\\java\\seng202\\group2\\development_code\\data\\all.csv");
            ArrayList<Activity> test = testParser.getActivitiesRead();

            for (Activity activity : test){
                System.out.println(activity.getActivityName());
                System.out.println(activity.getTotalDistance());
            }
        } catch (FileFormatException e){
            e.printStackTrace();
        }

    }
}*/
