package de.slothsoft.gaugebar;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Control;

public class GaugeBar extends Control {

    public static final EventType<Event> EVENT_TYPE_CHANGE_VALUE = new EventType<Event>(EventType.ROOT, "de.slothsoft.gaugebar.GaugeBar.EVENT_TYPE_CHANGE_VALUE");
    public static final EventType<Event> EVENT_TYPE_CHANGE_MAX_VALUE = new EventType<Event>(EventType.ROOT, "de.slothsoft.gaugebar.GaugeBar.EVENT_TYPE_CHANGE_MAX_VALUE");

    protected int maxValue = 100;
    protected int value = this.maxValue;

    public GaugeBar() {
	setSkin(new GaugeBarSkin(this));
    }

    public int getMaxValue() {
	return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
	if (maxValue < this.value)
	    throw new IllegalArgumentException("Max value must be bigger than value!");
	this.maxValue = maxValue;
	fireEvent(new Event(Integer.valueOf(maxValue), this, EVENT_TYPE_CHANGE_MAX_VALUE));
    }

    public void setValue(int value) {
	if (this.maxValue < value)
	    throw new IllegalArgumentException("Value must be smaller than max value!");
	if (value < 0)
	    throw new IllegalArgumentException("Value must be bigger than zero!");
	this.value = value;
	fireEvent(new Event(Integer.valueOf(value), this, EVENT_TYPE_CHANGE_VALUE));
    }

    public int getValue() {
	return this.value;
    }

}
