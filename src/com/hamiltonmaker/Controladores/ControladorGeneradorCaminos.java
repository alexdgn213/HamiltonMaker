package com.hamiltonmaker.Controladores;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import com.hamiltonmaker.Main;
import com.hamiltonmaker.Persistencia.DAOCamino;
import com.hamiltonmaker.Vistas.Celdas.CaminoDobleCellFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class ControladorGeneradorCaminos {

    @FXML
    ComboBox size;
    @FXML
    ComboBox inicio;
    @FXML
    ComboBox fin;
    @FXML
    Button generar;
    @FXML
    Canvas tableroVacio;
    @FXML
    ListView<CaminoHamiltoniano[]> listaCaminos;
    @FXML
    AnchorPane contenedor;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Button volver;

    Tablero tablero;
    CaminoHamiltoniano caminoVacio;
    ArrayList<CaminoHamiltoniano> caminos;
    Thread hilo;



    @FXML
    private void initialize() {
        size.getItems().clear();
        size.getItems().addAll(3,4,5,6,7);
        listaCaminos.setCellFactory(new CaminoDobleCellFactory());
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

        generar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generar();
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

    private void seleccionarSize(){
        if(size.getValue()!=null){
            tablero = new Tablero((int) size.getValue());
            caminoVacio = tablero.getTableroVacio();
            caminoVacio.dibujar(tableroVacio.getGraphicsContext2D());
            inicio.getItems().clear();
            inicio.getItems().add("Seleccionar Todos");
            for(int i = 0; i<caminoVacio.getNodos().size() ;i++){
                Nodo nodo = caminoVacio.getNodos().get(i);
                if(nodo.isHabilitado()){
                    inicio.getItems().add(caminoVacio.getNodos().indexOf(nodo));
                }
            }
            actualizarControles();
        }
    }

    private void seleccionarInicio(){
        if(inicio.getValue()!=null){
            fin.getItems().clear();
            tablero.setFin(-1);
            if(!inicio.getValue().equals("Seleccionar Todos")){
                tablero.setInicio((int) inicio.getValue());
                for(int i = 0; i<caminoVacio.getNodos().size() ;i++){
                    Nodo nodo = caminoVacio.getNodos().get(i);
                    int ini = (int) inicio.getValue();
                    if(nodo.isHabilitado() && ini != caminoVacio.getNodos().indexOf(nodo)){
                        fin.getItems().add(caminoVacio.getNodos().indexOf(nodo));
                    }
                }
            } else {
                tablero.setInicio(-1);
                fin.getItems().add("Seleccionar Todos");
            }
            caminoVacio = tablero.getCaminoVacio();
            caminoVacio.dibujar(tableroVacio.getGraphicsContext2D());
            actualizarControles();
        }
    }

    private void seleccionarFin(){
        if(fin.getValue()!=null){
            if(!fin.getValue().equals("Seleccionar Todos")){
                tablero.setFin((int) fin.getValue());
            } else {
                tablero.setFin(-1);
            }
            caminoVacio = tablero.getCaminoVacio();
            caminoVacio.dibujar(tableroVacio.getGraphicsContext2D());
            actualizarControles();
        }
    }

    private void generar(){
        bloquearControles();
        progressIndicator.setVisible(true);
        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                caminos = new ArrayList<>();
                if(tablero.getInicio()>=0 && tablero.getFin()>=0){
                    caminos = DAOCamino.obtenerCaminos((int) size.getValue(), (int)inicio.getValue(), (int) fin.getValue());
                }
                if(caminos.size()==0){
                    if(tablero.getInicio()>=0 && tablero.getFin()>=0){
                        caminos = tablero.depthFirst((int)inicio.getValue(), (int) fin.getValue());
                    } else {
                        caminos = new ArrayList<>();
                        for(int i = 0; i<caminoVacio.getNodos().size() ;i++){
                            Nodo nodo = caminoVacio.getNodos().get(i);
                            if(nodo.isHabilitado()){
                                for(int j = 0; j<caminoVacio.getNodos().size() ;j++){
                                    Nodo nodo2 = caminoVacio.getNodos().get(j);
                                    if(nodo2.isHabilitado() && nodo != nodo2){
                                        caminos.addAll(tablero.depthFirst(i,j));
                                    }
                                }
                            }
                        }
                    }
                }
                ArrayList<CaminoHamiltoniano[]> caminosDoble = new ArrayList<>();
                for(int i = 0; i< caminos.size(); i+=2){
                    CaminoHamiltoniano[] dupla;
                    if(i+1<caminos.size()){
                        dupla = new CaminoHamiltoniano[]{caminos.get(i), caminos.get(i + 1)};
                    }else {
                        dupla = new CaminoHamiltoniano[]{caminos.get(i)};
                    }
                    caminosDoble.add(dupla);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listaCaminos.getItems().clear();
                        listaCaminos.getItems().addAll(caminosDoble);
                        progressIndicator.setVisible(false);
                        actualizarControles();
                        if(caminos.size()==0){
                            mostrarAlerta("No se encontraron caminos hamiltonianos",
                                    "No existen caminos hamiltonianos entre los nodos selecionados. \n\nUtiliza la pestaña Generar Caminos para buscarlos.");
                        }
                    }
                });
            }
        });
        hilo.start();

    }

    private void actualizarControles(){
        size.setDisable(false);
        inicio.setDisable(true);
        fin.setDisable(true);
        generar.setDisable(true);
        if(size.getValue()!=null){
            inicio.setDisable(false);
            if(inicio.getValue()!=null){
                fin.setDisable(false);
                if(fin.getValue()!=null){
                    generar.setDisable(false);
                }
            }
        }
    }

    private void bloquearControles(){
        size.setDisable(true);
        inicio.setDisable(true);
        fin.setDisable(true);
        generar.setDisable(true);
        listaCaminos.getItems().clear();
    }

    private void mostrarAlerta(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
