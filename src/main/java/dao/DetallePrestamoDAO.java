package dao;

/**
 *
 * @author Nuevo usuario
 */

import database.ConexionDB;
import model.DetallePrestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DetallePrestamoDAO {

    // Insertar detalle de préstamo
    public int insertar(DetallePrestamo detalle) {
        String sql = """
            INSERT INTO DetallePrestamo (prestamo_id, libro_id, cantidad)
            VALUES (?, ?, ?)
        """;
        int detalleId = 0;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, detalle.getPrestamoId());
            ps.setInt(2, detalle.getLibroId());
            ps.setInt(3, detalle.getCantidad());

            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                detalleId = rs.getInt(1);
                detalle.setDetalleId(detalleId);
            }
            
            System.out.println("[OK] Detalle de préstamo insertado con ID: " + detalleId);

        } catch (Exception e) {
            System.out.println("[ERROR] Error al insertar detalle: " + e.getMessage());
            e.printStackTrace();
        }
        
        return detalleId;
    }

    // Obtener detalles por ID de préstamo
    public List<DetallePrestamo> obtenerPorPrestamo(int prestamoId) {
        List<DetallePrestamo> detalles = new ArrayList<>();
        String sql = "SELECT * FROM DetallePrestamo WHERE prestamo_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, prestamoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetallePrestamo detalle = new DetallePrestamo(
                        rs.getInt("detalle_id"),
                        rs.getInt("prestamo_id"),
                        rs.getInt("libro_id"),
                        rs.getInt("cantidad")
                );
                detalles.add(detalle);
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener detalles: " + e.getMessage());
            e.printStackTrace();
        }

        return detalles;
    }

    // Obtener todos los detalles
    public List<DetallePrestamo> obtenerTodos() {
        List<DetallePrestamo> detalles = new ArrayList<>();
        String sql = "SELECT * FROM DetallePrestamo";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetallePrestamo detalle = new DetallePrestamo(
                        rs.getInt("detalle_id"),
                        rs.getInt("prestamo_id"),
                        rs.getInt("libro_id"),
                        rs.getInt("cantidad")
                );
                detalles.add(detalle);
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener detalles: " + e.getMessage());
            e.printStackTrace();
        }

        return detalles;
    }

    // Eliminar detalle por ID
    public void eliminar(int detalleId) {
        String sql = "DELETE FROM DetallePrestamo WHERE detalle_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, detalleId);
            ps.executeUpdate();
            System.out.println("[OK] Detalle eliminado");

        } catch (Exception e) {
            System.out.println("[ERROR] Error al eliminar detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
