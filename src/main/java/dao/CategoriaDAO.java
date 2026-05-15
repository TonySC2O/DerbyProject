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
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // Insertar nueva categoría
    public void insertar(Categoria categoria) {
        String sql = "INSERT INTO Categorias (nombre) VALUES (?)";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, categoria.getNombre());
            ps.executeUpdate();
            System.out.println("Categoría insertada: " + categoria.getNombre());

        } catch (Exception e) {
            System.out.println("Error al insertar categoría: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Actualizar categoría
    public void actualizar(Categoria categoria) {
        String sql = "UPDATE Categorias SET nombre = ? WHERE categoria_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getCategoriaId());
            ps.executeUpdate();
            System.out.println("Categoría actualizada: " + categoria.getNombre());

        } catch (Exception e) {
            System.out.println("Error al actualizar categoría: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Eliminar categoría
    public void eliminar(int categoriaId) {
        String sql = "DELETE FROM Categorias WHERE categoria_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, categoriaId);
            ps.executeUpdate();
            System.out.println("Categoría eliminada con ID: " + categoriaId);

        } catch (Exception e) {
            System.out.println("Error al eliminar categoría: " + e.getMessage());
            e.printStackTrace();
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
        String sql = "SELECT * FROM Categorias ORDER BY nombre";

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
