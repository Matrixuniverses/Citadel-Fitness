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

    //helper function to retrieve an Observable List of Users from a ResultSet Object
    private static ObservableList<User> getUsersFromRS(ResultSet rs) throws SQLException {

        ObservableList<User> retrievedUsers = FXCollections.observableArrayList();

        //loop through each record
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            int age = rs.getInt(3);
            double height = rs.getDouble(4);
            double weight = rs.getDouble(5);
            String gender = rs.getString(6);
            User retrievedUser = new User(id, name, age, height, weight, gender);

            retrievedUsers.add(retrievedUser);
        }

        return retrievedUsers;
    }

    /**
     * Searches the data for a user in the table users using a given user_id and returns the User if the
     * user has be found by the query in the data.
     * This function automatically connects to and disconnects from the data.
     * @param userID the id of the user that will be searched for.
     * @return the User associated with the user id from the data if the user_id exists in the data.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static User getUserFromDB(int userID) throws SQLException {

        //executes a query that retrieves a user from the database with a specified id.
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Users WHERE user_id = ?";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, userID);
        ResultSet queryResult = pQueryStmt.executeQuery();

        User retrievedUser = null;
        ObservableList<User> retrievedUsers = getUsersFromRS(queryResult);
        //if the returned list has a length greater than 0 it must contain the retrieved user object.
        if (retrievedUsers.size() > 0) {
            retrievedUser = retrievedUsers.get(0);
        }

        //freeing resources
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
        //creates a new database if previously does not exist
        DatabaseOperations.createDatabase();
        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * from Users";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);

        ResultSet queryResult = pQueryStmt.executeQuery();


        ObservableList<User> retrievedUsers = getUsersFromRS(queryResult);

        //freeing resources
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

        //create and execute prepared statement
        PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStmt.setString(1, user.getName());
        pUpdateStmt.setInt(2, user.getAge());
        pUpdateStmt.setDouble(3,  user.getHeight());
        pUpdateStmt.setDouble(4, user.getWeight());
        pUpdateStmt.setString(5, user.getGender());
        pUpdateStmt.executeUpdate();

        //retrieve generated primary key
        ResultSet results = pUpdateStmt.getGeneratedKeys();
        results.next();
        int userID = results.getInt(1);

        //free resources
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
        //check if the user exists in the database by searching for the user via their unique id
        if (getUserFromDB(user.getId()) != null) {

            Connection dbConn = DatabaseOperations.connectToDB();
            //create an execute a prepared statement
            PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStmt.setString(1, user.getName());
            pUpdateStmt.setInt(2, user.getAge());
            pUpdateStmt.setDouble(3, user.getHeight());
            pUpdateStmt.setDouble(4, user.getWeight());
            pUpdateStmt.setInt(5, user.getId());
            pUpdateStmt.executeUpdate();

            //freeing resources
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

        //create and execute prepared statement.
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, userID);
        pDeleteStmt.executeUpdate();

        //freeing resources
        pDeleteStmt.close();
        DatabaseOperations.disconnectFromDB();

        //check for whether the deleted user still exists in the db
        if (getUserFromDB(userID) == null) {
            return true;
        } else {
            return false;
        }

    }



}
