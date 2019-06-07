package com.hamiltonmaker.Persistencia;

import com.hamiltonmaker.Comun.Entidades.CaminoHamiltoniano;
import com.hamiltonmaker.Comun.Entidades.Nodo;
import com.hamiltonmaker.Comun.Entidades.Tablero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            e.printStackTrace();
        }
    }

    public static ArrayList<CaminoHamiltoniano> obtenerSoluciones(int size, int inicio, int fin){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        String query = "Select * from camino,soluecion where ca_id=so_camino and ca_size=? and ca_inicio=? and ca_fin=?";
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
        }
        return caminos;
    }

    public static ArrayList<CaminoHamiltoniano> obtenerSoluciones(CaminoHamiltoniano caminoHamiltoniano){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        String query = "Select * from camino,soluecion where ca_id=so_camino and so_camino=?";
        try {
            PreparedStatement preparedStatement = ConexionSQL.getPreparedStatement(query);
            preparedStatement.setInt(1,caminoHamiltoniano.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                caminos.add(mapperCamino(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caminos;
    }

    public static ArrayList<CaminoHamiltoniano> obtenerSoluciones(){
        ArrayList<CaminoHamiltoniano> caminos = new ArrayList<CaminoHamiltoniano>();
        String query = "Select * from camino,soluecion where ca_id=sol_camino";
        try {
            ResultSet resultSet = ConexionSQL.executeQuery(query);
            while (resultSet.next()) {
                caminos.add(mapperCamino(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        String[] visibles = resultSet.getString("so_visibles").split("-");
        for(int i=0; i<visibles.length; i++){
            int visibilidad = Integer.parseInt(recorrido[i]);
            if(visibilidad==0)
                caminoHamiltoniano.alterarNodo(i);
        }
        return  caminoHamiltoniano;
    }
}
