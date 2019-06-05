package com.hamiltonmaker.Comun.AlgoritmoGenetico;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Individuo {
    CaminoHamiltoniano caminoHamiltoniano;
    double fitness;
    double evaluacion;
    boolean solucion = false;

    public Individuo() {
    }

    public Individuo(CaminoHamiltoniano caminoHamiltoniano) {
        this.caminoHamiltoniano = caminoHamiltoniano.clonar();
    }

    public CaminoHamiltoniano getCaminoHamiltoniano() {
        return caminoHamiltoniano;
    }

    public void setCaminoHamiltoniano(CaminoHamiltoniano caminoHamiltoniano) {
        this.caminoHamiltoniano = caminoHamiltoniano;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public boolean isSolucion() {
        return solucion;
    }

    public void setSolucion(boolean solucion) {
        this.solucion = solucion;
    }

    public void alterarNodos(int cantidad){
        int i = 0;
        while(i<cantidad){
            int posicion = (int) (Math.random()*(caminoHamiltoniano.getNodos().size()-1));
            if(caminoHamiltoniano.getNodos().get(posicion).isVisible()&&caminoHamiltoniano.getNodos().get(posicion).isHabilitado()){
                caminoHamiltoniano.alterarNodo(posicion);
                i++;
            }
        }
    }

    public double funcionFitness(ArrayList<CaminoHamiltoniano> limitaciones,int adyacenciasDeseadas){
        int soluciones = 0;
        for (CaminoHamiltoniano c : limitaciones){
            if (c.contieneCromosoma(caminoHamiltoniano))
                soluciones ++;
        }
        if (soluciones == 0) {
            solucion = true;
        }
        else {
            solucion = false;
        }
        this.evaluacion = abs(caminoHamiltoniano.getVisibles()-adyacenciasDeseadas)+(soluciones);
        //this.evaluacion = (soluciones);
        this.fitness = 1.0/(1.0+this.evaluacion);
        return this.fitness;
    }

    public static void funcionDeCruce(Individuo padre1, Individuo padre2, Individuo hijo1, Individuo hijo2){
        CaminoHamiltoniano caminoHijo1 = padre1.caminoHamiltoniano.clonar();
        CaminoHamiltoniano caminoHijo2 = padre2.caminoHamiltoniano.clonar();
        int corte = (int) (Math.random()*(caminoHijo1.getNodos().size()-1));
        for (int i = 0; i<caminoHijo1.getNodos().size(); i++){
            if(i<corte){
                caminoHijo1.alterarNodo(i,padre1.caminoHamiltoniano);
                caminoHijo2.alterarNodo(i,padre2.caminoHamiltoniano);
            }
            else{
                caminoHijo1.alterarNodo(i,padre2.caminoHamiltoniano);
                caminoHijo2.alterarNodo(i,padre1.caminoHamiltoniano);
            }
        }
        hijo1.setCaminoHamiltoniano(caminoHijo1);
        hijo2.setCaminoHamiltoniano(caminoHijo2);
    }

    public void funcionDeMutacion(){
        int posicion = (int) (Math.random()*(caminoHamiltoniano.getNodos().size()-1));
        if(caminoHamiltoniano.getNodos().get(posicion).isHabilitado()){
            //System.out.print(" en el gen "+posicion);
            caminoHamiltoniano.alterarNodo(posicion);
        }
    }

    public String imprimir(int num){
        String cromosoma = " ";
        for(Nodo n : caminoHamiltoniano.getNodos()){
            if(n.isVisible())
                cromosoma += "1 ";
            else
                cromosoma += "0 ";
        }
        String mensaje = "Individuo "+num+":\t"+cromosoma+"\t  Evaluacion: "+this.evaluacion+"\t  Fitness: "+this.fitness;
        if(solucion) mensaje += "\t *";
        return mensaje;
    }
}
