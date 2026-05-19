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

    private JTextField idField;
    private JTextField nombreField;
    private JTextField buscarField;
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

        // Panel de búsqueda (separado)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(new Color(230, 230, 230));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Búsqueda"));
        
        searchPanel.add(new JLabel("Buscar por nombre:"));
        buscarField = new JTextField(15);
        searchPanel.add(buscarField);
        
        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(e -> buscarCategorias());
        searchPanel.add(buscarButton);
        
        JButton recargarButton = new JButton("Recargar");
        recargarButton.addActionListener(e -> cargarCategorias());
        searchPanel.add(recargarButton);

        // Panel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Información de la Categoría"));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(200, 200, 200));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        inputPanel.add(nombreField);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
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

        // Panel de entrada completo
        JPanel entryCompletePanel = new JPanel(new BorderLayout());
        entryCompletePanel.setBackground(new Color(245, 245, 245));
        entryCompletePanel.add(inputPanel, BorderLayout.CENTER);
        entryCompletePanel.add(buttonPanel, BorderLayout.SOUTH);

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
        northPanel.add(searchPanel, BorderLayout.CENTER);
        northPanel.add(entryCompletePanel, BorderLayout.SOUTH);

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
        buscarField.setText("");
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

    private void buscarCategorias() {
        String nombre = buscarField.getText().trim();

        if (nombre.isEmpty()) {
            cargarCategorias();
            return;
        }

        tableModel.setRowCount(0);
        List<Categoria> categorias = categoriaDAO.obtenerTodas();
        int resultados = 0;

        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equalsIgnoreCase(nombre)) {
                Object[] row = {
                        categoria.getCategoriaId(),
                        categoria.getNombre()
                };
                tableModel.addRow(row);
                resultados++;
            }
        }
        
        if (resultados == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron categorías con ese nombre", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Se encontraron " + resultados + " resultado(s)", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void guardarCategoria() {
        String nombre = nombreField.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Categoria categoria = new Categoria(nombre);
        String error = categoriaDAO.insertar(categoria);
        
        if (error == null) {
            limpiar();
            cargarCategorias();
            JOptionPane.showMessageDialog(this, "Categoría guardada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, error, "Error al guardar categoría", JOptionPane.ERROR_MESSAGE);
        }
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
        String error = categoriaDAO.actualizar(categoriaSeleccionada);
        
        if (error == null) {
            limpiar();
            cargarCategorias();
            JOptionPane.showMessageDialog(this, "Categoría actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, error, "Error al actualizar categoría", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCategoria() {
        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar esta categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String error = categoriaDAO.eliminar(categoriaSeleccionada.getCategoriaId());
            if (error == null) {
                limpiar();
                cargarCategorias();
                JOptionPane.showMessageDialog(this, "Categoría eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, error, "Error al eliminar categoría", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void seleccionarFila() {
        int row = categoriasTable.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            String nombre = (String) tableModel.getValueAt(row, 1);
            categoriaSeleccionada = new Categoria(id, nombre);
            idField.setText(String.valueOf(id));
            nombreField.setText(nombre);
        }
    }

    private void limpiar() {
        idField.setText("");
        nombreField.setText("");
        categoriaSeleccionada = null;
        categoriasTable.clearSelection();
    }
}
