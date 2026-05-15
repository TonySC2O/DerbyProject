/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package project.derbyproject;

import database.CrearTablas;

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
    }
}
