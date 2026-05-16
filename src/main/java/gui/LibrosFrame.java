package gui;

import dao.CategoriaDAO;
import dao.LibroDAO;
import model.Libro;
import model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Nuevo usuario
 */
public class LibrosFrame extends JFrame {

    private JTextField tituloField;
    private JTextField autorField;
    private JTextField stockField;
    private JComboBox<Categoria> categoriaCombo;
    private JTextField buscarField;
    private JTable librosTable;
    private DefaultTableModel tableModel;
    private LibroDAO libroDAO;
    private CategoriaDAO categoriaDAO;
    private Libro libroSeleccionado;

    public LibrosFrame() {
        setTitle("DerbyLibrary - Gestión de Libros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        libroDAO = new LibroDAO();
        categoriaDAO = new CategoriaDAO();
        libroSeleccionado = null;

        initComponents();
        cargarCategorias();
        cargarLibros();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Gestión de Libros");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(new JLabel("Título:"));
        tituloField = new JTextField();
        inputPanel.add(tituloField);

        inputPanel.add(new JLabel("Autor:"));
        autorField = new JTextField();
        inputPanel.add(autorField);

        inputPanel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        inputPanel.add(stockField);

        inputPanel.add(new JLabel("Categoría:"));
        categoriaCombo = new JComboBox<>();
        inputPanel.add(categoriaCombo);

        inputPanel.add(new JLabel("Buscar por título:"));
        buscarField = new JTextField();
        inputPanel.add(buscarField);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(e -> guardarLibro());
        buttonPanel.add(guardarButton);

        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(e -> actualizarLibro());
        buttonPanel.add(actualizarButton);

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(e -> eliminarLibro());
        buttonPanel.add(eliminarButton);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(e -> buscarLibros());
        buttonPanel.add(buscarButton);

        JButton limpiarButton = new JButton("Limpiar");
        limpiarButton.addActionListener(e -> limpiar());
        buttonPanel.add(limpiarButton);

        JButton recargarButton = new JButton("Recargar");
        recargarButton.addActionListener(e -> cargarLibros());
        buttonPanel.add(recargarButton);

        // Panel tabla
        String[] columnNames = {"ID", "Título", "Autor", "Stock", "Categoría"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        librosTable = new JTable(tableModel);
        librosTable.setFont(new Font("Arial", Font.PLAIN, 11));
        librosTable.setRowHeight(25);
        librosTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarFila();
            }
        });

        JScrollPane scrollPane = new JScrollPane(librosTable);
        
        
        // Botón de volver
        JPanel lowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lowPanel.setBackground(new Color(245, 245, 245));
        
        JButton volverButton = createButton("Volver", new Color(70, 130, 180));
        volverButton.addActionListener(e -> volver());
        volverButton.setPreferredSize(new Dimension(100, 30));

        lowPanel.add(volverButton);

        // Panel norte
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(lowPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void volver() {
        MenuPrincipalFrame frame = new MenuPrincipalFrame();
        frame.setVisible(true);
        this.dispose();
    }

    private void cargarCategorias() {
        List<Categoria> categorias = categoriaDAO.obtenerTodas();
        categoriaCombo.removeAllItems();
        for (Categoria cat : categorias) {
            categoriaCombo.addItem(cat);
        }
    }

    private void cargarLibros() {
        tableModel.setRowCount(0);
        List<Libro> libros = libroDAO.obtenerTodos();

        for (Libro libro : libros) {
            Categoria cat = categoriaDAO.obtenerPorId(libro.getCategoriaId());
            String nomCategoria = cat != null ? cat.getNombre() : "N/A";

            Object[] row = {
                    libro.getLibroId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getStock(),
                    nomCategoria
            };
            tableModel.addRow(row);
        }
    }

    private void guardarLibro() {
        String titulo = tituloField.getText().trim();
        String autor = autorField.getText().trim();
        String stockStr = stockField.getText().trim();
        Categoria categoria = (Categoria) categoriaCombo.getSelectedItem();

        if (titulo.isEmpty() || autor.isEmpty() || stockStr.isEmpty() || categoria == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int stock = Integer.parseInt(stockStr);
            Libro libro = new Libro(titulo, autor, stock, categoria.getCategoriaId());
            libroDAO.insertar(libro);
            limpiar();
            cargarLibros();
            JOptionPane.showMessageDialog(this, "Libro guardado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarLibro() {
        if (libroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = tituloField.getText().trim();
        String autor = autorField.getText().trim();
        String stockStr = stockField.getText().trim();
        Categoria categoria = (Categoria) categoriaCombo.getSelectedItem();

        if (titulo.isEmpty() || autor.isEmpty() || stockStr.isEmpty() || categoria == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int stock = Integer.parseInt(stockStr);
            libroSeleccionado.setTitulo(titulo);
            libroSeleccionado.setAutor(autor);
            libroSeleccionado.setStock(stock);
            libroSeleccionado.setCategoriaId(categoria.getCategoriaId());

            libroDAO.actualizar(libroSeleccionado);
            limpiar();
            cargarLibros();
            JOptionPane.showMessageDialog(this, "Libro actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarLibro() {
        if (libroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este libro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            libroDAO.eliminar(libroSeleccionado.getLibroId());
            limpiar();
            cargarLibros();
        }
    }

    private void buscarLibros() {
        String titulo = buscarField.getText().trim();

        if (titulo.isEmpty()) {
            cargarLibros();
            return;
        }

        tableModel.setRowCount(0);
        List<Libro> libros = libroDAO.buscarPorTitulo(titulo);

        for (Libro libro : libros) {
            Categoria cat = categoriaDAO.obtenerPorId(libro.getCategoriaId());
            String nomCategoria = cat != null ? cat.getNombre() : "N/A";

            Object[] row = {
                    libro.getLibroId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getStock(),
                    nomCategoria
            };
            tableModel.addRow(row);
        }
    }

    private void seleccionarFila() {
        int row = librosTable.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            libroSeleccionado = libroDAO.obtenerPorId(id);

            if (libroSeleccionado != null) {
                tituloField.setText(libroSeleccionado.getTitulo());
                autorField.setText(libroSeleccionado.getAutor());
                stockField.setText(String.valueOf(libroSeleccionado.getStock()));

                // Seleccionar categoría en combo
                Categoria cat = categoriaDAO.obtenerPorId(libroSeleccionado.getCategoriaId());
                if (cat != null) {
                    categoriaCombo.setSelectedItem(cat);
                }
            }
        }
    }

    private void limpiar() {
        tituloField.setText("");
        autorField.setText("");
        stockField.setText("");
        buscarField.setText("");
        libroSeleccionado = null;
        librosTable.clearSelection();
    }
}
