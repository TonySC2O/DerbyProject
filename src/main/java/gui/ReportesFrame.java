package gui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Nuevo usuario
 */
public class ReportesFrame extends JFrame {

    private JTable reporteTable;
    private DefaultTableModel tableModel;
    private PrestamoDAO prestamoDAO;
    private LibroDAO libroDAO;
    private UsuarioDAO usuarioDAO;

    public ReportesFrame() {
        setTitle("DerbyLibrary - Reportes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        prestamoDAO = new PrestamoDAO();
        libroDAO = new LibroDAO();
        usuarioDAO = new UsuarioDAO();

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Reportes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton prestamosActivosButton = new JButton("Préstamos Activos");
        prestamosActivosButton.addActionListener(e -> mostrarPrestamosActivos());
        buttonPanel.add(prestamosActivosButton);

        JButton librosDisponiblesButton = new JButton("Libros Disponibles");
        librosDisponiblesButton.addActionListener(e -> mostrarLibrosDisponibles());
        buttonPanel.add(librosDisponiblesButton);

        JButton estadisticasButton = new JButton("Estadísticas");
        estadisticasButton.addActionListener(e -> mostrarEstadisticas());
        buttonPanel.add(estadisticasButton);

        // Panel tabla
        String[] columnNames = {"Información"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reporteTable = new JTable(tableModel);
        reporteTable.setFont(new Font("Arial", Font.PLAIN, 12));
        reporteTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(reporteTable);
        
        // Botón de volver
        JPanel lowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lowPanel.setBackground(new Color(245, 245, 245));
        
        JButton volverButton = createButton("Volver", new Color(70, 130, 180));
        volverButton.addActionListener(e -> volver());
        volverButton.setPreferredSize(new Dimension(100, 30));

        lowPanel.add(volverButton);
        
        JPanel allLowPanel = new JPanel(new BorderLayout());
        allLowPanel.setBackground(new Color(245, 245, 245));
        
        allLowPanel.add(scrollPane, BorderLayout.CENTER);
        allLowPanel.add(lowPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(allLowPanel, BorderLayout.SOUTH);

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

    private void mostrarPrestamosActivos() {
        tableModel.setColumnCount(4);
        tableModel.setColumnIdentifiers(new String[]{"ID", "Usuario", "Fecha Préstamo", "Estado"});
        tableModel.setRowCount(0);

        List<Prestamo> prestamos = prestamoDAO.obtenerActivos();

        for (Prestamo prestamo : prestamos) {
            Usuario usuario = usuarioDAO.obtenerPorId(prestamo.getUsuarioId());
            String nomUsuario = usuario != null ? usuario.getNombre() : "N/A";

            Object[] row = {
                    prestamo.getPrestamoId(),
                    nomUsuario,
                    prestamo.getFechaPrestamo(),
                    prestamo.getEstado()
            };
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "Mostrando " + prestamos.size() + " préstamos activos", "Reporte", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarLibrosDisponibles() {
        tableModel.setColumnCount(4);
        tableModel.setColumnIdentifiers(new String[]{"ID", "Título", "Autor", "Stock"});
        tableModel.setRowCount(0);

        List<Libro> libros = libroDAO.obtenerDisponibles();

        for (Libro libro : libros) {
            Object[] row = {
                    libro.getLibroId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getStock()
            };
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "Mostrando " + libros.size() + " libros disponibles", "Reporte", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarEstadisticas() {
        tableModel.setColumnCount(2);
        tableModel.setColumnIdentifiers(new String[]{"Métrica", "Valor"});
        tableModel.setRowCount(0);

        // Estadísticas
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();
        List<Libro> libros = libroDAO.obtenerTodos();
        List<Prestamo> prestamosActivos = prestamoDAO.obtenerActivos();
        List<Prestamo> todosPrestamos = prestamoDAO.obtenerTodos();

        Object[][] estadisticas = {
                {"Total de Usuarios", usuarios.size()},
                {"Total de Libros", libros.size()},
                {"Préstamos Activos", prestamosActivos.size()},
                {"Total de Préstamos", todosPrestamos.size()},
                {"Libros Disponibles", libroDAO.obtenerDisponibles().size()}
        };

        for (Object[] row : estadisticas) {
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "Estadísticas cargadas", "Reporte", JOptionPane.INFORMATION_MESSAGE);
    }
}
