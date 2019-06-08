package com.hamiltonmaker.Controladores;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import com.hamiltonmaker.Comun.Utils.JSONManager;
import com.hamiltonmaker.Comun.Utils.OutputManager;
import com.hamiltonmaker.Main;
import com.hamiltonmaker.Persistencia.DAOCamino;
import com.hamiltonmaker.Persistencia.DAOSolucion;
import com.hamiltonmaker.Vistas.Celdas.CaminoDobleCellFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ControladorExportar {

    @FXML
    ComboBox minSize;
    @FXML
    ComboBox maxSize;
    @FXML
    ComboBox dificultad;
    @FXML
    TextField maxCaminos;
    @FXML
    TextField maxSoluciones;
    @FXML
    Button exportar;
    @FXML
    ListView<CaminoHamiltoniano[]> listaCaminos;
    @FXML
    AnchorPane contenedor;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Button volver;

    File archivo;
    Thread hilo;

    @FXML
    private void initialize() {
        minSize.getItems().clear();
        minSize.getItems().addAll(3,4,5,6,7);
        dificultad.getItems().clear();
        dificultad.getItems().addAll("Fácil","Medio","Difícil","Avanzado");
        listaCaminos.setCellFactory(new CaminoDobleCellFactory());
        maxCaminos.setText("10");
        maxSoluciones.setText("10");
        progressIndicator.setVisible(false);


        minSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seleccionarMinSize();
            }
        });

        maxSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actualizarControles();
            }
        });

        dificultad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actualizarControles();
            }
        });

        maxSoluciones.setOnInputMethodTextChanged(new EventHandler<InputMethodEvent>() {
            @Override
            public void handle(InputMethodEvent event) {
                actualizarControles();
            }
        });

        maxCaminos.setOnInputMethodTextChanged(new EventHandler<InputMethodEvent>() {
            @Override
            public void handle(InputMethodEvent event) {
                actualizarControles();
            }
        });


        maxSoluciones.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actualizarControles();
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
        actualizarControles();
    }

    private void seleccionarMinSize(){
        if(minSize.getValue()!=null){
            int sizeMin = (int) minSize.getValue();
            maxSize.getItems().clear();
            for(int i = sizeMin; i<8 ;i++){
                maxSize.getItems().add(i);
            }
            actualizarControles();
        }
    }



    private void exportar(){
        progressIndicator.setVisible(true);
        bloquearControles();
        listaCaminos.getItems().clear();
        archivo = OutputManager.abrirfileChooser(contenedor);
        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CaminoHamiltoniano[]> caminosDoble = new ArrayList<>();
                if(archivo!= null && maxCaminos.getText().matches("[0-9]*") && maxSoluciones.getText().matches("[0-9]*") && maxCaminos.getText().length()>0 && maxSoluciones.getText().length()>0){
                    int caminosMax = Integer.valueOf(maxCaminos.getText());
                    int solucionesMax = Integer.valueOf(maxSoluciones.getText());
                    int numDificultad = dificultad.getSelectionModel().getSelectedIndex();
                    int sizeMin = (int) minSize.getValue();
                    int sizeMax = (int) maxSize.getValue();

                    ArrayList<CaminoHamiltoniano> caminos = new ArrayList<>();

                    caminos = DAOCamino.obtenerPorDificultad(sizeMin,sizeMax,numDificultad,caminosMax);

                    ArrayList<CaminoHamiltoniano> soluciones = new ArrayList<>();
                    if(caminos.size()>0){
                        JSONManager.iniciarArchivo(archivo);
                        int totalCaminos = caminos.size();
                        for(CaminoHamiltoniano c: caminos){
                            soluciones.add(c);
                            ArrayList<CaminoHamiltoniano> solucinesEspecificas = DAOSolucion.obtenerPorDificultad(c,numDificultad,solucionesMax);
                            soluciones.addAll(solucinesEspecificas);
                            JSONManager.agregarArchivo(c,solucinesEspecificas);
                        }
                        int totalSoluciones = soluciones.size()-totalCaminos;
                        String dificultad = soluciones.get(1).obtenerDificultad();
                        JSONManager.cerrarArchivo(dificultad,totalCaminos,totalSoluciones);
                    }


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
                        listaCaminos.getItems().clear();
                        listaCaminos.getItems().addAll(caminosDoble);
                        progressIndicator.setVisible(false);
                        actualizarControles();
                    }
                });
            }
        });
        hilo.start();

    }


    private void actualizarControles(){
        minSize.setDisable(false);
        maxSize.setDisable(true);
        exportar.setDisable(true);
        maxCaminos.setDisable(false);
        maxSoluciones.setDisable(false);
        if(minSize.getValue()!=null){
            maxSize.setDisable(false);

            if(maxSize.getValue()!=null && dificultad.getValue()!=null){
                exportar.setDisable(false);
            }
        }
    }

    private void bloquearControles(){
        minSize.setDisable(true);
        maxSize.setDisable(true);
        exportar.setDisable(true);
        maxCaminos.setDisable(true);
        maxSoluciones.setDisable(true);
    }

    private void volver(){
        try {
            AnchorPane nuevoContenedor = FXMLLoader.load(Main.class.getResource("Vistas/VistaMenu.fxml"));
            contenedor.getChildren().setAll(nuevoContenedor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
