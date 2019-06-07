package com.hamiltonmaker.Controladores;

import com.hamiltonmaker.Comun.AlgoritmoGenetico.AlgotirmoGenetico;
import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import com.hamiltonmaker.Main;
import com.hamiltonmaker.Persistencia.DAOCamino;
import com.hamiltonmaker.Persistencia.DAOSolucion;
import com.hamiltonmaker.Vistas.CaminoCellFactory;
import com.hamiltonmaker.Vistas.CaminoDobleCellFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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
    Button iniciar;
    @FXML
    ListView<CaminoHamiltoniano> listaCaminos;
    @FXML
    ListView<CaminoHamiltoniano[]> listaSoluciones;
    @FXML
    AnchorPane contenedor;
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



    @FXML
    private void initialize() {
        size.getItems().clear();
        size.getItems().addAll(3,4,5,6,7);
        inicio.setDisable(true);
        fin.setDisable(true);
        buscar.setDisable(true);
        iniciar.setDisable(true);
        adyacencias.setDisable(true);
        listaCaminos.setCellFactory(new CaminoCellFactory());
        listaSoluciones.setCellFactory(new CaminoDobleCellFactory());

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
                buscarSoluciones();
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
        inicio.setDisable(true);
        fin.setDisable(true);
        buscar.setDisable(true);
        adyacencias.setDisable(true);
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
            inicio.setDisable(false);
        }
    }

    private void seleccionarInicio(){
        fin.setDisable(true);
        buscar.setDisable(true);
        adyacencias.setDisable(true);
        listaCaminos.getItems().clear();
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
            fin.setDisable(false);
        }
    }

    private void seleccionarFin(){
        adyacencias.setDisable(true);
        buscar.setDisable(true);
        listaCaminos.getItems().clear();
        if(fin.getValue()!=null){
            tablero.setFin((int) fin.getValue());
            adyacencias.getItems().clear();
            caminoVacio = tablero.getCaminoVacio();
            for(int i = 1; i<caminoVacio.getNodos().size()-tablero.getInhabilitados()-1;i++){
                adyacencias.getItems().add(i);
            }
            adyacencias.setDisable(false);
        }
    }

    private void seleccionarAdyacencias(){
        buscar.setDisable(true);
        listaCaminos.getItems().clear();
        if(adyacencias.getValue()!=null){
            buscar.setDisable(false);
        }
    }

    private void buscar(){
        caminos = DAOCamino.obtenerCaminosConSolucion((int) size.getValue(), (int)inicio.getValue(), (int) fin.getValue());
        if(caminos.size()>0){
            listaCaminos.getItems().clear();
            listaCaminos.getItems().addAll(caminos);
            iniciar.setDisable(false);
        }
    }

    private void buscarSoluciones(){
        ArrayList<CaminoHamiltoniano> soluciones = new ArrayList<>();
        CaminoHamiltoniano caminoHamiltoniano = listaCaminos.getSelectionModel().getSelectedItem();
        if(caminoHamiltoniano!=null){
            soluciones = DAOSolucion.obtenerSoluciones(caminoHamiltoniano,(int) adyacencias.getValue()+1);
        }
        this.numSolucionesOptimas.setText(String.valueOf(soluciones.size()));
        if(soluciones.size()>0){
            ArrayList<CaminoHamiltoniano[]> caminosDoble = new ArrayList<>();
            for(int i = 0; i< soluciones.size(); i+=2){
                CaminoHamiltoniano[] dupla;
                if(i+1<soluciones.size()){
                    dupla = new CaminoHamiltoniano[]{soluciones.get(i), soluciones.get(i + 1)};
                }else {
                    dupla = new CaminoHamiltoniano[]{soluciones.get(i)};
                }
                caminosDoble.add(dupla);
            }
            listaSoluciones.getItems().clear();
            listaSoluciones.getItems().addAll(caminosDoble);
        }
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
