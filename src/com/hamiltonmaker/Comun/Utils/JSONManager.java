package com.hamiltonmaker.Comun.Utils;

import com.google.gson.stream.JsonWriter;
import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSONManager {
    private static JsonWriter writerGlobal;

    public static void nodoToJSON(int posicion, Nodo nodo,  JsonWriter writer) throws IOException {

        writer.beginObject();

        writer.name("posicion").value(posicion);
        writer.name("posicion_x").value(nodo.getPosX());
        writer.name("posicion_y").value(nodo.getPosY());
        writer.name("habilitado").value(nodo.isHabilitado());
        writer.name("visible").value(nodo.isVisible());

        writer.endObject();
    }

    public static void caminoToJSON(CaminoHamiltoniano caminoHamiltoniano, JsonWriter writer) throws IOException {

        writer.name("id").value(caminoHamiltoniano.getId());
        writer.name("tamano").value((int) Math.sqrt(caminoHamiltoniano.getNodos().size()));
        writer.name("inicio").value(caminoHamiltoniano.getInicio());
        writer.name("fin").value(caminoHamiltoniano.getFin());

        Nodo nodo = caminoHamiltoniano.getNodos().get(caminoHamiltoniano.getInicio());
        writer.name("nodos");
        writer.beginArray();
        while(nodo!= null){
            int posicion = caminoHamiltoniano.getNodos().indexOf(nodo);
            nodoToJSON(posicion,nodo,writer);
            nodo = nodo.getSiguiente();
        }
        writer.endArray();
        writer.name("nodos_inhabilitados");
        writer.beginArray();
        for(Nodo n : caminoHamiltoniano.getNodos()){
            if(!n.isHabilitado()){
                int posicion = caminoHamiltoniano.getNodos().indexOf(n);
                nodoToJSON(posicion,n,writer);
            }
        }
        writer.endArray();

    }

    public static void solucionToJSON(CaminoHamiltoniano caminoHamiltoniano, JsonWriter writer) throws IOException {

        writer.beginObject();
        writer.name("id_camino").value(caminoHamiltoniano.getId());
        writer.name("tamano").value((int) Math.sqrt(caminoHamiltoniano.getNodos().size()));
        writer.name("inicio").value(caminoHamiltoniano.getInicio());
        writer.name("fin").value(caminoHamiltoniano.getFin());
        writer.name("adyacencias_visibles").value(caminoHamiltoniano.getVisibles()-1);
        writer.name("dificultad").value(caminoHamiltoniano.obtenerDificultad());

        Nodo nodo = caminoHamiltoniano.getNodos().get(caminoHamiltoniano.getInicio());
        writer.name("nodos");
        writer.beginArray();
        while(nodo!= null){
            int posicion = caminoHamiltoniano.getNodos().indexOf(nodo);
            nodoToJSON(posicion,nodo,writer);
            nodo = nodo.getSiguiente();
        }
        writer.endArray();
        writer.name("nodos_inhabilitados");
        writer.beginArray();
        for(Nodo n : caminoHamiltoniano.getNodos()){
            if(!n.isHabilitado()){
                int posicion = caminoHamiltoniano.getNodos().indexOf(n);
                nodoToJSON(posicion,n,writer);
            }
        }
        writer.endArray();
        writer.endObject();

    }

    public static void writeCaminoConSoluciones(File archivo, CaminoHamiltoniano caminoHamiltoniano,ArrayList<CaminoHamiltoniano>solucines){
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(archivo));
            writer.beginObject();
            caminoToJSON(caminoHamiltoniano,writer);
            writer.name("soluciones");
            writer.beginArray();
            for(CaminoHamiltoniano c: solucines){
                solucionToJSON(c,writer);
            }
            writer.endArray();
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

    public static void iniciarArchivo(File archivo){
        try {
            writerGlobal = new JsonWriter(new FileWriter(archivo));
            writerGlobal.beginObject();
            writerGlobal.name("caminos");
            writerGlobal.beginArray();

        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }

    public static void agregarArchivo(CaminoHamiltoniano caminoHamiltoniano,ArrayList<CaminoHamiltoniano>solucines) {
        try {
            writerGlobal.beginObject();
            caminoToJSON(caminoHamiltoniano,writerGlobal);
            writerGlobal.name("soluciones");
            writerGlobal.beginArray();
            for(CaminoHamiltoniano c: solucines){
                solucionToJSON(c,writerGlobal);
            }
            writerGlobal.endArray();
            writerGlobal.endObject();
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }

    }
    public static void cerrarArchivo(String dificultad, int cantCaminos, int cantSoluciones){
        try {
            writerGlobal.endArray();
            writerGlobal.name("dificultad").value(dificultad);
            writerGlobal.name("total_caminos").value(cantCaminos);
            writerGlobal.name("total_solucines").value(cantSoluciones);
            writerGlobal.endObject();
            writerGlobal.close();
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.alertarError();
        }
    }
}
