package com.hamiltonmaker.Vistas;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListCell;

public class CaminoCell extends ListCell<CaminoHamiltoniano> {
    @Override
    public void updateItem(CaminoHamiltoniano item, boolean empty)
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
            Canvas canvas = new Canvas(250,250);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            item.dibujar(gc);
            setGraphic(canvas);
        }

        this.setText(null);

    }
}
