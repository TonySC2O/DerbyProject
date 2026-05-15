package dao;

/**
 *
 * @author Nuevo usuario
 */

import database.ConexionDB;
import model.Prestamo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    // Registrar nuevo préstamo
    public void registrarPrestamo(Prestamo prestamo) {
        String sql = """
            INSERT INTO Prestamos (usuario_id, fecha_prestamo, estado)
            VALUES (?, ?, ?)
        """;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, prestamo.getUsuarioId());
            ps.setDate(2, Date.valueOf(prestamo.getFechaPrestamo()));
            ps.setString(3, prestamo.getEstado());

            ps.executeUpdate();
            System.out.println("[OK] Préstamo registrado para usuario: " + prestamo.getUsuarioId());

        } catch (Exception e) {
            System.out.println("[ERROR] Error al registrar préstamo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Registrar devolución (actualizar fecha y estado)
    public void registrarDevolucion(int prestamoId, LocalDate fechaDevolucion) {
        String sql = """
            UPDATE Prestamos
            SET fecha_devolucion = ?, estado = 'DEVUELTO'
            WHERE prestamo_id = ?
        """;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, Date.valueOf(fechaDevolucion));
            ps.setInt(2, prestamoId);

            ps.executeUpdate();
            System.out.println("[OK] Devolución registrada para préstamo: " + prestamoId);

        } catch (Exception e) {
            System.out.println("[ERROR] Error al registrar devolución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Obtener préstamo por ID
    public Prestamo obtenerPorId(int prestamoId) {
        String sql = "SELECT * FROM Prestamos WHERE prestamo_id = ?";
        Prestamo prestamo = null;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, prestamoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                prestamo = new Prestamo(
                        rs.getInt("prestamo_id"),
                        rs.getInt("usuario_id"),
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null,
                        rs.getString("estado")
                );
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener préstamo: " + e.getMessage());
            e.printStackTrace();
        }

        return prestamo;
    }

    // Obtener préstamos activos (no devueltos)
    public List<Prestamo> obtenerActivos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos WHERE estado = 'ACTIVO' ORDER BY fecha_prestamo DESC";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("prestamo_id"),
                        rs.getInt("usuario_id"),
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null,
                        rs.getString("estado")
                );
                prestamos.add(prestamo);
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener préstamos activos: " + e.getMessage());
            e.printStackTrace();
        }

        return prestamos;
    }

    // Obtener historial de préstamos por usuario
    public List<Prestamo> obtenerHistorialUsuario(int usuarioId) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos WHERE usuario_id = ? ORDER BY fecha_prestamo DESC";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("prestamo_id"),
                        rs.getInt("usuario_id"),
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null,
                        rs.getString("estado")
                );
                prestamos.add(prestamo);
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener historial: " + e.getMessage());
            e.printStackTrace();
        }

        return prestamos;
    }

    // Obtener todos los préstamos
    public List<Prestamo> obtenerTodos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos ORDER BY fecha_prestamo DESC";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("prestamo_id"),
                        rs.getInt("usuario_id"),
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null,
                        rs.getString("estado")
                );
                prestamos.add(prestamo);
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener préstamos: " + e.getMessage());
            e.printStackTrace();
        }

        return prestamos;
    }

    // Obtener último préstamo insertado (para recuperar el ID generado)
    public int obtenerUltimoPrestamoId() {
        String sql = "SELECT MAX(prestamo_id) as max_id FROM Prestamos";
        int ultimoId = 0;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ultimoId = rs.getInt("max_id");
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener último préstamo: " + e.getMessage());
            e.printStackTrace();
        }

        return ultimoId;
    }
}
