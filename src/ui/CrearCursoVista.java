package ui;

import abstractfactory.*;
import builder.*;
import prototype.*;
import dao.*;
import model.*;

// Nuevos patrones
import adapter.*;
import bridge.*;
import composite.*;
import decorator.*;
import facade.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana para crear cursos usando tres patrones de diseño:
 * 1. Abstract Factory - Para crear el curso base segun su tipo
 * 2. Builder - Para construir la estructura interna (modulos, clases, evaluaciones)
 * 3. Prototype - Para clonar cursos existentes a nuevos periodos
 * @author USUARIO
 */
public class CrearCursoVista extends JFrame {

    // ==================== ABSTRACT FACTORY ====================
    private JComboBox<String> cbTipoCurso;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtProfesorId;
    private JTextField txtPeriodo;
    private JButton btnCrearCurso;
    private CourseComponentFactory factory;
    private ICurso cursoCreado;
    private int cursoIdCreado;

    // ==================== BUILDER ====================
    private JPanel panelBuilder;
    private JTextField txtModuloNombre;
    private JTextArea txtModuloDesc;
    private JComboBox<String> cbModuloSelector;
    private JTextField txtClaseNombre;
    private JTextArea txtClaseDesc;
    private JComboBox<String> cbTipoClase;
    private JSpinner spnDuracionClase;
    private JTextField txtEvalNombre;
    private JComboBox<String> cbTipoEval;
    private JSpinner spnPesoEval;
    private JTextArea txtResultado;

    private ICursoBuilder cursoBuilder;
    private CursoDirector cursoDirector;
    private List<Modulo> modulosCreados;

    // ==================== PROTOTYPE ====================
    private JPanel panelPrototype;
    private JComboBox<CursoPrototype> cbCursosExistentes;
    private JTextField txtNuevoPeriodo;
    private JTextArea txtPreviewClon;
    private List<CursoPrototype> cursosPrototipos;

    // ==================== DAOs ====================
    private CursoDAO cursoDAO;
    private MaterialDAO materialDAO;
    private EvaluacionDAO evaluacionDAO;

    public CrearCursoVista() {
        initDAOs();
        initBuilder();
        initPrototype();
        initComponents();
    }

    private void initDAOs() {
        cursoDAO = new CursoDAO();
        materialDAO = new MaterialDAO();
        evaluacionDAO = new EvaluacionDAO();
    }

    private void initBuilder() {
        modulosCreados = new ArrayList<>();
        cursoBuilder = new CursoCompletoBuilder();
        cursoDirector = new CursoDirector(cursoBuilder);
    }

    private void initPrototype() {
        cursosPrototipos = new ArrayList<>();
        cargarCursosComoPrototipos();
    }

    private void cargarCursosComoPrototipos() {
        // Cargar cursos existentes de la BD y convertirlos a prototipos
        cursosPrototipos.clear();
        try {
            List<Curso> cursos = cursoDAO.obtenerTodos();
            for (Curso curso : cursos) {
                CursoPrototype proto = new CursoPrototype(
                    curso.getId(),
                    curso.getCodigo(),
                    curso.getNombre(),
                    curso.getDescripcion(),
                    curso.getTipoCurso() != null ? curso.getTipoCurso().getValor() : "virtual",
                    curso.getProfesorTitularId(),
                    curso.getPeriodoAcademico(),
                    curso.getCupoMaximo()
                );
                proto.setEstrategiaEvaluacion(curso.getEstrategiaEvaluacion());

                // Agregar modulos de ejemplo (en produccion se cargarian de BD)
                ModuloPrototype modDemo = new ModuloPrototype("Modulo Principal", "Contenido del curso", 1);
                modDemo.agregarClase(new ClasePrototype("Clase 1", "VIDEO", 45, 1));
                modDemo.agregarEvaluacion(new EvaluacionPrototype("Evaluacion 1", "QUIZ", new BigDecimal("100")));
                proto.agregarModulo(modDemo);

                cursosPrototipos.add(proto);
            }
        } catch (Exception e) {
            // Si hay error, agregar cursos de ejemplo
            agregarCursosDeEjemplo();
        }

        if (cursosPrototipos.isEmpty()) {
            agregarCursosDeEjemplo();
        }
    }

    private void agregarCursosDeEjemplo() {
        // Curso ejemplo 1
        CursoPrototype ejemplo1 = new CursoPrototype(0, "JAVA-2024", "Programacion Java",
            "Curso completo de Java", "virtual", 1, "2024-2", 30);

        ModuloPrototype m1 = new ModuloPrototype("Fundamentos", "Conceptos basicos", 1);
        m1.agregarClase(new ClasePrototype("Introduccion", "VIDEO", 45, 1));
        m1.agregarClase(new ClasePrototype("Variables", "VIDEO", 60, 2));
        m1.agregarEvaluacion(new EvaluacionPrototype("Quiz 1", "QUIZ", new BigDecimal("30")));
        ejemplo1.agregarModulo(m1);

        ModuloPrototype m2 = new ModuloPrototype("POO", "Orientacion a Objetos", 2);
        m2.agregarClase(new ClasePrototype("Clases", "VIDEO", 50, 1));
        m2.agregarClase(new ClasePrototype("Herencia", "VIDEO", 55, 2));
        m2.agregarEvaluacion(new EvaluacionPrototype("Examen", "EXAMEN", new BigDecimal("70")));
        ejemplo1.agregarModulo(m2);

        cursosPrototipos.add(ejemplo1);

        // Curso ejemplo 2
        CursoPrototype ejemplo2 = new CursoPrototype(0, "WEB-2024", "Desarrollo Web",
            "HTML, CSS y JavaScript", "hibrido", 2, "2024-2", 25);

        ModuloPrototype mw1 = new ModuloPrototype("HTML", "Estructura web", 1);
        mw1.agregarClase(new ClasePrototype("Tags basicos", "VIDEO", 40, 1));
        mw1.agregarEvaluacion(new EvaluacionPrototype("Tarea HTML", "TAREA", new BigDecimal("50")));
        ejemplo2.agregarModulo(mw1);

        cursosPrototipos.add(ejemplo2);
    }

