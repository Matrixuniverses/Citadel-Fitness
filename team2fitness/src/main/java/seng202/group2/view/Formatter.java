package seng202.group2.view;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

    private Object val;
    private String type;

    public Formatter() {
    }

    public void formatDate(Date date) {
        val = date;
        type = "date";
    }

    public void formatTime(double time) {
        val = time;
        type = "time";
    }

    public void formatDistance(double dist) {
        val = dist;
        type = "dist";
    }

    /**
     * Override toString of dates and times so new formatted strings are displayed
     * @return String containing reformatted value
     */
    @Override
    public String toString() {
        if (this.type.equals("date")) {
            return new SimpleDateFormat("MMMM d, YYYY").format(this.val);
        }

        if (this.type.equals("time")) {
            double sec = ((Double) this.val).intValue();
            return String.format("%.0fh %.0fm %.0fs", sec / 3600, (sec % 3600) / 60, (sec % 60));
        }

        if (this.type.equals("dist")) {
            return String.format("%.0fm", (Double) this.val);
        }

        return null;
    }
}
