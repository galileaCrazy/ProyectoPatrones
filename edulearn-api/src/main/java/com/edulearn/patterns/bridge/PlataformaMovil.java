package com.edulearn.patterns.bridge;

/**
 * PATRÓN BRIDGE - Implementación Concreta: Móvil
 * Renderiza la interfaz para dispositivos móviles (iOS/Android)
 */
public class PlataformaMovil implements IPlataforma {

    @Override
    public String getNombre() {
        return "Mobile App (iOS/Android)";
    }

    @Override
    public String renderizarNavegacion() {
        return "<!-- Navegación Móvil -->\n" +
               "<MobileNavigation>\n" +
               "  <TopBar>\n" +
               "    <Logo>EduLearn</Logo>\n" +
               "    <MenuButton icon=\"☰\" />\n" +
               "  </TopBar>\n" +
               "  <BottomTabBar>\n" +
               "    <Tab icon=\"🏠\" label=\"Inicio\" />\n" +
               "    <Tab icon=\"📚\" label=\"Cursos\" />\n" +
               "    <Tab icon=\"👤\" label=\"Perfil\" />\n" +
               "  </BottomTabBar>\n" +
               "</MobileNavigation>";
    }

    @Override
    public String renderizarCursos() {
        return "<!-- Lista de Cursos Móvil -->\n" +
               "<ScrollView>\n" +
               "  <VerticalList>\n" +
               "    <CourseCard swipeable=\"true\">\n" +
               "      <Thumbnail size=\"small\" />\n" +
               "      <CourseTitle fontSize=\"16\">Nombre del Curso</CourseTitle>\n" +
               "      <Description lines=\"2\">Descripción breve...</Description>\n" +
               "      <TouchableButton>Ver Curso</TouchableButton>\n" +
               "    </CourseCard>\n" +
               "  </VerticalList>\n" +
               "</ScrollView>";
    }

    @Override
    public String renderizarPerfil() {
        return "<!-- Perfil de Usuario Móvil -->\n" +
               "<MobileProfile>\n" +
               "  <Header>\n" +
               "    <Avatar size=\"80\" circular=\"true\" />\n" +
               "    <UserName fontSize=\"20\">Nombre del Usuario</UserName>\n" +
               "    <Email fontSize=\"14\">usuario@edulearn.com</Email>\n" +
               "  </Header>\n" +
               "  <StatsRow>\n" +
               "    <Stat label=\"Cursos\" value=\"5\" />\n" +
               "    <Stat label=\"Progreso\" value=\"75%\" />\n" +
               "  </StatsRow>\n" +
               "  <ActionButton fullWidth=\"true\">Editar Perfil</ActionButton>\n" +
               "</MobileProfile>";
    }

    @Override
    public String obtenerResolucion() {
        return "390x844 (iPhone 14) / 360x800 (Android) - Portrait";
    }

    @Override
    public String obtenerCapacidades() {
        return "Touch Gestures (swipe, pinch, long-press), Biometrics, Push Notifications, Camera, GPS, Native APIs";
    }
}
