package com.hamiltonmaker.Persistencia;

import com.hamiltonmaker.Comun.Utils.AlertManager;

import java.sql.*;

/**
 * Descripción: Singleton para el manejo de la conexión a la base de datos
 * Autor: Alexander Garcia
 */
public class ConexionSQL {
    private static Connection conexion;

    public static Connection getConexion(){
        if(conexion==null){
            try {
                conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hamiltonpaths", "HamiltonMaker", "hamilton");

            } catch (SQLException e) {
                e.printStackTrace();
                AlertManager.alertarErrorBD();
            }
        }
        return conexion;
    }

    public static void cerrarConexion(){
        if(conexion!=null){
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
                AlertManager.alertarErrorBD();
            }
        }
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConexion().prepareStatement(sql);
    }

    public static ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = getConexion().createStatement();
        return statement.executeQuery(sql);
    }

}
