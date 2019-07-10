package com.hamiltonmaker.Comun.AlgoritmoGenetico;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Utils.AlertManager;
import com.hamiltonmaker.Comun.Utils.OutputManager;
import com.hamiltonmaker.Persistencia.DAOSolucion;
import com.hamiltonmaker.Vistas.Celdas.CaminoDobleCellFactory;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;

/**
 * Descripción: Clase encargada de gestionar el algoritmo genetico y sus poblaciones en segundo plano
 * Autor: Alexander Garcia
 */
public class AlgotirmoGenetico implements Runnable{
    private Thread hilo;
    private boolean activo;
    int numPoblacion;
    CaminoHamiltoniano camino;
    ArrayList<CaminoHamiltoniano> limitaciones;
    ArrayList<CaminoHamiltoniano> solucionesOptimas;
    ArrayList<Individuo> poblacion;
    int adyacenciasDeseadas = 1;
    double fitnessTotal;
    double coeficienteMutacion = 0.5;
    double coeficienteCruce = 0.7;
    int sizePoblacion = 20;
    ListView<CaminoHamiltoniano[]> lista;
    Label lPoblacion;
    Label lSolucionesOptimas;
    boolean pausado = false;

    public AlgotirmoGenetico(CaminoHamiltoniano camino, ArrayList<CaminoHamiltoniano> limitaciones, ListView<CaminoHamiltoniano[]>lista, int adyacenciasDeseadas, Label lPoblacion, Label lSolucionesOptimas) {
        this.camino = camino;
        this.limitaciones = limitaciones;
        this.lista = lista;
        this.adyacenciasDeseadas = adyacenciasDeseadas;
        this.poblacion = new ArrayList<Individuo>();
        this.solucionesOptimas = new ArrayList<CaminoHamiltoniano>();
        lista.setCellFactory(new CaminoDobleCellFactory());
        this.lPoblacion = lPoblacion;
        this.lSolucionesOptimas = lSolucionesOptimas;
    }

    public CaminoHamiltoniano getCamino() {
        return camino;
    }

    public void setCamino(CaminoHamiltoniano camino) {
        this.camino = camino;
    }

    public ArrayList<CaminoHamiltoniano> getLimitaciones() {
        return limitaciones;
    }

    public void setLimitaciones(ArrayList<CaminoHamiltoniano> limitaciones) {
        this.limitaciones = limitaciones;
    }

    public ArrayList<Individuo> getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(ArrayList<Individuo> poblacion) {
        this.poblacion = poblacion;
    }

    public double getCoeficienteMutacion() {
        return coeficienteMutacion;
    }

    public void setCoeficienteMutacion(double coeficienteMutacion) {
        this.coeficienteMutacion = coeficienteMutacion;
    }

    public double getCoeficienteCruce() {
        return coeficienteCruce;
    }

    public void setCoeficienteCruce(double coeficienteCruce) {
        this.coeficienteCruce = coeficienteCruce;
    }

    public int getSizePoblacion() {
        return sizePoblacion;
    }

    public void setSizePoblacion(int sizePoblacion) {
        this.sizePoblacion = sizePoblacion;
    }

    public void Iniciar(){

    }

    public void generarPoblacionInicial(){
        for(int c = 0; c<sizePoblacion; c++){
            Individuo individuo = new Individuo(camino);
            int adyacenciasAfectadas = (int) (Math.random()*(individuo.caminoHamiltoniano.getNodos().size()-3-individuo.caminoHamiltoniano.getInhabilitados())+1);
            individuo.alterarNodos(adyacenciasAfectadas);
            poblacion.add(individuo);
        }
    }

    public void calcularFitness(){
        fitnessTotal = 0;
        for (Individuo individuo : poblacion){
            fitnessTotal += individuo.funcionFitness(limitaciones,adyacenciasDeseadas);
            evaluarSolucion(individuo);
        }
    }

    public ArrayList<CaminoHamiltoniano[]> obtenerCaminos(){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        for(Individuo individuo: poblacion)
            caminos.add(individuo.caminoHamiltoniano);
        ArrayList<CaminoHamiltoniano[]> caminosDoble = new ArrayList<>();
        for(int i = 0; i< caminos.size(); i+=2){
            CaminoHamiltoniano[] dupla;
            if(i+1<caminos.size()){
                dupla = new CaminoHamiltoniano[]{caminos.get(i), caminos.get(i + 1)};
            }else {
                dupla = new CaminoHamiltoniano[]{caminos.get(i)};
            }
            caminosDoble.add(dupla);
        }
        return caminosDoble;
    }

