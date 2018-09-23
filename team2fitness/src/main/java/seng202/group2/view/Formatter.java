package seng202.group2.view;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

    private Object val;

    public Formatter (Date date) {
        this.val = date;
    }

    public Formatter (double time) {
        this.val = time;
    }

    @Override
    public String toString() {
        if (this.val instanceof Date) {
            return new SimpleDateFormat("MMMM d, YYYY").format(this.val);
        }

        if (this.val instanceof Double) {
            int sec = ((Double) this.val).intValue();

            return String.format("%d:%02d:%02d", sec / 3600, (sec % 3600) / 60, (sec % 60));
        }
        return null;
    }
}
