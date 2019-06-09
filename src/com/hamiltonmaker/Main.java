package com.hamiltonmaker;

import com.hamiltonmaker.Comun.Utils.AlertManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Hamilton Maker");
        this.primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/recursos/imagenes/ic_icono.png")));

        initRootLayout();

        mostrarMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Vistas/VistaPrincipal.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

    public void mostrarMenu() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Vistas/VistaMenu.fxml"));
            AnchorPane generador = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(generador);
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
