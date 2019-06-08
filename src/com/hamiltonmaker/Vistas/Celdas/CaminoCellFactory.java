package com.hamiltonmaker.Vistas.Celdas;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CaminoCellFactory implements Callback<ListView<CaminoHamiltoniano>, ListCell<CaminoHamiltoniano>> {
    @Override
    public ListCell<CaminoHamiltoniano> call(ListView<CaminoHamiltoniano> listview)
    {
        return new CaminoCell();
    }
}
