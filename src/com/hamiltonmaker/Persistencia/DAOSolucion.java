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
 * Descripción: Clase encagada de almacenar y buscar soluciones en la base de datos
 * Autor: Alexander Garcia
 */
public class DAOSolucion {

    public static void saveSolucion(CaminoHamiltoniano caminoHamiltoniano){
        try {
            String query = "Insert into Solucion(so_camino,so_visibles,so_cant_visibles) values (?,?,?)";
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,caminoHamiltoniano.getId());
            String visibles = "";
            for(Nodo n : caminoHamiltoniano.getNodos()){
                if(n.isVisible()){
                    visibles += "1";
                }
                else{
                    visibles += "0";
                }
            }
            preparedStatement.setString(2,visibles);
            preparedStatement.setInt(3,caminoHamiltoniano.getVisibles());
            preparedStatement.execute();
        } catch (SQLException e) {
           // e.printStackTrace();
        }
    }

    public static ArrayList<CaminoHamiltoniano> obtenerSoluciones(CaminoHamiltoniano caminoHamiltoniano){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        String query = "Select * from camino,solucion where ca_id=so_camino and so_camino=? order by so_cant_visibles desc";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,caminoHamiltoniano.getId());
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

    public static ArrayList<CaminoHamiltoniano> obtenerSoluciones(CaminoHamiltoniano caminoHamiltoniano, int adyacencias){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        String query = "Select * from camino,solucion where ca_id=so_camino and so_camino=? and so_cant_visibles=?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,caminoHamiltoniano.getId());
            preparedStatement.setInt(2,adyacencias);
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

    public static ArrayList<CaminoHamiltoniano> obtenerPorDificultad(CaminoHamiltoniano caminoHamiltoniano, int dificultad, int max){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        int visibleMinimos = dificultad*10;
        int visiblesMaximos = (dificultad+1)*10;
        if(dificultad == 3) visiblesMaximos = 50;
        String query = "Select * from camino,solucion where ca_id=so_camino and so_camino=? and (ca_size*ca_size)-so_cant_visibles >= ? and (ca_size*ca_size)-so_cant_visibles < ? order by RANDOM() limit ?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,caminoHamiltoniano.getId());
            preparedStatement.setInt(2,visibleMinimos);
            preparedStatement.setInt(3,visiblesMaximos);
            preparedStatement.setInt(4,max);
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
        String visibles = resultSet.getString("so_visibles");
        for(int i=0; i<visibles.length(); i++){
            int visibilidad = Integer.parseInt(String.valueOf(visibles.charAt(i)));
            if(visibilidad==0)
                caminoHamiltoniano.alterarNodo(i);
        }
        return  caminoHamiltoniano;
    }

}
