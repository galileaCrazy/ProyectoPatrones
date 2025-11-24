package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import factorymethod.*;
import dao.UsuarioDAO;

public class RegistroVista extends JFrame {
    private JTextField nombreField;
    private JTextField apellidosField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private ButtonGroup userTypeGroup;
    private JRadioButton estudianteRadio;
    private JRadioButton profesorRadio;
    private JRadioButton administradorRadio;
    private JButton registerButton;
    private JLabel loginLink;

    // Colores modernos
    private final Color PRIMARY_COLOR = new Color(59, 130, 246); // Azul moderno
    private final Color PRIMARY_HOVER = new Color(37, 99, 235);
    private final Color BACKGROUND_COLOR = new Color(249, 250, 251);
    private final Color CARD_BACKGROUND = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private final Color BORDER_COLOR = new Color(229, 231, 235);

    public RegistroVista() {
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        setTitle("Registrate - Sistema de Gestión de Aprendizaje");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void setupLayout() {
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Card central con sombra
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(CARD_BACKGROUND);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Centrar el card
        int cardWidth = 400;
        int cardHeight = 720;
        int cardX = (500 - cardWidth) / 2;
        int cardY = (800 - cardHeight) / 2;
        cardPanel.setBounds(cardX, cardY, cardWidth, cardHeight);

        // Título
        JLabel titleLabel = new JLabel("Registrate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBounds(40, 20, 320, 40);
        cardPanel.add(titleLabel);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Sistema de Gestión de Aprendizaje", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setBounds(40, 65, 320, 20);
        cardPanel.add(subtitleLabel);

        // Nombre
        JLabel nombreLabel = new JLabel("Nombre");
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nombreLabel.setForeground(TEXT_PRIMARY);
        nombreLabel.setBounds(40, 105, 320, 20);
        cardPanel.add(nombreLabel);

        nombreField = new JTextField("Carolina Vásquez");
        nombreField.setBounds(40, 130, 320, 42);
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nombreField.setForeground(TEXT_SECONDARY);
        nombreField.setBackground(CARD_BACKGROUND);
        nombreField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        cardPanel.add(nombreField);

        // Placeholder behavior para nombre
        setupFieldPlaceholder(nombreField, "Carolina Vásquez");

        // Apellidos
        JLabel apellidosLabel = new JLabel("Apellidos");
        apellidosLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        apellidosLabel.setForeground(TEXT_PRIMARY);
        apellidosLabel.setBounds(40, 182, 320, 20);
        cardPanel.add(apellidosLabel);

        apellidosField = new JTextField("Pérez García");
        apellidosField.setBounds(40, 207, 320, 42);
        apellidosField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        apellidosField.setForeground(TEXT_SECONDARY);
        apellidosField.setBackground(CARD_BACKGROUND);
        apellidosField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        cardPanel.add(apellidosField);

        // Placeholder behavior para apellidos
        setupFieldPlaceholder(apellidosField, "Pérez García");

        // Correo Electrónico
        JLabel emailLabel = new JLabel("Correo Electrónico");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setForeground(TEXT_PRIMARY);
        emailLabel.setBounds(40, 259, 320, 20);
        cardPanel.add(emailLabel);

        emailField = new JTextField("miNombre@itoaxca.edu.mx");
        emailField.setBounds(40, 284, 320, 42);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setForeground(TEXT_SECONDARY);
        emailField.setBackground(CARD_BACKGROUND);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        cardPanel.add(emailField);

        // Placeholder behavior para email
        setupFieldPlaceholder(emailField, "miNombre@itoaxca.edu.mx");

        // Contraseña
        JLabel passwordLabel = new JLabel("Contraseña");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(TEXT_PRIMARY);
        passwordLabel.setBounds(40, 336, 320, 20);
        cardPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 361, 320, 42);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBackground(CARD_BACKGROUND);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        cardPanel.add(passwordField);

        // Focus listener para contraseña
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        // Tipo de Usuario
        JLabel userTypeLabel = new JLabel("Tipo de Usuario");
        userTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userTypeLabel.setForeground(TEXT_PRIMARY);
        userTypeLabel.setBounds(40, 413, 320, 20);
        cardPanel.add(userTypeLabel);

        userTypeGroup = new ButtonGroup();

        estudianteRadio = createStyledRadioButton("Estudiante", 40, 438);
        profesorRadio = createStyledRadioButton("Profesor", 40, 483);
        administradorRadio = createStyledRadioButton("Administrador", 40, 528);

        userTypeGroup.add(estudianteRadio);
        userTypeGroup.add(profesorRadio);
        userTypeGroup.add(administradorRadio);

        cardPanel.add(estudianteRadio);
        cardPanel.add(profesorRadio);
        cardPanel.add(administradorRadio);

        estudianteRadio.setSelected(true);

        // Botón Registrarse
        registerButton = new JButton("Crear Cuenta");
        registerButton.setBounds(40, 583, 320, 48);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        registerButton.setBackground(PRIMARY_COLOR);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        cardPanel.add(registerButton);

        // Link de login
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBounds(40, 641, 320, 30);
        loginPanel.setBackground(CARD_BACKGROUND);

        JLabel hasAccountLabel = new JLabel("¿Ya tienes una cuenta?");
        hasAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hasAccountLabel.setForeground(TEXT_SECONDARY);

        loginLink = new JLabel("Iniciar sesión");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLink.setForeground(PRIMARY_COLOR);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginPanel.add(hasAccountLabel);
        loginPanel.add(loginLink);
        cardPanel.add(loginPanel);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private void setupFieldPlaceholder(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_SECONDARY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    private JRadioButton createStyledRadioButton(String text, int x, int y) {
        JRadioButton radio = new JRadioButton(text);
        radio.setBounds(x, y, 320, 40);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radio.setBackground(CARD_BACKGROUND);
        radio.setForeground(TEXT_PRIMARY);
        radio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        radio.setOpaque(true);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        radio.setFocusPainted(false);
        
        // Efecto hover y selección
        radio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!radio.isSelected()) {
                    radio.setBackground(new Color(243, 244, 246));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!radio.isSelected()) {
                    radio.setBackground(CARD_BACKGROUND);
                }
            }
        });

