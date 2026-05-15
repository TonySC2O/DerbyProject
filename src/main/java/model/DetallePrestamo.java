package model;

/**
 *
 * @author Nuevo usuario
 */
public class DetallePrestamo {

    private int detalleId;
    private int prestamoId;
    private int libroId;
    private int cantidad;

    public DetallePrestamo() {
    }

    public DetallePrestamo(int detalleId, int prestamoId, int libroId, int cantidad) {
        this.detalleId = detalleId;
        this.prestamoId = prestamoId;
        this.libroId = libroId;
        this.cantidad = cantidad;
    }

    public DetallePrestamo(int prestamoId, int libroId, int cantidad) {
        this.prestamoId = prestamoId;
        this.libroId = libroId;
        this.cantidad = cantidad;
    }

    public int getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(int detalleId) {
        this.detalleId = detalleId;
    }

    public int getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
    }

    public int getLibroId() {
        return libroId;
    }

    public void setLibroId(int libroId) {
        this.libroId = libroId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DetallePrestamo{" +
                "detalleId=" + detalleId +
                ", prestamoId=" + prestamoId +
                ", libroId=" + libroId +
                ", cantidad=" + cantidad +
                '}';
    }
}
