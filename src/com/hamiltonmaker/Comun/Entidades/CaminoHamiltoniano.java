package com.hamiltonmaker.Comun.Entidades;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;


/**
 * Descripción: Clase que representa un camino hamiltoniano o una solucion parcial junto con
 * sus operaciones básicas
 * Autor: Alexander Garcia
 */
public class CaminoHamiltoniano {
    private int id;
    private List<Nodo> nodos; //Nodos que conforman el camino.
    private int inicio; //Posicion en la que inicia el camino
    private int fin; //Posicion en la que finaliza el camino
    private int visibles = 0;
    private int inhabilitados;
    private final Color azul = Color.web("#161899");
    private final Color verde = Color.web("#648b3f");
    private final Color gris = Color.web("#B2B2B2");

    public CaminoHamiltoniano() {
        this.nodos = new ArrayList<Nodo>();
    }

    public CaminoHamiltoniano(ArrayList<Nodo> nodos, LinkedList<Nodo> orden, int inicio, int fin, int inhabilitados) {
        this.inicio = inicio;
        this.fin = fin;
        this.inhabilitados = inhabilitados;
        this.nodos = new ArrayList<Nodo>();
        for(Nodo nodo: nodos){
            addNodo(nodo.clonar());
        }
        int pos;
        int posSiguiente;
        for(int i=0; i+1<orden.size();i++){
            pos = nodos.indexOf(orden.get(i));
            posSiguiente = nodos.indexOf(orden.get(i+1));
            if( this.nodos.get(pos)!=null && this.nodos.get(posSiguiente)!=null)
                this.nodos.get(pos).setSiguiente(this.nodos.get(posSiguiente));
        }
    }

    public CaminoHamiltoniano(ArrayList<Nodo> nodos, int inicio, int fin) {
        this.inicio = inicio;
        this.fin = fin;
        this.nodos = new ArrayList<Nodo>();
        for(Nodo nodo: nodos){
            addNodo(nodo.clonar());
        }
    }

