package com.edulearn.controller;

import com.edulearn.model.ModuloCurso;
import com.edulearn.patterns.structural.composite.*;
import com.edulearn.repository.ModuloCursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    @Autowired
    private ModuloCursoRepository moduloRepository;

    @GetMapping
    public List<ModuloCurso> getAll() {
        return moduloRepository.findAll();
    }

    @GetMapping("/{id}")
    public ModuloCurso getById(@PathVariable Integer id) {
        return moduloRepository.findById(id).orElse(null);
    }

    @GetMapping("/curso/{cursoId}")
    public List<ModuloCurso> getByCurso(@PathVariable Integer cursoId) {
        return moduloRepository.findByCursoId(cursoId);
    }

    @GetMapping("/curso/{cursoId}/raiz")
    public List<ModuloCurso> getRaizByCurso(@PathVariable Integer cursoId) {
        return moduloRepository.findByCursoIdAndModuloPadreIdIsNull(cursoId);
    }

    @PostMapping
    public ModuloCurso crear(@RequestBody ModuloCurso modulo) {
        return moduloRepository.save(modulo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        moduloRepository.deleteById(id);
    }

    // ========== ENDPOINTS CON PATRÓN COMPOSITE ==========

    /**
     * GET /api/modulos/curso/{cursoId}/arbol
     * Obtener estructura jerárquica completa usando Composite
     */
    @GetMapping("/curso/{cursoId}/arbol")
    public Map<String, Object> obtenerArbol(@PathVariable Integer cursoId) {
        List<ModuloCurso> todosModulos = moduloRepository.findByCursoId(cursoId);

        // Construir estructura Composite
        ComponenteModulo raiz = construirArbolComposite(todosModulos, null);

        Map<String, Object> response = new HashMap<>();
        response.put("cursoId", cursoId);
        response.put("totalModulos", todosModulos.size());

        if (raiz != null) {
            response.put("duracionTotal", raiz.calcularDuracionTotal());
            response.put("estructura", raiz.renderizar(0));
            response.put("info", raiz.obtenerInfo());

            if (raiz instanceof Modulo) {
                response.put("totalComponentes", ((Modulo) raiz).contarComponentes());
                response.put("totalTemas", ((Modulo) raiz).obtenerTodosTemas().size());
            }
        } else {
            response.put("mensaje", "No hay módulos para este curso");
        }

        return response;
    }

    /**
     * POST /api/modulos/crear-estructura
     * Crear estructura jerárquica completa usando Composite
     */
    @PostMapping("/crear-estructura")
    public Map<String, Object> crearEstructura(@RequestBody Map<String, Object> params) {
        Integer cursoId = (Integer) params.get("cursoId");
        String tipo = (String) params.getOrDefault("tipo", "BASICO");

        List<ModuloCurso> modulosCreados = new ArrayList<>();

        // Crear estructura según tipo usando patrón Composite
        Modulo cursoCompleto = crearEstructuraCompositePredefinida(tipo);

        // Guardar en BD
        modulosCreados.addAll(guardarEstructuraEnBD(cursoCompleto, cursoId, null, 0));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cursoId", cursoId);
        response.put("tipo", tipo);
        response.put("modulosCreados", modulosCreados.size());
        response.put("estructura", cursoCompleto.renderizar(0));
        response.put("duracionTotal", cursoCompleto.calcularDuracionTotal());
        response.put("patron", "Composite");

        return response;
    }

    /**
     * POST /api/modulos/calcular-duracion
     * Calcular duración total de un módulo y sus hijos usando Composite
     */
    @PostMapping("/calcular-duracion")
    public Map<String, Object> calcularDuracion(@RequestBody Map<String, Integer> params) {
        Integer moduloId = params.get("moduloId");

        ModuloCurso modulo = moduloRepository.findById(moduloId).orElse(null);
        if (modulo == null) {
            return Map.of("error", "Módulo no encontrado");
        }

        // Construir estructura Composite desde este módulo
        List<ModuloCurso> todosModulos = moduloRepository.findByCursoId(modulo.getCursoId());
        ComponenteModulo componente = construirArbolComposite(todosModulos, moduloId);

        Map<String, Object> response = new HashMap<>();
        response.put("moduloId", moduloId);
        response.put("nombre", modulo.getNombre());

        if (componente != null) {
            response.put("duracionTotal", componente.calcularDuracionTotal());
            response.put("esHoja", componente.esHoja());
            response.put("estructura", componente.renderizar(0));

            if (componente instanceof Modulo) {
                response.put("totalComponentes", ((Modulo) componente).contarComponentes());
                response.put("totalTemas", ((Modulo) componente).obtenerTodosTemas().size());
            }
        }

        return response;
    }

    // ========== MÉTODOS AUXILIARES ==========

    private ComponenteModulo construirArbolComposite(List<ModuloCurso> todosModulos, Integer moduloPadreId) {
        List<ModuloCurso> hijosDirectos = todosModulos.stream()
            .filter(m -> Objects.equals(m.getModuloPadreId(), moduloPadreId))
            .sorted(Comparator.comparing(ModuloCurso::getOrden, Comparator.nullsLast(Comparator.naturalOrder())))
            .toList();

        if (hijosDirectos.isEmpty()) {
            return null;
        }

        if (hijosDirectos.size() == 1 && hijosDirectos.get(0).getEsHoja()) {
            ModuloCurso mc = hijosDirectos.get(0);
            return new Tema(mc.getNombre(), mc.getDescripcion(), mc.getDuracionHoras() != null ? mc.getDuracionHoras() : 0);
        }

        Modulo raizVirtual = new Modulo("Curso Completo", "Estructura completa del curso");

        for (ModuloCurso mc : hijosDirectos) {
            ComponenteModulo componente = construirComponente(todosModulos, mc);
            if (componente != null) {
                raizVirtual.agregar(componente);
            }
        }

        return raizVirtual;
    }

    private ComponenteModulo construirComponente(List<ModuloCurso> todosModulos, ModuloCurso moduloCurso) {
        if (moduloCurso.getEsHoja()) {
            return new Tema(
                moduloCurso.getNombre(),
                moduloCurso.getDescripcion(),
                moduloCurso.getDuracionHoras() != null ? moduloCurso.getDuracionHoras() : 0
            );
        } else {
            Modulo modulo = new Modulo(moduloCurso.getNombre(), moduloCurso.getDescripcion());

            List<ModuloCurso> hijos = todosModulos.stream()
                .filter(m -> Objects.equals(m.getModuloPadreId(), moduloCurso.getId()))
                .sorted(Comparator.comparing(ModuloCurso::getOrden, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

            for (ModuloCurso hijo : hijos) {
                ComponenteModulo componente = construirComponente(todosModulos, hijo);
                if (componente != null) {
                    modulo.agregar(componente);
                }
            }

            return modulo;
        }
    }

    private Modulo crearEstructuraCompositePredefinida(String tipo) {
        Modulo cursoCompleto = new Modulo("Curso Completo", "Estructura predefinida " + tipo);

        switch (tipo.toUpperCase()) {
            case "BASICO":
                Modulo mod1 = new Modulo("Módulo 1: Introducción", "Conceptos básicos");
                mod1.agregar(new Tema("1.1 Introducción al tema", "Primeros pasos", 2));
                mod1.agregar(new Tema("1.2 Conceptos fundamentales", "Base teórica", 3));
                cursoCompleto.agregar(mod1);

                Modulo mod2 = new Modulo("Módulo 2: Práctica", "Ejercicios prácticos");
                mod2.agregar(new Tema("2.1 Ejercicio 1", "Primer ejercicio", 2));
                mod2.agregar(new Tema("2.2 Ejercicio 2", "Segundo ejercicio", 2));
                cursoCompleto.agregar(mod2);
                break;

            case "INTERMEDIO":
                Modulo modA = new Modulo("Módulo 1: Fundamentos Avanzados", "Teoría avanzada");
                modA.agregar(new Tema("1.1 Teoría", "Conceptos avanzados", 4));

                Modulo modA1 = new Modulo("1.2 Subtemas", "Detalles específicos");
                modA1.agregar(new Tema("1.2.1 Subtema A", "Detalle A", 2));
                modA1.agregar(new Tema("1.2.2 Subtema B", "Detalle B", 2));
                modA.agregar(modA1);

                cursoCompleto.agregar(modA);

                Modulo modB = new Modulo("Módulo 2: Aplicaciones", "Casos de uso");
                modB.agregar(new Tema("2.1 Caso 1", "Aplicación práctica 1", 3));
                modB.agregar(new Tema("2.2 Caso 2", "Aplicación práctica 2", 3));
                cursoCompleto.agregar(modB);
                break;

            case "AVANZADO":
                Modulo modX = new Modulo("Módulo 1: Teoría Completa", "Fundamentos profundos");

                Modulo modX1 = new Modulo("1.1 Parte I", "Primera parte");
                modX1.agregar(new Tema("1.1.1 Introducción", "Inicio", 2));
                modX1.agregar(new Tema("1.1.2 Desarrollo", "Contenido", 4));
                modX.agregar(modX1);

                Modulo modX2 = new Modulo("1.2 Parte II", "Segunda parte");
                Modulo modX2a = new Modulo("1.2.1 Sección A", "Detalles A");
                modX2a.agregar(new Tema("1.2.1.1 Tema específico", "Contenido específico", 3));
                modX2.agregar(modX2a);
                modX.agregar(modX2);

                cursoCompleto.agregar(modX);
                break;
        }

        return cursoCompleto;
    }

    private List<ModuloCurso> guardarEstructuraEnBD(ComponenteModulo componente, Integer cursoId, Integer padreId, int nivel) {
        List<ModuloCurso> guardados = new ArrayList<>();

        if (componente instanceof Tema) {
            Tema tema = (Tema) componente;
            ModuloCurso mc = new ModuloCurso();
            mc.setCursoId(cursoId);
            mc.setNombre(tema.getNombre());
            mc.setDescripcion(tema.getDescripcion());
            mc.setDuracionHoras(tema.calcularDuracionTotal());
            mc.setModuloPadreId(padreId);
            mc.setEsHoja(true);
            mc.setNivel(nivel);
            mc.setOrden(guardados.size());
            guardados.add(moduloRepository.save(mc));
        } else if (componente instanceof Modulo) {
            Modulo modulo = (Modulo) componente;
            ModuloCurso mc = new ModuloCurso();
            mc.setCursoId(cursoId);
            mc.setNombre(modulo.getNombre());
            mc.setDescripcion(modulo.getDescripcion());
            mc.setModuloPadreId(padreId);
            mc.setEsHoja(false);
            mc.setNivel(nivel);
            mc.setOrden(guardados.size());
            mc = moduloRepository.save(mc);
            guardados.add(mc);

            for (ComponenteModulo hijo : modulo.obtenerHijos()) {
                guardados.addAll(guardarEstructuraEnBD(hijo, cursoId, mc.getId(), nivel + 1));
            }
        }

        return guardados;
    }
}
