package model;

/**
 *
 * @author Nuevo usuario
 */
public class Libro {

    private int libroId;
    private String titulo;
    private String autor;
    private int stock;
    private int categoriaId;

    public Libro() {
    }

    public Libro(int libroId, String titulo, String autor, int stock, int categoriaId) {
        this.libroId = libroId;
        this.titulo = titulo;
        this.autor = autor;
        this.stock = stock;
        this.categoriaId = categoriaId;
    }

    public Libro(String titulo, String autor, int stock, int categoriaId) {
        this.titulo = titulo;
        this.autor = autor;
        this.stock = stock;
        this.categoriaId = categoriaId;
    }

    public int getLibroId() {
        return libroId;
    }

    public void setLibroId(int libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
