package seng202.group2.data;

import org.junit.Test;
import seng202.group2.data.DataParser;
import seng202.group2.data.MalformedLine;
import seng202.group2.model.Activity;

import java.io.File;
import java.util.ArrayList;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class ParserCSVErrorTests {
    private String testData = "src/test/java/seng202/group2/testData";

    @Test
    public void TestNoActivityDelimiterStartOfFile() {
        // There should only be one activity read, this checks that if no start delimiter exists then the following
        // datapoints are discarded until a start delimiter is found

        try {
            DataParser parser = new DataParser(new File(testData + "/noStartDelimiter.csv"));
            ArrayList<Activity> activities = parser.getActivitiesRead();

            assertEquals(1, activities.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception should occur");
        }
    }

    @Test
    public void TestIncorrectNumberOfFieldsInHeader() {
        // If there is an unexpected number of fields in the header, then the program should default to the normal
        // number
        try {
            DataParser parser = new DataParser(new File(testData + "/incorrectStartField.csv"));
            ArrayList<Activity> activities = parser.getActivitiesRead();
            assertEquals(2, activities.get(0).getActivityData().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception should occur");
        }
    }

    @Test
    public void TestIncorrectNumberOfFieldsInData() {
        try {
            DataParser parser = new DataParser(new File(testData + "/incorrectStartField.csv"));
            ArrayList<MalformedLine> malformed = parser.getMalformedLines();
            ArrayList<Activity> readActivity = parser.getActivitiesRead();
            assertEquals(3, malformed.size());
            assertEquals(4, readActivity.get(1).getActivityData().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception should occur");
        }
    }

    @Test
    public void TestIncorrectDateFormat() {
        try {
            DataParser parser = new DataParser(new File(testData + "/dateBroken.csv"));
            ArrayList<Activity> activities = parser.getActivitiesRead();
            ArrayList<MalformedLine> malforms = parser.getMalformedLines();
            assertEquals(4, malforms.size());
            assertEquals(1, activities.size()); // Still needs to create an activity
            assertEquals(3, activities.get(0).getActivityData().size());

        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception should occur");
        }
    }
}
