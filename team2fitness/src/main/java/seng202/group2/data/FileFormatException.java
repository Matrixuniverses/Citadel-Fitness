package seng202.group2.data;

/**
 * If an error occurs with reading a given CSV file or parsing the data provided, then a FileFormatException will
 * be thrown. Optionally allows for caller to view the line that caused a parser error
 */
public class FileFormatException extends Exception {

    /**
     * Error occurs when reading or parsing a CSV file
     * @param message Error message to be displayed to the caller
     */
    public FileFormatException(String message) {
        super(message);
    }

}
