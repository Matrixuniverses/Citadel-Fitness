package seng202.group2.data;


import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;
import seng202.group2.model.User;

import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseTest {

    private static final String testDBURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessTestingDatabase.db";

    @Before
    public void testDataBaseSetUp() throws SQLException{

        DatabaseOperations.setDatabaseURL(testDBURL);

        User user1 = new User("User1", 17, 160.0, 70, "Male");
        User user2 = new User("User2", 18, 175.0, 67, "Male");
        User user3 = new User("User3", 19, 180.0, 80, "Male");
        User user4 = new User("User4", 21, 180.0, 80, "Male");

        Instant dateNow = Instant.now();


        Activity activity1 = new Activity("Activity1", Date.from(dateNow), "Run", 100.0, 15.0);
        Activity activity2 = new Activity("Activity2", Date.from(dateNow), "Run", 90.0, 12.0);
        Activity activity3 = new Activity("Activity3", Date.from(dateNow), "Run", 90.0, 10.0);
        Activity activity4 = new Activity("Activity4", Date.from(dateNow.minus(Duration.ofDays(20))), "Walk", 70.0, 11.0);
        Activity activity5 = new Activity("Activity5", Date.from(dateNow.minus(Duration.ofDays(10))), "Walk", 80.0, 9.0);
        Activity activity6 = new Activity("Activity6", Date.from(dateNow.minus(Duration.ofDays(40))), "Walk", 90.0, 3.5);
        Activity activity7 = new Activity("Activity6", Date.from(dateNow.minus(Duration.ofDays(60))), "Walk", 89.0, 3.43);


        DataPoint dp1 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(10))), 170, 10.0, 10.0, 100.0);
        DataPoint dp2 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(20))), 160, 9.9, 10.0, 100.0);
        DataPoint dp3 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(30))), 165, 9.8, 10.0, 99.0);
        DataPoint dp4 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(40))), 167, 9.76, 10.0, 98.0);
        DataPoint dp5 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(50))), 168, 9.72, 10.0, 99.0);
        DataPoint dp6 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(10))), 168, 9.72, 10.0, 99.0);


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
        ActivityDBOperations.insertNewActivity(activity6, 3);
        ActivityDBOperations.insertNewActivity(activity7, 3);

        //inserting test datapoints
        DatapointDBOperations.insertNewDataPoint(dp1, 1);
        DatapointDBOperations.insertNewDataPoint(dp2, 1);
        DatapointDBOperations.insertNewDataPoint(dp3, 1);
        DatapointDBOperations.insertNewDataPoint(dp4, 1);
        DatapointDBOperations.insertNewDataPoint(dp5, 1);
        DatapointDBOperations.insertNewDataPoint(dp6, 7);



    }

    @Test
    public void testConnectToDatabase() throws SQLException {

        Connection conn = DatabaseOperations.connectToDB();
        assertNotNull(conn);

    }

    @Test
    public void testDisconnectFromDatabase() throws SQLException {

        Connection conn = DatabaseOperations.connectToDB();
        DatabaseOperations.disconnectFromDB();

        assertEquals(true, conn.isClosed());



    }

    @Test
    public void testInsertNewUser() throws SQLException {

        User user4 = new User("User5", 19, 190.0, 85, "Male");
        assertEquals(5, UserDBOperations.insertNewUser(user4));


    }

    @Test
    public void testGetAllUsers() throws SQLException {

        ObservableList<User> userList = UserDBOperations.getAllUsers();
        assertEquals(false, userList.isEmpty());
    }

    @Test
    public void testGetUserFromRS() throws SQLException {


        User matchingUser = new User(1, "User1", 17, 160.0, 70, "Male");
        User retrievedUser = UserDBOperations.getUserFromRS(1);
        assertEquals(true, matchingUser.getName().equals(retrievedUser.getName()));


    }

    @Test
    public void testUpdateUserReturnValueSuccess() throws SQLException {


        User retrievedUser = UserDBOperations.getUserFromRS(3);
        retrievedUser.setWeight(90);
        assertEquals(true, UserDBOperations.updateExistingUser(retrievedUser));


    }

    @Test
    public void testUpdateUserReturnValueFail() throws SQLException {

        User retrievedUser = UserDBOperations.getUserFromRS(3);
        retrievedUser.setWeight(90);
        retrievedUser.setId(1000000);
        assertEquals(false, UserDBOperations.updateExistingUser(retrievedUser));
    }

    @Test
    public void testUserRecordStateAfterUpdate() throws SQLException {


        User retrievedUser = UserDBOperations.getUserFromRS(3);
        retrievedUser.setWeight(91.2);
        if (UserDBOperations.updateExistingUser(retrievedUser)) {
            User updatedDatabaseUser = UserDBOperations.getUserFromRS(3);
            assertEquals(91.2, updatedDatabaseUser.getWeight(), 0.01);
        } else {
            fail();
        }

    }

    @Test
    public void testDeleteExistingUser() throws SQLException {

        assertEquals(true, UserDBOperations.deleteExistingUser(4));

    }

    @Test
    public void testDeleteExistingUserCascade() throws SQLException {

        if (UserDBOperations.deleteExistingUser(2)) {
            assertEquals(null, ActivityDBOperations.getActivityFromDB(2));
        } else {
            fail();
        }

    }

    @Test
    public void testInsertNewActivitySuccess() throws SQLException {

        Instant dateNow = Instant.now();
        Activity activity8 = new Activity("Activity8", Date.from(dateNow.minus(Duration.ofDays(50))), "Rest", 10.0, 0.0);
        assertEquals(8, ActivityDBOperations.insertNewActivity(activity8, 1));

    }

    @Test
    public void testInsertNewActivityFail() throws SQLException {

        Instant dateNow = Instant.now();
        Activity garbage = new Activity("Will not be inserted", Date.from(dateNow.minus(Duration.ofDays(50))), "Rest", 10.0, 0.0);
        assertEquals(-1, ActivityDBOperations.insertNewActivity(garbage, 1000));

    }


    @Test
    public void testGetClashingActivity() throws SQLException {

        Instant dateNow = Instant.now();
        Activity activity9a = new Activity("Activity9", Date.from(dateNow.minus(Duration.ofDays(70))), "Walk", 20.0, 2.0);
        Activity activity9b = new Activity("Activity9", Date.from(dateNow.minus(Duration.ofDays(70))), "Run", 20.0, 4.0);
        ActivityDBOperations.insertNewActivity(activity9a, 1);

        boolean results[] = {ActivityDBOperations.checkDuplicateActivity(activity9b, 1), ActivityDBOperations.checkDuplicateActivity(activity9b, 2)};
        boolean expectedResults[] = {true, false};
        assertArrayEquals(expectedResults, results);
    }

    @Test
    public void testGetAllActivitiesForUserSuccess() throws SQLException {

        ObservableList<Activity> activities = ActivityDBOperations.getAllUsersActivities(1);
        assertNotNull(activities);

    }

    @Test
    public void testGetAllActivitiesForUserOrdering() throws SQLException {

        ObservableList<Activity> activities = ActivityDBOperations.getAllUsersActivities(1);
        assertEquals(true, (activities.get(0).getDate().before(activities.get(1).getDate())));

    }

    @Test
    public void testGetAllActivitiesForUserFail() throws SQLException {

        ObservableList<Activity> activities = ActivityDBOperations.getAllUsersActivities(1000);
        assertEquals(true, activities.isEmpty());

    }

    @Test
    public void testGetActivityFromDBValid() throws SQLException {

        Activity activity = ActivityDBOperations.getActivityFromDB(1);
        assertEquals(1, activity.getId());

    }

    @Test
    public void testgetActivityFromDBInvalid() throws SQLException {

        Activity activity = ActivityDBOperations.getActivityFromDB(1000);
        assertEquals(null, activity);

    }


    @Test
    public void testUpdateActivityReturnValueSuccess() throws SQLException {

        Activity activity = ActivityDBOperations.getActivityFromDB(3);
        activity.setTotalDistance(9.8);
        assertEquals(true, ActivityDBOperations.updateExistingActivity(activity));

    }


    @Test
    public void testUpdateActivityReturnValueFail() throws SQLException {

        Activity activity = ActivityDBOperations.getActivityFromDB(3);
        activity.setId(1000);
        assertEquals(false, ActivityDBOperations.updateExistingActivity(activity));

    }


    @Test
    public void testDeleteExistingActivity() throws SQLException {

        assertEquals(true,ActivityDBOperations.deleteExistingActivity(6));

    }


    @Test
    public void testDeleteExistingActivityCascade() throws SQLException {

        ActivityDBOperations.deleteExistingActivity(7);
        assertEquals(null, DatapointDBOperations.getDataPointFromDB(7));

    }

    @Test
    public void testInsertNewDataPointSuccess() throws SQLException {

        Instant dateNow = Instant.now();
        DataPoint dp7 = new DataPoint(Date.from(dateNow), 164,100.0,100.0,99.0);
        assertEquals(7, DatapointDBOperations.insertNewDataPoint(dp7, 7));

    }

    @Test
    public void testInsertNewDataPointFail() throws SQLException {

        Instant dateNow = Instant.now();
        DataPoint rubbish = new DataPoint(Date.from(dateNow), 164,100.0,100.0,99.0);
        assertEquals(-1, DatapointDBOperations.insertNewDataPoint(rubbish, 1000));

    }

    @Test
    public void testGetAllDataPointsForActivitySuccess() throws SQLException {

        ObservableList<DataPoint> datapoints = DatapointDBOperations.getAllActivityDatapoints(1);
        assertNotNull(datapoints);

    }

    @Test
    public void testGetAllDataPointsForActivityOrdering() throws SQLException {

        ObservableList<DataPoint> datapoints = DatapointDBOperations.getAllActivityDatapoints(1);
        assertEquals(true, (datapoints.get(0).getDate().before(datapoints.get(1).getDate())));

    }

    @Test
    public void testGetAllDataPointsForActivityFail() throws SQLException {

        ObservableList<DataPoint> datapoints = DatapointDBOperations.getAllActivityDatapoints(1000);
        assertEquals(true, datapoints.isEmpty());

    }

    @Test
    public void testGetDataPointFromDBValid() throws SQLException {

        DataPoint dp = DatapointDBOperations.getDataPointFromDB(1);
        assertEquals(1, dp.getId());

    }

    @Test
    public void testGetDataPointFromDBInvalid() throws SQLException {

        DataPoint shouldntExist = DatapointDBOperations.getDataPointFromDB(1000);
        assertEquals(null, shouldntExist);

    }

    @Test
    public void testUpdateDataPointValid() throws SQLException {

        DataPoint retrievedDB  = DatapointDBOperations.getDataPointFromDB(5);
        retrievedDB.setHeartRate(199);
        assertEquals(true, DatapointDBOperations.updateExistingDataPoint(retrievedDB));

    }

    @Test
    public void testUpdateDataPointInvalid() throws SQLException {

        DataPoint retrievedDB  = DatapointDBOperations.getDataPointFromDB(5);
        retrievedDB.setId(1000);
        assertEquals(false, DatapointDBOperations.updateExistingDataPoint(retrievedDB));

    }


    @Test
    public void testDeleteDatapoint() throws SQLException {

        assertEquals(true, DatapointDBOperations.deleteExistingDataPoint(5));

    }

    @Test
    public void testDataPointTransaction() throws SQLException {

            Instant dateNow = Instant.now();
            DataPoint dp7 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(100))), 168, 9.72, 10.0, 99.0);
            DataPoint dp8 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(90))), 168, 9.72, 10.0, 99.0);
            ArrayList<DataPoint> testPoints = new ArrayList<>();
            testPoints.add(dp7);
            testPoints.add(dp8);
            ArrayList<Integer> pkeys = DatapointDBOperations.insertDataPointList(testPoints, 1);
            int[] expectedKeys = {7,8};
            int[] actualKeys = {pkeys.get(0),pkeys.get(1)};
            assertArrayEquals(expectedKeys, actualKeys);

    }

    @Test
    public void testDataPointTransactionEmptyList() throws SQLException {
        ArrayList<DataPoint> testPoints = new ArrayList<>();
        ArrayList<Integer> pkeys = DatapointDBOperations.insertDataPointList(testPoints, 1);
        assertEquals(true, pkeys.isEmpty());
    }




    @After
    public void resetTestingDB() throws SQLException{

        DatabaseOperations.connectToDB();
        DatabaseOperations.resetDatabase(true);
        DatabaseOperations.disconnectFromDB();


    }











}
