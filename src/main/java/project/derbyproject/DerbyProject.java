/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package project.derbyproject;

import database.ConexionDB;
import database.CrearTablas;
import gui.LoginFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Nuevo usuario
 */
public class DerbyProject {

    public static void main(String[] args) {
        // Crear tablas en la base de datos
        System.out.println("Inicializando base de datos...");
        CrearTablas.crear();
        System.out.println("Base de datos lista.\n");

        // Agregar hook de shutdown para cerrar Derby correctamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nCerrando aplicación...");
            ConexionDB.shutdown();
            System.out.println("Aplicación cerrada.");
        }));

        // Lanzar interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
