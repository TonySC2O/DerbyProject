package gui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import service.PrestamoService;

/**
 *
 * @author Nuevo usuario
 */
public class PrestamosFrame extends JFrame {

    private JComboBox<Usuario> usuarioCombo;
    private JComboBox<Libro> libroCombo;
    private JSpinner cantidadSpinner;
    private JTable prestamosTable;
    private DefaultTableModel tableModel;
    private UsuarioDAO usuarioDAO;
    private LibroDAO libroDAO;
    private PrestamoDAO prestamoDAO;
    private DetallePrestamoDAO detalleDAO;

    public PrestamosFrame() {
        setTitle("DerbyLibrary - Gestión de Préstamos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        usuarioDAO = new UsuarioDAO();
        libroDAO = new LibroDAO();
        prestamoDAO = new PrestamoDAO();
        detalleDAO = new DetallePrestamoDAO();
        
        // Servicio transaccional
        
        

        initComponents();
        cargarUsuarios();
        cargarLibros();
        cargarPrestamos();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Gestión de Préstamos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel de entrada (mejorado)
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(new JLabel("Usuario:"));
        usuarioCombo = new JComboBox<>();
        inputPanel.add(usuarioCombo);

        inputPanel.add(new JLabel("Libro:"));
        libroCombo = new JComboBox<>();
        inputPanel.add(libroCombo);

        inputPanel.add(new JLabel("Cantidad:"));
        cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        inputPanel.add(cantidadSpinner);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton prestarButton = new JButton("Registrar Préstamo");
        prestarButton.addActionListener(e -> registrarPrestamo());
        buttonPanel.add(prestarButton);

        JButton devolverButton = new JButton("Registrar Devolución");
        devolverButton.addActionListener(e -> registrarDevolucion());
        buttonPanel.add(devolverButton);

        JButton recargarButton = new JButton("Recargar");
        recargarButton.addActionListener(e -> cargarPrestamos());
        buttonPanel.add(recargarButton);

        // Panel tabla
        String[] columnNames = {"ID Préstamo", "Usuario", "Fecha Préstamo", "Fecha Devolución", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        prestamosTable = new JTable(tableModel);
        prestamosTable.setFont(new Font("Arial", Font.PLAIN, 11));
        prestamosTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(prestamosTable);
        
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

    private void cargarUsuarios() {
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();
        usuarioCombo.removeAllItems();
        for (Usuario u : usuarios) {
            usuarioCombo.addItem(u);
        }
    }

    private void cargarLibros() {
        List<Libro> libros = libroDAO.obtenerDisponibles();
        libroCombo.removeAllItems();
        for (Libro l : libros) {
            libroCombo.addItem(l);
        }
    }

    private void cargarPrestamos() {
        tableModel.setRowCount(0);
        List<Prestamo> prestamos = prestamoDAO.obtenerActivos();

        for (Prestamo prestamo : prestamos) {
            Usuario usuario = usuarioDAO.obtenerPorId(prestamo.getUsuarioId());
            String nomUsuario = usuario != null ? usuario.getNombre() : "N/A";

            Object[] row = {
                    prestamo.getPrestamoId(),
                    nomUsuario,
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion() != null ? prestamo.getFechaDevolucion() : "Pendiente",
                    prestamo.getEstado()
            };
            tableModel.addRow(row);
        }
    }

    private void registrarPrestamo() {
        Usuario usuario = (Usuario) usuarioCombo.getSelectedItem();
        Libro libro = (Libro) libroCombo.getSelectedItem();
        int cantidad = (int) cantidadSpinner.getValue();

        if (usuario == null || libro == null) {
            JOptionPane.showMessageDialog(this, "Seleccione usuario y libro", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (libro.getStock() < cantidad) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            PrestamoService service = new PrestamoService();
            boolean ok = service.realizarPrestamoTransaccional(usuario.getUsuarioId(), libro.getLibroId(), cantidad);
            if (ok) {
                cargarLibros();
                cargarPrestamos();
                JOptionPane.showMessageDialog(this, "Préstamo registrado (transaccional)", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo completar el préstamo (se hizo rollback)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarDevolucion() {
        int selectedRow = prestamosTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int prestamoId = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            Prestamo prestamo = prestamoDAO.obtenerPorId(prestamoId);
            List<DetallePrestamo> detalles = detalleDAO.obtenerPorPrestamo(prestamoId);

            // Registrar devolución
            prestamoDAO.registrarDevolucion(prestamoId, LocalDate.now());

            // Aumentar stock de libros
            for (DetallePrestamo detalle : detalles) {
                libroDAO.aumentarStock(detalle.getLibroId(), detalle.getCantidad());
            }

            cargarLibros();
            cargarPrestamos();
            JOptionPane.showMessageDialog(this, "Devolución registrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
