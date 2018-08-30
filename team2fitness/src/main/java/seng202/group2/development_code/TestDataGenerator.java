package seng202.group2.development_code;

import seng202.group2.data_functions.FileFormatException;
import seng202.group2.data_functions.Parser;
import seng202.group2.model.User;

import java.io.IOException;

public class TestDataGenerator {

    public static User createUser1() {
        Parser parser = null;
        User user = new User("Adam", 20, 177, 70);
        try {
            parser = new Parser("team2fitness/src/main/java/seng202/group2/development_code/data/all.csv");
        }
        catch (IOException ex_) {
            ex_.printStackTrace();
        }
        catch (FileFormatException f) {
            f.printStackTrace();
        }

        user.getActivityList().addAll(parser.getActivitiesRead());
        return user;
    }

    public static void main( String[] args ) {
        System.out.println(createUser1().getActivityList().get(0).getActivityName());
    }
}


