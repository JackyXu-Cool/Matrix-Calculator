package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomePage extends Stage {
    private Button start = new Button("I want to start");
    private Button exit = new Button("Leave");
    private Label greeting = new Label("Welcome to my Matrix Calculator!");
    private BorderPane pane = new BorderPane();
    private VBox content = new VBox();

    WelcomePage() {
        start.setId("start");
        exit.setId("exit");
        pane.setCenter(content);
        pane.setId("welcomePane");
        greeting.setId("greeting");
        content.getChildren().add(greeting);
        content.getChildren().addAll(start,exit);
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER);
        start.setOnAction(e -> {
            new matrixCalculator();
            close();
        });
        exit.setOnAction(e -> {
            close();
        });
        Scene scene = new Scene(pane,450,450);
        scene.getStylesheets().add(WelcomePage.class.getResource("stylesheet.css").toExternalForm());
        setScene(scene);
        setTitle("Matrix Calculator");
        show();
    }
}
