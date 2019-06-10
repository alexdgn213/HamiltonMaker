package com.hamiltonmaker.Persistencia;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Comun.Entidades.Tablero;
import com.hamiltonmaker.Comun.Utils.AlertManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Descripción: Clase encagada de almacenar y buscar caminos en la base de datos
 * Autor: Alexander Garcia
 */
public class DAOCamino {

    public static void saveCamino(CaminoHamiltoniano caminoHamiltoniano){
        try {
            String query = "Insert into Camino(ca_inicio,ca_fin,ca_size,ca_recorrido) values (?,?,?,?)";
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,caminoHamiltoniano.getInicio());
            preparedStatement.setInt(2,caminoHamiltoniano.getFin());
            preparedStatement.setInt(3,(int) Math.sqrt(caminoHamiltoniano.getNodos().size()));
            Nodo nodo = caminoHamiltoniano.getNodos().get(caminoHamiltoniano.getInicio());
            String recorrido = String.valueOf(caminoHamiltoniano.getNodos().indexOf(nodo));
            nodo = nodo.getSiguiente();
            while(nodo!=null){
                recorrido += "-"+String.valueOf(caminoHamiltoniano.getNodos().indexOf(nodo));
                nodo = nodo.getSiguiente();
            }
            preparedStatement.setString(4,recorrido);
            preparedStatement.execute();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static ArrayList<CaminoHamiltoniano> obtenerCaminos(int size,int inicio, int fin){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        String query = "Select * from camino where ca_size=? and ca_inicio=? and ca_fin=?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,size);
            preparedStatement.setInt(2,inicio);
            preparedStatement.setInt(3,fin);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                caminos.add(mapperCamino(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertManager.alertarErrorBD();
        }
        return caminos;
    }

    public static ArrayList<CaminoHamiltoniano> obtenerCaminosConSolucion(int size,int inicio, int fin){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        String query = "select distinct ca_id,ca_inicio,ca_size,ca_fin,ca_recorrido from camino,solucion where ca_id=so_camino and ca_size=? and ca_inicio = ? and ca_fin = ?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,size);
            preparedStatement.setInt(2,inicio);
            preparedStatement.setInt(3,fin);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                caminos.add(mapperCamino(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertManager.alertarErrorBD();
        }
        return caminos;
    }

    public static ArrayList<CaminoHamiltoniano> obtenerPorDificultad(int minSize, int maxSize, int dificultad, int cantMax){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        int visibleMinimos = dificultad*10;
        int visiblesMaximos = (dificultad+1)*10;
        if(dificultad == 3) visiblesMaximos = 50;
        String query = "Select * from (select distinct ca_id,ca_inicio,ca_size,ca_fin,ca_recorrido " +
                "from camino,solucion where ca_id=so_camino and (ca_size*ca_size)-so_cant_visibles >= ? " +
                "and (ca_size*ca_size)-so_cant_visibles < ? and ca_size>= ? and ca_size<= ?) " +
                "as sub order by RANDOM() limit ?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,visibleMinimos);
            preparedStatement.setInt(2,visiblesMaximos);
            preparedStatement.setInt(3,minSize);
            preparedStatement.setInt(4,maxSize);
            preparedStatement.setInt(5,cantMax);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                caminos.add(mapperCamino(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertManager.alertarErrorBD();

        }
        return caminos;
    }

    private static CaminoHamiltoniano mapperCamino(ResultSet resultSet) throws SQLException {
        CaminoHamiltoniano caminoHamiltoniano = new Tablero(resultSet.getInt("ca_size")).getCaminoVacio();
        caminoHamiltoniano.setId(resultSet.getInt("ca_id"));
        caminoHamiltoniano.setInicio(resultSet.getInt("ca_inicio"));
        caminoHamiltoniano.setFin(resultSet.getInt("ca_fin"));
        String[] recorrido = resultSet.getString("ca_recorrido").split("-");
        for(int i=0; i<recorrido.length-1; i++){
            int nodoActual = Integer.parseInt(recorrido[i]);
            int nodoSiguiente = Integer.parseInt(recorrido[i+1]);
            caminoHamiltoniano.getNodos().get(nodoActual).setSiguiente(caminoHamiltoniano.getNodos().get(nodoSiguiente));
        }
        return  caminoHamiltoniano;
    }
}
