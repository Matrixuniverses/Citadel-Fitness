package seng202.group2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.data_functions.databaseWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBOperations {

    /**
     * Searches the database for a user in the table users using a given user_id and returns the User if the
     * user has be found by the query in the database.
     * @param user_id the id of the user that will be searched for.
     * @return the User associated with the user id from the database if the user_id exists in the database.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static User getUserFromRS(int user_id) throws SQLException {

        databaseWriter.connectToDB();

        String sqlQuery = "SELECT * FROM Users WHERE user_id = "+ user_id + ";";

        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQuery);

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
        databaseWriter.disconnectFromDB();


        return retrievedUser;


    }


    /**
     * Queries the database for all user records in the database and returns them as an observable list.
     * @return an Observable list of all the current users in the database.
     * @throws SQLException if any error occurs preforming the sql operations on the database.
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        databaseWriter.connectToDB();

        String sqlQuery = "SELECT * from Users";

        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQuery);


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
        databaseWriter.disconnectFromDB();

        return retrievedUsers;



    }


    /**
     * Inserts a new User into the database.
     * @param user The User object to be stored in the database.
     * @throws SQLException If there is a sql related error when trying to preform the insert operation on the database.
     */
    public static void insertNewUser(User user) throws SQLException {
        databaseWriter.connectToDB();
        String sqlInsertStmt = "INSERT INTO Users(name, age, height, weight) VALUES(?,?,?,?)";

        Connection dbConn = databaseWriter.getDbConnection();

        PreparedStatement pInsertStmt = dbConn.prepareStatement(sqlInsertStmt);
        pInsertStmt.setString(1, user.getName());
        pInsertStmt.setInt(2, user.getAge());
        pInsertStmt.setDouble(3,  user.getHeight());
        pInsertStmt.setDouble(4, user.getWeight());
        pInsertStmt.executeUpdate();
        databaseWriter.disconnectFromDB();




    }


    /**
     * Updates a user that is already stored within the database
     * @param user The updated version of the a particular USer object that will have its database record updated
     * @return true if the user does exist inside the database. false otherwise
     * @throws SQLException If there was a sql related error when trying to update a user in the database.
     */
    public static boolean updateExistingUser(User user) throws SQLException{

        String sqlUpdateStmt = "UPDATE Users SET name = ?, age = ?, height = ?, weight = ? WHERE user_id = ?";
        if (getUserFromRS(user.getId()) != null) {
            databaseWriter.connectToDB();
            Connection dbConn = databaseWriter.getDbConnection();

            PreparedStatement pUpdateStatement = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStatement.setString(1, user.getName());
            pUpdateStatement.setInt(2, user.getAge());
            pUpdateStatement.setDouble(3, user.getHeight());
            pUpdateStatement.setDouble(4, user.getWeight());
            pUpdateStatement.setInt(5, user.getId());
            pUpdateStatement.executeUpdate();
            databaseWriter.disconnectFromDB();
            return true;
        } else {

            return false;
        }


    }


    /**
     * Deletes a user from the database using the user's ID.
     * @param userId The ID of the user to be removed from the database
     * @return true if the user with the inputted id no longer exists within the database
     * @throws SQLException if an sql related error occurs while attempting to delete a user from the database.
     */
    public static boolean deleteExistingUser(int userId) throws SQLException {
        databaseWriter.connectToDB();
        String sqlDeleteStmt = "DELETE FROM Users WHERE user_id = ?";
        Connection dbConn = databaseWriter.getDbConnection();
        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, userId);
        pDeleteStmt.executeUpdate();
        databaseWriter.disconnectFromDB();
        if (getUserFromRS(userId) == null) {
            return true;
        } else {
            return false;
        }

    }






    //Function for manually testing UserDBOperations.
    //Uncomment this method to test functionality
    //Creates DB
    //Inserts 5 users into the Users Table
    //Changes user with id 2's name to Bob
    //Prints out User 3's details (id, name and bmi)
    //Deletes user 3 and displays the results
    //Inserts another user called Jimmy and displays the results

    /*public static void main(String[] args) {
        try {

            databaseWriter.createDatabase();
            User testUser = new User(0,"Test0", 17, 1.8, 75);
            for (int i = 1; i < 6; i++) {
                testUser.setName(testUser.getName().substring(0, testUser.getName().length() - 1) + Integer.toString(i));
                insertNewUser(testUser);
            }
            User editedUser = getUserFromRS(2);
            editedUser.setName("Bob");
            System.out.println("User has been updated: " + updateExistingUser(editedUser));




            ObservableList<User> retrievedUsers = getAllUsers();
            if (retrievedUsers != null) {
                for (User user : retrievedUsers) {
                    System.out.println(user.getId() + " " + user.getName());

                }
            }
            System.out.println("");
            User user3 = getUserFromRS(3);
            if (user3 != null) {
                System.out.println(user3.getId() + " name =" + user3.getName() + ", bmi = " + user3.getBmi());
                System.out.println(deleteExistingUser(3));
            }

            System.out.println("");

            retrievedUsers = getAllUsers();
            if (retrievedUsers != null) {
                for (User user : retrievedUsers) {
                    System.out.println(user.getId() + " " + user.getName());

                }
            }
            System.out.println("");
            User insertedUser = new User(0,"Jimmy", 18, 1.6, 65);
            insertNewUser(insertedUser);
            System.out.println("");
            System.out.println("After inserting Jimmy:");
            retrievedUsers = getAllUsers();
            if (retrievedUsers != null) {
                for (User user : retrievedUsers) {
                    System.out.println(user.getId() + " " + user.getName());

                }
            }



        } catch (SQLException e) {
            e.printStackTrace();

        }


    }*/



}
