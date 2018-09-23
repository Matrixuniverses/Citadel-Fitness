package seng202.group2.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seng202.group2.data.FileFormatException;
import seng202.group2.data.DataParser;

import java.io.File;

import static org.junit.Assert.fail;


public class ParserFileTests {
    private String testData = "src/test/java/seng202/group2/testData";

    @Rule
    public ExpectedException thrown =  ExpectedException.none();

    @Test
    public void TestFileNotFound() throws Exception {

        thrown.expect(FileFormatException.class);
        thrown.expectMessage("File not found");
        DataParser parser = new DataParser(new File("file/that/does/not/exist"));
    }

    @Test
    public void TestNullFilePointer() throws Exception {
        thrown.expect(FileFormatException.class);
        thrown.expectMessage("File is null");
        DataParser parser = new DataParser(null);
    }


    @Test
    public void TestIncorrectExtension() throws Exception {
        thrown.expect(FileFormatException.class);
        thrown.expectMessage("Incorrect file format");
        DataParser parser = new DataParser(new File(testData + "/csv.txt"));
    }

    @Test
    public void TestNormalCSVFile() {
        try {
            DataParser parser = new DataParser(new File(testData + "/all.csv"));
        } catch (Exception e) {
            fail("Should not get exception");
        }
    }
}
