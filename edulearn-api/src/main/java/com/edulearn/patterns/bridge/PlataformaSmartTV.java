package com.edulearn.patterns.bridge;

/**
 * PATRÓN BRIDGE - Implementación Concreta: Smart TV
 * Renderiza la interfaz para televisores inteligentes
 */
public class PlataformaSmartTV implements IPlataforma {

    @Override
    public String getNombre() {
        return "Smart TV (Samsung/LG/Android TV)";
    }

    @Override
    public String renderizarNavegacion() {
        return "<!-- Navegación Smart TV -->\n" +
               "<TVNavigation focusable=\"true\">\n" +
               "  <HorizontalMenu>\n" +
               "    <Logo size=\"large\">EduLearn</Logo>\n" +
               "    <MenuItem id=\"1\" selected=\"true\">🏠 Inicio</MenuItem>\n" +
               "    <MenuItem id=\"2\">📚 Mis Cursos</MenuItem>\n" +
               "    <MenuItem id=\"3\">👤 Perfil</MenuItem>\n" +
               "    <MenuItem id=\"4\">⚙️ Configuración</MenuItem>\n" +
               "  </HorizontalMenu>\n" +
               "  <RemoteHint>Use ◀ ▶ para navegar | OK para seleccionar</RemoteHint>\n" +
               "</TVNavigation>";
    }

    @Override
    public String renderizarCursos() {
        return "<!-- Grid de Cursos Smart TV -->\n" +
               "<TVGrid rows=\"2\" cols=\"4\" spacing=\"20\">\n" +
               "  <CourseTile focusable=\"true\" highlightOnFocus=\"true\">\n" +
               "    <Poster size=\"300x450\" quality=\"4K\" />\n" +
               "    <Title fontSize=\"28\" bold=\"true\">Nombre del Curso</Title>\n" +
               "    <Progress percent=\"60\" barHeight=\"8\" />\n" +
               "    <PlayButton icon=\"▶\" size=\"large\" />\n" +
               "  </CourseTile>\n" +
               "</TVGrid>\n" +
               "<FooterInfo>Presione [OK] para reproducir | [BACK] para volver</FooterInfo>";
    }

    @Override
    public String renderizarPerfil() {
        return "<!-- Perfil de Usuario Smart TV -->\n" +
               "<TVProfile>\n" +
               "  <LeftPanel width=\"40%\">\n" +
               "    <Avatar size=\"200\" quality=\"HD\" />\n" +
               "    <UserName fontSize=\"32\" bold=\"true\">Nombre del Usuario</UserName>\n" +
               "    <Email fontSize=\"20\">usuario@edulearn.com</Email>\n" +
               "  </LeftPanel>\n" +
               "  <RightPanel width=\"60%\">\n" +
               "    <StatCard>\n" +
               "      <Icon>📚</Icon>\n" +
               "      <Label fontSize=\"24\">Cursos Activos</Label>\n" +
               "      <Value fontSize=\"48\" color=\"green\">5</Value>\n" +
               "    </StatCard>\n" +
               "    <FocusableButton size=\"large\">Editar Perfil</FocusableButton>\n" +
               "  </RightPanel>\n" +
               "</TVProfile>";
    }

    @Override
    public String obtenerResolucion() {
        return "3840x2160 (4K UHD) - Landscape";
    }

    @Override
    public String obtenerCapacidades() {
        return "Remote Control (D-pad navigation), Voice Commands, 4K Streaming, 10-foot UI, Large Text, High Contrast";
    }
}
