package seng202.group2.data;

import org.junit.Test;
import seng202.group2.analysis.DataAnalyzer;
import seng202.group2.data.DataParser;
import seng202.group2.model.Activity;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.apache.commons.collections.CollectionUtils.size;

public class ParserTest {
    private String testData = "src/test/java/seng202/group2/testData";

    @Test
    public void activityTitleCorrect1() {
        try {
            DataParser testParser = new DataParser(new File(testData + "/blockrun.csv"));
            ArrayList<Activity> test = testParser.getActivitiesRead();
            assertEquals("Run around the block", test.get(0).getActivityName());

        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not throw exception.");
        }
    }


    @Test
    public void activityTitleCorrect2() {
        try {
            DataParser testParser = new DataParser(new File(testData + "/woodswalk.csv"));
            ArrayList<Activity> test = testParser.getActivitiesRead();
            assertEquals("Walk in the woods", test.get(0).getActivityName());

        } catch (Exception e) {
            fail("Should not throw exception.");
        }
    }

    @Test
    public void activityTitleCorrect3() {
        try {
            DataParser testParser = new DataParser(new File(testData + "/all.csv"));
            ArrayList<Activity> test = testParser.getActivitiesRead();
            assertEquals("Walk with dog", test.get(4).getActivityName());

        } catch (Exception e) {
            fail("Should not throw exception.");
        }
    }

    @Test
    public void activityTitleCorrect4() {
        try {
            DataParser testParser = new DataParser(new File(testData + "/all.csv"));
            ArrayList<Activity> test = testParser.getActivitiesRead();
            assertEquals("Hike in the mountains", test.get(5).getActivityName());

        } catch (Exception e) {
            fail("Should not throw exception.");
        }
    }

    @Test
    public void activityTotalDistanceCorrect1() {
        try {
            DataParser testParser = new DataParser(new File(testData + "/blockrun.csv"));
            double distance = 0.0;

            distance += DataAnalyzer.calcDistance(30.245576, -97.823843, 30.246356, -97.823326);
            distance += DataAnalyzer.calcDistance(30.246356, -97.823326, 30.246539, -97.821931);
            distance += DataAnalyzer.calcDistance(30.246539, -97.821931, 30.247105, -97.821064);
            distance += DataAnalyzer.calcDistance(30.247105, -97.821064, 30.247719, -97.820641);
            distance += DataAnalyzer.calcDistance(30.247719, -97.820641, 30.248482, -97.820708);
            distance += DataAnalyzer.calcDistance(30.248482, -97.820708, 30.24915, -97.820722);

            ArrayList<Activity> test = testParser.getActivitiesRead();
            assertEquals(distance, test.get(0).getTotalDistance(), 0.01);
        } catch (Exception e) {
            fail("Should not throw exception.");
        }
    }

    @Test
    public void activityTotalDistanceCorrect2() {
        try {
            DataParser testParser = new DataParser(new File(testData + "/all.csv"));
            double distance = 0.0;

            distance += DataAnalyzer.calcDistance(30.24913327, -97.82023906, 30.24912858, -97.82021148);
            distance += DataAnalyzer.calcDistance(30.24912858, -97.82021148, 30.24918893, -97.82028189);
            distance += DataAnalyzer.calcDistance(30.24918893, -97.82028189, 30.24925263, -97.82029614);
            distance += DataAnalyzer.calcDistance(30.24925263, -97.82029614, 30.24929379, -97.82031097);
            ArrayList<Activity> test = testParser.getActivitiesRead();
            assertEquals(distance, test.get(6).getTotalDistance(), 0.01);
        } catch (Exception e) {
            fail("Should not throw exception.");
        }
    }

    @Test
    public void activityCountCorrect1() {
        try {
            DataParser testParser = new DataParser(new File(testData + "/all.csv"));
            ArrayList<Activity> test = testParser.getActivitiesRead();
            assertEquals(12, size(test));

        } catch (Exception e) {
            fail("Should not throw exception.");
        }
    }
}
