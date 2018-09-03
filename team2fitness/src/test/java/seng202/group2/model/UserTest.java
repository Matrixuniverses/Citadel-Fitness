package seng202.group2.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;




public class UserTest {

    User user;
    @Before public void setup(){
        user = new User("Adam", 20, 1.80, 80);
    }

    @Test
    public void testBmi(){
        assertEquals(24.69, user.getBmi(), 1e-2);
    }

    @Test
    public void testBmiWeightUpdate(){
        user.setWeight(120);
        assertEquals(37.04, user.getBmi(), 1e-2);
    }

    @Test
    public void testBmiHeightUpdate(){
        user.setHeight(1.90);
        assertEquals(22.16, user.getBmi(), 1e-2);
    }

}
