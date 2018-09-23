package seng202.group2.view;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter extends Date {

    public DateFormatter(Date date) {
        super(date.getTime());
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("MMMM d, YYYY").format(this);
    }
}
