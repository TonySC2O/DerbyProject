/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package dao;

/**
 *
 * @author Nuevo usuario
 */

import database.ConexionDB;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Insertar nuevo usuario
    public int insertar(Usuario usuario) {
        String sql = """
            INSERT INTO Usuarios (nombre, correo, telefono)
            VALUES (?, ?, ?)
        """;
        
        int usuarioId = 0;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getTelefono());

            ps.executeUpdate();
            
            // Obtener el ID generado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuarioId = rs.getInt(1);
                usuario.setUsuarioId(usuarioId);
            }
            
            System.out.println("[OK] Usuario insertado con ID: " + usuarioId);

        } catch (Exception e) {
            System.out.println("[ERROR] Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarioId;
    }

    // Actualizar usuario existente
    public void actualizar(Usuario usuario) {
        String sql = """
            UPDATE Usuarios
            SET nombre = ?, correo = ?, telefono = ?
            WHERE usuario_id = ?
        """;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getTelefono());
            ps.setInt(4, usuario.getUsuarioId());

            ps.executeUpdate();
            System.out.println("[OK] Usuario actualizado: " + usuario.getNombre());

        } catch (Exception e) {
            System.out.println("[ERROR] Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Eliminar usuario
    public void eliminar(int usuarioId) {
        String sql = "DELETE FROM Usuarios WHERE usuario_id = ?";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
            System.out.println("[OK] Usuario eliminado con ID: " + usuarioId);

        } catch (Exception e) {
            System.out.println("[ERROR] Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Obtener usuario por ID
    public Usuario obtenerPorId(int usuarioId) {
        String sql = "SELECT * FROM Usuarios WHERE usuario_id = ?";
        Usuario usuario = null;

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );
                usuario.setUsuarioId(rs.getInt("usuario_id"));
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return usuario;
    }

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );
                usuario.setUsuarioId(rs.getInt("usuario_id"));
                usuarios.add(usuario);
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al obtener usuarios: " + e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }

    // Buscar usuario por nombre
    public List<Usuario> buscarPorNombre(String nombre) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE UPPER(nombre) LIKE UPPER(?)";

        try (
                Connection conn = ConexionDB.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );
                usuario.setUsuarioId(rs.getInt("usuario_id"));
                usuarios.add(usuario);
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error al buscar usuarios: " + e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }
}
