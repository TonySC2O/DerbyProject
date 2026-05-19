/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package model;

/**
 *
 * @author Nuevo usuario
 */
public class Usuario {

    private int usuarioId;
    private String nombre;
    private String correo;
    private String telefono;

    public Usuario() {
    }

    public Usuario(
            String nombre, String correo, String telefono) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return usuarioId + " - " + nombre;
    }
}
