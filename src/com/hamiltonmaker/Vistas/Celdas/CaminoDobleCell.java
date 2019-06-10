package com.hamiltonmaker.Vistas.Celdas;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListCell;

/**
 * Descripción: Celda para dibujar dos camninos hamiltonianos o soluciones en pantalla
 * Autor: Alexander Garcia
 */
public class CaminoDobleCell extends ListCell<CaminoHamiltoniano[]> {
    @Override
    public void updateItem(CaminoHamiltoniano item[], boolean empty)
    {
        super.updateItem(item, empty);

        int index = this.getIndex();
        String name = null;

        // Format name
        if (item == null || empty)
        {
            this.setGraphic(null);
        }
        else
        {
            Canvas canvas = new Canvas(500,250);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            if(item.length>0){
                item[0].dibujarDos(gc,0);
                if(item.length>1){
                    item[1].dibujarDos(gc,1);
                }
            }
            setGraphic(canvas);
        }

        this.setText(null);

    }
}
