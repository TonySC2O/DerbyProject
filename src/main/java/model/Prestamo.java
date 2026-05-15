package model;

import java.time.LocalDate;

/**
 *
 * @author Nuevo usuario
 */
public class Prestamo {

    private int prestamoId;
    private int usuarioId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private String estado;

    public Prestamo() {
    }

    public Prestamo(int prestamoId, int usuarioId, LocalDate fechaPrestamo, 
                    LocalDate fechaDevolucion, String estado) {
        this.prestamoId = prestamoId;
        this.usuarioId = usuarioId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public Prestamo(int usuarioId, LocalDate fechaPrestamo, String estado) {
        this.usuarioId = usuarioId;
        this.fechaPrestamo = fechaPrestamo;
        this.estado = estado;
    }

    public int getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "prestamoId=" + prestamoId +
                ", usuarioId=" + usuarioId +
                ", estado='" + estado + '\'' +
                '}';
    }
}
