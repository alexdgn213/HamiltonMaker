package com.hamiltonmaker.Comun.Entidades;

/**
 * Descripción: Clase que representa un nodo en un tablero, camino hamiltoniano o solución parcial
 * Autor: Alexander Garcia
 */
public class Nodo {
    private int posX; //Indica la posicion X del nodo en el espacio.
    private int posY; //Indica la posicion Y del nodo en el espacion.
    private Nodo siguiente;
    private boolean visible; //Indica si la adyacencia es visible para el jugador.
    private boolean habilitado;

    public Nodo() {
        this.visible = true;
    }

    public Nodo(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.visible = true;
        this.habilitado = true;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }


    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public boolean compararSiguiente(Nodo nodo2){
        boolean resultado = this.comparar(nodo2);
        if((this.siguiente!=null) && (nodo2.siguiente!=null)){
            resultado = resultado && this.siguiente.comparar(nodo2.getSiguiente());
        }
        else if((this.siguiente == null) != (nodo2.siguiente == null)){
            resultado = false;
        }
        return resultado;
    }

    public boolean compararCruce(Nodo nodo2){
        boolean resultado = false;
        if((this.siguiente!=null) && (nodo2.siguiente!=null)){
            if(!this.comparar(nodo2))
                resultado = nodo2.siguiente.comparar(this);
        }
        return resultado;
    }

    public boolean comparar(Nodo nodo2){
        return this.posX==nodo2.posX && this.posY == nodo2.posY;
    }

    public Nodo clonar(){
        Nodo nuevoNodo = new Nodo();
        nuevoNodo.setPosX(this.posX);
        nuevoNodo.setPosY(this.posY);
        nuevoNodo.setVisible(this.visible);
        nuevoNodo.setHabilitado(this.habilitado);
        return nuevoNodo;
    }

    @Override
    public String toString() {
        String ret = "\nNodo{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", visible=" + visible;
        if(siguiente!=null)
                ret +=", siguiente=" + siguiente.posX + "," +siguiente.posY;
        else
            ret += "FIN";
        ret += '}';
        return ret;
    }
}

