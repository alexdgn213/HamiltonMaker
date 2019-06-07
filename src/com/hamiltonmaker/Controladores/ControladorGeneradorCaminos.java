package com.hamiltonmaker.Controladores;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import com.hamiltonmaker.Main;
import com.hamiltonmaker.Persistencia.DAOCamino;
import com.hamiltonmaker.Vistas.CaminoDobleCellFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
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
    Button volver;

    Tablero tablero;
    CaminoHamiltoniano caminoVacio;



    @FXML
    private void initialize() {
        size.getItems().clear();
        size.getItems().addAll(3,4,5,6,7);
        inicio.setDisable(true);
        fin.setDisable(true);
        generar.setDisable(true);
        listaCaminos.setCellFactory(new CaminoDobleCellFactory());

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
    }

    private void seleccionarSize(){
        inicio.setDisable(true);
        fin.setDisable(true);
        generar.setDisable(true);
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
            inicio.setDisable(false);
        }
    }

    private void seleccionarInicio(){
        fin.setDisable(true);
        generar.setDisable(true);
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
            fin.setDisable(false);
        }
    }

    private void seleccionarFin(){
        generar.setDisable(true);
        if(fin.getValue()!=null){
            if(!fin.getValue().equals("Seleccionar Todos")){
                tablero.setFin((int) fin.getValue());
            } else {
                tablero.setFin(-1);
            }
            caminoVacio = tablero.getCaminoVacio();
            caminoVacio.dibujar(tableroVacio.getGraphicsContext2D());
            generar.setDisable(false);
        }
    }

    private void generar(){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<>();
        if(tablero.getInicio()>=0 && tablero.getFin()>=0){
            caminos = DAOCamino.obtenerCaminos((int) size.getValue(), (int)inicio.getValue(), (int) fin.getValue());
        }
        if(caminos.size()==0){
            if(tablero.getInicio()>=0 && tablero.getFin()>=0){
                caminos = tablero.depthFirst((int)inicio.getValue(), (int) fin.getValue());
            } else {
                caminos = generarTodos();
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
        listaCaminos.getItems().clear();
        listaCaminos.getItems().addAll(caminosDoble);
    }

    private ArrayList<CaminoHamiltoniano> generarTodos(){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<>();
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

        return caminos;
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
