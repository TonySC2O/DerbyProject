package gui;

import dao.CategoriaDAO;
import model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Nuevo usuario
 */
public class CategoriesFrame extends JFrame {

    private JTextField nombreField;
    private JTable categoriasTable;
    private DefaultTableModel tableModel;
    private CategoriaDAO categoriaDAO;
    private Categoria categoriaSeleccionada;

    public CategoriesFrame() {
        setTitle("DerbyLibrary - Gestión de Categorías");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        categoriaDAO = new CategoriaDAO();
        categoriaSeleccionada = null;

        initComponents();
        cargarCategorias();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel superior
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Gestión de Categorías");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        inputPanel.add(nombreField);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(e -> guardarCategoria());
        buttonPanel.add(guardarButton);

        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(e -> actualizarCategoria());
        buttonPanel.add(actualizarButton);

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(e -> eliminarCategoria());
        buttonPanel.add(eliminarButton);

        JButton limpiarButton = new JButton("Limpiar");
        limpiarButton.addActionListener(e -> limpiar());
        buttonPanel.add(limpiarButton);

        // Panel tabla
        String[] columnNames = {"ID", "Nombre"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        categoriasTable = new JTable(tableModel);
        categoriasTable.setFont(new Font("Arial", Font.PLAIN, 12));
        categoriasTable.setRowHeight(25);
        categoriasTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarFila();
            }
        });

        JScrollPane scrollPane = new JScrollPane(categoriasTable);
        
        
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
        tableModel.setRowCount(0);
        List<Categoria> categorias = categoriaDAO.obtenerTodas();

        for (Categoria categoria : categorias) {
            Object[] row = {
                    categoria.getCategoriaId(),
                    categoria.getNombre()
            };
            tableModel.addRow(row);
        }
    }

    private void guardarCategoria() {
        String nombre = nombreField.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Categoria categoria = new Categoria(nombre);
        categoriaDAO.insertar(categoria);
        limpiar();
        cargarCategorias();
        JOptionPane.showMessageDialog(this, "Categoría guardada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarCategoria() {
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = nombreField.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        categoriaSeleccionada.setNombre(nombre);
        categoriaDAO.actualizar(categoriaSeleccionada);
        limpiar();
        cargarCategorias();
        JOptionPane.showMessageDialog(this, "Categoría actualizada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarCategoria() {
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar esta categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            categoriaDAO.eliminar(categoriaSeleccionada.getCategoriaId());
            limpiar();
            cargarCategorias();
        }
    }

    private void seleccionarFila() {
        int row = categoriasTable.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            String nombre = (String) tableModel.getValueAt(row, 1);
            categoriaSeleccionada = new Categoria(id, nombre);
            nombreField.setText(nombre);
        }
    }

    private void limpiar() {
        nombreField.setText("");
        categoriaSeleccionada = null;
        categoriasTable.clearSelection();
    }
}
