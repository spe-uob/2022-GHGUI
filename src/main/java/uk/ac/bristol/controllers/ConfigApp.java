package uk.ac.bristol.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfigApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        ConfigUtil.ensureConfigFileExists();

        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10, 10, 10, 10));

        root = new VBox();

        final ObjectNode[] stringOptions = {ConfigUtil.getStringConfigOptions()};
        final ObjectNode[] booleanOptions = {ConfigUtil.getBooleanConfigOptions()};


        Label gitPathLabel = new Label("Git Path:");
        TextField gitPathField = new TextField(stringOptions[0].get("gitPath").asText());
        HBox gitPathBox = new HBox(gitPathLabel, gitPathField);
        gitPathBox.setSpacing(10);
        gitPathBox.setAlignment(Pos.CENTER_LEFT);


        Label shortcutLabel = new Label("Shortcut:");
        TextField shortcutField = new TextField(stringOptions[0].get("shortcut").asText());
        HBox shortcutBox = new HBox(shortcutLabel, shortcutField);
        shortcutBox.setSpacing(10);
        shortcutBox.setAlignment(Pos.CENTER_LEFT);


        Label darkModeLabel = new Label("Dark Mode:");
        CheckBox darkModeCheckBox = new CheckBox();
        darkModeCheckBox.setSelected(booleanOptions[0].get("darkMode").asBoolean());
        HBox darkModeBox = new HBox(darkModeLabel, darkModeCheckBox);
        darkModeBox.setSpacing(10);
        darkModeBox.setAlignment(Pos.CENTER_LEFT);


        Button saveButton = new Button("Save");
        Button resetButton = new Button("Reset to Default");
        HBox buttonBox = new HBox(saveButton, resetButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        saveButton.setOnAction(event -> {
            try {
                ConfigUtil.updateStringConfigOption("gitPath", gitPathField.getText());
                ConfigUtil.updateStringConfigOption("shortcut", shortcutField.getText());
                ConfigUtil.updateBooleanConfigOption("darkMode", darkModeCheckBox.isSelected());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        resetButton.setOnAction(event -> {
            try {
                ConfigUtil.resetPreferencesToDefault();
                stringOptions[0] = ConfigUtil.getStringConfigOptions();
                booleanOptions[0] = ConfigUtil.getBooleanConfigOptions();
                gitPathField.setText(stringOptions[0].get("gitPath").asText());
                shortcutField.setText(stringOptions[0].get("shortcut").asText());
                darkModeCheckBox.setSelected(booleanOptions[0].get("darkMode").asBoolean());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        root.getChildren().addAll(gitPathBox, shortcutBox, darkModeBox, buttonBox);


        VBox gitBox = new VBox(gitPathLabel,gitPathBox,shortcutLabel,shortcutBox,darkModeBox);
        VBox buttonBoxVBox = new VBox(buttonBox);
        Accordion accordion = new Accordion();
        TitledPane gitPane = new TitledPane("Preferences", gitBox);
        accordion.getPanes().addAll(gitPane);
        accordion.setExpandedPane(gitPane);
        root.getChildren().addAll(accordion, buttonBoxVBox);
        Scene scene = new Scene(root, 400, 240);
        primaryStage.setTitle("Config App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}