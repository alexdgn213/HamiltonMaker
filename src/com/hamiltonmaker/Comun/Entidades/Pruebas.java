package com.hamiltonmaker.Comun.Entidades;

import com.hamiltonmaker.Comun.AlgoritmoGenetico.AlgotirmoGenetico;
import com.hamiltonmaker.Comun.AlgoritmoGenetico.Individuo;
import com.hamiltonmaker.Persistencia.DAOCamino;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class Pruebas {

    public Pruebas() {


    }

    public static CaminoHamiltoniano camino1(){
        Nodo n1 = new Nodo(0,0);
        Nodo n2 = new Nodo(1,0);
        Nodo n3 = new Nodo(2,0);
        Nodo n4 = new Nodo(0,1);
        Nodo n5 = new Nodo(1,1);
        Nodo n6 = new Nodo(2,1);
        Nodo n7 = new Nodo(0,2);
        Nodo n8 = new Nodo(1,2);
        Nodo n9 = new Nodo(2,2);

        n1.setSiguiente(n2);
        n2.setSiguiente(n3);
        n3.setSiguiente(n6);
        n6.setSiguiente(n5);
        n5.setSiguiente(n4);
        n4.setSiguiente(n7);
        n7.setSiguiente(n8);
        n8.setSiguiente(n9);

        CaminoHamiltoniano c = new CaminoHamiltoniano();
        c.addNodo(n1);
        c.addNodo(n2);
        c.addNodo(n3);
        c.addNodo(n4);
        c.addNodo(n5);
        c.addNodo(n6);
        c.addNodo(n7);
        c.addNodo(n8);
        c.addNodo(n9);

        return c;

        }

    public static CaminoHamiltoniano camino2(){
        Nodo n1 = new Nodo(0,0);
        Nodo n2 = new Nodo(1,0);
        Nodo n3 = new Nodo(2,0);
        Nodo n4 = new Nodo(0,1);
        Nodo n5 = new Nodo(1,1);
        Nodo n6 = new Nodo(2,1);
        Nodo n7 = new Nodo(0,2);
        Nodo n8 = new Nodo(1,2);
        Nodo n9 = new Nodo(2,2);

        n1.setSiguiente(n4);
        n4.setSiguiente(n7);
        n7.setSiguiente(n8);
        n8.setSiguiente(n5);
        n5.setSiguiente(n2);
        n2.setSiguiente(n3);
        n3.setSiguiente(n6);
        n6.setSiguiente(n9);

        CaminoHamiltoniano c = new CaminoHamiltoniano();
        c.addNodo(n1);
        c.addNodo(n2);
        c.addNodo(n3);
        c.addNodo(n4);
        c.addNodo(n5);
        c.addNodo(n6);
        c.addNodo(n7);
        c.addNodo(n8);
        c.addNodo(n9);

        return c;

    }

    public static void Prueba1(GraphicsContext gc) {
        camino1().dibujar(gc);

    }

    public static void Prueba2(GraphicsContext gc) {
        camino2().dibujar(gc);
    }

    public static void Prueba3(GraphicsContext gc) {


        CaminoHamiltoniano c = camino1();
        CaminoHamiltoniano c2 = camino2();

        CaminoHamiltoniano inter = CaminoHamiltoniano.intersectar(c,c2);
        inter.dibujar(gc);

    }

    public static ArrayList<CaminoHamiltoniano> Prueba4(int tamano, int inicio, int fin, ListView<CaminoHamiltoniano> lista) {
       Tablero tablero = new Tablero();
       tablero.generar(tamano);
       ArrayList<CaminoHamiltoniano> caminos = tablero.depthFirst(inicio,fin);
       ArrayList<CaminoHamiltoniano> caminosInt = new ArrayList<CaminoHamiltoniano>();
       if(caminos.size()>0){
           caminosInt = CaminoHamiltoniano.intersectar(caminos.get(0),caminos);
           AlgotirmoGenetico algotirmoGenetico = new AlgotirmoGenetico(caminos.get(0),caminosInt,lista);
           //algotirmoGenetico.start();
           return DAOCamino.obtenerCaminos(4,0,12);
       }
       return caminosInt;
    }

    public static CaminoHamiltoniano Prueba5(int tamano) {
        Tablero tablero = new Tablero();
        tablero.generar(tamano);
        return tablero.getTableroVacio();
    }
}
