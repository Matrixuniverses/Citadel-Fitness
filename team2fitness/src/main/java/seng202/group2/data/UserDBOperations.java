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
     * @param user_id the id of the user that will be searched for.
     * @return the User associated with the user id from the data if the user_id exists in the data.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static User getUserFromRS(int user_id) throws SQLException {

        DatabaseOperations.connectToDB();

        String sqlQuery = "SELECT * FROM Users WHERE user_id = "+ user_id + ";";

        ResultSet queryResult = DatabaseOperations.executeDBQuery(sqlQuery);

        User retrievedUser = null;

        // TODO - Replace this with an index structure @Chris
        if (queryResult.next()) {

            int id = queryResult.getInt("user_id");
            String name = queryResult.getString("name");
            int age = queryResult.getInt("age");
            double height = queryResult.getDouble("height");
            double weight = queryResult.getDouble("weight");
            retrievedUser = new User(id,name, age, height, weight);



        }
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
        DatabaseOperations.createDatabase();
        DatabaseOperations.connectToDB();

        String sqlQuery = "SELECT * from Users";

        ResultSet queryResult = DatabaseOperations.executeDBQuery(sqlQuery);


        ObservableList<User> retrievedUsers = FXCollections.observableArrayList();

        while (queryResult.next()) {
            int id = queryResult.getInt("user_id");
            String name = queryResult.getString("name");
            int age = queryResult.getInt("age");
            double height = queryResult.getDouble("height");
            double weight = queryResult.getDouble("weight");
            User retrievedUser = new User(id,name, age, height, weight);

            retrievedUsers.add(retrievedUser);
        }
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
        DatabaseOperations.connectToDB();
        String sqlInsertStmt = "INSERT INTO Users(name, age, height, weight) VALUES(?,?,?,?)";

        Connection dbConn = DatabaseOperations.getDbConnection();

        PreparedStatement pInsertStmt = dbConn.prepareStatement(sqlInsertStmt);
        pInsertStmt.setString(1, user.getName());
        pInsertStmt.setInt(2, user.getAge());
        pInsertStmt.setDouble(3,  user.getHeight());
        pInsertStmt.setDouble(4, user.getWeight());
        pInsertStmt.executeUpdate();

        ResultSet results = pInsertStmt.getGeneratedKeys();
        results.next();
        int user_id = results.getInt(1);

        pInsertStmt.close();
        DatabaseOperations.disconnectFromDB();
        return user_id;

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
            DatabaseOperations.connectToDB();
            Connection dbConn = DatabaseOperations.getDbConnection();

            PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStatement.setString(1, user.getName());
            pUpdateStatement.setInt(2, user.getAge());
            pUpdateStatement.setDouble(3, user.getHeight());
            pUpdateStatement.setDouble(4, user.getWeight());
            pUpdateStatement.setInt(5, user.getId());
            pUpdateStatement.executeUpdate();

            pUpdateStatement.close();
            DatabaseOperations.disconnectFromDB();
            return true;
        } else {

            return false;
        }


    }


    /**
     * Deletes a user from the data using the user's ID.
     * This function automatically connects to and disconnects from the data.
     * @param userId The ID of the user to be removed from the data
     * @return true if the user with the inputted id no longer exists within the data
     * @throws SQLException if an sql related error occurs while attempting to delete a user from the database.
     */
    public static boolean deleteExistingUser(int userId) throws SQLException {
        DatabaseOperations.connectToDB();
        String sqlDeleteStmt = "DELETE FROM Users WHERE user_id = ?";
        Connection dbConn = DatabaseOperations.getDbConnection();
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, userId);
        pDeleteStmt.executeUpdate();

        pDeleteStmt.close();
        DatabaseOperations.disconnectFromDB();
        if (getUserFromRS(userId) == null) {
            return true;
        } else {
            return false;
        }

    }



}
