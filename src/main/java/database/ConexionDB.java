/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author Nuevo usuario
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {

    // Derby Embedded Database
    private static final String url = "jdbc:derby:bibliotecaDB;create=true";

    public static Connection conectar() {
        
        try {
            // Driver para Derby Embedded
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(url);

            System.out.println("Conexion a Derby Embedded exitosa");
            return conn;

        } catch (Exception e) {
            System.out.println("Error de conexion: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}