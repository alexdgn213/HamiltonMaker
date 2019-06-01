package com.hamiltonmaker.Comun.AlgoritmoGenetico;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Vistas.CaminoCellFactory;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;

public class AlgotirmoGenetico implements Runnable{

    CaminoHamiltoniano camino;
    ArrayList<CaminoHamiltoniano> limitaciones;
    ArrayList<CaminoHamiltoniano> soluciones;
    ArrayList<CaminoHamiltoniano> solucionesOptimas;
    ArrayList<Individuo> poblacion;
    int adyacenciasDeseadas = 1;
    double fitnessTotal;
    double cohefisienteMutacion = 0.5;
    double cohefisienteCruce = 0.5;
    ListView<CaminoHamiltoniano> lista;

    public AlgotirmoGenetico(CaminoHamiltoniano camino, ArrayList<CaminoHamiltoniano> limitaciones, ListView<CaminoHamiltoniano>lista) {
        this.camino = camino;
        this.limitaciones = limitaciones;
        this.lista = lista;
        this.poblacion = new ArrayList<Individuo>();
        this.solucionesOptimas = new ArrayList<CaminoHamiltoniano>();
        this.soluciones = new ArrayList<CaminoHamiltoniano>();
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

    public void Iniciar(){

    }

    public void generarPoblacionInicial(){
        for(int c = 0; c<20; c++){
            Individuo individuo = new Individuo(camino);
            int adyacenciasAfectadas = (int) (Math.random()*(individuo.caminoHamiltoniano.getNodos().size()-3-individuo.caminoHamiltoniano.getInhabilitados())+1);
            individuo.alterarNodos(adyacenciasAfectadas);
            poblacion.add(individuo);
        }
    }

    public void calcularFitness(){
        long st = System.currentTimeMillis();
        fitnessTotal = 0;
        for (Individuo individuo : poblacion){
            fitnessTotal += individuo.funcionFitness(limitaciones,adyacenciasDeseadas);
            evaluarSolucion(individuo);
        }
        System.out.println();
        System.out.println("Soluciones optimas: "+ this.solucionesOptimas.size());
        System.out.println("Soluciones: "+ this.soluciones.size());
    }

    public ArrayList<CaminoHamiltoniano> obtenerCaminos(){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        for(Individuo individuo: poblacion)
            caminos.add(individuo.caminoHamiltoniano);
        return caminos;
    }

    public void mutarPoblacion(){
        int count = 0;
        System.out.println();
        System.out.println();
        System.out.println("Mutaciones:");
        for(Individuo individuo: poblacion){
            count++;
            double probabilidad = Math.random();
            if(probabilidad<=cohefisienteMutacion){
                System.out.println("");
                System.out.print("    Mutacion del individuo: "+count);
                individuo.funcionDeMutacion();
            }
        }
    }

    public Individuo SeleccionarIndividuo(){
        double ruleta = Math.random();
        double acumulado = 0;
        int count = 0;
        for(Individuo individuo: poblacion){
            count ++;
            double probabilidad = ((double)individuo.fitness)/((double)this.fitnessTotal);
            if(acumulado<=ruleta && ruleta<= (acumulado+probabilidad)){
                System.out.print(count + " ");
                return individuo;
            }
            acumulado+=probabilidad;
        }
        return null;
    }

    public void reproducirPoblacion(){
        ArrayList<Individuo> nuevaPoblacion= new ArrayList<>();
        System.out.println();
        System.out.println();
        System.out.println("Cruces:");
        while(nuevaPoblacion.size()<this.poblacion.size()){
            double probabilidad = Math.random();
            System.out.println();
            System.out.print("    Seleccionados los individuos: ");
            Individuo padre1 = SeleccionarIndividuo();
            Individuo padre2 = SeleccionarIndividuo();
            while(padre1==padre2){
                padre2 = SeleccionarIndividuo();
            }
            Individuo hijo1 = new Individuo();
            Individuo hijo2 = new Individuo();
            if(probabilidad<=cohefisienteCruce){
                System.out.print("y cruzados");
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
                CaminoHamiltoniano.agregarALista(soluciones,individuo.getCaminoHamiltoniano());
            }
            else{
                CaminoHamiltoniano.agregarALista(soluciones,individuo.getCaminoHamiltoniano());
            }
        }
    }

    public void imprimirPoblacion(int numero){
        System.out.println();
        System.out.println();
        System.out.println("--------------------------- Poblacion "+numero+" -----------------------------");
        System.out.println();
        int num=0;
        for(Individuo i: poblacion){
            num++;
            i.imprimir(num);
        }
    }

    @Override
    public void run() {
        generarPoblacionInicial();
        adyacenciasDeseadas =10;
        int i= 0;
        //Thread t = new Thread(new Dibujante());
        //t.start();
        while(i<10000){
            calcularFitness();
            imprimirPoblacion(i);
            reproducirPoblacion();
            mutarPoblacion();
            i++;
        }
        //t.interrupt();
        imprimirPoblacion(i);
    }

    class Dibujante implements Runnable{

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(5000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lista.getItems().clear();
                            lista.getItems().addAll(obtenerCaminos());
                            lista.setCellFactory(new CaminoCellFactory());
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
