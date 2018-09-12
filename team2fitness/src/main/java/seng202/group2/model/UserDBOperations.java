package seng202.group2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.data_functions.databaseWriter;

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

        String sqlQuery = "SELECT * FROM users WHERE user_id = "+ user_id;
        databaseWriter.connectToDB();
        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQuery);
        databaseWriter.disconnectFromDB();
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

        String sqlQuery = "SELECT * from users";
        databaseWriter.connectToDB();
        ResultSet queryResult = databaseWriter.executeDBQuery(sqlQuery);
        databaseWriter.disconnectFromDB();

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
        String sqlInsertStmt = "BEGIN\n" +
                "INSERT INTO users\n" +
                "(name, age, height, weight)\n" +
                "VALUES\n" +
                "('" + userName + "', "+ userAge +" ,"+ userHeight +", " + userWeight + ");\n" +
                "END;";

        databaseWriter.connectToDB();
        databaseWriter.executeDBUpdate(sqlInsertStmt);
        databaseWriter.disconnectFromDB();
    }


}