    public void mutarPoblacion(){
        for(Individuo individuo: poblacion){
            double probabilidad = Math.random();
            if(probabilidad<= coeficienteMutacion){
                individuo.funcionDeMutacion();
            }
        }
    }

    public Individuo SeleccionarIndividuo(){
        double ruleta = Math.random();
        double acumulado = 0;
        for(Individuo individuo: poblacion){
            double probabilidad = ((double)individuo.fitness)/((double)this.fitnessTotal);
            if(acumulado<=ruleta && ruleta<= (acumulado+probabilidad)){
                return individuo;
            }
            acumulado+=probabilidad;
        }
        return null;
    }

    public void reproducirPoblacion(){
        ArrayList<Individuo> nuevaPoblacion= new ArrayList<>();
        while(nuevaPoblacion.size()<this.poblacion.size()){
            double probabilidad = Math.random();
            Individuo padre1 = SeleccionarIndividuo();
            Individuo padre2 = SeleccionarIndividuo();
            while(padre1==padre2){
                padre2 = SeleccionarIndividuo();
            }
            Individuo hijo1 = new Individuo();
            Individuo hijo2 = new Individuo();
            if(probabilidad<= coeficienteCruce){
                Individuo.funcionDeCruce(padre1,padre2,hijo1,hijo2);
            }
            else{
                hijo1=padre1;
                hijo2=padre2;
            }
            nuevaPoblacion.add(hijo1);
            nuevaPoblacion.add(hijo2);
        }
    }

    public void evaluarSolucion(Individuo individuo){
        if(individuo.isSolucion()){
            if(individuo.evaluacion == 0){
                CaminoHamiltoniano.agregarALista(solucionesOptimas, individuo.getCaminoHamiltoniano());
            }
        }
    }

    public void start() {
        hilo = new Thread(this);
        activo = true;
        hilo.start();
    }

    public void stop(){
        activo= false;
    }

    public void pausar(){
        pausado= true;
    }

    public void renaudar(){
        pausado= false;
    }

    @Override
    public void run() {
        generarPoblacionInicial();
        int i= 0;
        Dibujante dibujante = new Dibujante();
        dibujante.start();
        System.out.println("------------------------------------------------------------------");
        System.out.println("Tamaño: "+this.camino.getNodos().size());
        System.out.println("Poblacion: "+ sizePoblacion);
        System.out.println("Cruce: "+ coeficienteCruce);
        System.out.println("Mutacion: "+ coeficienteMutacion);
        System.out.println();
        long tinicio = System.currentTimeMillis();
        int j = 0;
        while(activo){
            if(!pausado){
                calcularFitness();
                OutputManager.imprimirPoblacion(i,this.poblacion);
                reproducirPoblacion();
                mutarPoblacion();
                if(System.currentTimeMillis()>tinicio+10000 && j<6){
                    j++;
                    tinicio = System.currentTimeMillis();
                    System.out.println("Datos luego de " + j*10 + " Sg:");
                    System.out.println("    Poblacion: "+numPoblacion);
                    System.out.println("    Soluciones encontradas: "+solucionesOptimas.size());
                }
                i++;
                numPoblacion++;

            }
        }
        dibujante.stop();
        OutputManager.imprimirPoblacion(i,this.poblacion);
        for (CaminoHamiltoniano c: solucionesOptimas){
            DAOSolucion.saveSolucion(c);
        }
        solucionesOptimas.clear();
    }

    class Dibujante implements Runnable{
        private Thread hilo;
        private boolean activo;

        public void start() {
            hilo = new Thread(this);
            activo = true;
            hilo.start();
        }

        public void stop(){
            activo= false;
        }

        @Override
        public void run() {
            while(activo){
                try {
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lista.getItems().clear();
                            lista.getItems().addAll(obtenerCaminos());
                            lPoblacion.setText(String.valueOf(numPoblacion));
                            lSolucionesOptimas.setText(String.valueOf(solucionesOptimas.size()));
                        }
                    });
                } catch (InterruptedException e) {
                    AlertManager.alertarError();
                }
            }

        }
    }

}
