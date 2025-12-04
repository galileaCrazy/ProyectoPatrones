package com.edulearn.controller;

import com.edulearn.dto.MaterialDTO;
import com.edulearn.dto.ModuloCursoDTO;
import com.edulearn.model.Material;
import com.edulearn.model.ModuloCurso;
import com.edulearn.repository.MaterialRepository;
import com.edulearn.repository.ModuloCursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador CRUD completo para Módulos de Curso
 * Incluye gestión de materiales asociados
 */
@RestController
@RequestMapping("/api/modulos-curso")
@CrossOrigin(origins = "*")
public class ModuloCursoCRUDController {

    @Autowired
    private ModuloCursoRepository moduloCursoRepository;

    @Autowired
    private MaterialRepository materialRepository;

    /**
     * Obtener todos los módulos de un curso con estructura jerárquica
     */
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ModuloCursoDTO>> obtenerModulosPorCurso(@PathVariable Integer cursoId) {
        try {
            List<ModuloCurso> modulos = moduloCursoRepository.findByCursoIdOrderByOrden(cursoId);
            List<ModuloCursoDTO> dtos = new ArrayList<>();

            for (ModuloCurso modulo : modulos) {
                ModuloCursoDTO dto = convertirADTO(modulo);

                // Cargar materiales del módulo
                List<Material> materiales = materialRepository.findByModuloIdOrderByOrden(modulo.getId().longValue());
                dto.setMateriales(materiales.stream()
                    .map(this::convertirMaterialADTO)
                    .collect(Collectors.toList()));

                dtos.add(dto);
            }

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener un módulo específico por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModuloCursoDTO> obtenerModuloPorId(@PathVariable Integer id) {
        try {
            return moduloCursoRepository.findById(id)
                .map(modulo -> {
                    ModuloCursoDTO dto = convertirADTO(modulo);

                    // Cargar materiales
                    List<Material> materiales = materialRepository.findByModuloIdOrderByOrden(modulo.getId().longValue());
                    dto.setMateriales(materiales.stream()
                        .map(this::convertirMaterialADTO)
                        .collect(Collectors.toList()));

                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crear un nuevo módulo
     */
    @PostMapping
    public ResponseEntity<ModuloCursoDTO> crearModulo(@RequestBody ModuloCursoDTO dto) {
        try {
            ModuloCurso modulo = convertirAEntidad(dto);
            ModuloCurso moduloGuardado = moduloCursoRepository.save(modulo);

            ModuloCursoDTO resultado = convertirADTO(moduloGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualizar un módulo existente (incluyendo materiales)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ModuloCursoDTO> actualizarModulo(
            @PathVariable Integer id,
            @RequestBody ModuloCursoDTO dto) {
        try {
            return moduloCursoRepository.findById(id)
                .map(moduloExistente -> {
                    // Actualizar campos del módulo
                    moduloExistente.setNombre(dto.getNombre());
                    moduloExistente.setDescripcion(dto.getDescripcion());
                    moduloExistente.setDuracionHoras(dto.getDuracionHoras());
                    moduloExistente.setOrden(dto.getOrden());
                    moduloExistente.setEstado(dto.getEstado());
                    moduloExistente.setModuloPadreId(dto.getModuloPadreId());
                    moduloExistente.setEsHoja(dto.getEsHoja());
                    moduloExistente.setNivel(dto.getNivel());

                    ModuloCurso moduloActualizado = moduloCursoRepository.save(moduloExistente);

                    // Actualizar materiales si se enviaron
                    if (dto.getMateriales() != null) {
                        actualizarMaterialesModulo(moduloActualizado.getId(), dto.getMateriales());
                    }

                    ModuloCursoDTO resultado = convertirADTO(moduloActualizado);

                    // Cargar materiales actualizados
                    List<Material> materiales = materialRepository.findByModuloIdOrderByOrden(moduloActualizado.getId().longValue());
                    resultado.setMateriales(materiales.stream()
                        .map(this::convertirMaterialADTO)
                        .collect(Collectors.toList()));

                    return ResponseEntity.ok(resultado);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Eliminar un módulo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarModulo(@PathVariable Integer id) {
        try {
            if (!moduloCursoRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // Eliminar materiales asociados primero
            materialRepository.deleteByModuloId(id.longValue());

            // Eliminar módulo
            moduloCursoRepository.deleteById(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Módulo eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar módulo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    /**
     * Actualiza los materiales de un módulo
     */
    private void actualizarMaterialesModulo(Integer moduloId, List<MaterialDTO> materialesDTO) {
        if (materialesDTO == null || materialesDTO.isEmpty()) {
            System.out.println("⚠️ No hay materiales para actualizar");
            return;
        }

        System.out.println("🔄 Actualizando " + materialesDTO.size() + " materiales para módulo " + moduloId);

        // Eliminar materiales existentes
        materialRepository.deleteByModuloId(moduloId.longValue());

        // Crear nuevos materiales
        for (MaterialDTO materialDTO : materialesDTO) {
            System.out.println("📝 Procesando material: " + materialDTO.getNombre() + " (tipo: " + materialDTO.getTipo() + ")");

            Material material = new Material();
            material.setModuloId(moduloId.longValue());
            material.setNombre(materialDTO.getNombre());
            material.setTitulo(materialDTO.getTitulo() != null ? materialDTO.getTitulo() : materialDTO.getNombre());
            material.setDescripcion(materialDTO.getDescripcion() != null ? materialDTO.getDescripcion() : materialDTO.getNombre());

            // Tipo Material (string)
            String tipoMaterial = materialDTO.getTipo() != null ? materialDTO.getTipo() : materialDTO.getTipoMaterial();
            if (tipoMaterial == null) tipoMaterial = "OTHER";
            material.setTipoMaterial(tipoMaterial);

            // Mapear enum tipo (convertir a lowercase para el enum)
            try {
                // El enum es: video, pdf, documento, enlace, imagen, audio
                String tipoEnum = tipoMaterial.toLowerCase();

                // Mapear tipos del frontend a tipos del enum
                switch (tipoEnum) {
                    case "lecture":
                        tipoEnum = "documento";
                        break;
                    case "task":
                    case "quiz":
                    case "form":
                    case "practice":
                    case "supplement":
                        tipoEnum = "documento";
                        break;
                    case "image":
                        tipoEnum = "imagen";
                        break;
                    case "link":
                        tipoEnum = "enlace";
                        break;
                    // video, pdf, audio ya están bien
                }

                material.setTipo(Material.TipoMaterial.valueOf(tipoEnum));
                System.out.println("  ✓ Tipo mapeado: " + tipoMaterial + " -> " + tipoEnum);
            } catch (Exception e) {
                System.err.println("  ⚠️ Error al mapear tipo '" + tipoMaterial + "', usando 'documento' por defecto: " + e.getMessage());
                material.setTipo(Material.TipoMaterial.documento);
            }

            // URL y archivo
            String urlRecurso = materialDTO.getUrlRecurso() != null ? materialDTO.getUrlRecurso() : materialDTO.getFile();
            material.setUrlRecurso(urlRecurso);

            // Extraer archivoPath de la URL si es necesario
            if (urlRecurso != null && urlRecurso.contains("/descargar/")) {
                String archivoPath = urlRecurso.substring(urlRecurso.lastIndexOf("/") + 1);
                // Remover query params si existen
                if (archivoPath.contains("?")) {
                    archivoPath = archivoPath.substring(0, archivoPath.indexOf("?"));
                }
                material.setArchivoPath(archivoPath);
                System.out.println("  ✓ Archivo path extraído: " + archivoPath);
            }

            // Duración
            material.setDuracionSegundos(materialDTO.getDuracion() != null ? materialDTO.getDuracion() * 60 : null);

            // Tamaño
            material.setTamanoBytes(materialDTO.getTamanoBytes());

            // Orden
            material.setOrden(materialDTO.getOrden() != null ? materialDTO.getOrden() : 1);

            // Estado y obligatorio
            material.setEstado(materialDTO.getEstado() != null ? materialDTO.getEstado() : "activo");
            material.setEsObligatorio(materialDTO.getEsObligatorio() != null ? materialDTO.getEsObligatorio() : false);

            Material savedMaterial = materialRepository.save(material);
            System.out.println("  ✅ Material guardado con ID: " + savedMaterial.getId());
        }

        System.out.println("✅ Todos los materiales actualizados correctamente");
    }

    /**
     * Convierte ModuloCurso entity a DTO
     */
    private ModuloCursoDTO convertirADTO(ModuloCurso modulo) {
        ModuloCursoDTO dto = new ModuloCursoDTO();
        dto.setId(modulo.getId());
        dto.setCursoId(modulo.getCursoId());
        dto.setNombre(modulo.getNombre());
        dto.setDescripcion(modulo.getDescripcion());
        dto.setOrden(modulo.getOrden());
        dto.setDuracionHoras(modulo.getDuracionHoras());
        dto.setModuloPadreId(modulo.getModuloPadreId());
        dto.setEsHoja(modulo.getEsHoja());
        dto.setNivel(modulo.getNivel());
        dto.setEstado(modulo.getEstado());
        return dto;
    }

    /**
     * Convierte DTO a ModuloCurso entity
     */
    private ModuloCurso convertirAEntidad(ModuloCursoDTO dto) {
        ModuloCurso modulo = new ModuloCurso();
        modulo.setCursoId(dto.getCursoId());
        modulo.setNombre(dto.getNombre());
        modulo.setDescripcion(dto.getDescripcion());
        modulo.setOrden(dto.getOrden());
        modulo.setDuracionHoras(dto.getDuracionHoras());
        modulo.setModuloPadreId(dto.getModuloPadreId());
        modulo.setEsHoja(dto.getEsHoja() != null ? dto.getEsHoja() : true);
        modulo.setNivel(dto.getNivel() != null ? dto.getNivel() : 0);
        modulo.setEstado(dto.getEstado() != null ? dto.getEstado() : "ACTIVO");
        return modulo;
    }

    /**
     * Convierte Material entity a DTO
     */
    private MaterialDTO convertirMaterialADTO(Material material) {
        MaterialDTO dto = new MaterialDTO();
        dto.setId(material.getId());
        dto.setModuloId(material.getModuloId());
        dto.setNombre(material.getNombre());
        dto.setTitulo(material.getTitulo());
        dto.setDescripcion(material.getDescripcion());
        dto.setTipo(material.getTipoMaterial());
        dto.setTipoMaterial(material.getTipoMaterial());
        dto.setFile(material.getUrlRecurso());
        dto.setUrlRecurso(material.getUrlRecurso());
        dto.setDuracion(material.getDuracionSegundos() != null ? material.getDuracionSegundos() / 60 : null);
        dto.setDuracionSegundos(material.getDuracionSegundos());
        dto.setTamanoBytes(material.getTamanoBytes());
        dto.setOrden(material.getOrden());
        dto.setEsObligatorio(material.getEsObligatorio());
        dto.setEstado(material.getEstado());

        // Formatear tamaño si existe
        if (material.getTamanoBytes() != null) {
            dto.setSize(formatearTamano(material.getTamanoBytes()));
        }

        return dto;
    }

    /**
     * Formatea el tamaño en bytes a formato legible
     */
    private String formatearTamano(Long bytes) {
        if (bytes == null) return null;

        double mb = bytes / (1024.0 * 1024.0);
        if (mb >= 1) {
            return String.format("%.0f MB", mb);
        }

        double kb = bytes / 1024.0;
        return String.format("%.0f KB", kb);
    }
}