    public CaminoHamiltoniano(ArrayList<Nodo> nodos, int inicio, int fin, int inhabilitados) {
        this.inicio = inicio;
        this.fin = fin;
        this.inhabilitados = inhabilitados;
        this.nodos = new ArrayList<Nodo>();
        for(Nodo nodo: nodos){
            addNodo(nodo.clonar());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(List<Nodo> nodos) {
        this.nodos = nodos;
    }

    public void addNodo(Nodo nodo) {
        this.nodos.add(nodo);
        if (nodo.isVisible() && nodo.isHabilitado()) visibles++;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    public int getVisibles() {
        return visibles;
    }

    public void setVisibles(int visibles) {
        this.visibles = visibles;
    }

    public int getInhabilitados() {
        return inhabilitados;
    }

    public void setInhabilitados(int inhabilitados) {
        this.inhabilitados = inhabilitados;
    }

    public void dibujar(GraphicsContext gc){
        dibujar(gc,1,0);
    }

    public void dibujarDos(GraphicsContext gc, int pos){
        double x = gc.getCanvas().getWidth()/2;
        double offset = 0;
        if(pos%2!=0){
            offset = x;
        }
        dibujar(gc,2,offset);
    }

    public void dibujar(GraphicsContext gc,int columnas, double offset){
        double x = gc.getCanvas().getWidth()/columnas;
        double y = gc.getCanvas().getHeight();
        gc.clearRect(offset, 0, x, y);
        gc.setFill(verde);
        gc.setStroke(azul);
        gc.setLineWidth(5);
        for(Nodo n:this.nodos){
            if(n!=null){
                if(n.getSiguiente()!=null && n.isVisible()){
                    gc.setStroke(azul);
                    gc.strokeLine(move(n.getPosX(),x,offset), move(n.getPosY(),y), move(n.getSiguiente().getPosX(),x,offset), move(n.getSiguiente().getPosY(),y));
                }
            }

        }
        int i = 0;
        for(Nodo n:this.nodos){
            if(n!=null){
                double radio =  (x /(Math.sqrt(this.nodos.size())*2) );
                if(n.isHabilitado()){
                    gc.setFill(verde);
                    gc.setStroke(azul);
                    gc.fillOval(move(n.getPosX(),x,offset)-radio/2, move(n.getPosY(),y)-radio/2, radio, radio);
                    gc.strokeOval(move(n.getPosX(),x,offset)-radio/2, move(n.getPosY(),y)-radio/2, radio+1, radio+1);
                    double proporcion = 0.7;
                    if(i==this.inicio) {
                        final Image image = new Image("/recursos/imagenes/ic_inicio.png");
                        gc.drawImage(image,move(n.getPosX(),x,offset)-(radio*proporcion)/2, move(n.getPosY(),y)-(radio*proporcion)/2, radio*proporcion, radio*proporcion);
                    }
                    if(i==this.fin) {
                        final Image image = new Image("/recursos/imagenes/ic_pausa.png");
                        gc.drawImage(image,move(n.getPosX(),x,offset)-(radio*proporcion)/2, move(n.getPosY(),y)-(radio*proporcion)/2, radio*proporcion, radio*proporcion);
                    }
                }
                else{
                    gc.setFill(gris);
                    gc.setStroke(gris);
                    radio =  radio*1.5;
                    gc.fillRect(move(n.getPosX(),x,offset)-radio/2, move(n.getPosY(),y)-radio/2, radio, radio);
                    gc.strokeRect(move(n.getPosX(),x,offset)-radio/2, move(n.getPosY(),y)-radio/2, radio, radio);
                }
            }
            i++;
        }
    }
    public double move(int pos, double max){
        return pos*(max / (Math.sqrt(this.nodos.size())+1)) + max / (Math.sqrt(this.nodos.size())+1);
    }

    public double move(int pos, double max, double offset){
        return offset + move(pos, max);
    }


    public CaminoHamiltoniano clonar(){
        CaminoHamiltoniano nuevoCamino = new CaminoHamiltoniano();
        nuevoCamino.id = this.id;
        nuevoCamino.fin = this.fin;
        nuevoCamino.inicio = this.inicio;
        nuevoCamino.inhabilitados = this.inhabilitados;
        for(Nodo n: this.nodos){
            nuevoCamino.addNodo(n.clonar());
        }
        for(int i =0; i<nuevoCamino.nodos.size();i++){
            if(this.nodos.get(i).getSiguiente()!=null)
                nuevoCamino.nodos.get(i).setSiguiente(
                	nuevoCamino.nodos.get(this.nodos.indexOf(this.nodos.get(i).getSiguiente())));
        }
        return nuevoCamino;
    }


    public static CaminoHamiltoniano intersectar(CaminoHamiltoniano camino1, CaminoHamiltoniano camino2){
        CaminoHamiltoniano interseccion = camino1.clonar();
        for(int i=0; i<camino1.nodos.size(); i++){
            if(camino1.nodos.get(i).compararSiguiente(camino2.nodos.get(i))){
                interseccion.nodos.get(i).setVisible(true);
            }
            else{
                interseccion.nodos.get(i).setSiguiente(null);
                interseccion.nodos.get(i).setVisible(false);
            }
        }
        return interseccion;
    }

    public static ArrayList<CaminoHamiltoniano> intersectar(CaminoHamiltoniano camino1, ArrayList<CaminoHamiltoniano> caminos){
        ArrayList<CaminoHamiltoniano> intersecciones = new ArrayList<CaminoHamiltoniano>();
        for (CaminoHamiltoniano c : caminos)
            if(!camino1.comparar(c))
                intersecciones.add(c);
        return intersecciones;
    }

    public boolean contieneSubconjunto(CaminoHamiltoniano camino){
        boolean contenido = true;
        for(int i=0; i<this.nodos.size() && contenido; i++){
            if(camino.getNodos().get(i).isVisible() && camino.getNodos().get(i).isHabilitado()){
                if(this.getNodos().get(i).isVisible()){
                    contenido = this.getNodos().get(i).compararSiguiente(camino.getNodos().get(i));
                    if(!contenido){
                        contenido = this.nodos.get(i).compararCruce(camino.buscarNodo(this.nodos.get(i).getSiguiente())) ||
                                camino.nodos.get(i).compararCruce(this.buscarNodo(camino.nodos.get(i).getSiguiente()));
                    }
                }
                else{
                    contenido = false;
                }
            }
        }
        return contenido;
    }

    public boolean comparar(CaminoHamiltoniano camino){
        boolean igual = true;
        if(this.inicio!=camino.inicio || this.fin!=camino.fin)
            igual = false;
        for(int i=0; i<this.nodos.size() && igual; i++){
            igual = this.nodos.get(i).compararSiguiente(camino.nodos.get(i));
        }
        return igual;
    }

    public boolean compararSubconjunto(CaminoHamiltoniano camino){
        boolean igual = true;
        for(int i=0; i<this.nodos.size() && igual; i++){{
            if(this.getNodos().get(i).isVisible()!=camino.getNodos().get(i).isVisible())
                igual = false;
            }
        }
        return igual;
    }


    public void alterarNodo(int posicion){
        if(nodos.get(posicion).isHabilitado() && (nodos.get(posicion).getSiguiente()!=null)){
            if(nodos.get(posicion).isVisible()){
                nodos.get(posicion).setVisible(false);
                visibles--;
            }
            else {
                nodos.get(posicion).setVisible(true);
                visibles++;
            }
        }
    }

    public void alterarNodo(int posicion, CaminoHamiltoniano padre){
        if(nodos.get(posicion).isVisible()!=padre.getNodos().get(posicion).isVisible()){
            alterarNodo(posicion);
        }
    }

    public Nodo buscarNodo(Nodo nodo){
        for(Nodo n: nodos){
            if(n.comparar(nodo))
                return n;
        }
        return null;
    }

    public String obtenerDificultad(){
        int dificultad = this.nodos.size() - visibles;
        if(dificultad<10){
            return "Fácil";
        } else if (dificultad<20){
            return "Medio";
        } else if (dificultad < 30){
            return "Difícil";
        } else {
            return "Avanzado";
        }
    }

    public static void agregarALista(ArrayList<CaminoHamiltoniano> lista, CaminoHamiltoniano camino){
        for( CaminoHamiltoniano c : lista)
            if(c.compararSubconjunto(camino)){
                return;
            }
        lista.add(camino.clonar());
    }

    @Override
    public String toString() {
        return "CaminoHamiltoniano{" +
                ", inicio=" + inicio +
                ", fin=" + fin +
                ", nodos=" + nodos +
                '}';
    }
}
