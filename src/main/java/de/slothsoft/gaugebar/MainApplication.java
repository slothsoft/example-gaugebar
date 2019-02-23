package de.slothsoft.gaugebar;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		final TextField valueText = new TextField();
		valueText.setText("50");
		valueText.setLayoutX(10);
		valueText.setLayoutY(10);

		final Button button = new Button();
		button.setLayoutX(160);
		button.setLayoutY(10);
		button.setText("Change gauge");

		final Label responseLabel = new Label();
		responseLabel.setLayoutX(10);
		responseLabel.setLayoutY(40);

		final GaugeBar gaugeBar = new GaugeBar();
		gaugeBar.setLayoutX(10);
		gaugeBar.setLayoutY(70);

		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					final int value = Integer.parseInt(valueText.getText());
					responseLabel.setText("Set value to " + value + "!");
					gaugeBar.setValue(value);
				} catch (final NumberFormatException e) {
					responseLabel.setText("Value is no integer!");
				} catch (final Exception e) {
					responseLabel.setText(e.getMessage());
				}
			}
		});

		final Scene scene = new Scene(new Group(valueText, button, responseLabel, gaugeBar), 300, 200);
		// scene.getStylesheets().add("style.css");

		primaryStage.setTitle("Gauge Bar Demo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}