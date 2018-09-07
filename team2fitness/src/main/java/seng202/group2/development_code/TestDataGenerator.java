package seng202.group2.development_code;

import seng202.group2.data_functions.FileFormatException;
import seng202.group2.data_functions.Parser;
import seng202.group2.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestDataGenerator {

    public static User createUser1() {
        Parser parser = null;
        User user = new User("Adam Conway", 20, 1.77, 70);
        try {
            File file = new File("team2fitness/src/main/java/seng202/group2/development_code/data/all.csv");
            parser = new Parser(file);
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


