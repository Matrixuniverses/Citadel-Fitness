package seng202.group2.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;




public class UserTest {

    User user;
    @Before public void setup(){
        user = new User(1, "Adam", 20, 180, 80);
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
        user.setHeight(190);
        assertEquals(22.16, user.getBmi(), 1e-2);
    }

}
