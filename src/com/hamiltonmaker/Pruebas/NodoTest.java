package com.hamiltonmaker.Pruebas;

import com.hamiltonmaker.Comun.Entidades.Nodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Descripción: Pruebas unitarias para los métodos de la clase Nodo
 * Autor: Alexander Garcia
 */
class NodoTest {
    Nodo nodo1;
    Nodo nodo2;
    Nodo nodo3;
    Nodo nodo4;

    @BeforeEach
    void setUp() {
        nodo1 = new Nodo(0,0);
        nodo2 = new Nodo(0,1);
        nodo3 = new Nodo(1,0);
        nodo4 = new Nodo(1,1);
        nodo1.setSiguiente(nodo2);
        nodo3.setSiguiente(nodo4);
    }

    @Test
    void compararSiguiente() {
        assertFalse(nodo1.compararSiguiente(nodo3));
        Nodo nodoCopia = nodo1.clonar();
        Nodo nodoCopia2 = nodo2.clonar();
        nodoCopia.setSiguiente(nodoCopia2);
        assertTrue(nodo1.compararSiguiente(nodoCopia));

    }

    @Test
    void clonar() {
        Nodo nodoCopia = nodo1.clonar();
        assertEquals(nodo1.getPosX(),nodoCopia.getPosX());
        assertEquals(nodo1.getPosY(),nodoCopia.getPosY());
        assertEquals(nodo1.isHabilitado(),nodoCopia.isHabilitado());
        assertEquals(nodo1.isVisible(),nodoCopia.isVisible());
    }
}