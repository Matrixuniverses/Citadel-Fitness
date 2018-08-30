package seng202.group2.Data_Analysis;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Parser {
    private ArrayList<String[]> malformedLines = new ArrayList<String[]>();
    private ArrayList<Activity> activitiesRead = new ArrayList<Activity>();

    private Date checkDateTimeFormat(String date, String time) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy,HH:mm:ss", Locale.ENGLISH);

        try {
            Date pointDate = dateFormatter.parse(date + "," + time);
            return pointDate;
        } catch (ParseException e) {
            return null;
        }
    }


    public Parser(String filepath) throws FileFormatException, IOException {
        try {
            FileReader readFile = new FileReader(filepath);
            CSVReader readCSV = new CSVReader(readFile);

            String[] line;

            while((line = readCSV.readNext()) != null) {

                Activity readableActivity = new Activity("Unnamed");

                if (line[0].equals("#start") && !line[1].equals("")) {
                    readableActivity.setActivityTitle(line[1]);
                    activitiesRead.add(readableActivity);
                    line = readCSV.readNext();

                } else if (line[0].equals("#start")) {
                    activitiesRead.add(readableActivity);
                    line = readCSV.readNext();
                }

                String dateString = line[0] + "," + line[1];
                int heart = Integer.parseInt(line[2]);
                double lat = Double.parseDouble(line[3]);
                double lon = Double.parseDouble(line[4]);
                double alt = Double.parseDouble(line[5]);

                readableActivity.addDataPoint(new DataPoint(dateString, heart, lat, lon, alt));
            }

        } catch (IOException ioexcept) {
                System.err.println("IO Exception occurred");
                throw ioexcept;
        }

    }

    public ArrayList<Activity> getActivitiesRead(){
        return activitiesRead;
    }


    public static void main(String[] args) {
        try{
            Parser testParser = new Parser("/home/cosc/student/sjs227/Uni/SENG202/seng202group2/team2fitness/seng202_2018_example_data.csv");
            ArrayList<Activity> test = testParser.getActivitiesRead();

            for (Activity activity : test){
                System.out.println(activity.getName());
            }
        } catch (FileFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Unreadable file");
        }

    }
}
