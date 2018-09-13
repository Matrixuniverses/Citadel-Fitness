package seng202.group2.data_functions;

import org.omg.CORBA.ACTIVITY_COMPLETED;
import seng202.group2.model.Activity;

public class MalformedLine {
    private String message;
    private String[] line;
    private Activity activity;

    public MalformedLine(String[] line, Activity activity, String message) {
        this.line = line;
        this.activity = activity;
        this.message = message;
    }

    public String[] getLine() {
        return this.line;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public String getMessage() {
        return this.message;
    }

}
