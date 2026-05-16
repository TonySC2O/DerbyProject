package gui;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 *
 * @author Nuevo usuario
 */
public class UsuariosFrame extends JFrame {

    private JTextField nombreField;
    private JTextField correoField;
    private JTextField telefonoField;
    private JTextField buscarField;
    private JTable usuariosTable;
    private DefaultTableModel tableModel;
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioSeleccionado;

    public UsuariosFrame() {
        setTitle("DerbyLibrary - Gestión de Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        usuarioDAO = new UsuarioDAO();
        usuarioSeleccionado = null;

        initComponents();
        cargarUsuarios();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Panel superior - Título
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Gestión de Usuarios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel de entrada de datos
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        inputPanel.add(nombreField);

        inputPanel.add(new JLabel("Correo:"));
        correoField = new JTextField();
        inputPanel.add(correoField);

        inputPanel.add(new JLabel("Teléfono:"));
        telefonoField = new JTextField();
        inputPanel.add(telefonoField);

        inputPanel.add(new JLabel("Buscar por nombre:"));
        buscarField = new JTextField();
        inputPanel.add(buscarField);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(e -> guardarUsuario());
        buttonPanel.add(guardarButton);

        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(e -> actualizarUsuario());
        buttonPanel.add(actualizarButton);

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(e -> eliminarUsuario());
        buttonPanel.add(eliminarButton);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(e -> buscarUsuarios());
        buttonPanel.add(buscarButton);

        JButton limpiarButton = new JButton("Limpiar");
        limpiarButton.addActionListener(e -> limpiar());
        buttonPanel.add(limpiarButton);

        JButton recargarButton = new JButton("Recargar");
        recargarButton.addActionListener(e -> cargarUsuarios());
        buttonPanel.add(recargarButton);

        // Panel norte (con título, entrada y botones)
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Panel de tabla
        String[] columnNames = {"ID", "Nombre", "Correo", "Teléfono"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usuariosTable = new JTable(tableModel);
        usuariosTable.setFont(new Font("Arial", Font.PLAIN, 11));
        usuariosTable.setRowHeight(25);
        usuariosTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        
        
        // Botón de volver
        JPanel lowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lowPanel.setBackground(new Color(245, 245, 245));
        
        JButton volverButton = createButton("Volver", new Color(70, 130, 180));
        volverButton.addActionListener(e -> volver());
        volverButton.setPreferredSize(new Dimension(100, 30));
        
        lowPanel.add(volverButton);

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
        tableModel.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();

        for (Usuario usuario : usuarios) {
            Object[] row = {
                    usuario.getUsuarioId(),
                    usuario.getNombre(),
                    usuario.getCorreo(),
                    usuario.getTelefono()
            };
            tableModel.addRow(row);
        }
    }

    private void guardarUsuario() {
        String nombre = nombreField.getText().trim();
        String correo = correoField.getText().trim();
        String telefono = telefonoField.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y correo son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = new Usuario(nombre, correo, telefono);
        usuarioDAO.insertar(usuario);
        limpiar();
        cargarUsuarios();
        JOptionPane.showMessageDialog(this, "Usuario guardado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = nombreField.getText().trim();
        String correo = correoField.getText().trim();
        String telefono = telefonoField.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y correo son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        usuarioSeleccionado.setNombre(nombre);
        usuarioSeleccionado.setCorreo(correo);
        usuarioSeleccionado.setTelefono(telefono);

        usuarioDAO.actualizar(usuarioSeleccionado);
        limpiar();
        cargarUsuarios();
        JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar al usuario " + usuarioSeleccionado.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            usuarioDAO.eliminar(usuarioSeleccionado.getUsuarioId());
            limpiar();
            cargarUsuarios();
            JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarUsuarios() {
        String nombre = buscarField.getText().trim();

        if (nombre.isEmpty()) {
            cargarUsuarios();
            return;
        }

        tableModel.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.buscarPorNombre(nombre);

        for (Usuario usuario : usuarios) {
            Object[] row = {
                    usuario.getUsuarioId(),
                    usuario.getNombre(),
                    usuario.getCorreo(),
                    usuario.getTelefono()
            };
            tableModel.addRow(row);
        }
    }

    private void seleccionarFila() {
        int row = usuariosTable.getSelectedRow();

        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            String nombre = (String) tableModel.getValueAt(row, 1);
            String correo = (String) tableModel.getValueAt(row, 2);
            String telefono = (String) tableModel.getValueAt(row, 3);

            usuarioSeleccionado = new Usuario(nombre, correo, telefono);
            usuarioSeleccionado.setUsuarioId(id);

            nombreField.setText(nombre);
            correoField.setText(correo);
            telefonoField.setText(telefono);
        }
    }

    private void limpiar() {
        nombreField.setText("");
        correoField.setText("");
        telefonoField.setText("");
        buscarField.setText("");
        usuarioSeleccionado = null;
        usuariosTable.clearSelection();
    }
}
