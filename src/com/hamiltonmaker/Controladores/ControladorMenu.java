package com.hamiltonmaker.Controladores;

import com.hamiltonmaker.Comun.Utils.AlertManager;
import com.hamiltonmaker.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Descripción: Controlador de la vista de Menu Principal
 * Autor: Alexander Garcia
 */
public class ControladorMenu {

    @FXML
    AnchorPane contenedor;
    @FXML
    Button generador;
    @FXML
    Button algoritmo;
    @FXML
    Button soluciones;
    @FXML
    Button exportar;

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
        soluciones.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirSoluciones();
            }
        });
        exportar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirExportar();
            }
        });

    }

    private void abrirGenerador(){
        try {
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaGeneradorCaminos.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

    private void abrirAlgoritmo(){
        try {
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaAlgoritmo.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

    private void abrirSoluciones(){
        try {
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaSoluciones.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

    private void abrirExportar(){
        try {
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaExportar.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

}
