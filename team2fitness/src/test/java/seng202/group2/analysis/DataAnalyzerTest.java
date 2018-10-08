package seng202.group2.analysis;

import org.junit.Test;
import seng202.group2.analysis.DataAnalyzer;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class DataAnalyzerTest {

    @Test
    public void calcDistanceCrossesLongitudeLineCorrectly() {
        double metres;
        double quartEarthCircum = 10020000;

        metres = DataAnalyzer.calcDistance(-17.7850915, 178.212, -13.647548, -172.741952);
        assertTrue(metres < quartEarthCircum);
    }

    @Test
    public void calcDistanceReturnsCorrectResult1() {
        double metres;
        double umtPythag;
        double pythagError;

        metres = DataAnalyzer.calcDistance(-43.520745, 172.628365, -43.520800, 172.634780);
        umtPythag = Math.sqrt(Math.pow(631599.94 - 632118.27, 2) + Math.pow(5180066.78 - 5180050.51, 2));
        pythagError = 0.0025 * umtPythag;
        assertEquals(umtPythag, metres, pythagError);   // Pythagoras is less accurate than the calcDistance method
    }

    @Test
    public void calcDistanceReturnsCorrectResult2() {
        double metres;
        double umtPythag;
        double pythagError;

        metres = DataAnalyzer.calcDistance(-43.511429, 172.620444, -43.528793, 172.633692);
        umtPythag = Math.sqrt(Math.pow(630979.93 - 632012.90, 2) + Math.pow(5181113.93 - 5179164.52, 2));
        pythagError = 0.0025 * umtPythag;
        assertEquals(umtPythag, metres, pythagError);   // Pythagoras is less accurate than the calcDistance method
    }

    @Test
    public void calcDistanceReturnsCorrectResult3() {
        double metres;
        double umtPythag;
        double pythagError;

        metres = DataAnalyzer.calcDistance(-43.522526, 172.613248, -43.52254, 172.613272);
        umtPythag = Math.sqrt(Math.pow(630374.37 - 630376.28, 2) + Math.pow(5179892.79 - 5179891.20, 2));
        pythagError = 0.0025 * umtPythag;
        assertEquals(umtPythag, metres, pythagError);   // Pythagoras is less accurate than the calcDistance method
    }

    @Test
    public void calcAverageSpeedReturnsCorrectResult1() {
        double averageSpeed;

        averageSpeed = DataAnalyzer.calcAverageSpeed(100, 30, 40);
        assertEquals(10.0, averageSpeed);
    }

    @Test
    public void calcAverageSpeedReturnsCorrectResult2() {
        double averageSpeed;

        averageSpeed = DataAnalyzer.calcAverageSpeed(139.567, 1045.64, 1101.09);
        assertEquals(2.517, averageSpeed, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calcAverageSpeedReturnsCorrectResult3() {
        DataAnalyzer.calcAverageSpeed(139.567, 101.0, 101.0);
    }

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

    @Test(expected = IllegalArgumentException.class)
    public void calcBMIReturnsCorrectResult3() {
        DataAnalyzer.calcBMI(0.0, 97.2);
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
    public void isCardiovascularMortalityProne1() {
        boolean result;

        result = DataAnalyzer.cardiovascularMortalityProne(17, 85);
        assertFalse(result);
    }

    @Test
    public void isCardiovascularMortalityProne2() {
        boolean result;

        result = DataAnalyzer.cardiovascularMortalityProne(18, 83);
        assertFalse(result);
    }

    @Test
    public void isCardiovascularMortalityProne3() {
        boolean result;

        result = DataAnalyzer.cardiovascularMortalityProne(17, 84);
        assertFalse(result);
    }
    /*
    @Test
    public void convertsToValidSearchTerm1() {
        DataAnalyzer.webSearch_Google("usingValidChars");
    }

    @Test
    public void convertsToValidSearchTerm2() {
        DataAnalyzer.webSearch_Google("using!nval!dCh&r5");
    }

    @Test
    public void convertsToValidSearchTerm3() {
        DataAnalyzer.webSearch_Google("noSpacesHere");
    }

    @Test
    public void convertsToValidSearchTerm4() {
        DataAnalyzer.webSearch_Google("multiple spaces here");
    }
    */
}