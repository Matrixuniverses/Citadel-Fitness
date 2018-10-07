package seng202.group2.data;

import seng202.group2.model.Activity;

/**
 * Used to display what lines were not parsed by the Dataparser, useful for checking if an activity has been
 * corrected or simply dropped
 */
public class MalformedLine {
    private String message;
    private String[] line;
    private Activity activity;

    /**
     * Creates a new malformed line attached to the activity that it would have been inserted into, along with a
     * message stating why the line is malformed
     * @param line String[] with the line that is malformed
     * @param activity Activity that the line was to be added to
     * @param message String containing a message as to why the line is malformed
     */
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
