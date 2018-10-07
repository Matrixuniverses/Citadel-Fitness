package seng202.group2.data;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import seng202.group2.model.Activity;
import seng202.group2.model.DataPoint;
import seng202.group2.model.Target;
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
        Activity activity4 = new Activity("Activity4", Date.from(dateNow.minus(Duration.ofDays(20))), "Walkplus", 70.0, 11.0);
        Activity activity5 = new Activity("Activity5", Date.from(dateNow.minus(Duration.ofDays(10))), "Walk", 80.0, 9.0);
        Activity activity6 = new Activity("Activity6", Date.from(dateNow.minus(Duration.ofDays(40))), "Walk", 90.0, 3.5);
        Activity activity7 = new Activity("Activity7", Date.from(dateNow.minus(Duration.ofDays(60))), "Walk", 89.0, 3.43);


        DataPoint dp1 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(10))), 170, 10.0, 10.0, 100.0);
        DataPoint dp2 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(20))), 160, 9.9, 10.0, 100.0);
        DataPoint dp3 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(30))), 165, 9.8, 10.0, 99.0);
        DataPoint dp4 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(40))), 167, 9.76, 10.0, 98.0);
        DataPoint dp5 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(50))), 168, 9.72, 10.0, 99.0);
        DataPoint dp6 = new DataPoint(Date.from(dateNow.minus(Duration.ofSeconds(10))), 168, 9.72, 10.0, 99.0);


        Target target1 = new Target("Target1", Date.from(dateNow.plus(Duration.ofDays(3))),  "type1", 0.0, 10.0, 100.0);
        Target target2 = new Target("Target2", Date.from(dateNow.plus(Duration.ofDays(6))), "type1", 0.0, 70.0, 80.0);
        Target target3 = new Target("Target3", Date.from(dateNow.plus(Duration.ofDays(9))), "type2", 0.0, 70.0, 80.0);


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


        TargetDBOperations.insertNewTarget(target1, 1);
        TargetDBOperations.insertNewTarget(target2, 2);
        TargetDBOperations.insertNewTarget(target3, 1);



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
        User retrievedUser = UserDBOperations.getUserFromDB(1);
        assertEquals(true, matchingUser.getName().equals(retrievedUser.getName()));


    }

    @Test
    public void testUpdateUserReturnValueSuccess() throws SQLException {


        User retrievedUser = UserDBOperations.getUserFromDB(3);
        retrievedUser.setWeight(90);
        assertEquals(true, UserDBOperations.updateExistingUser(retrievedUser));


    }

    @Test
    public void testUpdateUserReturnValueFail() throws SQLException {

        User retrievedUser = UserDBOperations.getUserFromDB(3);
        retrievedUser.setWeight(90);
        retrievedUser.setId(1000000);
        assertEquals(false, UserDBOperations.updateExistingUser(retrievedUser));
    }

    @Test
    public void testUserRecordStateAfterUpdate() throws SQLException {


        User retrievedUser = UserDBOperations.getUserFromDB(3);
        retrievedUser.setWeight(91.2);
        if (UserDBOperations.updateExistingUser(retrievedUser)) {
            User updatedDatabaseUser = UserDBOperations.getUserFromDB(3);
            assertEquals(91.2, updatedDatabaseUser.getWeight(), 0.1);
        } else {
            fail();
        }

    }

    @Test
    public void testDeleteExistingUser() throws SQLException {

        assertEquals(true, UserDBOperations.deleteExistingUser(4));

    }

    @Test
    public void testDeleteExistingUserCascadeActivities() throws SQLException {

        if (UserDBOperations.deleteExistingUser(2)) {
            assertEquals(null, ActivityDBOperations.getActivityFromDB(2));
        } else {
            fail();
        }

    }

    @Test
    public void testDeleteExistingUserCascadeTargets() throws SQLException {

        if (UserDBOperations.deleteExistingUser(2)) {
            assertEquals(null, TargetDBOperations.getTargetFromDB(2));
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
    public void testGetActivitiesBetweenDatesSuccessLen() throws SQLException {

        Instant endDate = Instant.now();
        Instant startDate = endDate.minus(Duration.ofDays(41));
        assertEquals(2, ActivityDBOperations.getActivitiesBetweenDates(new java.sql.Date(Date.from(startDate).getTime()), new java.sql.Date(Date.from(endDate).getTime()), 3).size());
    }

    @Test
    public void testGetActivitiesBetweenDatesSuccessOrdering() throws SQLException {

        Instant endDate = Instant.now();
        Instant startDate = endDate.minus(Duration.ofDays(51));
        ObservableList<Activity> activities = ActivityDBOperations.getActivitiesBetweenDates(new java.sql.Date(Date.from(startDate).getTime()), new java.sql.Date(Date.from(endDate).getTime()), 3);
        assertEquals(true, (activities.get(0).getDate().before(activities.get(1).getDate())));
    }

    @Test
    public void testGetActivitiesBetweenDatesSuccessEmpty() throws SQLException {
        Instant endDate = Instant.now().minus(Duration.ofDays(10));
        Instant startDate = endDate.minus(Duration.ofDays(10));
        assertEquals(true, ActivityDBOperations.getActivitiesBetweenDates(new java.sql.Date(Date.from(startDate).getTime()), new java.sql.Date(Date.from(endDate).getTime()), 3).isEmpty());

    }

    @Test
    public void testGetActivitiesBetweenDatesInvalidUserId() throws SQLException {
        Instant endDate = Instant.now();
        Instant startDate = endDate.minus(Duration.ofDays(1000));
        assertEquals(true, ActivityDBOperations.getActivitiesBetweenDates(new java.sql.Date(Date.from(startDate).getTime()), new java.sql.Date(Date.from(endDate).getTime()), 1000).isEmpty());
    }

    @Test
    public void testGetAllActivitiesForUserSuccess() throws SQLException {


        ObservableList<Activity> activities = ActivityDBOperations.getAllUsersActivities(1);
        assertNotNull(activities);

    }

    @Test
    public void testGetAllActivitiesForUserOrdering() throws SQLException {

        ObservableList<Activity> activities = ActivityDBOperations.getAllUsersActivities(1);
        Activity firstActivity = activities.get(0);
        Activity secondActivity = activities.get(1);
        firstActivity.setManualEntry(true); //to bypass active listener controlled activityData
        secondActivity.setManualEntry(true);
        assertEquals(true, firstActivity.getDate().before(secondActivity.getDate()));

    }

    @Test
    public void testGetAllActivitiesForUserFail() throws SQLException {

        ObservableList<Activity> activities = ActivityDBOperations.getAllUsersActivities(1000);
        assertEquals(true, activities.isEmpty());

    }

    @Test
    public void testGetActivityFromDBValid() throws SQLException {

        Activity activity = ActivityDBOperations.getActivityFromDB(1);
        assertEquals(false, activity.isManualEntry());

    }

    @Test
    public void testGetManualEntryActivityFromDBValid() throws SQLException {

        Activity activity = ActivityDBOperations.getActivityFromDB(2);
        assertEquals(true, activity.isManualEntry());

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
            ObservableList<DataPoint> testPoints = FXCollections.observableArrayList();
            testPoints.add(dp7);
            testPoints.add(dp8);
            ArrayList<Integer> pkeys = DatapointDBOperations.insertDataPointList(testPoints, 1);
            int[] expectedKeys = {7,8};
            int[] actualKeys = {pkeys.get(0),pkeys.get(1)};
            assertArrayEquals(expectedKeys, actualKeys);

    }

    @Test
    public void testDataPointTransactionEmptyList() throws SQLException {
        ObservableList<DataPoint> testPoints = FXCollections.observableArrayList();
        ArrayList<Integer> pkeys = DatapointDBOperations.insertDataPointList(testPoints, 1);
        assertEquals(true, pkeys.isEmpty());
    }

    @Test
    public void testInsertNewTargetSuccess() throws SQLException {
        Instant dateNow = Instant.now();
        Target newTarget = new Target("Target4", Date.from(dateNow.plus(Duration.ofDays(12))), "type2", 0.0, 70.0, 90.0);
        assertEquals(4, TargetDBOperations.insertNewTarget(newTarget, 3));
    }

    @Test
    public void testInsertNewTargetFail() throws SQLException {
        Instant dateNow = Instant.now();
        Target newTarget = new Target("Target4", Date.from(dateNow.plus(Duration.ofDays(12))), "type2", 0.0, 70.0, 90.0);
        assertEquals(-1, TargetDBOperations.insertNewTarget(newTarget, 3000));
    }

    @Test
    public void testGetAllTargetsSuccess() throws SQLException {
        ObservableList<Target> targets = TargetDBOperations.getAllUserTargets(1);
        assertEquals(false, targets.isEmpty());
    }

    @Test
    public void testGetAllTargetsEmpty() throws SQLException {
        ObservableList<Target> noTargets = TargetDBOperations.getAllUserTargets(4);
        assertEquals(true, noTargets.isEmpty());
    }

    @Test
    public void testGetAllTargetsInvalid() throws SQLException {
        ObservableList<Target> noTargets = TargetDBOperations.getAllUserTargets(1000);
        assertEquals(true, noTargets.isEmpty());
    }

    @Test
    public void testGetTargetFromDBValid() throws SQLException {
        Target retrievedTarget = TargetDBOperations.getTargetFromDB(1);
        assertEquals(1, retrievedTarget.getId());
    }

    @Test
    public void testGetTargetFromDBInvalid() throws SQLException {
        Target nonExistent = TargetDBOperations.getTargetFromDB(1000);
        assertNull(nonExistent);
    }

    @Test
    public void testUpdateTargetValid() throws SQLException {
        Target targetToUpdate = TargetDBOperations.getTargetFromDB(2);
        targetToUpdate.updateProgress(75);
        assertEquals(true, TargetDBOperations.updateExistingTarget(targetToUpdate));
    }

    @Test
    public void testUpdateTargetInvalid() throws SQLException {
        Target targetToUpdate = TargetDBOperations.getTargetFromDB(2);
        targetToUpdate.setId(1000);
        assertEquals(false, TargetDBOperations.updateExistingTarget(targetToUpdate));
    }

    @Test
    public void testDeleteTarget() throws SQLException{
        assertEquals(true, TargetDBOperations.deleteExistingTarget(3));
    }






    @After
    public void resetTestingDB() throws SQLException{

        DatabaseOperations.connectToDB();
        DatabaseOperations.resetDatabase(true);
        DatabaseOperations.disconnectFromDB();


    }











}
