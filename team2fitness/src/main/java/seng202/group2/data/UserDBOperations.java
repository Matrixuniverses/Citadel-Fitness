package seng202.group2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The UserDBOperations is a static class handles all sql queries and methods relating specifically to the users relation
 * in the database.
 */
public class UserDBOperations {

    /**
     * Searches the data for a user in the table users using a given user_id and returns the User if the
     * user has be found by the query in the data.
     * This function automatically connects to and disconnects from the data.
     * @param userID the id of the user that will be searched for.
     * @return the User associated with the user id from the data if the user_id exists in the data.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static User getUserFromRS(int userID) throws SQLException {

        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Users WHERE user_id = ?";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, userID);
        ResultSet queryResult = pQueryStmt.executeQuery();



        User retrievedUser = null;

        if (queryResult.next()) {
            int id = queryResult.getInt(1);
            String name = queryResult.getString(2);
            int age = queryResult.getInt(3);
            double height = queryResult.getDouble(4);
            double weight = queryResult.getDouble(5);
            String gender = queryResult.getString(6);
            retrievedUser = new User(id,name, age, height, weight, gender);
        }

        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();

        return retrievedUser;
    }


    /**
     * Queries the data for all user records in the data and returns them as an observable list.
     * This function automatically connects to and disconnects from the data.
     * @return an Observable list of all the current users in the data.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        //creates a new database if previously doesnt exist
        DatabaseOperations.createDatabase();
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * from Users";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);

        ResultSet queryResult = pQueryStmt.executeQuery();


        ObservableList<User> retrievedUsers = FXCollections.observableArrayList();

        while (queryResult.next()) {
            int id = queryResult.getInt(1);
            String name = queryResult.getString(2);
            int age = queryResult.getInt(3);
            double height = queryResult.getDouble(4);
            double weight = queryResult.getDouble(5);
            String gender = queryResult.getString(6);
            User retrievedUser = new User(id,name, age, height, weight, gender);

            retrievedUsers.add(retrievedUser);
        }
        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();

        return retrievedUsers;



    }


    /**
     * Inserts a new User into the data.
     * This function automatically connects to and disconnects from the data.
     * @param user The User object to be stored in the data.
     * @throws SQLException If there is a sql related error when trying to preform the insert operation on the database.
     */
    public static int insertNewUser(User user) throws SQLException {

        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlInsertStmt = "INSERT INTO Users(name, age, height, weight, gender) VALUES(?,?,?,?,?)";


        PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStmt.setString(1, user.getName());
        pUpdateStmt.setInt(2, user.getAge());
        pUpdateStmt.setDouble(3,  user.getHeight());
        pUpdateStmt.setDouble(4, user.getWeight());
        pUpdateStmt.setString(5, user.getGender());
        pUpdateStmt.executeUpdate();

        ResultSet results = pUpdateStmt.getGeneratedKeys();
        results.next();
        int userID = results.getInt(1);

        pUpdateStmt.close();
        DatabaseOperations.disconnectFromDB();

        return userID;

    }


    /**
     * Updates a user that is already stored within the data
     * This function automatically connects to and disconnects from the data.
     * @param user The updated version of the a particular USer object that will have its data record updated
     * @return true if the user does exist inside the data. false otherwise
     * @throws SQLException If there was a sql related error when trying to update a user in the database.
     */
    public static boolean updateExistingUser(User user) throws SQLException{

        String sqlUpdateStmt = "UPDATE Users SET name = ?, age = ?, height = ?, weight = ? WHERE user_id = ?";
        if (getUserFromRS(user.getId()) != null) {

            Connection dbConn = DatabaseOperations.connectToDB();

            PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStmt.setString(1, user.getName());
            pUpdateStmt.setInt(2, user.getAge());
            pUpdateStmt.setDouble(3, user.getHeight());
            pUpdateStmt.setDouble(4, user.getWeight());
            pUpdateStmt.setInt(5, user.getId());
            pUpdateStmt.executeUpdate();

            pUpdateStmt.close();
            DatabaseOperations.disconnectFromDB();
            return true;
        } else {

            return false;
        }


    }


    /**
     * Deletes a user from the data using the user's ID.
     * This function automatically connects to and disconnects from the data.
     * @param userID The ID of the user to be removed from the data
     * @return true if the user with the inputted id no longer exists within the data
     * @throws SQLException if an sql related error occurs while attempting to delete a user from the database.
     */
    public static boolean deleteExistingUser(int userID) throws SQLException {

        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlDeleteStmt = "DELETE FROM Users WHERE user_id = ?";

        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, userID);
        pDeleteStmt.executeUpdate();

        pDeleteStmt.close();
        DatabaseOperations.disconnectFromDB();
        if (getUserFromRS(userID) == null) {
            return true;
        } else {
            return false;
        }

    }



}
