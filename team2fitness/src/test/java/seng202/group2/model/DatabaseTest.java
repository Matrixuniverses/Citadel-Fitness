package seng202.group2.model;


import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import seng202.group2.data_functions.DatabaseWriter;

import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;


import java.sql.SQLException;
import java.util.Date;

public class DatabaseTest {

    private static final String testDBURL = "jdbc:sqlite:" + System.getProperty("user.home") + "/CitadelFitnessTestingDatabase.db";

    @Before
    public void testDataBaseSetUp() {

        DatabaseWriter.setDatabaseURL(testDBURL);

        User user1 = new User("User1", 17, 160.0, 70);
        User user2 = new User("User2", 18, 175.0, 67);
        User user3 = new User("User3", 19, 180.0, 80);

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
            DatabaseWriter.createDatabase();

            //inserting test users
            UserDBOperations.insertNewUser(user1);
            UserDBOperations.insertNewUser(user2);
            UserDBOperations.insertNewUser(user3);

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

            DatabaseWriter.disconnectFromDB();


        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    @Test
    public void testConnecttoDatabase() {
        Connection conn = null;
        try {
            DatabaseWriter.connectToDB();
            conn = DatabaseWriter.getDbConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertNotNull(conn);

    }

    @Test
    public void testDisconnectFromDatabase() {

        try {
            DatabaseWriter.connectToDB();
            DatabaseWriter.disconnectFromDB();
            Connection conn = DatabaseWriter.getDbConnection();
            assertEquals(true, conn.isClosed());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testInsertNewUser() {
        User user4 = new User("User4", 19, 190.0, 85);
        try {
            assertEquals(4, UserDBOperations.insertNewUser(user4));

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

    @After
    public void resetTestingDB() {
        try {
            DatabaseWriter.connectToDB();
            DatabaseWriter.resetDatabase(true);
            DatabaseWriter.disconnectFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }





}
