package com.hamiltonmaker;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Pruebas;
import com.hamiltonmaker.Vistas.CaminoCell;
import com.hamiltonmaker.Vistas.CaminoCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Observable;

public class Controller {
    @FXML
    Canvas canvas1;
    @FXML
    Canvas canvas3;
    @FXML
    ListView<CaminoHamiltoniano> lista;
    @FXML
    TextField tamano;
    @FXML
    TextField inicio;
    @FXML
    TextField fin;
    @FXML
    Button boton;




    @FXML
    private void initialize() {

        ArrayList<GraphicsContext> gcs = new ArrayList<GraphicsContext>();


        boton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GraphicsContext gc = canvas1.getGraphicsContext2D();
                Pruebas.Prueba5(Integer.parseInt(tamano.getText())).dibujar(gc);
                lista.getItems().clear();
                lista.getItems().addAll(Pruebas.Prueba4(Integer.parseInt(tamano.getText()),Integer.parseInt(inicio.getText()),Integer.parseInt(fin.getText()),lista));
                lista.setCellFactory(new CaminoCellFactory());
            }
        });   }

    private void drawShapes(GraphicsContext gc) {

    }

}
