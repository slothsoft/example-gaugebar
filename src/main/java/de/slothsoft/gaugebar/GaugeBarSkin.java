package de.slothsoft.gaugebar;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

public class GaugeBarSkin implements Skin<GaugeBar> {

	private static final int GAUGE_BORDER = 2;
	private static final int GAUGE_MAX_SIZE = 10;
	private static final int TICK_DEGREE = 10;

	private final GaugeBar gaugeBar;
	private Group rootNode;
	private final int size = 50;

	public GaugeBarSkin(GaugeBar gaugeBar) {
		this.gaugeBar = gaugeBar;
		hookEventHandler();
	}

	private void hookEventHandler() {
		this.gaugeBar.addEventHandler(GaugeBar.EVENT_TYPE_CHANGE_VALUE, event -> redraw());
		this.gaugeBar.addEventHandler(GaugeBar.EVENT_TYPE_CHANGE_MAX_VALUE, event -> redraw());
	}

	@Override
	public GaugeBar getSkinnable() {
		return this.gaugeBar;
	}

	@Override
	public Node getNode() {
		if (this.rootNode == null) {
			this.rootNode = new Group();
			redraw();
		}
		return this.rootNode;
	}

	protected void redraw() {
		final List<Node> rootChildren = new ArrayList<>();
		rootChildren.add(createBackground());
		rootChildren.add(createGauge());
		rootChildren.add(createTicks());
		rootChildren.add(createGaugeBlend());
		rootChildren.add(createBorder());
		this.rootNode.getChildren().setAll(rootChildren);
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	private Node createBackground() {
		return new Circle(this.size, this.size, this.size + 1);
	}

	private Node createGauge() {
		final Stop[] stops = new Stop[]{new Stop(0, Color.LIGHTGREEN), new Stop(1, Color.DARKGREEN)};
		final Circle circle = new Circle(this.size, this.size, this.size - 2 * GAUGE_BORDER);
		circle.setFill(new LinearGradient(1, 0, 0.5, 1, true, CycleMethod.NO_CYCLE, stops));
		circle.getStyleClass().add("gauge");
		return circle;
	}

	private Node createTicks() {
		final Path tickMarks = new Path();
		final ObservableList<PathElement> pathChildren = tickMarks.getElements();
		for (int i = 0; i < 360; i += TICK_DEGREE) {
			pathChildren.add(new MoveTo(this.size, this.size));
			pathChildren.add(new LineTo(this.size * Math.cos(Math.toRadians(i)) + this.size,
					this.size * Math.sin(Math.toRadians(i)) + this.size));
		}
		return tickMarks;
	}

	private Node createGaugeBlend() {
		final Group group = new Group();

		final float arcBlendDegrees = 130 + (1 - (float) this.gaugeBar.value / this.gaugeBar.maxValue) * 230;
		final Arc arcBlend = new Arc(this.size, this.size, this.size, this.size, -90, arcBlendDegrees);
		arcBlend.setType(ArcType.ROUND);
		arcBlend.setFill(Color.BLACK);

		final Circle circleBlend = new Circle(this.size, this.size + 3 * GAUGE_MAX_SIZE / 2,
				this.size - 2 * GAUGE_MAX_SIZE);
		circleBlend.setFill(Color.BLACK);

		group.getChildren().setAll(arcBlend, circleBlend);
		return group;
	}

	private Node createBorder() {
		final Circle circle = new Circle(this.size, this.size, this.size);
		circle.setFill(null);
		circle.setStroke(Color.WHITE);
		return circle;
	}
}