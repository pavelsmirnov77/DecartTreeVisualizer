package com.example.decarttree;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Decart tree visualizer");
        stage.getIcons().add(new Image("D:\\DecartTree\\src\\main\\resources\\com\\example\\decarttree\\image_tree.png"));
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("decart-tree-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1495, 800);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}