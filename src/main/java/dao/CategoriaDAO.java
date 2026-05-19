package dao;

/**
 *
 * @author Nuevo usuario
 */

import database.ConexionDB;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // Insertar nueva categoría
    public String insertar(Categoria categoria) {
        String sql = "INSERT INTO Categorias (nombre) VALUES (?)";
        int categoriaId = 0;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, categoria.getNombre());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                categoriaId = rs.getInt(1);
                categoria.setCategoriaId(categoriaId);
            }
            
            System.out.println("[OK] Categoría insertada con ID: " + categoriaId);
            return null; // Éxito

        } catch (SQLException e) {
            // Detectar violación de UNIQUE constraint (SQLState 23505)
            if ("23505".equals(e.getSQLState())) {
                String errorMsg = "Ya existe una categoría con ese nombre.";
                System.out.println("[UNIQUE_ERROR] " + errorMsg);
                return errorMsg;
            }
            
            System.out.println("[ERROR] Error al insertar categoría: " + e.getMessage());
            e.printStackTrace();
            return "Error al insertar categoría: " + e.getMessage();
        }
    }

    // Actualizar categoría
    public String actualizar(Categoria categoria) {
        String sql = "UPDATE Categorias SET nombre = ? WHERE categoria_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getCategoriaId());
            ps.executeUpdate();
            System.out.println("Categoría actualizada: " + categoria.getNombre());
            return null; // Éxito

        } catch (SQLException e) {
            // Detectar violación de UNIQUE constraint
            if ("23505".equals(e.getSQLState())) {
                String errorMsg = "Ya existe una categoría con ese nombre.";
                System.out.println("[UNIQUE_ERROR] " + errorMsg);
                return errorMsg;
            }
            
            System.out.println("Error al actualizar categoría: " + e.getMessage());
            e.printStackTrace();
            return "Error al actualizar categoría: " + e.getMessage();
        }
    }

    // Eliminar categoría
    public String eliminar(int categoriaId) {
        String sql = "DELETE FROM Categorias WHERE categoria_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, categoriaId);
            ps.executeUpdate();
            System.out.println("Categoría eliminada con ID: " + categoriaId);
            return null;

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                return "No se puede eliminar la categoría porque hay libros asociados.";
            }
            System.out.println("Error al eliminar categoría: " + e.getMessage());
            e.printStackTrace();
            return "Error al eliminar categoría: " + e.getMessage();
        } catch (Exception e) {
            System.out.println("Error al eliminar categoría: " + e.getMessage());
            e.printStackTrace();
            return "Error al eliminar categoría: " + e.getMessage();
        }
    }

    // Obtener categoría por ID
    public Categoria obtenerPorId(int categoriaId) {
        String sql = "SELECT * FROM Categorias WHERE categoria_id = ?";
        Categoria categoria = null;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, categoriaId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                categoria = new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("nombre")
                );
            }

        } catch (Exception e) {
            System.out.println("Error al obtener categoría: " + e.getMessage());
            e.printStackTrace();
        }

        return categoria;
    }

    // Obtener todas las categorías
    public List<Categoria> obtenerTodas() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categorias ORDER BY categoria_id ASC";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("nombre")
                );
                categorias.add(categoria);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener categorías: " + e.getMessage());
            e.printStackTrace();
        }

        return categorias;
    }
}
