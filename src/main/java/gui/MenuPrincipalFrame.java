package gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Nuevo usuario
 */
public class MenuPrincipalFrame extends JFrame {

    public MenuPrincipalFrame() {
        setTitle("DerbyLibrary - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Panel superior con título
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Librería TEC - Menú Principal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel central con botones
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Botón Gestión de Usuarios
        JButton usuariosButton = createButton("Gestión de Usuarios", new Color(70, 130, 180));
        usuariosButton.addActionListener(e -> abrirUsuarios());

        // Botón Gestión de Categorías
        JButton categoriasButton = createButton("Gestión de Categorías", new Color(70, 130, 180));
        categoriasButton.addActionListener(e -> abrirCategorias());

        // Botón Gestión de Libros
        JButton librosButton = createButton("Gestión de Libros", new Color(70, 130, 180));
        librosButton.addActionListener(e -> abrirLibros());

        // Botón Gestión de Préstamos
        JButton prestamosButton = createButton("Gestión de Préstamos", new Color(70, 130, 180));
        prestamosButton.addActionListener(e -> abrirPrestamos());

        // Botón Reportes
        JButton reportesButton = createButton("Reportes", new Color(70, 130, 180));
        reportesButton.addActionListener(e -> abrirReportes());

        centerPanel.add(usuariosButton);
        centerPanel.add(categoriasButton);
        centerPanel.add(librosButton);
        centerPanel.add(prestamosButton);
        centerPanel.add(reportesButton);
        
        // Panel bajo con opciones
        JPanel lowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lowPanel.setBackground(new Color(245, 245, 245));
        lowPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 30));
        
        // Botón de volver
        JButton volverButton = createButton("Volver", new Color(70, 130, 180));
        volverButton.addActionListener(e -> volver());
        volverButton.setPreferredSize(new Dimension(100, 30));

        // Botón Salir
        JButton salirButton = createButton("Salir", new Color(70, 130, 180));
        salirButton.addActionListener(e -> System.exit(0));
        salirButton.setPreferredSize(new Dimension(100, 30));
        
        lowPanel.add(volverButton);
        lowPanel.add(salirButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(lowPanel, BorderLayout.SOUTH);

        setContentPane(panel);
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

    private void abrirUsuarios() {
        UsuariosFrame frame = new UsuariosFrame();
        frame.setVisible(true);
        this.dispose();
    }

    private void abrirCategorias() {
        CategoriesFrame frame = new CategoriesFrame();
        frame.setVisible(true);
        this.dispose();
    }

    private void abrirLibros() {
        LibrosFrame frame = new LibrosFrame();
        frame.setVisible(true);
        this.dispose();
    }

    private void abrirPrestamos() {
        PrestamosFrame frame = new PrestamosFrame();
        frame.setVisible(true);
        this.dispose();
    }

    private void abrirReportes() {
        ReportesFrame frame = new ReportesFrame();
        frame.setVisible(true);
        this.dispose();
    }
    
    private void volver() {
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalFrame frame = new MenuPrincipalFrame();
            frame.setVisible(true);
        });
    }
}
