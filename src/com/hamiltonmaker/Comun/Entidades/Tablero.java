package com.hamiltonmaker.Comun.Entidades;

import java.util.*;

public class Tablero {
    private Map<Nodo, LinkedHashSet<Nodo>> map = new HashMap();
    private ArrayList<Nodo> nodos;
    private int size;
    private int inicio;
    private int fin;
    private int inhabilitados;

    public Tablero() {
    }

    public void generar(int size){
        this.size = size;
        map = new HashMap();
        nodos = new ArrayList<Nodo>();

        for(int j = 0; j<size; j++){
            for(int i=0; i<size; i++){
                nodos.add(new Nodo(i,j));
            }
        }


        if(size==8){
            nodos.get(14).setHabilitado(false);
            nodos.get(15).setHabilitado(false);
            nodos.get(20).setHabilitado(false);
            nodos.get(21).setHabilitado(false);
            this.inhabilitados=4;
        }

        if(size==7){
            nodos.get(0).setHabilitado(false);
            nodos.get(1).setHabilitado(false);
            nodos.get(7).setHabilitado(false);
            nodos.get(8).setHabilitado(false);
            nodos.get(48).setHabilitado(false);
            nodos.get(47).setHabilitado(false);
            nodos.get(40).setHabilitado(false);
            nodos.get(41).setHabilitado(false);
            this.inhabilitados=8;
        }

        for(Nodo nodo : nodos){
            if(nodo.getPosX()>0)
                addEdge(nodo,nodos.get(nodo.getPosX()-1+size*(nodo.getPosY())));
            if(nodo.getPosX()<size-1)
                addEdge(nodo,nodos.get(nodo.getPosX()+1+size*(nodo.getPosY())));
            if(nodo.getPosY()>0)
                addEdge(nodo,nodos.get(nodo.getPosX()+size*(nodo.getPosY()-1)));
            if(nodo.getPosY()<size-1)
                addEdge(nodo,nodos.get(nodo.getPosX()+size*(nodo.getPosY()+1)));
        }
    }

    public void addEdge(Nodo node1, Nodo node2) {
        LinkedHashSet<Nodo> adjacent = map.get(node1);
        if(node1.isHabilitado()&&node2.isHabilitado()){
            if(adjacent==null) {
                adjacent = new LinkedHashSet();
                map.put(node1, adjacent);
            }
            adjacent.add(node2);
        }
    }

    public void addTwoWayVertex(Nodo node1, Nodo node2) {
        addEdge(node1, node2);
        addEdge(node2, node1);
    }

    public boolean isConnected(Nodo node1, Nodo node2) {
        Set adjacent = map.get(node1);
        if(adjacent==null) {
            return false;
        }
        return adjacent.contains(node2);
    }

    public LinkedList<Nodo> adjacentNodes(Nodo last) {
        LinkedHashSet<Nodo> adjacent = map.get(last);
        if(adjacent==null) {
            return new LinkedList();
        }
        return new LinkedList<Nodo>(adjacent);
    }

    public void depthFirst(LinkedList<Nodo> visited, Nodo END, ArrayList<CaminoHamiltoniano> caminos) {
        LinkedList<Nodo> nodes = this.adjacentNodes(visited.getLast());
        // examine adjacent nodes
        for (Nodo node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(END)) {
                visited.add(node);
                agregarCamino(visited,caminos);
                visited.removeLast();
                break;
            }
        }
        for (Nodo node : nodes) {
            if (visited.contains(node) || node.equals(END)) {
                continue;
            }
            visited.addLast(node);
            depthFirst(visited, END,caminos);
            visited.removeLast();
        }
    }

    //Algoritmo DFS para hallar todos los caminos con mismo origen y fin
    public ArrayList<CaminoHamiltoniano> depthFirst(int inicio, int fin){
        long st = System.currentTimeMillis();
        this.inicio= inicio;
        this.fin = fin;
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        LinkedList<Nodo> visited = new LinkedList();
        visited.add(this.nodos.get(inicio));
        depthFirst(visited,this.nodos.get(fin),caminos);
        System.out.println("Tiempo empleado en cacular caminos: "+ (System.currentTimeMillis()-st));
        return caminos;
    }

    public CaminoHamiltoniano getTableroVacio(){
        return new CaminoHamiltoniano(this.nodos,this.inicio, this.fin);
    }

    public void agregarCamino(LinkedList<Nodo> visited,ArrayList<CaminoHamiltoniano>caminos) {
        if(visited.size()==(size*size)-inhabilitados){
            CaminoHamiltoniano cam = new CaminoHamiltoniano(this.nodos,visited,this.inicio,this.fin,this.inhabilitados);
            caminos.add(cam);
        }
    }
}