    private void initComponents() {
        setTitle("Plataforma LMS - 8 Patrones de Diseño");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel superior - Titulo
        add(createTitlePanel(), BorderLayout.NORTH);

        // Panel central con PESTAÑAS
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));

        // TAB 1: Patrones Creacionales (Abstract Factory, Builder, Prototype)
        JPanel panelCreacionales = new JPanel();
        panelCreacionales.setLayout(new BoxLayout(panelCreacionales, BoxLayout.Y_AXIS));
        panelCreacionales.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCreacionales.add(createAbstractFactoryPanel());
        panelCreacionales.add(Box.createVerticalStrut(15));
        panelBuilder = createBuilderPanel();
        habilitarBuilder(false);
        panelCreacionales.add(panelBuilder);
        panelCreacionales.add(Box.createVerticalStrut(15));
        panelPrototype = createPrototypePanel();
        panelCreacionales.add(panelPrototype);
        tabbedPane.addTab("Creacionales (Factory/Builder/Prototype)", new JScrollPane(panelCreacionales));

        // TAB 2: Adapter
        tabbedPane.addTab("Adapter", new JScrollPane(createAdapterPanel()));

        // TAB 3: Bridge
        tabbedPane.addTab("Bridge", new JScrollPane(createBridgePanel()));

        // TAB 4: Composite
        tabbedPane.addTab("Composite", new JScrollPane(createCompositePanel()));

        // TAB 5: Decorator
        tabbedPane.addTab("Decorator", new JScrollPane(createDecoratorPanel()));

        // TAB 6: Facade
        tabbedPane.addTab("Facade", new JScrollPane(createFacadePanel()));

        add(tabbedPane, BorderLayout.CENTER);

        // Panel inferior - Resultados
        add(createResultPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(1200, 70));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("PLATAFORMA LMS - 8 PATRONES DE DISEÑO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Creacionales | Adapter | Bridge | Composite | Decorator | Facade");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitulo.setForeground(Color.WHITE);

        panel.add(lblTitulo);
        panel.add(lblSubtitulo);
        return panel;
    }

    // ==================== ABSTRACT FACTORY PANEL ====================
    private JPanel createAbstractFactoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            "PASO 1: Crear Curso con Abstract Factory"));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Tipo, Codigo, Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        content.add(new JLabel("Tipo de Curso (Fabrica):"), gbc);
        gbc.gridx = 1;
        cbTipoCurso = new JComboBox<>(new String[]{"PRESENCIAL", "VIRTUAL", "HIBRIDO"});
        cbTipoCurso.setFont(new Font("Arial", Font.BOLD, 12));
        content.add(cbTipoCurso, gbc);

        gbc.gridx = 2;
        content.add(new JLabel("Codigo:"), gbc);
        gbc.gridx = 3;
        txtCodigo = new JTextField(12);
        content.add(txtCodigo, gbc);

        gbc.gridx = 4;
        content.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 5; gbc.weightx = 1;
        txtNombre = new JTextField(15);
        content.add(txtNombre, gbc);

        // Fila 2: Profesor, Periodo
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        content.add(new JLabel("ID Profesor:"), gbc);
        gbc.gridx = 1;
        txtProfesorId = new JTextField("1", 12);
        content.add(txtProfesorId, gbc);

        gbc.gridx = 2;
        content.add(new JLabel("Periodo:"), gbc);
        gbc.gridx = 3;
        txtPeriodo = new JTextField("2025-1", 12);
        content.add(txtPeriodo, gbc);

        // Fila 3: Descripcion
        gbc.gridx = 0; gbc.gridy = 2;
        content.add(new JLabel("Descripcion:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 5; gbc.weightx = 1;
        txtDescripcion = new JTextArea(2, 40);
        txtDescripcion.setLineWrap(true);
        content.add(new JScrollPane(txtDescripcion), gbc);

        // Fila 4: Boton crear
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 6;
        gbc.insets = new Insets(15, 8, 5, 8);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        btnCrearCurso = new JButton("CREAR CURSO CON ABSTRACT FACTORY");
        btnCrearCurso.setBackground(new Color(46, 204, 113));
        btnCrearCurso.setForeground(Color.WHITE);
        btnCrearCurso.setFont(new Font("Arial", Font.BOLD, 14));
        btnCrearCurso.addActionListener(e -> crearCursoConFactory());

        JButton btnInfo = new JButton("Ver Info Fabrica");
        btnInfo.addActionListener(e -> mostrarInfoFabrica());

        btnPanel.add(btnCrearCurso);
        btnPanel.add(btnInfo);
        content.add(btnPanel, gbc);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ==================== BUILDER PANEL ====================
    private JPanel createBuilderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "PASO 2: Construir Estructura del Curso con Builder (Modulos, Clases, Evaluaciones)"));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sub-panel: Agregar Modulo
        content.add(createModuloSubPanel());
        content.add(Box.createVerticalStrut(10));

        // Sub-panel: Agregar Clase
        content.add(createClaseSubPanel());
        content.add(Box.createVerticalStrut(10));

        // Sub-panel: Agregar Evaluacion
        content.add(createEvaluacionSubPanel());
        content.add(Box.createVerticalStrut(10));

        // Sub-panel: Acciones finales
        content.add(createAccionesSubPanel());

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createModuloSubPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 1),
            "2.1 Agregar Modulo"));

        panel.add(new JLabel("Nombre:"));
        txtModuloNombre = new JTextField(15);
        panel.add(txtModuloNombre);

        panel.add(new JLabel("Descripcion:"));
        txtModuloDesc = new JTextArea(1, 20);
        txtModuloDesc.setLineWrap(true);
        panel.add(new JScrollPane(txtModuloDesc));

        JButton btnAgregar = new JButton("+ Agregar Modulo");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.addActionListener(e -> agregarModulo());
        panel.add(btnAgregar);

        return panel;
    }

    private JPanel createClaseSubPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
            "2.2 Agregar Clase al Modulo"));

        panel.add(new JLabel("Modulo:"));
        cbModuloSelector = new JComboBox<>();
        cbModuloSelector.setPreferredSize(new Dimension(150, 25));
        panel.add(cbModuloSelector);

        panel.add(new JLabel("Nombre Clase:"));
        txtClaseNombre = new JTextField(12);
        panel.add(txtClaseNombre);

        panel.add(new JLabel("Tipo:"));
        cbTipoClase = new JComboBox<>(new String[]{"VIDEO", "LECTURA", "PRACTICA", "INTERACTIVA"});
        panel.add(cbTipoClase);

        panel.add(new JLabel("Duracion(min):"));
        spnDuracionClase = new JSpinner(new SpinnerNumberModel(30, 5, 300, 5));
        panel.add(spnDuracionClase);

        JButton btnAgregar = new JButton("+ Agregar Clase");
        btnAgregar.setBackground(new Color(52, 152, 219));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.addActionListener(e -> agregarClase());
        panel.add(btnAgregar);

        return panel;
    }

    private JPanel createEvaluacionSubPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 1),
            "2.3 Agregar Evaluacion al Modulo"));

        panel.add(new JLabel("Nombre:"));
        txtEvalNombre = new JTextField(12);
        panel.add(txtEvalNombre);

        panel.add(new JLabel("Tipo:"));
        cbTipoEval = new JComboBox<>(new String[]{"QUIZ", "TAREA", "EXAMEN", "PROYECTO", "PARTICIPACION"});
        panel.add(cbTipoEval);

        panel.add(new JLabel("Peso(%):"));
        spnPesoEval = new JSpinner(new SpinnerNumberModel(20, 1, 100, 5));
        panel.add(spnPesoEval);

        JButton btnAgregar = new JButton("+ Agregar Evaluacion");
        btnAgregar.setBackground(new Color(230, 126, 34));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.addActionListener(e -> agregarEvaluacion());
        panel.add(btnAgregar);

        return panel;
    }

    private JPanel createAccionesSubPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Acciones"));

        JButton btnPlantillaBasica = new JButton("Plantilla Basica");
        btnPlantillaBasica.setToolTipText("1 modulo, 2 clases, 1 quiz");
        btnPlantillaBasica.addActionListener(e -> aplicarPlantilla("basico"));

        JButton btnPlantillaEstandar = new JButton("Plantilla Estandar");
        btnPlantillaEstandar.setToolTipText("3 modulos con clases y evaluaciones");
        btnPlantillaEstandar.addActionListener(e -> aplicarPlantilla("estandar"));

        JButton btnFinalizar = new JButton("FINALIZAR CONSTRUCCION");
        btnFinalizar.setBackground(new Color(155, 89, 182));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 13));
        btnFinalizar.addActionListener(e -> finalizarConstruccion());

        JButton btnNuevo = new JButton("Nuevo Curso");
        btnNuevo.setBackground(new Color(231, 76, 60));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.addActionListener(e -> reiniciarTodo());

        panel.add(btnPlantillaBasica);
        panel.add(btnPlantillaEstandar);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(btnFinalizar);
        panel.add(btnNuevo);

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resultado y Vista Previa"));
        panel.setPreferredSize(new Dimension(1100, 200));

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtResultado.setText("Listo para crear curso...\n\n" +
            "FLUJO DE TRABAJO:\n" +
            "1. Complete los datos del curso y presione 'CREAR CURSO CON ABSTRACT FACTORY'\n" +
            "2. Una vez creado, use el Builder para agregar modulos, clases y evaluaciones\n" +
            "3. Presione 'FINALIZAR CONSTRUCCION' para ver el resultado completo\n");

        panel.add(new JScrollPane(txtResultado), BorderLayout.CENTER);
        return panel;
    }

    // ==================== LOGICA ABSTRACT FACTORY ====================
    private void crearCursoConFactory() {
        if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete Codigo y Nombre del curso", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            log("\n========== PASO 1: ABSTRACT FACTORY ==========\n");

            // Seleccionar fabrica segun tipo
            String tipoSeleccionado = (String) cbTipoCurso.getSelectedItem();
            factory = seleccionarFactory(tipoSeleccionado);
            log("Fabrica seleccionada: " + tipoSeleccionado + "CourseFactory\n");

            // Crear productos con la fabrica
            cursoCreado = factory.crearCurso(
                txtCodigo.getText(),
                txtNombre.getText(),
                txtDescripcion.getText(),
                Integer.parseInt(txtProfesorId.getText()),
                txtPeriodo.getText()
            );

            IMaterial material = factory.crearMaterial(
                "Material - " + txtNombre.getText(),
                "Material principal del curso"
            );

            IEvaluacion evaluacion = factory.crearEvaluacion(
                "Evaluacion - " + txtNombre.getText(),
                "Evaluacion inicial"
            );

            log("\nProductos creados por Abstract Factory:\n");
            log("  - Curso: " + cursoCreado.getNombre() + " [" + cursoCreado.getTipo() + "]\n");
            log("  - Material: " + material.getNombre() + " [" + material.getTipoMaterial() + "]\n");
            log("  - Evaluacion: " + evaluacion.getNombre() + " [" + evaluacion.getTipoEvaluacion() + "]\n");

            // Guardar en BD
            Curso cursoModel = cursoCreado.toModel();
            cursoIdCreado = cursoDAO.insertar(cursoModel);

            if (cursoIdCreado > 0) {
                log("\nCurso guardado en BD con ID: " + cursoIdCreado + "\n");

                // Inicializar Builder con datos del curso creado
                inicializarBuilderConCurso();

                // Habilitar panel Builder
                habilitarBuilder(true);

                log("\n========== PASO 2: BUILDER HABILITADO ==========\n");
                log("Ahora puede agregar modulos, clases y evaluaciones al curso.\n");

                JOptionPane.showMessageDialog(this,
                    "Curso creado exitosamente con Abstract Factory!\n\n" +
                    "ID: " + cursoIdCreado + "\n" +
                    "Tipo: " + tipoSeleccionado + "\n\n" +
                    "Ahora use el Builder (Paso 2) para agregar\n" +
                    "modulos, clases y evaluaciones.",
                    "Curso Creado", JOptionPane.INFORMATION_MESSAGE);

                // Deshabilitar formulario de creacion
                btnCrearCurso.setEnabled(false);
                txtCodigo.setEnabled(false);
                txtNombre.setEnabled(false);
                cbTipoCurso.setEnabled(false);

            } else {
                log("ERROR: No se pudo guardar el curso\n");
            }

        } catch (Exception ex) {
            log("ERROR: " + ex.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void inicializarBuilderConCurso() {
        // Configurar el Builder con la info del curso creado
        CursoCompleto.TipoCurso tipo;
        switch (cursoCreado.getTipo()) {
            case "PRESENCIAL": tipo = CursoCompleto.TipoCurso.PRESENCIAL; break;
            case "HIBRIDO": tipo = CursoCompleto.TipoCurso.HIBRIDO; break;
            default: tipo = CursoCompleto.TipoCurso.VIRTUAL;
        }

        cursoBuilder.setInformacionBasica(
                txtCodigo.getText(),
                txtNombre.getText(),
                txtDescripcion.getText())
            .setTipoCurso(tipo)
            .setPeriodoAcademico(txtPeriodo.getText())
            .setInstructor("Profesor ID: " + txtProfesorId.getText())
            .setCupoMaximo(cursoCreado.getCupoMaximo())
            .setDuracionYNivel(40, "Intermedio");
    }

    private CourseComponentFactory seleccionarFactory(String tipo) {
        switch (tipo) {
            case "PRESENCIAL": return new PresencialCourseFactory();
            case "VIRTUAL": return new VirtualCourseFactory();
            case "HIBRIDO": return new HibridoCourseFactory();
            default: return new VirtualCourseFactory();
        }
    }

    private void mostrarInfoFabrica() {
        String tipo = (String) cbTipoCurso.getSelectedItem();
        String info = getFactoryInfo(tipo);
        JOptionPane.showMessageDialog(this, info, "Informacion de Fabrica: " + tipo, JOptionPane.INFORMATION_MESSAGE);
    }

    private String getFactoryInfo(String tipo) {
        StringBuilder sb = new StringBuilder();
        sb.append("FABRICA: ").append(tipo).append("\n\n");
        switch (tipo) {
            case "PRESENCIAL":
                sb.append("Productos:\n");
                sb.append("- Curso: Cupo 40, Estrategia Ponderada\n");
                sb.append("- Material: PDF descargable\n");
                sb.append("- Evaluacion: EXAMEN, 1 intento, 120 min\n");
                break;
            case "VIRTUAL":
                sb.append("Productos:\n");
                sb.append("- Curso: Cupo 100, Promedio simple\n");
                sb.append("- Material: VIDEO, requiere visualizacion\n");
                sb.append("- Evaluacion: QUIZ, 3 intentos, 60 min\n");
                break;
            case "HIBRIDO":
                sb.append("Productos:\n");
                sb.append("- Curso: Cupo 60, Por competencias\n");
                sb.append("- Material: DOCUMENTO interactivo\n");
                sb.append("- Evaluacion: PROYECTO, 2 intentos\n");
                break;
        }
        return sb.toString();
    }

    // ==================== LOGICA BUILDER ====================
    private void habilitarBuilder(boolean habilitar) {
        Component[] components = panelBuilder.getComponents();
        habilitarComponentesRecursivo(panelBuilder, habilitar);
        panelBuilder.setEnabled(habilitar);

        if (habilitar) {
            panelBuilder.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                "PASO 2: Construir Estructura del Curso con Builder (ACTIVO)"));
        } else {
            panelBuilder.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "PASO 2: Builder (Primero cree el curso con Abstract Factory)"));
        }
    }

    private void habilitarComponentesRecursivo(Container container, boolean habilitar) {
        for (Component comp : container.getComponents()) {
            comp.setEnabled(habilitar);
            if (comp instanceof Container) {
                habilitarComponentesRecursivo((Container) comp, habilitar);
            }
        }
    }

    private void agregarModulo() {
        if (txtModuloNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese nombre del modulo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Modulo modulo = cursoBuilder.agregarModulo(txtModuloNombre.getText(), txtModuloDesc.getText());
        modulosCreados.add(modulo);

        cbModuloSelector.addItem("Modulo " + modulo.getOrden() + ": " + modulo.getNombre());
        cbModuloSelector.setSelectedIndex(cbModuloSelector.getItemCount() - 1);

        log("+ Modulo agregado: " + modulo.getNombre() + "\n");
        txtModuloNombre.setText("");
        txtModuloDesc.setText("");

        actualizarVistaPrevia();
    }

    private void agregarClase() {
        if (modulosCreados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero agregue un modulo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtClaseNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese nombre de la clase", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idx = cbModuloSelector.getSelectedIndex();
        Modulo modulo = modulosCreados.get(idx);
        Clase.TipoClase tipo = Clase.TipoClase.valueOf(cbTipoClase.getSelectedItem().toString());

        cursoBuilder.agregarClase(modulo, txtClaseNombre.getText(), "", tipo, (Integer) spnDuracionClase.getValue());

        log("  + Clase agregada a " + modulo.getNombre() + ": " + txtClaseNombre.getText() + "\n");
        txtClaseNombre.setText("");

        actualizarVistaPrevia();
    }

    private void agregarEvaluacion() {
        if (modulosCreados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero agregue un modulo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtEvalNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese nombre de la evaluacion", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idx = cbModuloSelector.getSelectedIndex();
        Modulo modulo = modulosCreados.get(idx);
        EvaluacionModulo.TipoEvaluacion tipo = EvaluacionModulo.TipoEvaluacion.valueOf(cbTipoEval.getSelectedItem().toString());

        cursoBuilder.agregarEvaluacion(modulo, txtEvalNombre.getText(), tipo, (Integer) spnPesoEval.getValue());

        log("  + Evaluacion agregada a " + modulo.getNombre() + ": " + txtEvalNombre.getText() + "\n");
        txtEvalNombre.setText("");

        actualizarVistaPrevia();
    }

    private void aplicarPlantilla(String tipo) {
        if (cursoCreado == null) {
            JOptionPane.showMessageDialog(this, "Primero cree el curso con Abstract Factory", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Limpiar modulos anteriores
        modulosCreados.clear();
        cbModuloSelector.removeAllItems();
        cursoBuilder.reset();
        inicializarBuilderConCurso();

        if (tipo.equals("basico")) {
            Modulo m1 = cursoBuilder.agregarModulo("Introduccion", "Modulo introductorio");
            modulosCreados.add(m1);
            cbModuloSelector.addItem("Modulo 1: Introduccion");

            cursoBuilder.agregarClase(m1, "Bienvenida", "Presentacion", Clase.TipoClase.VIDEO, 15);
            cursoBuilder.agregarClase(m1, "Conceptos Basicos", "Fundamentos", Clase.TipoClase.LECTURA, 30);
            cursoBuilder.agregarEvaluacion(m1, "Quiz Introductorio", EvaluacionModulo.TipoEvaluacion.QUIZ, 100);

            log("\nPlantilla BASICA aplicada: 1 modulo, 2 clases, 1 evaluacion\n");

        } else if (tipo.equals("estandar")) {
            Modulo m1 = cursoBuilder.agregarModulo("Fundamentos", "Conceptos basicos");
            Modulo m2 = cursoBuilder.agregarModulo("Desarrollo", "Contenido principal");
            Modulo m3 = cursoBuilder.agregarModulo("Evaluacion Final", "Proyecto y examen");
            modulosCreados.add(m1);
            modulosCreados.add(m2);
            modulosCreados.add(m3);
            cbModuloSelector.addItem("Modulo 1: Fundamentos");
            cbModuloSelector.addItem("Modulo 2: Desarrollo");
            cbModuloSelector.addItem("Modulo 3: Evaluacion Final");

            cursoBuilder.agregarClase(m1, "Introduccion", "", Clase.TipoClase.VIDEO, 30);
            cursoBuilder.agregarClase(m1, "Teoria Basica", "", Clase.TipoClase.LECTURA, 45);
            cursoBuilder.agregarEvaluacion(m1, "Quiz 1", EvaluacionModulo.TipoEvaluacion.QUIZ, 20);

            cursoBuilder.agregarClase(m2, "Tema Principal", "", Clase.TipoClase.VIDEO, 60);
            cursoBuilder.agregarClase(m2, "Practica Guiada", "", Clase.TipoClase.PRACTICA, 90);
            cursoBuilder.agregarEvaluacion(m2, "Tarea", EvaluacionModulo.TipoEvaluacion.TAREA, 30);

            cursoBuilder.agregarClase(m3, "Repaso General", "", Clase.TipoClase.INTERACTIVA, 45);
            cursoBuilder.agregarEvaluacion(m3, "Examen Final", EvaluacionModulo.TipoEvaluacion.EXAMEN, 50);

            log("\nPlantilla ESTANDAR aplicada: 3 modulos con clases y evaluaciones\n");
        }

        actualizarVistaPrevia();
    }

    private void finalizarConstruccion() {
        if (modulosCreados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un modulo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CursoCompleto cursoCompleto = cursoBuilder.getResultado();

        StringBuilder sb = new StringBuilder();
        sb.append("\n==========================================\n");
        sb.append("   CURSO CONSTRUIDO EXITOSAMENTE\n");
        sb.append("==========================================\n\n");
        sb.append("INFORMACION (Abstract Factory):\n");
        sb.append("  ID en BD: ").append(cursoIdCreado).append("\n");
        sb.append("  Codigo: ").append(cursoCompleto.getCodigo()).append("\n");
        sb.append("  Nombre: ").append(cursoCompleto.getNombre()).append("\n");
        sb.append("  Tipo: ").append(cursoCompleto.getTipoCurso()).append("\n\n");

        sb.append("ESTRUCTURA (Builder):\n");
        sb.append("  Total Modulos: ").append(cursoCompleto.getModulos().size()).append("\n");
        sb.append("  Total Clases: ").append(cursoCompleto.getTotalClases()).append("\n");
        sb.append("  Total Evaluaciones: ").append(cursoCompleto.getTotalEvaluaciones()).append("\n\n");

        for (Modulo m : cursoCompleto.getModulos()) {
            sb.append("MODULO ").append(m.getOrden()).append(": ").append(m.getNombre()).append("\n");
            for (Clase c : m.getClases()) {
                sb.append("  - Clase: ").append(c.getNombre()).append(" [").append(c.getTipo()).append("]\n");
            }
            for (EvaluacionModulo e : m.getEvaluaciones()) {
                sb.append("  - Eval: ").append(e.getNombre()).append(" [").append(e.getTipo()).append("] ").append(e.getPesoPorcentual()).append("%\n");
            }
        }
        sb.append("\n==========================================\n");

        txtResultado.setText(sb.toString());
        txtResultado.setCaretPosition(0);

        JOptionPane.showMessageDialog(this,
            "Curso construido exitosamente!\n\n" +
            "Modulos: " + cursoCompleto.getModulos().size() + "\n" +
            "Clases: " + cursoCompleto.getTotalClases() + "\n" +
            "Evaluaciones: " + cursoCompleto.getTotalEvaluaciones(),
            "Construccion Finalizada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarVistaPrevia() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vista previa del curso en construccion:\n\n");

        for (Modulo m : modulosCreados) {
            sb.append("MODULO ").append(m.getOrden()).append(": ").append(m.getNombre()).append("\n");
            for (Clase c : m.getClases()) {
                sb.append("  - ").append(c.getNombre()).append(" [").append(c.getTipo()).append("] ").append(c.getDuracionMinutos()).append("min\n");
            }
            for (EvaluacionModulo e : m.getEvaluaciones()) {
                sb.append("  * ").append(e.getNombre()).append(" [").append(e.getTipo()).append("] ").append(e.getPesoPorcentual()).append("%\n");
            }
            sb.append("\n");
        }

        if (!modulosCreados.isEmpty()) {
            txtResultado.append("\n" + sb.toString());
        }
    }

    private void reiniciarTodo() {
        // Reiniciar Abstract Factory
        cursoCreado = null;
        cursoIdCreado = 0;
        txtCodigo.setText("");
        txtCodigo.setEnabled(true);
        txtNombre.setText("");
        txtNombre.setEnabled(true);
        txtDescripcion.setText("");
        cbTipoCurso.setEnabled(true);
        btnCrearCurso.setEnabled(true);

        // Reiniciar Builder
        initBuilder();
        cbModuloSelector.removeAllItems();
        txtModuloNombre.setText("");
        txtModuloDesc.setText("");
        txtClaseNombre.setText("");
        txtEvalNombre.setText("");

        habilitarBuilder(false);

        txtResultado.setText("Formulario reiniciado. Listo para crear un nuevo curso.\n");
    }

    private void log(String msg) {
        txtResultado.append(msg);
        txtResultado.setCaretPosition(txtResultado.getDocument().getLength());
    }

    // ==================== PROTOTYPE PANEL ====================
    private JPanel createPrototypePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
            "PASO 3: Clonar Curso Existente con Prototype (Duplicar para nuevo periodo)"));

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior - Seleccion y acciones
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        topPanel.add(new JLabel("Curso a clonar:"));
        cbCursosExistentes = new JComboBox<>();
        cbCursosExistentes.setPreferredSize(new Dimension(300, 28));
        for (CursoPrototype proto : cursosPrototipos) {
            cbCursosExistentes.addItem(proto);
        }
        cbCursosExistentes.addActionListener(e -> mostrarPreviewCursoSeleccionado());
        topPanel.add(cbCursosExistentes);

        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("Nuevo Periodo:"));
        txtNuevoPeriodo = new JTextField("2025-1", 10);
        topPanel.add(txtNuevoPeriodo);

        JButton btnClonar = new JButton("CLONAR CURSO");
        btnClonar.setBackground(new Color(231, 76, 60));
        btnClonar.setForeground(Color.WHITE);
        btnClonar.setFont(new Font("Arial", Font.BOLD, 13));
        btnClonar.addActionListener(e -> clonarCursoSeleccionado());
        topPanel.add(btnClonar);

        JButton btnRefrescar = new JButton("Refrescar Lista");
        btnRefrescar.addActionListener(e -> refrescarListaCursos());
        topPanel.add(btnRefrescar);

        content.add(topPanel, BorderLayout.NORTH);

        // Panel central - Preview del curso seleccionado
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Vista Previa del Curso Seleccionado"));

        txtPreviewClon = new JTextArea(8, 40);
        txtPreviewClon.setEditable(false);
        txtPreviewClon.setFont(new Font("Monospaced", Font.PLAIN, 11));
        previewPanel.add(new JScrollPane(txtPreviewClon), BorderLayout.CENTER);

        content.add(previewPanel, BorderLayout.CENTER);

        panel.add(content, BorderLayout.CENTER);

        // Mostrar preview inicial
        if (!cursosPrototipos.isEmpty()) {
            mostrarPreviewCursoSeleccionado();
        }

        return panel;
    }

    private void mostrarPreviewCursoSeleccionado() {
        CursoPrototype seleccionado = (CursoPrototype) cbCursosExistentes.getSelectedItem();
        if (seleccionado == null) {
            txtPreviewClon.setText("No hay curso seleccionado");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CURSO ORIGINAL (PROTOTIPO)\n");
        sb.append("══════════════════════════════════════\n\n");
        sb.append("Codigo: ").append(seleccionado.getCodigo()).append("\n");
        sb.append("Nombre: ").append(seleccionado.getNombre()).append("\n");
        sb.append("Tipo: ").append(seleccionado.getTipoCurso()).append("\n");
        sb.append("Periodo: ").append(seleccionado.getPeriodoAcademico()).append("\n");
        sb.append("Cupo: ").append(seleccionado.getCupoMaximo()).append("\n\n");

        sb.append("ESTRUCTURA:\n");
        sb.append("  Modulos: ").append(seleccionado.getModulos().size()).append("\n");
        sb.append("  Clases: ").append(seleccionado.getTotalClases()).append("\n");
        sb.append("  Evaluaciones: ").append(seleccionado.getTotalEvaluaciones()).append("\n\n");

        for (ModuloPrototype m : seleccionado.getModulos()) {
            sb.append("  Modulo ").append(m.getOrden()).append(": ").append(m.getNombre()).append("\n");
            for (ClasePrototype c : m.getClases()) {
                sb.append("    - ").append(c.toString()).append("\n");
            }
            for (EvaluacionPrototype e : m.getEvaluaciones()) {
                sb.append("    * ").append(e.toString()).append("\n");
            }
        }

        sb.append("\n══════════════════════════════════════\n");
        sb.append("Al clonar, se creara una copia completa\n");
        sb.append("con el nuevo periodo: ").append(txtNuevoPeriodo.getText());

        txtPreviewClon.setText(sb.toString());
        txtPreviewClon.setCaretPosition(0);
    }

    private void clonarCursoSeleccionado() {
        CursoPrototype original = (CursoPrototype) cbCursosExistentes.getSelectedItem();
        if (original == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para clonar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoPeriodo = txtNuevoPeriodo.getText().trim();
        if (nuevoPeriodo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nuevo periodo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        log("\n========== PASO 3: PROTOTYPE ==========\n");
        log("Clonando curso: " + original.getNombre() + "\n");
        log("Periodo original: " + original.getPeriodoAcademico() + "\n");
        log("Nuevo periodo: " + nuevoPeriodo + "\n");

        // USAR EL PATRON PROTOTYPE PARA CLONAR
        CursoPrototype clon = original.clonarParaNuevoPeriodo(nuevoPeriodo);

        log("\nCurso clonado exitosamente!\n");
        log("  Nuevo codigo: " + clon.getCodigo() + "\n");
        log("  Nombre: " + clon.getNombre() + "\n");
        log("  Modulos copiados: " + clon.getModulos().size() + "\n");
        log("  Clases copiadas: " + clon.getTotalClases() + "\n");
        log("  Evaluaciones copiadas: " + clon.getTotalEvaluaciones() + "\n");

        // Agregar el clon a la lista de prototipos
        cursosPrototipos.add(clon);
        cbCursosExistentes.addItem(clon);

        // Mostrar resultado
        StringBuilder sb = new StringBuilder();
        sb.append("CURSO CLONADO EXITOSAMENTE\n");
        sb.append("══════════════════════════════════════\n\n");
        sb.append("ORIGINAL:\n");
        sb.append("  ").append(original.getCodigo()).append(" - ").append(original.getPeriodoAcademico()).append("\n\n");
        sb.append("CLON:\n");
        sb.append("  ").append(clon.getCodigo()).append(" - ").append(clon.getPeriodoAcademico()).append("\n\n");
        sb.append("Estructura clonada (deep copy):\n");
        sb.append("  Modulos: ").append(clon.getModulos().size()).append("\n");
        sb.append("  Clases: ").append(clon.getTotalClases()).append("\n");
        sb.append("  Evaluaciones: ").append(clon.getTotalEvaluaciones()).append("\n\n");

        for (ModuloPrototype m : clon.getModulos()) {
            sb.append("  Modulo ").append(m.getOrden()).append(": ").append(m.getNombre()).append("\n");
            for (ClasePrototype c : m.getClases()) {
                sb.append("    - ").append(c.toString()).append("\n");
            }
            for (EvaluacionPrototype e : m.getEvaluaciones()) {
                sb.append("    * ").append(e.toString()).append("\n");
            }
        }

        sb.append("\n══════════════════════════════════════\n");
        sb.append("El clon es independiente del original.\n");
        sb.append("Puede modificarlo sin afectar al original.");

        txtPreviewClon.setText(sb.toString());
        txtPreviewClon.setCaretPosition(0);

        JOptionPane.showMessageDialog(this,
            "Curso clonado exitosamente!\n\n" +
            "Original: " + original.getCodigo() + " (" + original.getPeriodoAcademico() + ")\n" +
            "Clon: " + clon.getCodigo() + " (" + clon.getPeriodoAcademico() + ")\n\n" +
            "Estructura copiada:\n" +
            "  - " + clon.getModulos().size() + " modulos\n" +
            "  - " + clon.getTotalClases() + " clases\n" +
            "  - " + clon.getTotalEvaluaciones() + " evaluaciones",
            "Patron Prototype - Clonacion Exitosa",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void refrescarListaCursos() {
        cargarCursosComoPrototipos();
        cbCursosExistentes.removeAllItems();
        for (CursoPrototype proto : cursosPrototipos) {
            cbCursosExistentes.addItem(proto);
        }
        if (!cursosPrototipos.isEmpty()) {
            mostrarPreviewCursoSeleccionado();
        }
        log("Lista de cursos actualizada\n");
    }

    // ==================== PANEL ADAPTER ====================
    private JPanel createAdapterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Titulo
        JLabel titulo = new JLabel("PATRÓN ADAPTER - Integración de Herramientas Externas");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(titulo);
        content.add(Box.createVerticalStrut(15));

        // Panel Videoconferencia
        JPanel panelVideo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelVideo.setBorder(BorderFactory.createTitledBorder("Videoconferencia"));

        JComboBox<String> cbProveedor = new JComboBox<>(new String[]{"Zoom", "Google Meet"});
        JTextField txtTituloReunion = new JTextField("Clase de Patrones", 20);
        JSpinner spnDuracion = new JSpinner(new SpinnerNumberModel(60, 15, 180, 15));
        JButton btnCrearReunion = new JButton("Crear Reunión");
        JTextArea txtResultadoAdapter = new JTextArea(5, 50);
        txtResultadoAdapter.setEditable(false);

        btnCrearReunion.setBackground(new Color(46, 204, 113));
        btnCrearReunion.setForeground(Color.WHITE);
        btnCrearReunion.addActionListener(e -> {
            IVideoconferencia servicio;
            if (cbProveedor.getSelectedItem().equals("Zoom")) {
                servicio = new ZoomAdapter();
            } else {
                servicio = new GoogleMeetAdapter();
            }
            servicio.crearReunion(txtTituloReunion.getText(), (Integer)spnDuracion.getValue());
            txtResultadoAdapter.setText("Proveedor: " + servicio.getProveedor() + "\n");
            txtResultadoAdapter.append("Enlace: " + servicio.obtenerEnlace() + "\n");
            txtResultadoAdapter.append("\nEl Adapter convierte la interfaz de " + servicio.getProveedor() +
                " a la interfaz IVideoconferencia del LMS");
            log("\n[ADAPTER] Reunión creada con " + servicio.getProveedor() + ": " + servicio.obtenerEnlace() + "\n");
        });

        panelVideo.add(new JLabel("Proveedor:"));
        panelVideo.add(cbProveedor);
        panelVideo.add(new JLabel("Título:"));
        panelVideo.add(txtTituloReunion);
        panelVideo.add(new JLabel("Duración (min):"));
        panelVideo.add(spnDuracion);
        panelVideo.add(btnCrearReunion);

        content.add(panelVideo);
        content.add(Box.createVerticalStrut(10));

        // Panel Repositorio
        JPanel panelRepo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelRepo.setBorder(BorderFactory.createTitledBorder("Repositorio de Archivos"));

        JTextField txtNombreArchivo = new JTextField("material.pdf", 20);
        JButton btnSubir = new JButton("Subir a Google Drive");
        btnSubir.setBackground(new Color(52, 152, 219));
        btnSubir.setForeground(Color.WHITE);
        btnSubir.addActionListener(e -> {
            IRepositorioArchivos drive = new GoogleDriveAdapter();
            String fileId = drive.subirArchivo(txtNombreArchivo.getText(), "contenido".getBytes());
            txtResultadoAdapter.append("\n\nArchivo subido: " + txtNombreArchivo.getText());
            txtResultadoAdapter.append("\nURL: " + drive.obtenerUrlPublica(fileId));
            log("\n[ADAPTER] Archivo subido a " + drive.getProveedor() + "\n");
        });

        panelRepo.add(new JLabel("Archivo:"));
        panelRepo.add(txtNombreArchivo);
        panelRepo.add(btnSubir);

        content.add(panelRepo);
        content.add(Box.createVerticalStrut(10));
        content.add(new JScrollPane(txtResultadoAdapter));

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ==================== PANEL BRIDGE ====================
    private JPanel createBridgePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("PATRÓN BRIDGE - Renderizado Multi-dispositivo");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        content.add(titulo);
        content.add(Box.createVerticalStrut(15));

        // Controles
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JComboBox<String> cbDispositivo = new JComboBox<>(new String[]{"Web", "Móvil", "Smart TV"});
        JButton btnRenderizar = new JButton("Renderizar Curso");
        JTextArea txtResultadoBridge = new JTextArea(20, 60);
        txtResultadoBridge.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtResultadoBridge.setEditable(false);

        btnRenderizar.setBackground(new Color(155, 89, 182));
        btnRenderizar.setForeground(Color.WHITE);
        btnRenderizar.addActionListener(e -> {
            DispositivoRenderer renderer;
            switch (cbDispositivo.getSelectedItem().toString()) {
                case "Móvil": renderer = new MobileRenderer(); break;
                case "Smart TV": renderer = new SmartTVRenderer(); break;
                default: renderer = new WebRenderer();
            }

            CursoBridge curso = new CursoBridge(renderer);
            curso.cargarDatos("PAT-001", "Patrones de Diseño", "Aprende los 23 patrones GoF", "virtual");

            ModuloBridge modulo = new ModuloBridge(renderer, "Introducción", 1);
            modulo.agregarMaterial(new MaterialBridge(renderer, "Video Intro", "video", "http://video.com/1"));
            modulo.agregarEvaluacion(new EvaluacionBridge(renderer, "Quiz 1", "quiz", 100));
            curso.agregarModulo(modulo);

            // Capturar salida
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);

            curso.mostrar();

            System.out.flush();
            System.setOut(old);

            txtResultadoBridge.setText("Dispositivo: " + renderer.getNombreDispositivo() + "\n\n");
            txtResultadoBridge.append(baos.toString());
            txtResultadoBridge.append("\n\nEl Bridge separa la abstracción (Curso) de la implementación (Renderer)");

            log("\n[BRIDGE] Curso renderizado para " + renderer.getNombreDispositivo() + "\n");
        });

        controles.add(new JLabel("Dispositivo:"));
        controles.add(cbDispositivo);
        controles.add(btnRenderizar);

        content.add(controles);
        content.add(Box.createVerticalStrut(10));
        content.add(new JScrollPane(txtResultadoBridge));

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ==================== PANEL COMPOSITE ====================
    private JPanel createCompositePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("PATRÓN COMPOSITE - Estructura Jerárquica de Cursos");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        content.add(titulo);
        content.add(Box.createVerticalStrut(15));

        JTextArea txtResultadoComposite = new JTextArea(25, 60);
        txtResultadoComposite.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtResultadoComposite.setEditable(false);

        JButton btnCrearEstructura = new JButton("Crear Estructura Jerárquica");
        btnCrearEstructura.setBackground(new Color(230, 126, 34));
        btnCrearEstructura.setForeground(Color.WHITE);
        btnCrearEstructura.addActionListener(e -> {
            CursoComposite curso = new CursoComposite("JAVA-101", "Programación en Java");

            Unidad unidad1 = new Unidad("Fundamentos de Java", 1);
            Leccion leccion1 = new Leccion("Variables y Tipos");
            leccion1.agregar(new Actividad("Intro Variables", "video", 15));
            leccion1.agregar(new Actividad("Tipos Primitivos", "lectura", 10));
            leccion1.agregar(new Actividad("Ejercicio", "practica", 20));

            Leccion leccion2 = new Leccion("Operadores");
            leccion2.agregar(new Actividad("Operadores", "video", 12));
            leccion2.agregar(new Actividad("Quiz", "quiz", 15));

            unidad1.agregar(leccion1);
            unidad1.agregar(leccion2);

            Unidad unidad2 = new Unidad("POO en Java", 2);
            Leccion leccion3 = new Leccion("Clases y Objetos");
            leccion3.agregar(new Actividad("Conceptos POO", "video", 20));
            leccion3.agregar(new Actividad("Primera clase", "practica", 30));
            unidad2.agregar(leccion3);

            curso.agregar(unidad1);
            curso.agregar(unidad2);

            // Capturar salida
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);

            curso.mostrar(0);

            System.out.flush();
            System.setOut(old);

            txtResultadoComposite.setText(baos.toString());
            txtResultadoComposite.append("\n\nTotal actividades: " + curso.contarActividades());
            txtResultadoComposite.append("\nDuración total: " + curso.getDuracionTotal() + " minutos");
            txtResultadoComposite.append("\n\nEl Composite permite tratar uniformemente elementos simples y compuestos");

            log("\n[COMPOSITE] Estructura creada con " + curso.contarActividades() + " actividades\n");
        });

        content.add(btnCrearEstructura);
        content.add(Box.createVerticalStrut(10));
        content.add(new JScrollPane(txtResultadoComposite));

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ==================== PANEL DECORATOR ====================
    private JPanel createDecoratorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("PATRÓN DECORATOR - Agregar Funcionalidades a Módulos");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        content.add(titulo);
        content.add(Box.createVerticalStrut(15));

        // Checkboxes para decoradores
        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JCheckBox chkGamificacion = new JCheckBox("Gamificación (+$15)");
        JCheckBox chkCertificacion = new JCheckBox("Certificación (+$50)");
        JCheckBox chkTutoria = new JCheckBox("Tutoría (+$30)");

        checkPanel.add(chkGamificacion);
        checkPanel.add(chkCertificacion);
        checkPanel.add(chkTutoria);

        JTextArea txtResultadoDecorator = new JTextArea(20, 60);
        txtResultadoDecorator.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtResultadoDecorator.setEditable(false);

        JButton btnAplicar = new JButton("Aplicar Decoradores");
        btnAplicar.setBackground(new Color(231, 76, 60));
        btnAplicar.setForeground(Color.WHITE);
        btnAplicar.addActionListener(e -> {
            IModuloEducativo modulo = new ModuloBase("Introducción a Python", "Aprende Python desde cero");

            StringBuilder sb = new StringBuilder();
            sb.append("MÓDULO BASE:\n");
            sb.append("  Nombre: " + modulo.getNombre() + "\n");
            sb.append("  Costo adicional: $" + modulo.getCostoAdicional() + "\n\n");

            if (chkGamificacion.isSelected()) {
                modulo = new GamificacionDecorator(modulo);
                sb.append("+ GAMIFICACIÓN agregada\n");
            }
            if (chkCertificacion.isSelected()) {
                modulo = new CertificacionDecorator(modulo);
                sb.append("+ CERTIFICACIÓN agregada\n");
            }
            if (chkTutoria.isSelected()) {
                modulo = new TutoriaDecorator(modulo);
                sb.append("+ TUTORÍA agregada\n");
            }

            sb.append("\nRESULTADO FINAL:\n");
            sb.append("══════════════════════════════════════\n");
            sb.append("Características: " + modulo.getCaracteristicas() + "\n");
            sb.append("Costo adicional total: $" + modulo.getCostoAdicional() + "\n");
            sb.append("══════════════════════════════════════\n");
            sb.append("\nEl Decorator agrega funcionalidades dinámicamente sin modificar la clase original");

            txtResultadoDecorator.setText(sb.toString());
            log("\n[DECORATOR] Módulo decorado - Costo: $" + modulo.getCostoAdicional() + "\n");
        });

        checkPanel.add(btnAplicar);
        content.add(checkPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(new JScrollPane(txtResultadoDecorator));

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ==================== PANEL FACADE ====================
    private JPanel createFacadePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("PATRÓN FACADE - Interfaz Unificada del LMS");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        content.add(titulo);
        content.add(Box.createVerticalStrut(15));

        // Controles
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JSpinner spnEstudiante = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JSpinner spnCurso = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));

        controles.add(new JLabel("ID Estudiante:"));
        controles.add(spnEstudiante);
        controles.add(new JLabel("ID Curso:"));
        controles.add(spnCurso);

        JTextArea txtResultadoFacade = new JTextArea(20, 60);
        txtResultadoFacade.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtResultadoFacade.setEditable(false);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton btnInfo = new JButton("Ver Info Curso");
        btnInfo.addActionListener(e -> {
            LMSFacade lms = new LMSFacade();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);
            lms.mostrarInfoCurso((Integer)spnCurso.getValue());
            System.out.flush();
            System.setOut(old);
            txtResultadoFacade.setText(baos.toString());
        });

        JButton btnInscribir = new JButton("Inscribir Estudiante");
        btnInscribir.setBackground(new Color(46, 204, 113));
        btnInscribir.setForeground(Color.WHITE);
        btnInscribir.addActionListener(e -> {
            LMSFacade lms = new LMSFacade();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);
            lms.inscribirEstudiante((Integer)spnEstudiante.getValue(), (Integer)spnCurso.getValue());
            System.out.flush();
            System.setOut(old);
            txtResultadoFacade.setText(baos.toString());
            txtResultadoFacade.append("\n\nLa Facade simplifica la interacción con múltiples subsistemas");
            log("\n[FACADE] Inscripción procesada\n");
        });

        JButton btnContenido = new JButton("Ver Contenido");
        btnContenido.addActionListener(e -> {
            LMSFacade lms = new LMSFacade();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);
            lms.mostrarContenidoCurso((Integer)spnEstudiante.getValue(), (Integer)spnCurso.getValue());
            System.out.flush();
            System.setOut(old);
            txtResultadoFacade.setText(baos.toString());
        });

        JButton btnEvaluar = new JButton("Realizar Evaluación");
        btnEvaluar.setBackground(new Color(230, 126, 34));
        btnEvaluar.setForeground(Color.WHITE);
        btnEvaluar.addActionListener(e -> {
            LMSFacade lms = new LMSFacade();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);
            LMSFacade.ResultadoEvaluacion resultado = lms.realizarEvaluacion((Integer)spnEstudiante.getValue(), 1);
            System.out.flush();
            System.setOut(old);
            txtResultadoFacade.setText(baos.toString());
            txtResultadoFacade.append("\n\nCalificación: " + resultado.getCalificacion());
        });

        JButton btnResumen = new JButton("Resumen Estudiante");
        btnResumen.setBackground(new Color(155, 89, 182));
        btnResumen.setForeground(Color.WHITE);
        btnResumen.addActionListener(e -> {
            LMSFacade lms = new LMSFacade();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);
            LMSFacade.ResumenEstudiante resumen = lms.obtenerResumenEstudiante(
                (Integer)spnEstudiante.getValue(), (Integer)spnCurso.getValue());
            resumen.mostrar();
            System.out.flush();
            System.setOut(old);
            txtResultadoFacade.setText(baos.toString());
        });

        botones.add(btnInfo);
        botones.add(btnInscribir);
        botones.add(btnContenido);
        botones.add(btnEvaluar);
        botones.add(btnResumen);

        content.add(controles);
        content.add(botones);
        content.add(Box.createVerticalStrut(10));
        content.add(new JScrollPane(txtResultadoFacade));

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new CrearCursoVista().setVisible(true);
        });
    }
}
