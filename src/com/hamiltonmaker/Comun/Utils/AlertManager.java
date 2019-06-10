package com.hamiltonmaker.Comun.Utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Descripción: Clase auxiliar para mostrar mensajes en pantalla
 * Autor: Alexander Garcia
 */
public class AlertManager {

    public static void mostrarAlerta(String titulo, String mensaje){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(titulo);
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("\n"+mensaje);
                styleAlert(alert);
                alert.showAndWait();
            }
        });
    }

    private static void styleAlert(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(AlertManager.class.getResourceAsStream("/recursos/imagenes/ic_icono.png")));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertManager.class.getResource("/com/hamiltonmaker/Vistas/HamiltonMakerTheme.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
    }

    public static void alertarErrorBD(){
        AlertManager.mostrarAlerta("Error interno","Ha ocurrido un error con la base de datos, contacta al administrador si el mismo persiste");
    }

    public static void alertarError(){
        AlertManager.mostrarAlerta("Error interno","Ha ocurrido un error en la aplicaión, contacta al administrador si el mismo persiste");
    }
}
