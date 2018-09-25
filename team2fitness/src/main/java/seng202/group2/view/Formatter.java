package seng202.group2.view;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

    private Object val;

    /**
     * Called when formatting of a date is required
     * @param date Date to format
     */
    public Formatter (Date date) {
        this.val = date;
    }

    /**
     * Called when formatting a given number of seconds into time is needed
     * @param time Number of seconds to format into HH:MM:SS
     */
    public Formatter (double time) {
        this.val = time;
    }

    /**
     * Override toString of dates and times so new formatted strings are displayed
     * @return String containing reformatted value
     */
    @Override
    public String toString() {
        if (this.val instanceof Date) {
            return new SimpleDateFormat("MMMM d, YYYY").format(this.val);
        }

        if (this.val instanceof Double) {
            double sec = ((Double) this.val).intValue();

            return String.format("%.0fh %.0fm %.0fs", sec / 3600, (sec % 3600) / 60, (sec % 60));
        }
        return null;
    }
}
