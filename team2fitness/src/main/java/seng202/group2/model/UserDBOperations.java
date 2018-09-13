package seng202.group2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.data_functions.databaseWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBOperations {

    //TODO - Implement index structure to remove the loop


    /**
     * Searches the database for a user in the table users using a given user_id and returns the User if the
     * user has be found by the query in the database.
     * @param user_id the id of the user that will be searched for.
     * @return the User associated with the user id from the database if the user_id exists in the database.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static User getUserFromRS(String user_id) throws SQLException {

        String sqlQuery = "SELECT * FROM Users WHERE user_id = "+ user_id + ";";

        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQuery);

        User retrievedUser = null;

        // TODO - Replace this with an index structure @Chris
        if (queryResult.next()) {

            int id = queryResult.getInt("user_id");
            String name = queryResult.getString("name");
            int age = queryResult.getInt("age");
            double height = queryResult.getDouble("height");
            float weight = queryResult.getFloat("weight");
            retrievedUser = new User(name, age, height, weight);
            retrievedUser.setId(id);


        }


        return retrievedUser;


    }

    /**
     * Querys the database for all user records in the database and returns them as an observable list.
     * @return an Observable list of all the current users in the database.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static ObservableList<User> getAllUsers() throws SQLException {

        String sqlQuery = "SELECT * from Users";

        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQuery);


        ObservableList<User> retrievedUsers = FXCollections.observableArrayList();

        while (queryResult.next()) {
            int id = queryResult.getInt("user_id");
            String name = queryResult.getString("name");
            int age = queryResult.getInt("age");
            double height = queryResult.getDouble("height");
            float weight = queryResult.getFloat("weight");
            User retrievedUser = new User(name, age, height, weight);
            retrievedUser.setId(id);
            retrievedUsers.add(retrievedUser);
        }

        return retrievedUsers;



    }


    /**
     * Inserts a new user into the users table in the database.
     * @param userName the name of the user as a String.
     * @param userAge the age of the user as an Integer.
     * @param userHeight the height of the user in meters as a double.
     * @param userWeight the weight of the user in kg as a floating point value.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static void insertNewUser(String userName, int userAge, double userHeight, float userWeight) throws SQLException {
        String sqlInsertStmt = "INSERT INTO Users(name, age, height, weight) VALUES(?,?,?,?)";
        try {

            Connection dbConn = databaseWriter.getDbConnection();
            PreparedStatement pInsertStmt = dbConn.prepareStatement(sqlInsertStmt);
            pInsertStmt.setString(1, userName);
            pInsertStmt.setInt(2, userAge);
            pInsertStmt.setDouble(3, userHeight);
            pInsertStmt.setFloat(4, userWeight);
            pInsertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public static boolean deleteExistingUser(int userId) throws SQLException {
        String sqlDeleteStmt = "DELETE FROM Users WHERE user_id = ?";
        Connection dbConn = databaseWriter.getDbConnection();
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, userId);
        pDeleteStmt.executeUpdate();
        if (getUserFromRS(Integer.toString(userId)) == null) {
            return true;
        } else {
            return false;
        }

    }


    /*
    public static void main(String args[]) {
        try {
            databaseWriter.connectToDB();
            databaseWriter.createDatabase();
            System.out.println(deleteExistingUser(5));
            //insertNewUser("Test5", 18, 1.75, 76);
            //User user3 = getUserFromRS("3");
            //System.out.println(user3.getId() + " " + user3.getName());
            /*ObservableList<User> retrievedUsers = getAllUsers();
            if (retrievedUsers != null) {
                for (User user : retrievedUsers) {
                    System.out.println(user.getId() + " " + user.getName());

                }
            }

            databaseWriter.disconnectFromDB();
        } catch (SQLException e) {
            e.printStackTrace();

        }


    }
    */


}
