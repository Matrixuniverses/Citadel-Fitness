package seng202.group2.Data_Analysis;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Parser {
    private ArrayList<Activity> activitiesRead = new ArrayList<Activity>();

    public Parser(String filepath) {
        try {
            FileReader readFile = new FileReader(filepath);
            CSVReader readCSV = new CSVReader(readFile);

            String[] lines;

            while((lines = readCSV.readNext()) != null){
                // Reading into a blank activity in the case of there being data without an activity title
                Activity readableActivity = new Activity("Unnamed");

                if (lines[0].equals("#start")){
                    readableActivity.setActivityTitle(lines[1]);
                    activitiesRead.add(readableActivity);
                    lines = readCSV.readNext();
                }

                String dateString = lines[0] + "," + lines[1];
                int heart = Integer.parseInt(lines[2]);
                double lat = Double.parseDouble(lines[3]);
                double lon = Double.parseDouble(lines[4]);
                double alt = Double.parseDouble(lines[5]);

                readableActivity.addDataPoint(new DataPoint(dateString, heart, lat, lon, alt));
            }


        } catch (FileNotFoundException notFound) {
                System.err.println(notFound);
        } catch (IOException ioExcept) {
                System.err.println(ioExcept);
        }

    }

    public ArrayList<Activity> getList(){
        return activitiesRead;
    }

    /* This was purely for testing purposes, DO NOT USE IN PRODUCTION
    public static void main(String[] args) {
        Parser testParser = new Parser("/home/cosc/student/sjs227/Uni/SENG202/seng202group2/team2fitness/seng202_2018_example_data.csv");
        ArrayList<Activity> test = testParser.getList();

        for (Activity activity : test){
            System.out.println(activity.getName());
        }

    }
    */
}
