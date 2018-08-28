package seng202.group2.controllerTest;

import seng202.group2.controller.DataAnalyzer;

import org.junit.Test;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class DataAnalyzerTest {
    @Test
    public void calcBMIReturnsCorrectResult1() {
        double BMI;

        BMI = DataAnalyzer.calcBMI(180, 80.5);
        BMI = (double)Math.round(BMI * 100d) / 100d;
        assertEquals(24.85, BMI);
    }

    @Test
    public void calcBMIReturnsCorrectResult2() {
        double BMI;

        BMI = DataAnalyzer.calcBMI(180, 97.2);
        BMI = (double)Math.round(BMI * 100d) / 100d;
        assertEquals(30.00, BMI);
    }

    @Test
    public void calcVo2MaxReturnsCorrectResult1() {
        double vo2Max;

        vo2Max = DataAnalyzer.calcVo2Max(25, 75);
        assertEquals(38.862, vo2Max);
    }

    @Test
    public void calcVo2MaxReturnsCorrectResult2() {
        double vo2Max;

        vo2Max = DataAnalyzer.calcVo2Max(54, 53);
        vo2Max = (double)Math.round(vo2Max * 1000d) / 1000d;
        assertEquals(49.133, vo2Max);
    }

    @Test
    public void calcVo2Max_PFWTReturnsCorrectResult1() {
        double vo2Max;

        vo2Max = DataAnalyzer.calcVo2Max_PFWT(80, 28, 19.31, 115, true);
        vo2Max = (double)Math.round(vo2Max * 1000d) / 1000d;
        assertEquals(33.707, vo2Max);
    }

    @Test
    public void calcCaloriesReturnsCorrectResult1() {
        double calories;

        calories = DataAnalyzer.calcCalories(63, 88.4, 130, 45, false);
        calories = (double)Math.round(calories * 100d) / 100d;
        assertEquals(335.64, calories);
    }

    @Test
    public void calcCaloriesReturnsCorrectResult2() {
        double calories;

        calories = DataAnalyzer.calcCalories(37, 65, 145, 45, true);
        calories = (double)Math.round(calories * 100d) / 100d;
        assertEquals(332.31, calories);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult1() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(4, 137);
        assertFalse(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult2() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(4, 138);
        assertTrue(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult3() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(5, 133);
        assertFalse(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult4() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(5, 134);
        assertTrue(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult5() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(8, 130);
        assertFalse(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult6() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(8, 131);
        assertTrue(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult7() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(12, 119);
        assertFalse(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult8() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(12, 120);
        assertTrue(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult9() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(16, 100);
        assertFalse(result);
    }

    @Test
    public void hasTachycardiaReturnsCorrectResult10() {
        boolean result;

        result = DataAnalyzer.hasTachycardia(16, 101);
        assertTrue(result);
    }

    @Test
    public void hasBradycardiaReturnsCorrectResult1() {
        boolean result;

        result = DataAnalyzer.hasBradycardia(14, 61);
        assertFalse(result);
    }

    @Test
    public void hasBradycardiaReturnsCorrectResult2() {
        boolean result;

        result = DataAnalyzer.hasBradycardia(6, 60);
        assertTrue(result);
    }

    @Test
    public void hasBradycardiaReturnsCorrectResult3() {
        boolean result;

        result = DataAnalyzer.hasBradycardia(17, 56);
        assertFalse(result);
    }

    @Test
    public void hasBradycardiaReturnsCorrectResult4() {
        boolean result;

        result = DataAnalyzer.hasBradycardia(15, 55);
        assertTrue(result);
    }

    @Test
    public void hasBradycardiaReturnsCorrectResult5() {
        boolean result;

        result = DataAnalyzer.hasBradycardia(18, 51);
        assertFalse(result);
    }

    @Test
    public void hasBradycardiaReturnsCorrectResult6() {
        boolean result;

        result = DataAnalyzer.hasBradycardia(18, 50);
        assertTrue(result);
    }

    @Test
    public void webSearch_GoogleOpensCorrectSeachTerm1() {
        DataAnalyzer.webSearch_Google("calculator");
    }
}