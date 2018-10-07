package seng202.group2.model;

import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import seng202.group2.data.DatabaseOperations;
import seng202.group2.data.UserDBOperations;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ActivityTest {

    User user;
    User user1;
    User user2;
    Activity activity2;
    Activity activity3;
    Activity activity1;
    Activity activity4;

    private static final String testDBURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessTestingDatabase.db";

    @Before
    public void setup() throws Exception {


        user = new User(1, "Adam", 20, 180, 80, "Male");
        user1 = new User(2, "AdamClone", 20, 180, 80, "Male");
        user2 = new User(3, "AdamClone2", 20, 180, 80, "Male");

        Instant dateNow = Instant.now();
        activity1 = new Activity("Activity1", Date.from(dateNow), "Run", 100.0, 15.0);
        activity2 = new Activity("Activity2", Date.from(dateNow.minus(Duration.ofDays(20))), "Walk", 70.0, 11.0);
        activity3 = new Activity("Activity3", Date.from(dateNow.minus(Duration.ofDays(5))), "Cycle", 70.0, 9.0);
        activity4 = new Activity("manual1", Date.from(dateNow), "Run", 100.0, 15.0);


        DataPoint dp1 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(10))), 170, 10.0, 10.0, 100.0);
        DataPoint dp2 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(20))), 160, 9.9, 10.0, 100.0);
        DataPoint dp3 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(30))), 167, 9.8, 10.0, 99.0);
        DataPoint dp4 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(40))), 167, 9.76, 10.0, 98.0);
        DataPoint dp5 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(10))), 5, 9.72, 10.0, 99.0);
        DataPoint dp6 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(50))), 100, 9.72, 10.0, 99.0);

        activity2.getActivityData().addAll(dp1,dp2);
        activity3.getActivityData().addAll(dp3,dp4);
        activity1.getActivityData().addAll(dp5, dp6);



        DatabaseOperations.setDatabaseURL(testDBURL);
        DatabaseOperations.createDatabase();

        UserDBOperations.insertNewUser(user);
        UserDBOperations.insertNewUser(user1);
        UserDBOperations.insertNewUser(user2);
        user.addActivity(activity1);
        user.addActivity(activity2);
        user.addActivity(activity3);
    }

    @Test
    public void calcVo2MaxReturnsCorrectResult1() {
        double vo2Max;
        activity2.calcVo2Max();
        vo2Max = activity2.vo2MaxProperty().getValue();
        assertEquals(15.93, vo2Max,.01);
    }

    @Test
    public void calcVo2MaxReturnsCorrectResult2() {
        double vo2Max;
        activity1.calcVo2Max();
        vo2Max = activity1.vo2MaxProperty().getValue();
        assertEquals(300, vo2Max, .01);
    }

    /**
     * Tests function does not break when heartrate is constant
     */
    @Test
    public void calcVo2MaxReturnsCorrectResult3() {
        double vo2Max;
        activity3.calcVo2Max();
        vo2Max = activity3.vo2MaxProperty().getValue();
        assertEquals(15, vo2Max, .01);
    }

}