package seng202.group2;

import junit.framework.TestCase;

public class SamTest extends TestCase {
    int test_1 = 100;
    int test_2 = 400;

    public void test(){
        assert((test_1 + test_2) < 600);
    }
}
