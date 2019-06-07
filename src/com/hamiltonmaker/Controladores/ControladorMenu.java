package com.hamiltonmaker.Controladores;

import com.hamiltonmaker.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ControladorMenu {

    @FXML
    AnchorPane contenedor;
    @FXML
    Button generador;
    @FXML
    Button algoritmo;

    @FXML
    private void initialize() {
        generador.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirGenerador();

            }
        });
        algoritmo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirAlgoritmo();

            }
        });
    }

    private void abrirGenerador(){
        try {
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaGeneradorCaminos.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirAlgoritmo(){
        try {
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaAlgoritmo.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
