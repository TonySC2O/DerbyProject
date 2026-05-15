package dao;

/**
 *
 * @author Nuevo usuario
 */

import database.ConexionDB;
import model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    // Insertar nuevo libro
    public void insertar(Libro libro) {
        String sql = """
            INSERT INTO Libros (titulo, autor, stock, categoria_id)
            VALUES (?, ?, ?, ?)
        """;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getStock());
            ps.setInt(4, libro.getCategoriaId());

            ps.executeUpdate();
            System.out.println("Libro insertado: " + libro.getTitulo());

        } catch (Exception e) {
            System.out.println("Error al insertar libro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Actualizar libro
    public void actualizar(Libro libro) {
        String sql = """
            UPDATE Libros
            SET titulo = ?, autor = ?, stock = ?, categoria_id = ?
            WHERE libro_id = ?
        """;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getStock());
            ps.setInt(4, libro.getCategoriaId());
            ps.setInt(5, libro.getLibroId());

            ps.executeUpdate();
            System.out.println("Libro actualizado: " + libro.getTitulo());

        } catch (Exception e) {
            System.out.println("Error al actualizar libro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Eliminar libro
    public void eliminar(int libroId) {
        String sql = "DELETE FROM Libros WHERE libro_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, libroId);
            ps.executeUpdate();
            System.out.println("Libro eliminado con ID: " + libroId);

        } catch (Exception e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Obtener libro por ID
    public Libro obtenerPorId(int libroId) {
        String sql = "SELECT * FROM Libros WHERE libro_id = ?";
        Libro libro = null;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, libroId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                libro = new Libro(
                        rs.getInt("libro_id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("stock"),
                        rs.getInt("categoria_id")
                );
            }

        } catch (Exception e) {
            System.out.println("Error al obtener libro: " + e.getMessage());
            e.printStackTrace();
        }

        return libro;
    }

    // Obtener todos los libros
    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM Libros ORDER BY titulo";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("libro_id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("stock"),
                        rs.getInt("categoria_id")
                );
                libros.add(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener libros: " + e.getMessage());
            e.printStackTrace();
        }

        return libros;
    }

    // Buscar libro por título
    public List<Libro> buscarPorTitulo(String titulo) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM Libros WHERE UPPER(titulo) LIKE UPPER(?) ORDER BY titulo";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + titulo + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("libro_id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("stock"),
                        rs.getInt("categoria_id")
                );
                libros.add(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
            e.printStackTrace();
        }

        return libros;
    }

    // Buscar libro por autor
    public List<Libro> buscarPorAutor(String autor) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM Libros WHERE UPPER(autor) LIKE UPPER(?) ORDER BY titulo";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + autor + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("libro_id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("stock"),
                        rs.getInt("categoria_id")
                );
                libros.add(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
            e.printStackTrace();
        }

        return libros;
    }

    // Buscar libros por categoría
    public List<Libro> buscarPorCategoria(int categoriaId) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM Libros WHERE categoria_id = ? ORDER BY titulo";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, categoriaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("libro_id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("stock"),
                        rs.getInt("categoria_id")
                );
                libros.add(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al buscar libros por categoría: " + e.getMessage());
            e.printStackTrace();
        }

        return libros;
    }

    // Obtener libros disponibles (stock > 0)
    public List<Libro> obtenerDisponibles() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM Libros WHERE stock > 0 ORDER BY titulo";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("libro_id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("stock"),
                        rs.getInt("categoria_id")
                );
                libros.add(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener libros disponibles: " + e.getMessage());
            e.printStackTrace();
        }

        return libros;
    }

    // Actualizar stock (restar cantidad)
    public void reducirStock(int libroId, int cantidad) {
        String sql = "UPDATE Libros SET stock = stock - ? WHERE libro_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, cantidad);
            ps.setInt(2, libroId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al reducir stock: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Actualizar stock (aumentar cantidad)
    public void aumentarStock(int libroId, int cantidad) {
        String sql = "UPDATE Libros SET stock = stock + ? WHERE libro_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, cantidad);
            ps.setInt(2, libroId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al aumentar stock: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
