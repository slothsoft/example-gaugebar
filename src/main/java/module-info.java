module de.slothsoft.gaugebar {

	// Java Jigsaw: "Let's play a game!"
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.swing; // we don't; but how to define test dependencies?

	// also Jigsaw is just a poor man's OSGi
	opens de.slothsoft.gaugebar to javafx.fxml, javafx.graphics;
}