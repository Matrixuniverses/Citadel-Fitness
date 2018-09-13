package seng202.group2;

import junit.framework.TestCase;

public class TestTest extends TestCase {
    int value1 = -5;
    int value2 = 25;

    public void test(){
        int result = value1 + value2;
        assert(result == 20);
    }

}
