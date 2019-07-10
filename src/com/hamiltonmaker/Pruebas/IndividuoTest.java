package com.hamiltonmaker.Pruebas;

import com.hamiltonmaker.Comun.AlgoritmoGenetico.Individuo;
import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Descripción: Pruebas unitarias para los métodos de la clase Individuo
 * Autor: Alexander Garcia
 */
class IndividuoTest {
    static CaminoHamiltoniano caminoHamiltoniano;
    static ArrayList<CaminoHamiltoniano> restricciones;
    Individuo individuo1;
    Individuo individuo2;

    @BeforeAll
    static void  beforeAll(){
        Tablero tablero = new Tablero(3);
        tablero.setInicio(0);
        tablero.setFin(8);

        caminoHamiltoniano = tablero.getCaminoVacio();
        caminoHamiltoniano.getNodos().get(0).setSiguiente(caminoHamiltoniano.getNodos().get(1));
        caminoHamiltoniano.getNodos().get(1).setSiguiente(caminoHamiltoniano.getNodos().get(2));
        caminoHamiltoniano.getNodos().get(2).setSiguiente(caminoHamiltoniano.getNodos().get(5));
        caminoHamiltoniano.getNodos().get(5).setSiguiente(caminoHamiltoniano.getNodos().get(4));
        caminoHamiltoniano.getNodos().get(4).setSiguiente(caminoHamiltoniano.getNodos().get(3));
        caminoHamiltoniano.getNodos().get(3).setSiguiente(caminoHamiltoniano.getNodos().get(6));
        caminoHamiltoniano.getNodos().get(6).setSiguiente(caminoHamiltoniano.getNodos().get(7));
        caminoHamiltoniano.getNodos().get(7).setSiguiente(caminoHamiltoniano.getNodos().get(8));

        CaminoHamiltoniano camino2 = tablero.getCaminoVacio();
        camino2.getNodos().get(0).setSiguiente(camino2.getNodos().get(3));
        camino2.getNodos().get(3).setSiguiente(camino2.getNodos().get(6));
        camino2.getNodos().get(6).setSiguiente(camino2.getNodos().get(7));
        camino2.getNodos().get(7).setSiguiente(camino2.getNodos().get(4));
        camino2.getNodos().get(4).setSiguiente(camino2.getNodos().get(1));
        camino2.getNodos().get(1).setSiguiente(camino2.getNodos().get(2));
        camino2.getNodos().get(2).setSiguiente(camino2.getNodos().get(5));
        camino2.getNodos().get(5).setSiguiente(camino2.getNodos().get(8));

        restricciones = new ArrayList<>();
        restricciones.add(camino2);

    }

    @BeforeEach
    void setUp() {
        individuo1 = new Individuo(caminoHamiltoniano);
        individuo1.getCaminoHamiltoniano().alterarNodo(0);
        individuo1.getCaminoHamiltoniano().alterarNodo(1);
        individuo1.getCaminoHamiltoniano().alterarNodo(5);
        individuo1.getCaminoHamiltoniano().alterarNodo(7);
        individuo2 = new Individuo(caminoHamiltoniano);
        individuo2.getCaminoHamiltoniano().alterarNodo(0);
        individuo2.getCaminoHamiltoniano().alterarNodo(4);
        individuo2.getCaminoHamiltoniano().alterarNodo(5);
        individuo2.getCaminoHamiltoniano().alterarNodo(6);
        individuo2.getCaminoHamiltoniano().alterarNodo(7);

    }

    @Test
    void funcionFitness() {
        individuo1.funcionFitness(restricciones,5);
        assertEquals(0,individuo1.getEvaluacion());
        individuo1.funcionFitness(restricciones,6);
        assertEquals(1,individuo1.getEvaluacion());
        individuo2.funcionFitness(restricciones,4);
        assertEquals(1,individuo2.getEvaluacion());
        individuo2.funcionFitness(restricciones,5);
        assertEquals(2,individuo2.getEvaluacion());
    }

    @Disabled
    @Test
    void funcionDeCruce() {
        Individuo hijo1 = new Individuo();
        Individuo hijo2 = new Individuo();
        Individuo.funcionDeCruce(individuo1,individuo2,hijo1,hijo2);
        int puntoCruce = 0;
        for(int i = 0; i< individuo1.getCaminoHamiltoniano().getNodos().size();i++){
            if(individuo1.getCaminoHamiltoniano().getNodos().get(i).isVisible() !=
                    hijo1.getCaminoHamiltoniano().getNodos().get(i).isVisible()){
                puntoCruce = i;
                break;
            }
        }
        for(int i = 0; i< individuo1.getCaminoHamiltoniano().getNodos().size();i++){
            if(i<puntoCruce){
                assertTrue(individuo1.getCaminoHamiltoniano().getNodos().get(i).isVisible() ==
                        hijo1.getCaminoHamiltoniano().getNodos().get(i).isVisible());
                assertTrue(individuo2.getCaminoHamiltoniano().getNodos().get(i).isVisible() ==
                        hijo2.getCaminoHamiltoniano().getNodos().get(i).isVisible());
            } else {
                assertTrue(individuo1.getCaminoHamiltoniano().getNodos().get(i).isVisible() ==
                        hijo2.getCaminoHamiltoniano().getNodos().get(i).isVisible());
                assertTrue(individuo2.getCaminoHamiltoniano().getNodos().get(i).isVisible() ==
                        hijo1.getCaminoHamiltoniano().getNodos().get(i).isVisible());
            }
        }

    }

    @Test
    void funcionDeMutacion() {
        Individuo individuoNuevo = new Individuo();
        individuoNuevo.setCaminoHamiltoniano(individuo1.getCaminoHamiltoniano().clonar());
        individuoNuevo.funcionDeMutacion();
        int distintos = 0;
        for(int i = 0; i< individuo1.getCaminoHamiltoniano().getNodos().size();i++){
            if(individuo1.getCaminoHamiltoniano().getNodos().get(i).isVisible() !=
                        individuoNuevo.getCaminoHamiltoniano().getNodos().get(i).isVisible())
                distintos++;
        }
        assertEquals(1,distintos);
    }
}