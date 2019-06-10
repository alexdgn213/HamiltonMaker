package com.hamiltonmaker.Controladores;

import com.hamiltonmaker.Comun.AlgoritmoGenetico.AlgotirmoGenetico;
import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import com.hamiltonmaker.Comun.Utils.AlertManager;
import com.hamiltonmaker.Main;
import com.hamiltonmaker.Persistencia.DAOCamino;
import com.hamiltonmaker.Vistas.Celdas.CaminoCellFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Descripción: Controlador de la vista Algoritmo Genético
 * Autor: Alexander Garcia
 */
public class ControladorAlgoritmo {

    @FXML
    ComboBox size;
    @FXML
    ComboBox inicio;
    @FXML
    ComboBox fin;
    @FXML
    ComboBox adyacencias;
    @FXML
    Button buscar;
    @FXML
    Button iniciar;
    @FXML
    ListView<CaminoHamiltoniano> listaCaminos;
    @FXML
    ListView<CaminoHamiltoniano[]> listaSoluciones;
    @FXML
    AnchorPane contenedor;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Button volver;
    @FXML
    Label numPoblacion;
    @FXML
    Label numSolucionesOptimas;

    Tablero tablero;
    CaminoHamiltoniano caminoVacio;
    ArrayList<CaminoHamiltoniano> caminos;

    boolean iniciado = false;
    AlgotirmoGenetico algotirmoGenetico;
    Thread hilo;



    @FXML
    private void initialize() {
        size.getItems().clear();
        size.getItems().addAll(3,4,5,6,7);
        listaCaminos.setCellFactory(new CaminoCellFactory());
        progressIndicator.setVisible(false);

        size.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seleccionarSize();
            }
        });

        inicio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seleccionarInicio();
            }
        });

        fin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seleccionarFin();
            }
        });

        adyacencias.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seleccionarAdyacencias();
            }
        });

        buscar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                buscar();
            }
        });

        iniciar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               iniciar();
            }
        });

        volver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                volver();
            }
        });

        listaCaminos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                actualizarControles();
            }
        });

        actualizarControles();

    }

    private void seleccionarSize(){
        listaCaminos.getItems().clear();
        if(size.getValue()!=null){
            tablero = new Tablero((int) size.getValue());
            caminoVacio = tablero.getTableroVacio();
            inicio.getItems().clear();
            for(int i = 0; i<caminoVacio.getNodos().size() ;i++){
                Nodo nodo = caminoVacio.getNodos().get(i);
                if(nodo.isHabilitado()){
                    inicio.getItems().add(caminoVacio.getNodos().indexOf(nodo));
                }
            }
            adyacencias.getItems().clear();
            for(int i = 1; i<caminoVacio.getNodos().size()-tablero.getInhabilitados()-1;i++){
                adyacencias.getItems().add(i);
            }
            actualizarControles();
        }
    }

    private void seleccionarInicio(){
        if(inicio.getValue()!=null){
            fin.getItems().clear();
            for(int i = 0; i<caminoVacio.getNodos().size() ;i++){
                tablero.setInicio((int) inicio.getValue());
                tablero.setFin(-1);
                caminoVacio = tablero.getCaminoVacio();
                Nodo nodo = caminoVacio.getNodos().get(i);
                int ini = (int) inicio.getValue();
                if(nodo.isHabilitado() && ini != caminoVacio.getNodos().indexOf(nodo)){
                    fin.getItems().add(caminoVacio.getNodos().indexOf(nodo));
                }
            }
            actualizarControles();
        }
    }

    private void seleccionarFin(){
        adyacencias.setDisable(true);
        buscar.setDisable(true);
        listaCaminos.getItems().clear();
        if(fin.getValue()!=null){
            tablero.setFin((int) fin.getValue());
            actualizarControles();
        }
    }

    private void seleccionarAdyacencias(){
        buscar.setDisable(true);
        if(adyacencias.getValue()!=null){
            actualizarControles();
        }
    }

    private void buscar(){
        bloquearControles();
        progressIndicator.setVisible(true);
        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                caminos = DAOCamino.obtenerCaminos((int) size.getValue(), (int)inicio.getValue(), (int) fin.getValue());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listaCaminos.getItems().clear();
                        listaCaminos.getItems().addAll(caminos);
                        progressIndicator.setVisible(false);
                        if(caminos.size()==0){
                            AlertManager.mostrarAlerta("No se encontraron caminos hamiltonianos",
                                    "No se han descubierto caminos hamiltonianos entre los nodos selecionados. \nUtiliza la pestaña Generar Caminos para buscarlos.");
                        }
                        actualizarControles();
                    }
                });
            }
        });
        hilo.start();

    }

    private void iniciar(){
        if(!iniciado){
            iniciado=true;
            iniciar.setText("Detener");
            CaminoHamiltoniano caminoHamiltoniano = listaCaminos.getSelectionModel().getSelectedItem();
            if(caminoHamiltoniano!=null){
                ArrayList<CaminoHamiltoniano> caminosInt = CaminoHamiltoniano.intersectar(caminoHamiltoniano,caminos);
                algotirmoGenetico = new AlgotirmoGenetico(caminoHamiltoniano,caminosInt,listaSoluciones,1+(int) adyacencias.getValue(),numPoblacion,numSolucionesOptimas);
                algotirmoGenetico.start();
            }
        } else{
            iniciar.setText("Iniciar");
            algotirmoGenetico.stop();
            iniciado=false;
        }
    }

    private void actualizarControles(){
        size.setDisable(false);
        inicio.setDisable(true);
        fin.setDisable(true);
        adyacencias.setDisable(true);
        buscar.setDisable(true);
        iniciar.setDisable(true);
        if(size.getValue()!=null){
            inicio.setDisable(false);
            if(inicio.getValue()!=null){
                fin.setDisable(false);
                if(fin.getValue()!=null){
                    buscar.setDisable(false);
                }
            }
            adyacencias.setDisable(false);
            if(adyacencias.getValue()!=null && fin.getValue()!=null && listaCaminos.getSelectionModel().getSelectedItem()!=null)
                iniciar.setDisable(false);
        }
    }

    private void bloquearControles(){
        size.setDisable(true);
        inicio.setDisable(true);
        fin.setDisable(true);
        adyacencias.setDisable(true);
        buscar.setDisable(true);
        iniciar.setDisable(true);
        listaCaminos.getItems().clear();
        listaSoluciones.getItems().clear();
    }

    private void volver(){
        try {
            if(hilo!=null){
                hilo.stop();
            }
            if(algotirmoGenetico !=null){
                algotirmoGenetico.stop();
            }
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaMenu.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

}
