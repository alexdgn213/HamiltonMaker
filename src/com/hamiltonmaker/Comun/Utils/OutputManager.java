package com.hamiltonmaker.Comun.Utils;

import com.hamiltonmaker.Comun.AlgoritmoGenetico.Individuo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OutputManager {

    private static boolean activo = false;

    public  static void limpiarDatos(){
        try {
            FileWriter tiempos = new FileWriter("tiempos.txt");
            tiempos.close();
            FileWriter poblaciones= new FileWriter("poblaciones.txt",true);
            poblaciones.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
