package com.hamiltonmaker.Pruebas;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Descripción: Pruebas unitarias para los métodos de la clase CaminoHamiltoniano
 * Autor: Alexander Garcia
 */
class CaminoHamiltonianoTest {
    CaminoHamiltoniano camino1;
    CaminoHamiltoniano camino2;

    @BeforeEach
    void setUp() {
        Tablero tablero = new Tablero(3);
        tablero.setInicio(0);
        tablero.setFin(8);

        camino1 = tablero.getCaminoVacio();
        camino1.getNodos().get(0).setSiguiente(camino1.getNodos().get(1));
        camino1.getNodos().get(1).setSiguiente(camino1.getNodos().get(2));
        camino1.getNodos().get(2).setSiguiente(camino1.getNodos().get(5));
        camino1.getNodos().get(5).setSiguiente(camino1.getNodos().get(4));
        camino1.getNodos().get(4).setSiguiente(camino1.getNodos().get(3));
        camino1.getNodos().get(3).setSiguiente(camino1.getNodos().get(6));
        camino1.getNodos().get(6).setSiguiente(camino1.getNodos().get(7));
        camino1.getNodos().get(7).setSiguiente(camino1.getNodos().get(8));

        camino2 = tablero.getCaminoVacio();
        camino2.getNodos().get(0).setSiguiente(camino2.getNodos().get(3));
        camino2.getNodos().get(3).setSiguiente(camino2.getNodos().get(6));
        camino2.getNodos().get(6).setSiguiente(camino2.getNodos().get(7));
        camino2.getNodos().get(7).setSiguiente(camino2.getNodos().get(4));
        camino2.getNodos().get(4).setSiguiente(camino2.getNodos().get(1));
        camino2.getNodos().get(1).setSiguiente(camino2.getNodos().get(2));
        camino2.getNodos().get(2).setSiguiente(camino2.getNodos().get(5));
        camino2.getNodos().get(5).setSiguiente(camino2.getNodos().get(8));
    }

    @Test
    void clonar() {
        CaminoHamiltoniano caminoConado = camino1.clonar();
        assertTrue(camino1.comparar(caminoConado));
    }

    @Test
    void intersectar() {
        CaminoHamiltoniano interseccion = CaminoHamiltoniano.intersectar(camino1,camino2);
        assertTrue(camino1.contieneSubconjunto(interseccion));
        assertTrue(camino2.contieneSubconjunto(interseccion));
        interseccion.getNodos().get(0).setSiguiente(interseccion.getNodos().get(3));
        interseccion.getNodos().get(0).setVisible(true);
        assertFalse(camino1.contieneSubconjunto(interseccion));
        interseccion.getNodos().get(0).setSiguiente(interseccion.getNodos().get(1));
        assertFalse(camino2.contieneSubconjunto(interseccion));
    }

    @Test
    void contieneSubconjunto() {
        CaminoHamiltoniano caminoSubconjunto = camino1.clonar();
        caminoSubconjunto.getNodos().get(2).setSiguiente(null);
        caminoSubconjunto.getNodos().get(4).setSiguiente(null);
        assertTrue(camino1.compararSubconjunto(caminoSubconjunto));
    }

    @Test
    void comparar() {
        CaminoHamiltoniano caminoCopia = camino1.clonar();
        assertTrue(caminoCopia.comparar(camino1));
        assertTrue(camino1.comparar(camino1));
        assertFalse(camino1.comparar(camino2));
    }

    @Test
    void compararSubconjunto() {
        CaminoHamiltoniano caminoCopia = camino1.clonar();
        caminoCopia.getNodos().get(0).setSiguiente(null);
        caminoCopia.getNodos().get(5).setSiguiente(null);
        assertTrue(camino1.compararSubconjunto(caminoCopia));
    }

    @Test
    void alterarNodo() {
        CaminoHamiltoniano caminoCopia = camino1.clonar();
        caminoCopia.alterarNodo(2);
        assertTrue(caminoCopia.getNodos().get(2).isVisible()!=camino1.getNodos().get(2).isVisible());
    }
}