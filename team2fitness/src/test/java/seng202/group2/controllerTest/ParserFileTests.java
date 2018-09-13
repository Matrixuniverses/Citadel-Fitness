package seng202.group2.controllerTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seng202.group2.data_functions.FileFormatException;
import seng202.group2.data_functions.Parser;

import java.io.File;


public class ParserFileTests {

    @Rule
    public ExpectedException thrown =  ExpectedException.none();

    @Test
    public void TestFileNotFound() throws FileFormatException {

        thrown.expect(FileFormatException.class);
        thrown.expectMessage("File not found");
        Parser parser = new Parser(new File("file/that/does/not/exist"));
    }

    @Test
    public void TestCorruptFile() throws FileFormatException {
        thrown.expect(FileFormatException.class);
        thrown.expectMessage("Unreadable file");
        Parser parser = new Parser(new File(""));
    }
}
