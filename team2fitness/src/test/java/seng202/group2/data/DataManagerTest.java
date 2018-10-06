package seng202.group2.data;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import seng202.group2.model.Activity;
import seng202.group2.model.User;


import java.time.Duration;
import java.time.Instant;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


import static org.junit.Assert.assertEquals;

public class DataManagerTest {

    private static final String testDBURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessTestingDatabase.db";
    private DataManager dataManager;
    private User user1;
    private User user2;
    private Activity activity4;
    private Activity activity5;
    private Activity activity6;
    private Activity activity7;
    @Before
    public void testDataBaseSetUp() {

        DatabaseOperations.setDatabaseURL(testDBURL);

        user1 = new User("User1", 17, 160.0, 70, "Male");
        user2 = new User("User2", 18, 175.0, 67, "Male");
        User user3 = new User("User3", 19, 180.0, 80, "Female");
        User user4 = new User("User4", 21, 180.0, 80, "Female");

        Instant dateNow = Instant.now();


        Activity activity1 = new Activity("Activity1", Date.from(dateNow), "Run", 100.0, 15.0);
        Activity activity2 = new Activity("Activity2", Date.from(dateNow), "Run", 90.0, 12.0);
        Activity activity3 = new Activity("Activity3", Date.from(dateNow), "Run", 90.0, 10.0);
        activity4 = new Activity("Activity4", Date.from(dateNow.minus(Duration.ofDays(20))), "Walk", 70.0, 11.0);
        activity5 = new Activity("Activity5", Date.from(dateNow.minus(Duration.ofDays(10))), "Walk", 80.0, 9.0);
        activity6 = new Activity("Activity6", Date.from(dateNow.minus(Duration.ofDays(40))), "Walk", 90.0, 3.5);
        activity7 = new Activity("Activity6", Date.from(dateNow.minus(Duration.ofDays(60))), "Walk", 89.0, 3.43);


        try {
            DatabaseOperations.createDatabase();

            //inserting test users
            UserDBOperations.insertNewUser(user1);
            UserDBOperations.insertNewUser(user2);


            //inserting test activities
            ActivityDBOperations.insertNewActivity(activity1, 1);
            ActivityDBOperations.insertNewActivity(activity2, 2);
            ActivityDBOperations.insertNewActivity(activity3, 3);
            ActivityDBOperations.insertNewActivity(activity4, 1);
            ActivityDBOperations.insertNewActivity(activity5, 2);
            ActivityDBOperations.insertNewActivity(activity6, 3);




            dataManager = new DataManager();


        } catch (SQLException e) {
            e.printStackTrace();
        }



    }


    @Test
    public void testDataManagerInitialization() {
        assertEquals(2, dataManager.getUserList().size());

    }



    @Test
    public void testDataManagerDeleteUser(){

        dataManager.deleteUser(dataManager.getUserList().get(0));
        assertEquals(1, dataManager.getUserList().size());
    }

    @Test
    public void testDataManagerSetUser(){
        dataManager.setCurrentUser(user2);
        assertEquals(user2, dataManager.getCurrentUser());
    }

    @Test
    public void testDataManagerAddActivity(){
        dataManager.setCurrentUser(user2);
        dataManager.addActivity(activity7);
        assertEquals(1, dataManager.getActivityList().size());
    }

    @Test
    public void testDataManagerAddActivities(){
        dataManager.setCurrentUser(user2);
        ArrayList<Activity> newActivities = new ArrayList<Activity>();
        newActivities.add(activity4);
        newActivities.add(activity5);
        newActivities.add(activity6);
        dataManager.addActivities(newActivities);
        assertEquals(3,dataManager.getActivityList().size());
    }

    @Test
    public void testDataManagerDeleteActivities(){
        dataManager.setCurrentUser(user2);
        dataManager.addActivity(activity7);
        dataManager.deleteActivity(activity7);
        assertEquals(0,dataManager.getActivityList().size());
    }



    @After
    public void resetTestingDB() {
        try {
            DatabaseOperations.connectToDB();
            DatabaseOperations.resetDatabase(true);
            DatabaseOperations.disconnectFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
