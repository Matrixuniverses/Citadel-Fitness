package seng202.group2.data_functions;

public class FileFormatException extends Exception{

    private String[] offendingLine;

    public FileFormatException(String[] offendingLine, String message){
        super(message);
        this.offendingLine = offendingLine;
    }

    public String[] getLine(){
        return this.offendingLine;
    }

}
