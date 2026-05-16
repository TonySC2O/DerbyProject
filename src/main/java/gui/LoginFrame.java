package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import static javax.swing.SwingConstants.CENTER;

/**
 *
 * @author Nuevo usuario
 */
public class LoginFrame extends JFrame {

    private JTextField usuarioField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;

    public LoginFrame() {
        setTitle("DerbyLibrary - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 270);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Librería TEC");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Espacio
        gbc.gridy = 1;
        panel.add(Box.createVerticalStrut(10), gbc);

        // Label Usuario
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(usuarioLabel, gbc);

        // TextField Usuario
        usuarioField = new JTextField(20);
        usuarioField.setText("admin");
        gbc.gridx = 1;
        panel.add(usuarioField, gbc);

        // Label Password
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(passwordLabel, gbc);

        // PasswordField
        passwordField = new JPasswordField(20);
        passwordField.setText("admin");
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(150, 35));
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this::loginAction);
        loginButton.setHorizontalAlignment(CENTER);

        cancelButton = new JButton("Cancelar");
        cancelButton.setPreferredSize(new Dimension(150, 35));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(buttonPanel, gbc);

        setContentPane(panel);
    }

    private void loginAction(ActionEvent e) {
        String usuario = usuarioField.getText();
        String password = new String(passwordField.getPassword());

        // Validación simple (puedes conectar a BD si lo deseas)
        if (usuario.equals("admin") && password.equals("admin")) {
            // Abrir menú principal
            MenuPrincipalFrame menuFrame = new MenuPrincipalFrame();
            menuFrame.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Error de Login",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
