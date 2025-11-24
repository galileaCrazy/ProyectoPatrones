package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import factorymethod.*;
import dao.UsuarioDAO;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private ButtonGroup userTypeGroup;
    private JRadioButton estudianteRadio;
    private JRadioButton profesorRadio;
    private JRadioButton administradorRadio;
    private JButton loginButton;
    private JLabel registerLink;

    // Colores modernos
    private final Color PRIMARY_COLOR = new Color(59, 130, 246); // Azul moderno
    private final Color PRIMARY_HOVER = new Color(37, 99, 235);
    private final Color BACKGROUND_COLOR = new Color(249, 250, 251);
    private final Color CARD_BACKGROUND = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private final Color BORDER_COLOR = new Color(229, 231, 235);

    public LoginView() {
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        setTitle("EduLearn - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void setupLayout() {
        // Panel principal con layout null para posicionamiento manual
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
        int cardHeight = 590;
        int cardX = (500 - cardWidth) / 2;
        int cardY = (700 - cardHeight) / 2;
        cardPanel.setBounds(cardX, cardY, cardWidth, cardHeight);

        // Título
        JLabel titleLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBounds(40, 30, 320, 40);
        cardPanel.add(titleLabel);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Sistema de Gestión de Aprendizaje", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setBounds(40, 75, 320, 20);
        cardPanel.add(subtitleLabel);

        // Correo Electrónico
        JLabel emailLabel = new JLabel("Correo Electrónico");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setForeground(TEXT_PRIMARY);
        emailLabel.setBounds(40, 120, 320, 20);
        cardPanel.add(emailLabel);

        emailField = new JTextField("tu@universidad.edu");
        emailField.setBounds(40, 145, 320, 42);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setForeground(TEXT_SECONDARY);
        emailField.setBackground(CARD_BACKGROUND);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        cardPanel.add(emailField);

        // Placeholder behavior mejorado
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("tu@universidad.edu")) {
                    emailField.setText("");
                    emailField.setForeground(TEXT_PRIMARY);
                }
                emailField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("tu@universidad.edu");
                    emailField.setForeground(TEXT_SECONDARY);
                }
                emailField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        // Contraseña
        JLabel passwordLabel = new JLabel("Contraseña");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(TEXT_PRIMARY);
        passwordLabel.setBounds(40, 202, 320, 20);
        cardPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 227, 320, 42);
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
        userTypeLabel.setBounds(40, 284, 320, 20);
        cardPanel.add(userTypeLabel);

        userTypeGroup = new ButtonGroup();

        estudianteRadio = createStyledRadioButton("Estudiante", 40, 309);
        profesorRadio = createStyledRadioButton("Profesor", 40, 354);
        administradorRadio = createStyledRadioButton("Administrador", 40, 399);

        userTypeGroup.add(estudianteRadio);
        userTypeGroup.add(profesorRadio);
        userTypeGroup.add(administradorRadio);

        cardPanel.add(estudianteRadio);
        cardPanel.add(profesorRadio);
        cardPanel.add(administradorRadio);

        estudianteRadio.setSelected(true);

        // Botón Iniciar Sesión con diseño moderno
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBounds(40, 459, 320, 48);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        cardPanel.add(loginButton);

        // Link de registro
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBounds(40, 517, 320, 30);
        registerPanel.setBackground(CARD_BACKGROUND);

        JLabel noAccountLabel = new JLabel("¿Aún no tienes una cuenta?");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccountLabel.setForeground(TEXT_SECONDARY);

        registerLink = new JLabel("Regístrate aquí");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerLink.setForeground(PRIMARY_COLOR);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerPanel.add(noAccountLabel);
        registerPanel.add(registerLink);
        cardPanel.add(registerPanel);

        mainPanel.add(cardPanel);
        add(mainPanel);
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
        loginButton.addActionListener(e -> handleLogin());

        // Efecto hover mejorado
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(PRIMARY_COLOR);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                loginButton.setBackground(new Color(29, 78, 216));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                loginButton.setBackground(PRIMARY_HOVER);
            }
        });

        // Efecto hover para el link de registro
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRegisterView();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                registerLink.setText("<html><u>Regístrate aquí</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                registerLink.setText("Regístrate aquí");
            }
        });
    }

    private void handleLogin() {
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

        if (email.isEmpty() || email.equals("tu@universidad.edu") || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos",
                "Error de Validacion",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Usar el DAO para validar credenciales
            UsuarioDAO dao = new UsuarioDAO();
            IUsuario usuario = dao.validarCredenciales(email, password, userType);

            if (usuario != null) {
                // Login exitoso - mostrar informacion del usuario usando Factory Method
                UsuarioFactory factory = FactoryMethodDemo.obtenerFactory(userType);

                JOptionPane.showMessageDialog(this,
                    "Bienvenido!\n\n" +
                    usuario.mostrarInformacion(),
                    "Inicio de Sesion Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

                // Mostrar permisos del usuario
                System.out.println("\n" + usuario.obtenerPermisos());

                // Aqui abririas la siguiente ventana segun el tipo de usuario
                // Por ejemplo:
                // if (usuario instanceof Estudiante) {
                //     new EstudianteDashboard((Estudiante) usuario).setVisible(true);
                // } else if (usuario instanceof Profesor) {
                //     new ProfesorDashboard((Profesor) usuario).setVisible(true);
                // } else if (usuario instanceof Administrador) {
                //     new AdminDashboard((Administrador) usuario).setVisible(true);
                // }
                // dispose();

            } else {
                JOptionPane.showMessageDialog(this,
                    "Credenciales invalidas.\nVerifique su correo, contrasena y tipo de usuario.",
                    "Error de Autenticacion",
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al iniciar sesion: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openRegisterView() {
        new RegistroVista().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Usar look and feel del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginView().setVisible(true);
        });
    }
}