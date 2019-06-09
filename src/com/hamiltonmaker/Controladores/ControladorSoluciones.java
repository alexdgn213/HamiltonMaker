package com.hamiltonmaker.Controladores;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import com.hamiltonmaker.Comun.Utils.AlertManager;
import com.hamiltonmaker.Comun.Utils.JSONManager;
import com.hamiltonmaker.Comun.Utils.OutputManager;
import com.hamiltonmaker.Main;
import com.hamiltonmaker.Persistencia.DAOCamino;
import com.hamiltonmaker.Persistencia.DAOSolucion;
import com.hamiltonmaker.Vistas.Celdas.CaminoCellFactory;
import com.hamiltonmaker.Vistas.Celdas.CaminoDobleCellFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ControladorSoluciones {

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
    Button seleccionar;
    @FXML
    ListView<CaminoHamiltoniano> listaCaminos;
    @FXML
    ListView<CaminoHamiltoniano[]> listaSoluciones;
    @FXML
    AnchorPane contenedor;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    ProgressIndicator progressIndicator2;
    @FXML
    Button volver;
    @FXML
    Label numSolucionesOptimas;
    @FXML
    Button exportar;

    Tablero tablero;
    CaminoHamiltoniano caminoVacio;
    ArrayList<CaminoHamiltoniano> caminos;
    CaminoHamiltoniano caminoHamiltoniano;
    ArrayList<CaminoHamiltoniano> soluciones = new ArrayList<>();
    Thread hilo;


    @FXML
    private void initialize() {
        size.getItems().clear();
        size.getItems().addAll(3,4,5,6,7);
        actualizarControles();
        listaCaminos.setCellFactory(new CaminoCellFactory());
        listaSoluciones.setCellFactory(new CaminoDobleCellFactory());
        progressIndicator.setVisible(false);
        progressIndicator2.setVisible(false);

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

        listaCaminos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                actualizarControles();
            }
        });

        seleccionar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                buscarSoluciones();
            }
        });

        exportar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exportar();
            }
        });

        volver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                volver();
            }
        });

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
            adyacencias.getItems().add("Cualquiera");
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
        if(fin.getValue()!=null){
            tablero.setFin((int) fin.getValue());
            actualizarControles();
        }
    }

    private void seleccionarAdyacencias(){
        if(adyacencias.getValue()!=null){
            actualizarControles();
        }
    }

    private void buscar(){
        progressIndicator.setVisible(true);
        listaCaminos.getItems().clear();
        bloquearControles();
        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                caminos = DAOCamino.obtenerCaminosConSolucion((int) size.getValue(), (int)inicio.getValue(), (int) fin.getValue());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listaCaminos.getItems().clear();
                        listaCaminos.getItems().addAll(caminos);
                        progressIndicator.setVisible(false);
                        actualizarControles();
                        if(caminos.size()==0){
                            AlertManager.mostrarAlerta("No se encontraron caminos hamilnonianos",
                                    "No se han descubierto soluciones parciales para caminos hamiltonianos entre los nodos selecionados. \nUtiliza la pestaña Algoritmo Genético para buscarlas.");
                        }
                    }
                });
            }
        });
        hilo.start();
    }

    private void buscarSoluciones(){
        progressIndicator2.setVisible(true);
        listaSoluciones.getItems().clear();
        bloquearControles();
        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                caminoHamiltoniano = listaCaminos.getSelectionModel().getSelectedItem();
                if(caminoHamiltoniano!=null){
                    listaSoluciones.getItems().clear();
                    if(adyacencias.getValue()!="Cualquiera"){
                        soluciones = DAOSolucion.obtenerSoluciones(caminoHamiltoniano,(int) adyacencias.getValue()+1);
                    } else{
                        soluciones = DAOSolucion.obtenerSoluciones(caminoHamiltoniano);
                    }
                }
                ArrayList<CaminoHamiltoniano[]> caminosDoble = new ArrayList<>();
                if(soluciones.size()>0){
                    for(int i = 0; i< soluciones.size(); i+=2){
                        CaminoHamiltoniano[] dupla;
                        if(i+1<soluciones.size()){
                            dupla = new CaminoHamiltoniano[]{soluciones.get(i), soluciones.get(i + 1)};
                        }else {
                            dupla = new CaminoHamiltoniano[]{soluciones.get(i)};
                        }
                        caminosDoble.add(dupla);
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        numSolucionesOptimas.setText(String.valueOf(soluciones.size()));
                        listaSoluciones.getItems().clear();
                        listaSoluciones.getItems().addAll(caminosDoble);
                        progressIndicator2.setVisible(false);
                        if(soluciones.size()==0){
                            AlertManager.mostrarAlerta("No se encontraron soluciones parciales",
                                    "No se han descubierto soluciones parciales con el número de adyacencias deseado. \nUtiliza la pestaña Algoritmo Genético para buscarlas.");
                        }
                        actualizarControles();
                    }
                });
            }
        });
        hilo.start();

    }

    private void exportar(){
        File archivo = OutputManager.abrirfileChooser(contenedor);
        if(archivo!=null)
            JSONManager.writeCaminoConSoluciones(archivo,caminoHamiltoniano,soluciones);
    }

    private void actualizarControles(){
        size.setDisable(false);
        inicio.setDisable(true);
        fin.setDisable(true);
        adyacencias.setDisable(true);
        buscar.setDisable(true);
        seleccionar.setDisable(true);
        exportar.setDisable(true);
        if(size.getValue()!=null){
            inicio.setDisable(false);
            if(inicio.getValue()!=null){
                fin.setDisable(false);
                if(fin.getValue()!=null){
                    buscar.setDisable(false);
                }
            }
            adyacencias.setDisable(false);
            if(adyacencias.getValue()!=null && fin.getValue()!=null && listaCaminos.getSelectionModel().getSelectedItem()!=null){
                seleccionar.setDisable(false);
                if(soluciones.size()>0){
                    exportar.setDisable(false);
                }
            }
        }
    }

    private void bloquearControles(){
        size.setDisable(true);
        inicio.setDisable(true);
        fin.setDisable(true);
        adyacencias.setDisable(true);
        buscar.setDisable(true);
        seleccionar.setDisable(true);
        exportar.setDisable(true);
    }

    private void volver(){
        try {
            if(hilo!=null){
                hilo.stop();
            }
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaMenu.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

}
