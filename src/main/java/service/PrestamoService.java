package service;

import database.ConexionDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class PrestamoService {

    /**
     * Realiza un préstamo en una única transacción: inserta en Prestamos,
     * detalle en DetallePrestamo y actualiza stock en Libros.
     * Devuelve true si commitó correctamente; false si hubo rollback.
     */
    public boolean realizarPrestamoTransaccional(int usuarioId, int libroId, int cantidad) {
        String sqlInsertPrestamo = """
            INSERT INTO Prestamos (usuario_id, fecha_prestamo, estado)
            VALUES (?, ?, ?)
        """;

        String sqlInsertDetalle = """
            INSERT INTO DetallePrestamo (prestamo_id, libro_id, cantidad)
            VALUES (?, ?, ?)
        """;

        // Actualiza stock sólo si hay suficiente (condicional en WHERE evita stock negativo)
        String sqlUpdateStock = """
            UPDATE Libros SET stock = stock - ? WHERE libro_id = ? AND stock >= ?
        """;

        Connection conn = null;
        try {
            conn = ConexionDB.conectar();
            if (conn == null) return false;

            conn.setAutoCommit(false);

            int prestamoId;
            try (PreparedStatement psPrest = conn.prepareStatement(sqlInsertPrestamo, Statement.RETURN_GENERATED_KEYS)) {
                psPrest.setInt(1, usuarioId);
                psPrest.setDate(2, Date.valueOf(LocalDate.now()));
                psPrest.setString(3, "ACTIVO");
                psPrest.executeUpdate();

                try (ResultSet rs = psPrest.getGeneratedKeys()) {
                    if (rs.next()) {
                        prestamoId = rs.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Insertar detalle
            try (PreparedStatement psDet = conn.prepareStatement(sqlInsertDetalle, Statement.RETURN_GENERATED_KEYS)) {
                psDet.setInt(1, prestamoId);
                psDet.setInt(2, libroId);
                psDet.setInt(3, cantidad);
                psDet.executeUpdate();
            }

            // Reducir stock (verifica condición)
            try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdateStock)) {
                psUpd.setInt(1, cantidad);
                psUpd.setInt(2, libroId);
                psUpd.setInt(3, cantidad);
                int affected = psUpd.executeUpdate();
                if (affected == 0) {
                    // No se pudo actualizar (stock insuficiente)
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
