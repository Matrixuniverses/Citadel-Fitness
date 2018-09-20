package seng202.group2.model;


import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import seng202.group2.data.ActivityDBOperations;
import seng202.group2.data.DatabaseOperations;
import seng202.group2.data.DatapointDBOperations;
import seng202.group2.data.UserDBOperations;

import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;


import java.sql.SQLException;
import java.util.Date;

public class DatabaseTest {

    private static final String testDBURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessTestingDatabase.db";

    @Before
    public void testDataBaseSetUp() {

        DatabaseOperations.setDatabaseURL(testDBURL);

        User user1 = new User("User1", 17, 160.0, 70);
        User user2 = new User("User2", 18, 175.0, 67);
        User user3 = new User("User3", 19, 180.0, 80);
        User user4 = new User("User4", 21, 180.0, 80);

        Instant dateNow = Instant.now();


        Activity activity1 = new Activity("Activity1", Date.from(dateNow), "Run", 100.0, 15.0);
        Activity activity2 = new Activity("Activity2", Date.from(dateNow), "Run", 90.0, 12.0);
        Activity activity3 = new Activity("Activity3", Date.from(dateNow), "Run", 90.0, 10.0);
        Activity activity4 = new Activity("Activity4", Date.from(dateNow.minus(Duration.ofDays(20))), "Walk", 70.0, 11.0);
        Activity activity5 = new Activity("Activity5", Date.from(dateNow.minus(Duration.ofDays(10))), "Walk", 80.0, 9.0);


        DataPoint dp1 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(10))), 170, 10.0, 10.0, 100.0);
        DataPoint dp2 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(20))), 160, 9.9, 10.0, 100.0);
        DataPoint dp3 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(30))), 165, 9.8, 10.0, 99.0);
        DataPoint dp4 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(40))), 167, 9.76, 10.0, 98.0);
        DataPoint dp5 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(50))), 168, 9.72, 10.0, 99.0);

        try {
            DatabaseOperations.createDatabase();

            //inserting test users
            UserDBOperations.insertNewUser(user1);
            UserDBOperations.insertNewUser(user2);
            UserDBOperations.insertNewUser(user3);
            UserDBOperations.insertNewUser(user4);

            //inserting test activities
            ActivityDBOperations.insertNewActivity(activity1, 1);
            ActivityDBOperations.insertNewActivity(activity2, 2);
            ActivityDBOperations.insertNewActivity(activity3, 3);
            ActivityDBOperations.insertNewActivity(activity4, 1);
            ActivityDBOperations.insertNewActivity(activity5, 2);

            //inserting test datapoints
            DatapointDBOperations.insertNewDataPoint(dp1, 1);
            DatapointDBOperations.insertNewDataPoint(dp2, 1);
            DatapointDBOperations.insertNewDataPoint(dp3, 1);
            DatapointDBOperations.insertNewDataPoint(dp4, 1);
            DatapointDBOperations.insertNewDataPoint(dp5, 1);




        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    @Test
    public void testConnectToDatabase() {
        Connection conn = null;
        try {
            DatabaseOperations.connectToDB();
            conn = DatabaseOperations.getDbConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertNotNull(conn);

    }

    @Test
    public void testDisconnectFromDatabase() {

        try {
            DatabaseOperations.connectToDB();
            DatabaseOperations.disconnectFromDB();
            Connection conn = DatabaseOperations.getDbConnection();
            assertEquals(true, conn.isClosed());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testInsertNewUser() {
        User user4 = new User("User5", 19, 190.0, 85);
        try {
            DatabaseOperations.connectToDB();
            assertEquals(5, UserDBOperations.insertNewUser(user4));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllUsers() {
        try {
            ObservableList<User> userList = UserDBOperations.getAllUsers();
            assertEquals(false, userList.isEmpty());

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testGetUserFromRS() {
        try {
            //DatabaseOperations.connectToDB();
            User matchingUser = new User(1, "User1", 17, 160.0, 70);
            User retrievedUser = UserDBOperations.getUserFromRS(1);
            assertEquals(true, matchingUser.getName().equals(retrievedUser.getName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testUpdateUserReturnValueSuccess() {
        try {
            //DatabaseOperations.connectToDB();
            User retrievedUser = UserDBOperations.getUserFromRS(3);
            retrievedUser.setWeight(90);
            assertEquals(true, UserDBOperations.updateExistingUser(retrievedUser));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateUserReturnValueFail() {
        try {
            //DatabaseOperations.connectToDB();
            User retrievedUser = UserDBOperations.getUserFromRS(3);
            retrievedUser.setWeight(90);
            retrievedUser.setId(1000000);
            assertEquals(false, UserDBOperations.updateExistingUser(retrievedUser));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUserRecordStateAfterUpdate() {
        try {
            DatabaseOperations.connectToDB();
            User retrievedUser = UserDBOperations.getUserFromRS(3);
            retrievedUser.setWeight(91.2);
            if (UserDBOperations.updateExistingUser(retrievedUser)) {
                User updatedDatabaseUser = UserDBOperations.getUserFromRS(3);
                assertEquals(91.2, updatedDatabaseUser.getWeight(), 0.01);
            } else {
                fail();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteExistingUser() {
        try {
            assertEquals(true, UserDBOperations.deleteExistingUser(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDeleteExistingUserCascade() {
        try {
            if (UserDBOperations.deleteExistingUser(2)) {
                assertEquals(null, ActivityDBOperations.getActivityFromDB(2));
            } else {
                fail();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertNewActivity() {
        try {
            Instant dateNow = Instant.now();
            Activity activity6 = new Activity("Activity6", Date.from(dateNow.minus(Duration.ofDays(50))), "Rest", 10.0, 0.0);
            assertEquals(6, ActivityDBOperations.insertNewActivity(activity6, 1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //TODO - Change this test as checking the clash only results in a boolean value
    /*@Test
    public void testGetClashingActivity() {
        try {
            Instant dateNow = Instant.now();
            Activity activity7a = new Activity("Activity7a", Date.from(dateNow.minus(Duration.ofDays(70))), "Walk", 20.0, 2.0);
            Activity activity7b = new Activity("Activity7b", Date.from(dateNow.minus(Duration.ofDays(70))), "Run", 20.0, 4.0);
            ActivityDBOperations.insertNewActivity(activity7a, 1);
            assertNotNull(ActivityDBOperations.getClashingActivity(1, activity7b.getDate()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */


    //@Test
    //p/ublic void testGetAllActivitiesForUser() {
       // try {

      //  }
   // }




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
