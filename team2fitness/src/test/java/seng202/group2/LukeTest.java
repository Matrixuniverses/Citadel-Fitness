package seng202.group2;

import junit.framework.*;
public class LukeTest extends TestCase {

    int value1 = -5;
    int value2 = 250;

    public void test(){
        int result = value1 + value2;
        assert(result == 20);
    }
}
