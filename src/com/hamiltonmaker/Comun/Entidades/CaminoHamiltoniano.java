package com.hamiltonmaker.Comun.Entidades;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;

public class CaminoHamiltoniano {
    private List<Nodo> nodos; //Nodos que conforman el camino.
    private int inicio; //Posicion en la que inicia el camino
    private int fin; //Posicion en la que finaliza el camino

    public CaminoHamiltoniano() {
        this.nodos = new ArrayList<Nodo>();
    }

    public CaminoHamiltoniano(ArrayList<Nodo> nodos, LinkedList<Nodo> orden, int inicio, int fin) {
        this.inicio = inicio;
        this.fin = fin;
        this.nodos = new ArrayList<Nodo>();
        for(Nodo nodo: nodos){
            if(nodo!=null) this.nodos.add(nodo.clonar());
            else this.nodos.add(null);
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
            if(nodo!=null) this.nodos.add(nodo.clonar());
            else this.nodos.add(null);
        }
    }

    public List<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(List<Nodo> nodos) {
        this.nodos = nodos;
    }

    public void addNodo(Nodo nodo) {
        this.nodos.add(nodo);
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

    public void dibujar(GraphicsContext gc){
        double x = gc.getCanvas().getWidth();
        double y = gc.getCanvas().getHeight();
        gc.clearRect(0, 0, x, y);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        for(Nodo n:this.nodos){
            if(n!=null){
                if(n.getSiguiente()!=null && n.isVisible()){
                    gc.setStroke(Color.BLUE);
                    gc.strokeLine(move(n.getPosX(),x), move(n.getPosY(),y), move(n.getSiguiente().getPosX(),x), move(n.getSiguiente().getPosY(),y));
                }
                else if(n.getSiguiente()!=null && !n.isVisible()){
                    gc.setStroke(Color.GRAY);
                    gc.strokeLine(move(n.getPosX(),x), move(n.getPosY(),y), move(n.getSiguiente().getPosX(),x), move(n.getSiguiente().getPosY(),y));
                }
            }

        }
        int i = 0;
        for(Nodo n:this.nodos){
            if(n!=null){
                if(n.isHabilitado()){
                    gc.setFill(Color.GREEN);
                    gc.setStroke(Color.BLUE);
                    double radio =  100 / Math.sqrt(this.nodos.size());
                    gc.fillOval(move(n.getPosX(),x)-radio/2, move(n.getPosY(),y)-radio/2, radio, radio);
                    gc.strokeOval(move(n.getPosX(),x)-radio/2, move(n.getPosY(),y)-radio/2, radio, radio);

                    if(i==this.inicio) {
                        final Image image = new Image("/images/i_play.png");
                        gc.drawImage(image,move(n.getPosX(),x)-radio/2+2.5, move(n.getPosY(),y)-radio/2+2.5, radio-5, radio-5);
                    }
                    if(i==this.fin) {
                        final Image image = new Image("/images/i_pause.png");
                        gc.drawImage(image,move(n.getPosX(),x)-radio/2+2.5, move(n.getPosY(),y)-radio/2+2.5, radio-5, radio-5);
                    }
                }
                else{
                    gc.setFill(Color.GRAY);
                    gc.setStroke(Color.GRAY);
                    double radio =   200 / Math.sqrt(this.nodos.size());
                    gc.fillRect(move(n.getPosX(),x)-radio/2, move(n.getPosY(),y)-radio/2, radio, radio);
                    gc.strokeRect(move(n.getPosX(),x)-radio/2, move(n.getPosY(),y)-radio/2, radio, radio);
                }

            }
            i++;
        }
    }

    public double move(int pos, double max){
        return pos*(max / (Math.sqrt(this.nodos.size())+1)) + max / (Math.sqrt(this.nodos.size())+1);
    }

    public CaminoHamiltoniano clonar(){
        CaminoHamiltoniano nuevoCamino = new CaminoHamiltoniano();
        nuevoCamino.fin = this.fin;
        nuevoCamino.inicio = this.inicio;
        for(Nodo n: this.nodos){
            nuevoCamino.nodos.add(n.clonar());
        }
        for(int i =0; i<nuevoCamino.nodos.size();i++){
            if(this.nodos.get(i).getSiguiente()!=null)
                nuevoCamino.nodos.get(i).setSiguiente(nuevoCamino.nodos.get(this.nodos.indexOf(this.nodos.get(i).getSiguiente())));
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
                intersecciones.add(intersectar(camino1,c));
        return intersecciones;
    }


    public boolean contiene(CaminoHamiltoniano camino){
        boolean contenido = true;
        if(this.inicio!=camino.inicio || this.fin!=camino.fin)
            contenido = false;
        for(int i=0; i<this.nodos.size() && contenido; i++){
            if(!this.nodos.get(i).compararSiguiente(camino.nodos.get(i)) ||
                    (this.nodos.get(i).getSiguiente()==null && camino.nodos.get(i).getSiguiente()!=null)){
                contenido = false;
            }
        }
        return contenido;
    }

    public boolean comparar(CaminoHamiltoniano camino){
        boolean igual = true;
        if(this.inicio!=camino.inicio || this.fin!=camino.fin)
            igual = false;
        for(int i=0; i<this.nodos.size() && igual; i++){{
             if(!this.nodos.get(i).compararSiguiente(camino.nodos.get(i)) ||
                     (this.nodos.get(i).getSiguiente()==null ^ camino.nodos.get(i).getSiguiente()==null))
                 igual = false;
            }
        }
        return igual;
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
