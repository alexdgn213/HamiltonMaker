package com.hamiltonmaker.Comun.Entidades;

import java.util.LinkedList;

public class Search {

    public static void main(String[] args) {

    }

    public void depthFirst(Graph graph, LinkedList<String> visited, String END) {
        LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
        // examine adjacent nodes
        for (String node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(END)) {
                visited.add(node);
                printPath(visited);
                visited.removeLast();
                break;
            }
        }
        for (String node : nodes) {
            if (visited.contains(node) || node.equals(END)) {
                continue;
            }
            visited.addLast(node);
            depthFirst(graph, visited, END);
            visited.removeLast();
        }
    }

    public void printPath(LinkedList<String> visited) {
        if(visited.size()==9){
            for (String node : visited) {
                System.out.print(node);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
