package com.edulearn.patterns.creational.prototype;

import com.edulearn.model.Curso;

/**
 * Patrón Prototype para clonar cursos existentes.
 * Permite crear nuevos cursos basados en plantillas existentes,
 * útil para crear cursos similares en diferentes períodos académicos.
 */
public class CursoPrototype implements Cloneable {
    private Curso curso;

    public CursoPrototype(Curso curso) {
        this.curso = curso;
    }

    /**
     * Clona el curso actual creando una copia profunda.
     * El ID se establece a null para que JPA genere uno nuevo al guardar.
     *
     * @return Nuevo objeto Curso clonado
     */
    @Override
    public Curso clone() {
        try {
            Curso clonado = new Curso();

            // No copiar el ID (será generado por la BD)
            clonado.setId(null);

            // Generar un código único basado en el timestamp (últimos 6 dígitos)
            String codigoBase = curso.getCodigo() != null ? curso.getCodigo() : "CURSO";
            long timestamp = System.currentTimeMillis();
            String timestampCorto = String.valueOf(timestamp).substring(7); // Últimos 6 dígitos
            String nuevoCodigo = codigoBase + "-" + timestampCorto;
            clonado.setCodigo(nuevoCodigo);

            clonado.setNombre(curso.getNombre() + " (Copia)");
            clonado.setDescripcion(curso.getDescripcion());
            clonado.setTipoCurso(curso.getTipoCurso());
            clonado.setEstado("BORRADOR"); // Los clones empiezan en BORRADOR
            clonado.setProfesorTitularId(curso.getProfesorTitularId());
            clonado.setPeriodoAcademico(curso.getPeriodoAcademico());
            clonado.setDuracion(curso.getDuracion());
            clonado.setEstrategiaEvaluacion(curso.getEstrategiaEvaluacion());
            clonado.setCupoMaximo(curso.getCupoMaximo());

            return clonado;
        } catch (Exception e) {
            throw new RuntimeException("Error al clonar curso: " + e.getMessage(), e);
        }
    }

    /**
     * Clona el curso con personalizaciones específicas.
     *
     * @param nuevoNombre Nombre para el curso clonado
     * @param nuevoPeriodo Período académico para el curso clonado
     * @return Nuevo objeto Curso clonado y personalizado
     */
    public Curso cloneConPersonalizacion(String nuevoNombre, String nuevoPeriodo) {
        Curso clonado = clone();

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            clonado.setNombre(nuevoNombre);
        }

        if (nuevoPeriodo != null && !nuevoPeriodo.trim().isEmpty()) {
            clonado.setPeriodoAcademico(nuevoPeriodo);
        }

        return clonado;
    }

    /**
     * Crea una plantilla de curso con valores por defecto.
     *
     * @param tipoCurso Tipo de plantilla a crear
     * @return Curso plantilla listo para ser usado como prototipo
     */
    public static Curso crearPlantilla(String tipoCurso) {
        Curso plantilla = new Curso();
        plantilla.setEstado("BORRADOR");

        switch (tipoCurso.toUpperCase()) {
            case "REGULAR":
                plantilla.setNombre("Plantilla Curso Regular");
                plantilla.setTipoCurso("REGULAR");
                plantilla.setDuracion(40);
                plantilla.setDescripcion("Curso regular con duración estándar de 40 horas");
                break;

            case "INTENSIVO":
                plantilla.setNombre("Plantilla Curso Intensivo");
                plantilla.setTipoCurso("INTENSIVO");
                plantilla.setDuracion(80);
                plantilla.setDescripcion("Curso intensivo con mayor carga horaria de 80 horas");
                break;

            case "CERTIFICACION":
                plantilla.setNombre("Plantilla Curso Certificación");
                plantilla.setTipoCurso("CERTIFICACION");
                plantilla.setDuracion(60);
                plantilla.setDescripcion("Curso de certificación profesional de 60 horas");
                break;

            default:
                plantilla.setNombre("Plantilla Curso Genérica");
                plantilla.setTipoCurso("REGULAR");
                plantilla.setDuracion(40);
                plantilla.setDescripcion("Plantilla genérica de curso");
                break;
        }

        return plantilla;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
