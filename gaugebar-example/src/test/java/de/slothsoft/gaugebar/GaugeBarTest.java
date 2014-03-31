package de.slothsoft.gaugebar;

import org.junit.Rule;
import org.junit.Test;

public class GaugeBarTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private GaugeBar classUnderTest = new GaugeBar();

    @Test
    public void testValue() {
	this.classUnderTest.setValue(50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueToBig() {
	this.classUnderTest.setValue(200);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueToSmall() {
	this.classUnderTest.setValue(-5);
    }

    @Test
    public void testMaxValue() {
	this.classUnderTest.setMaxValue(200);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxValueToSmall() {
	this.classUnderTest.setMaxValue(50);
    }
}
