package seng202.group2.data_functions;

/**
 * If an error occurs with reading a given CSV file or parsing the data provided, then a FileFormatException will
 * be thrown. Optionally allows for caller to view the line that caused a parser error
 */
public class FileFormatException extends Exception {

    private String[] offendingLine;

    /**
     * Error occurs when reading or parsing a CSV file
     * @param offendingLine Read line from CSV parser that caused a parser error, or incorrect format
     * @param message       Error message to be displayed to the caller
     */
    public FileFormatException(String[] offendingLine, String message) {
        super(message);
        this.offendingLine = offendingLine;
    }

    public String[] getLine() {
        return this.offendingLine;
    }

}
