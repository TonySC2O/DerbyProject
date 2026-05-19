
package database;

/**
 *
 * @author Nuevo usuario
 */
import java.sql.Connection;
import java.sql.Statement;

public class CrearTablas {

    public static void crear() {
        
        try (
            Connection conn = ConexionDB.conectar();
            Statement stmt = conn.createStatement()
        ) {
            
            if (conn == null) {
                System.out.println("No se pudo conectar a la base de datos");
                return;
            }

            // Tabla Usuarios
            try {
                String usuarios = """
                    CREATE TABLE Usuarios (
                        usuario_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                        nombre VARCHAR(100) NOT NULL,
                        correo VARCHAR(100) NOT NULL,
                        telefono VARCHAR(20)
                    )
                """;
                stmt.executeUpdate(usuarios);
                System.out.println("Tabla Usuarios creada");
            } catch (Exception e) {
                System.out.println("Tabla Usuarios: " + e.getMessage());
            }

            // Tabla Categorias
            try {
                String categorias = """
                    CREATE TABLE Categorias (
                        categoria_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                        nombre VARCHAR(50) NOT NULL UNIQUE
                    )
                """;
                stmt.executeUpdate(categorias);
                System.out.println("Tabla Categorias creada");
            } catch (Exception e) {
                System.out.println("Tabla Categorias: " + e.getMessage());
            }

            // Tabla Libros
            try {
                String libros = """
                    CREATE TABLE Libros (
                        libro_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                        titulo VARCHAR(150) NOT NULL,
                        autor VARCHAR(100) NOT NULL,
                        stock INT NOT NULL CHECK (stock >= 0),
                        categoria_id INT NOT NULL,
                        FOREIGN KEY (categoria_id) REFERENCES Categorias(categoria_id)
                    )
                """;
                stmt.executeUpdate(libros);
                System.out.println("Tabla Libros creada");
            } catch (Exception e) {
                System.out.println("Tabla Libros: " + e.getMessage());
            }

            // Tabla Prestamos
            try {
                String prestamos = """
                    CREATE TABLE Prestamos (
                        prestamo_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                        usuario_id INT NOT NULL,
                        fecha_prestamo DATE NOT NULL,
                        fecha_devolucion DATE,
                        estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
                        FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id)
                    )
                """;
                stmt.executeUpdate(prestamos);
                System.out.println("Tabla Prestamos creada");
            } catch (Exception e) {
                System.out.println("Tabla Prestamos: " + e.getMessage());
            }

            // Tabla DetallePrestamo
            try {
                String detallePrestamo = """
                    CREATE TABLE DetallePrestamo (
                        detalle_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                        prestamo_id INT NOT NULL,
                        libro_id INT NOT NULL,
                        cantidad INT NOT NULL CHECK (cantidad > 0),
                        FOREIGN KEY (prestamo_id) REFERENCES Prestamos(prestamo_id),
                        FOREIGN KEY (libro_id) REFERENCES Libros(libro_id)
                    )
                """;
                stmt.executeUpdate(detallePrestamo);
                System.out.println("Tabla DetallePrestamo creada");
            } catch (Exception e) {
                System.out.println("Tabla DetallePrestamo: " + e.getMessage());
            }

            System.out.println("\nInicialización de tablas completada");

        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
