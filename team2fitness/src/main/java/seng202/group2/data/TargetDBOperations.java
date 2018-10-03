package seng202.group2.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.model.Target;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TargetDBOperations {


    private static ObservableList<Target> getResultSetTargets(ResultSet rs) throws SQLException{

        ObservableList<Target> collectedTargets = FXCollections.observableArrayList();

        while (rs.next()) {
            int targetID = rs.getInt(1);
            String targetName = rs.getString(3);


            java.util.Date targetDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

            try {
                targetDate = dateFormatter.parse(rs.getString(4));
            } catch (ParseException e) {
                System.err.println("Unable to parse date");
                e.printStackTrace();
            }
            String targetType = rs.getString(5);

            double targetInitVal = rs.getDouble(6);
            double targetCurrVal = rs.getDouble(7);
            double targetFinVal = rs.getDouble(8);

            Target newTarget = new Target(targetName, targetDate, targetType, targetInitVal, targetCurrVal, targetFinVal);
            newTarget.setId(targetID);
            collectedTargets.add(newTarget);
        }

        return collectedTargets;
    }

    /**
     *
     * @param userID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Target> getAllUserTargets(int userID) throws SQLException {

        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Targets WHERE user_id = ?";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, userID);
        ResultSet queryResult = pQueryStmt.executeQuery();

        ObservableList<Target> collectedTargets = getResultSetTargets(queryResult);

        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();
        return collectedTargets;


    }

    /**
     *
     * @param targetID
     * @return
     * @throws SQLException
     */
    public static Target getTargetFromDB(int targetID) throws SQLException {

        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlQueryStmt = "SELECT * FROM Targets WHERE target_id = ?";
        PreparedStatement pQueryStmt = dbConn.prepareStatement(sqlQueryStmt);
        pQueryStmt.setInt(1, targetID);
        ResultSet queryResult = pQueryStmt.executeQuery();

        Target collectedTarget = getResultSetTargets(queryResult).get(0);

        pQueryStmt.close();
        DatabaseOperations.disconnectFromDB();
        return collectedTarget;
    }

    /**
     *
     * @param target
     * @param userID
     * @return
     * @throws SQLException
     */
    public static int insertNewTarget(Target target, int userID) throws SQLException {

        //check whether the user identified by their userId is in the database
        if (UserDBOperations.getUserFromRS(userID) == null) {
            return -1;
        }

        Connection dbConn = DatabaseOperations.connectToDB();
        String sqlInsertStmt = "INSERT INTO Targets(user_id, name, date_achieved, type, initial_value, current_value, final_value) VALUES (?,?,?,?,?,?,?)";

        PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlInsertStmt);
        pUpdateStmt.setInt(1, userID);
        pUpdateStmt.setString(2, target.getName());
        pUpdateStmt.setString(3, target.getCompletionDate().toString());
        pUpdateStmt.setString(4, target.getType());
        pUpdateStmt.setDouble(5, target.getInitialValue());
        pUpdateStmt.setDouble(6, target.getCurrentValue());
        pUpdateStmt.setDouble(7, target.getFinalValue());
        pUpdateStmt.executeUpdate();

        ResultSet genKeysRS = pUpdateStmt.getGeneratedKeys();
        genKeysRS.next();

        int targetID = genKeysRS.getInt(1);


        pUpdateStmt.close();
        DatabaseOperations.disconnectFromDB();
        return targetID;
    }

    /**
     *
     * @param target
     * @return
     * @throws SQLException
     */
    public static boolean updateExistingTarget(Target target) throws SQLException {

        String sqlUpdateStmt = "UPDATE Targets SET name = ?, date_achieved = ?, type = ?, initial_value = ?, current_value = ?, final_value = ? WHERE target_id = ?";

        if (getTargetFromDB(target.getId()) != null) {

            Connection dbConn = DatabaseOperations.connectToDB();

            PreparedStatement pUpdateStmt = dbConn.prepareStatement(sqlUpdateStmt);
            pUpdateStmt.setString(1, target.getName());
            pUpdateStmt.setString(2, target.getCompletionDate().toString());
            pUpdateStmt.setString(3, target.getType());
            pUpdateStmt.setDouble(4, target.getInitialValue());
            pUpdateStmt.setDouble(5, target.getCurrentValue());
            pUpdateStmt.setDouble(6, target.getFinalValue());
            pUpdateStmt.setInt(7, target.getId());
            pUpdateStmt.executeUpdate();

            pUpdateStmt.close();
            DatabaseOperations.disconnectFromDB();

            return true;

        } else {

            return false;
        }
    }


    /**
     *
     * @param targetID
     * @return
     * @throws SQLException
     */
    public static boolean deleteExistingTarget(int targetID) throws SQLException {

        Connection dbConn = DatabaseOperations.connectToDB();

        String sqlDeleteStmt = "DELETE FROM Targets WHERE target_id = ?";


        PreparedStatement pDeleteStmt = dbConn.prepareStatement(sqlDeleteStmt);
        pDeleteStmt.setInt(1, targetID);
        pDeleteStmt.executeUpdate();

        DatabaseOperations.disconnectFromDB();

        return (getTargetFromDB(targetID) == null);
    }





}
