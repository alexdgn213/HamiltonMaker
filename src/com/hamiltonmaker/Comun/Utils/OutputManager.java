package com.hamiltonmaker.Comun.Utils;

import com.hamiltonmaker.Comun.AlgoritmoGenetico.Individuo;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Descripción: Clase encargada del manejo de archivos externos
 * Autor: Alexander Garcia
 */
public class OutputManager {

    private static boolean activo = false;


    public  static void imprimirTiempo(String mensaje){
        try {
            if(activo){
                FileWriter tiempos = new FileWriter("tiempos.txt",true);
                tiempos.write("\n"+mensaje);
                tiempos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static void imprimirPoblacion(int numero, ArrayList<Individuo> poblacion){
        try {
            if(activo && numero<=100){
                FileWriter poblaciones = new FileWriter("poblaciones.txt",true);
                poblaciones.write("\n\n\n--------------------------- Poblacion "+numero+" -----------------------------\n");
                int num=0;
                for(Individuo i: poblacion){
                    num++;
                    poblaciones.write("\n"+i.imprimir(num));
                }
                poblaciones.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File abrirfileChooser(AnchorPane contenedor){
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        return fileChooser.showSaveDialog(contenedor.getScene().getWindow());
    }
}