        radio.addItemListener(e -> {
            if (radio.isSelected()) {
                radio.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                radio.setBackground(new Color(239, 246, 255));
            } else {
                radio.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
                radio.setBackground(CARD_BACKGROUND);
            }
        });
        
        return radio;
    }

    private void setupListeners() {
        registerButton.addActionListener(e -> handleRegister());

        // Efecto hover mejorado para el botón
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(PRIMARY_COLOR);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                registerButton.setBackground(new Color(29, 78, 216));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                registerButton.setBackground(PRIMARY_HOVER);
            }
        });

        // Efecto hover para el link de login
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openLoginView();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Iniciar sesión</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Iniciar sesión");
            }
        });
    }

    private void handleRegister() {
        String nombre = nombreField.getText();
        String apellidos = apellidosField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String userType = "";

        if (estudianteRadio.isSelected()) {
            userType = "Estudiante";
        } else if (profesorRadio.isSelected()) {
            userType = "Profesor";
        } else if (administradorRadio.isSelected()) {
            userType = "Administrador";
        }

        // Validaciones
        if (nombre.isEmpty() || nombre.equals("Carolina Vasquez") ||
            apellidos.isEmpty() || apellidos.equals("Perez Garcia") ||
            email.isEmpty() || email.equals("miNombre@itoaxca.edu.mx") ||
            password.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos correctamente",
                "Error de Validacion",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Verificar si el correo ya existe
            UsuarioDAO dao = new UsuarioDAO();
            if (dao.existeCorreo(email)) {
                JOptionPane.showMessageDialog(this,
                    "El correo electronico ya esta registrado.\nPor favor, use otro correo.",
                    "Correo Duplicado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Usar el Factory Method para crear el usuario
            UsuarioFactory factory = FactoryMethodDemo.obtenerFactory(userType);
            IUsuario nuevoUsuario = factory.registrarUsuario(nombre, apellidos, email, password);

            // Guardar el usuario en la base de datos
            int id = dao.insertar(nuevoUsuario);

            if (id > 0) {
                JOptionPane.showMessageDialog(this,
                    "Cuenta creada exitosamente!\n\n" +
                    "Nombre: " + nombre + " " + apellidos + "\n" +
                    "Email: " + email + "\n" +
                    "Tipo: " + nuevoUsuario.getTipoUsuario() + "\n" +
                    "ID: " + id,
                    "Registro Completado",
                    JOptionPane.INFORMATION_MESSAGE);

                // Redireccionar al login
                openLoginView();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al registrar el usuario.\nPor favor, intente nuevamente.",
                    "Error de Registro",
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error de Validacion",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al registrar usuario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openLoginView() {
        new LoginView().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new RegistroVista().setVisible(true);
        });
    }
}