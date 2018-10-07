package seng202.group2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import seng202.group2.data.ActivityDBOperations;
import seng202.group2.data.DatabaseOperations;
import seng202.group2.data.UserDBOperations;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;




public class UserTest {

    User user;
    User userClone;
    User userClone2;
    User userClone3;
    User userClone4;

    private static final String testDBURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessTestingDatabase.db";

    @Before public void setup() throws Exception{



        user = new User(1, "Adam", 20, 180, 80, "Male");
        userClone = new User(2, "AdamClone", 20, 180, 80, "Male");
        userClone2 = new User(3, "AdamClone2", 20, 180, 80, "Male");
        userClone3 = new User(4, "AdamClone3", 20, 180, 80, "Male");
        userClone4 = new User("AdamClone4", 20, 180, 80, "Male");


        Instant dateNow = Instant.now();
        Activity activity1 = new Activity("Activity1", Date.from(dateNow), "Run", 100.0, 15.0);
        Activity activity2 = new Activity("Activity2", Date.from(dateNow.minus(Duration.ofDays(20))), "Walk", 70.0, 11.0);
        Activity activity3 = new Activity("Activity3", Date.from(dateNow.minus(Duration.ofDays(5))), "Cycle", 70.0, 9.0);
        activity3.setManualEntry(false);

        DatabaseOperations.setDatabaseURL(testDBURL);
        DatabaseOperations.createDatabase();

        UserDBOperations.insertNewUser(user);
        UserDBOperations.insertNewUser(userClone);
        UserDBOperations.insertNewUser(userClone2);
        user.addActivity(activity1);
        user.addActivity(activity2);
        user.addActivity(activity3);

        userClone.addActivity(activity1);
        userClone.addActivity(activity2);
        userClone.addActivity(activity3);

        userClone2.addActivity(activity1);
        userClone2.addActivity(activity2);
        userClone2.addActivity(activity3);

        userClone3.addActivity(activity1);
        userClone3.addActivity(activity2);
        userClone3.addActivity(activity3);

        userClone4.addActivity(activity1);
        userClone4.addActivity(activity2);
        userClone4.addActivity(activity3);
    }

    @Test
    public void testBmi(){
        assertEquals(24.69, user.getBmi(), 1e-2);
    }

    @Test
    public void testBmiWeightUpdate(){
        user.setWeight(120);
        assertEquals(37.04, user.getBmi(), 1e-2);
    }


    @Test
    public void testBmiHeightUpdate(){
        user.setHeight(190);
        assertEquals(22.16, user.getBmi(), 1e-2);
    }

    @Test
    public void testInitialTotalDistance() {
        assertEquals(35.0, user.getTotalDistance(), 1e-2);
    }

    @Test
    public void testRecalcTotalDistanceInsertActivity() {
        Instant dateNow = Instant.now();
        Activity activity4 = new Activity("Activity4", Date.from(dateNow.minus(Duration.ofDays(2))), "Run", 100.0, 15.0);
        userClone.addActivity(activity4);
        assertEquals(50.0, userClone.getTotalDistance(), 1e-2);

    }

    @Test
    public void testRecalcTotalDistanceDeleteActivity() {
        userClone2.deleteActivity(userClone2.getActivityList().get(2));
        assertEquals(26.0, userClone2.getTotalDistance(), 1e-2);
    }

    @Test
    public void testRecalcTotalDistanceModifyActivity() {
        Activity holder = userClone3.getActivityList().get(1);
        userClone3.deleteActivity(userClone3.getActivityList().get(1));
        holder.setTotalDistance(12.0);
        userClone3.addActivity(holder);
        assertEquals(36.0, userClone3.getTotalDistance(), 1e-2);
    }

    @Test
    public void testGetUserNonManualActivities() {
        assertEquals(1, user.getNonManualActivityList().size());
    }




    @After
    public void resetTestingDatabase() throws Exception {
        DatabaseOperations.connectToDB();
        DatabaseOperations.resetDatabase(true);
        DatabaseOperations.disconnectFromDB();
    }






}
