package com.edulearn.integrations.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * VideoIntegrationModule
 * ======================
 * Módulo auto contenido con patrón Adapter para integrar proveedores de videoconferencia
 * y almacenamiento (Drive) con endpoints REST listos para usar.
 *
 * Incluye:
 * - Contratos y DTOs de videoconferencia y storage
 * - Adaptador Google Meet (stub)
 * - Adaptador Google Drive (stub) para crear carpetas con el nombre indicado
 * - Servicio de orquestación
 * - Controlador REST
 *
 * Nota: Los adapters son stub (no requieren credenciales). Cuando se dispongan credenciales
 * reales (Google Calendar/Drive, Zoom, etc.), se reemplaza la lógica interna sin cambiar
 * la interfaz pública ni el frontend.
 */
public class VideoIntegrationModule {

    /* ===============================
     * CONTRATOS (Videoconferencia)
     * =============================== */

    public interface IVideoConferenceAdapter {
        MeetingInfo createMeeting(MeetingRequest request);
        Optional<MeetingInfo> getMeeting(String providerMeetingId);
        boolean deleteMeeting(String providerMeetingId);
        String getProvider();
    }

    public static class MeetingRequest {
        public Integer cursoId;
        public String titulo;
        public String descripcion;
        public String timezone; // e.g. "America/Mexico_City"
        public LocalDateTime fechaInicio;
        public Integer duracionMinutos; // default 60
        public Map<String, Object> options;

        public MeetingRequest() {}
    }

    public static class MeetingInfo {
        public String provider;           // "google_meet" | "zoom" | "teams"
        public String providerMeetingId;  // id del meeting en el proveedor
        public String url;                // url para unirse
        public String joinCode;           // si aplica
        public LocalDateTime startTime;
        public LocalDateTime endTime;
        public String timezone;
        public Map<String, Object> metadata = new HashMap<>();

        public MeetingInfo() {}
    }

    /* ===============================
     * ADAPTER: Google Meet (Stub)
     * =============================== */

    public static class GoogleMeetAdapter implements IVideoConferenceAdapter {
        private static final Logger log = LoggerFactory.getLogger(GoogleMeetAdapter.class);
        private final Map<String, MeetingInfo> storage = new ConcurrentHashMap<>();

        @Override
        public MeetingInfo createMeeting(MeetingRequest request) {
            String code = UUID.randomUUID().toString().substring(0, 8);
            String providerId = "gm-" + UUID.randomUUID();
            String url = "https://meet.google.com/lookup/" + code;

            LocalDateTime start = request.fechaInicio != null ? request.fechaInicio : LocalDateTime.now().plusMinutes(5);
            int dur = request.duracionMinutos != null ? request.duracionMinutos : 60;
            LocalDateTime end = start.plus(Duration.ofMinutes(dur));
            String tz = request.timezone != null ? request.timezone : ZoneId.systemDefault().getId();

            MeetingInfo info = new MeetingInfo();
            info.provider = getProvider();
            info.providerMeetingId = providerId;
            info.url = url;
            info.joinCode = code;
            info.startTime = start;
            info.endTime = end;
            info.timezone = tz;
            info.metadata.put("cursoId", request.cursoId);
            info.metadata.put("titulo", request.titulo);
            info.metadata.put("descripcion", request.descripcion);

            storage.put(providerId, info);
            log.info("[GoogleMeetAdapter] Meeting creado para curso {}: {}", request.cursoId, url);
            return info;
        }

        @Override
        public Optional<MeetingInfo> getMeeting(String providerMeetingId) {
            return Optional.ofNullable(storage.get(providerMeetingId));
        }

        @Override
        public boolean deleteMeeting(String providerMeetingId) {
            return storage.remove(providerMeetingId) != null;
        }

        @Override
        public String getProvider() {
            return "google_meet";
        }
    }

    /* ===============================
     * CONTRATOS (Storage - Drive)
     * =============================== */

    public interface IStorageAdapter {
        FolderInfo createFolder(FolderRequest request);
        Optional<FolderInfo> getFolder(String folderId);
        boolean deleteFolder(String folderId);
        String getProvider();
    }

    public static class FolderRequest {
        public Integer cursoId;
        public String nombre;       // nombre de la carpeta a crear
        public String descripcion;
        public Map<String, Object> options;
        public FolderRequest() {}
    }

    public static class FolderInfo {
        public String provider;     // "google_drive"
        public String folderId;     // id simulado
        public String url;          // url de acceso a la carpeta
        public String name;         // nombre de la carpeta
        public Map<String, Object> metadata = new HashMap<>();
        public FolderInfo() {}
    }

    /* ===============================
     * ADAPTER: Google Drive (Stub)
     * =============================== */

    public static class GoogleDriveAdapter implements IStorageAdapter {
        private static final Logger log = LoggerFactory.getLogger(GoogleDriveAdapter.class);
        private final Map<String, FolderInfo> storage = new ConcurrentHashMap<>();

        @Override
        public FolderInfo createFolder(FolderRequest request) {
            String id = "gd-" + UUID.randomUUID();
            String name = (request.nombre == null || request.nombre.isBlank()) ?
                ("Curso-" + request.cursoId + "-Folder") : request.nombre.trim();
            String url = "https://drive.google.com/drive/folders/" + id;

            FolderInfo info = new FolderInfo();
            info.provider = getProvider();
            info.folderId = id;
            info.url = url;
            info.name = name;
            info.metadata.put("cursoId", request.cursoId);
            info.metadata.put("descripcion", request.descripcion);

            storage.put(id, info);
            log.info("[GoogleDriveAdapter] Carpeta creada para curso {}: {} ({}).", request.cursoId, name, url);
            return info;
        }

        @Override
        public Optional<FolderInfo> getFolder(String folderId) {
            return Optional.ofNullable(storage.get(folderId));
        }

        @Override
        public boolean deleteFolder(String folderId) {
            return storage.remove(folderId) != null;
        }

        @Override
        public String getProvider() { return "google_drive"; }
    }

    /* ===============================
     * SERVICIO DE ORQUESTACIÓN
     * =============================== */

    @Service
    public static class IntegracionesService {
        private static final Logger log = LoggerFactory.getLogger(IntegracionesService.class);

        // Videoconferencia
        private final Map<String, IVideoConferenceAdapter> adapters = new HashMap<>();
        // Storage (Drive)
        private final Map<String, IStorageAdapter> storageAdapters = new HashMap<>();

        public IntegracionesService() {
            // Registrar adapters disponibles
            registerAdapter(new GoogleMeetAdapter());
            registerStorageAdapter(new GoogleDriveAdapter());
        }

        public void registerAdapter(IVideoConferenceAdapter adapter) {
            adapters.put(adapter.getProvider(), adapter);
            log.info("Adapter registrado: {}", adapter.getProvider());
        }

        public void registerStorageAdapter(IStorageAdapter adapter) {
            storageAdapters.put(adapter.getProvider(), adapter);
            log.info("Storage Adapter registrado: {}", adapter.getProvider());
        }

        // ---------- Videoconferencia ----------
        public MeetingInfo createMeetingForCourse(Integer cursoId, String proveedor, MeetingRequest req) {
            IVideoConferenceAdapter adapter = resolveAdapter(proveedor);
            req.cursoId = cursoId;
            MeetingInfo info = adapter.createMeeting(req);
            log.info("Reunión creada vía {} para curso {}: {}", proveedor, cursoId, info.url);
            return info;
        }

        public List<MeetingInfo> listMeetings(String proveedor) {
            IVideoConferenceAdapter adapter = resolveAdapter(proveedor);
            if (adapter instanceof GoogleMeetAdapter g) {
                return new ArrayList<>(g.storage.values());
            }
            return List.of();
        }

        public boolean deleteMeeting(String proveedor, String providerMeetingId) {
            IVideoConferenceAdapter adapter = resolveAdapter(proveedor);
            return adapter.deleteMeeting(providerMeetingId);
        }

        private IVideoConferenceAdapter resolveAdapter(String proveedor) {
            String key = (proveedor == null ? "google_meet" : proveedor.trim().toLowerCase());
            IVideoConferenceAdapter adapter = adapters.get(key);
            if (adapter == null) {
                throw new IllegalArgumentException("Proveedor no soportado: " + key);
            }
            return adapter;
        }

        // ------------- Storage (Drive) -------------
        public FolderInfo createFolderForCourse(Integer cursoId, String proveedor, FolderRequest req) {
            IStorageAdapter adapter = resolveStorageAdapter(proveedor);
            req.cursoId = cursoId;
            FolderInfo info = adapter.createFolder(req);
            log.info("Carpeta creada vía {} para curso {}: {} ({})", proveedor, cursoId, info.name, info.url);
            return info;
        }

        public List<FolderInfo> listFolders(String proveedor) {
            IStorageAdapter adapter = resolveStorageAdapter(proveedor);
            if (adapter instanceof GoogleDriveAdapter g) {
                return new ArrayList<>(g.storage.values());
            }
            return List.of();
        }

        public boolean deleteFolder(String proveedor, String folderId) {
            IStorageAdapter adapter = resolveStorageAdapter(proveedor);
            return adapter.deleteFolder(folderId);
        }

        private IStorageAdapter resolveStorageAdapter(String proveedor) {
            String key = (proveedor == null ? "google_drive" : proveedor.trim().toLowerCase());
            IStorageAdapter adapter = storageAdapters.get(key);
            if (adapter == null) {
                throw new IllegalArgumentException("Proveedor de almacenamiento no soportado: " + key);
            }
            return adapter;
        }
    }

    /* ===============================
     * CONTROLADOR REST
     * =============================== */

    @RestController
    @RequestMapping("/api/integraciones")
    @CrossOrigin(origins = "*")
    public static class IntegracionesController {
        private final IntegracionesService service;

        public IntegracionesController(IntegracionesService service) {
            this.service = service;
        }

        // ---------------- Videoconferencia ----------------
        @PostMapping("/cursos/{cursoId}/videoconferencia")
        public ResponseEntity<MeetingInfo> createMeeting(
                @PathVariable Integer cursoId,
                @RequestBody Map<String, Object> body) {

            String proveedor = optString(body.get("proveedor"), "google_meet");
            MeetingRequest req = new MeetingRequest();
            req.titulo = optString(body.get("titulo"), "Reunión del curso " + cursoId);
            req.descripcion = optString(body.get("descripcion"), "");
            req.timezone = optString(body.get("timezone"), ZoneId.systemDefault().getId());
            req.duracionMinutos = optInteger(body.get("duracionMinutos"), 60);
            req.options = optMap(body.get("options"));

            Object fi = body.get("fechaInicio");
            if (fi instanceof String s && !s.isBlank()) {
                req.fechaInicio = LocalDateTime.parse(s);
            } else {
                req.fechaInicio = LocalDateTime.now().plusMinutes(5);
            }

            MeetingInfo info = service.createMeetingForCourse(cursoId, proveedor, req);
            return ResponseEntity.ok(info);
        }

        @GetMapping("/videoconferencia")
        public ResponseEntity<List<MeetingInfo>> listMeetings(@RequestParam(required = false) String proveedor) {
            String prov = proveedor != null ? proveedor : "google_meet";
            return ResponseEntity.ok(service.listMeetings(prov));
        }

        @DeleteMapping("/videoconferencia/{providerMeetingId}")
        public ResponseEntity<Map<String, Object>> deleteMeeting(
                @PathVariable String providerMeetingId,
                @RequestParam(required = false) String proveedor) {
            String prov = proveedor != null ? proveedor : "google_meet";
            boolean ok = service.deleteMeeting(prov, providerMeetingId);
            return ResponseEntity.ok(Map.of("success", ok));
        }

        // ---------------- Drive (Storage) ------------------
        @PostMapping("/cursos/{cursoId}/drive")
        public ResponseEntity<FolderInfo> createFolder(
                @PathVariable Integer cursoId,
                @RequestBody Map<String, Object> body) {

            String proveedor = optString(body.get("proveedor"), "google_drive");
            FolderRequest req = new FolderRequest();
            req.nombre = optString(body.get("nombre"), "Carpeta del curso " + cursoId);
            req.descripcion = optString(body.get("descripcion"), "");
            req.options = optMap(body.get("options"));

            FolderInfo info = service.createFolderForCourse(cursoId, proveedor, req);
            return ResponseEntity.ok(info);
        }

        @GetMapping("/drive")
        public ResponseEntity<List<FolderInfo>> listFolders(@RequestParam(required = false) String proveedor) {
            String prov = proveedor != null ? proveedor : "google_drive";
            return ResponseEntity.ok(service.listFolders(prov));
        }

        @DeleteMapping("/drive/{folderId}")
        public ResponseEntity<Map<String, Object>> deleteFolder(
                @PathVariable String folderId,
                @RequestParam(required = false) String proveedor) {
            String prov = proveedor != null ? proveedor : "google_drive";
            boolean ok = service.deleteFolder(prov, folderId);
            return ResponseEntity.ok(Map.of("success", ok));
        }

        private static String optString(Object v, String def) {
            return v == null ? def : String.valueOf(v);
        }

        @SuppressWarnings("unchecked")
        private static Map<String, Object> optMap(Object v) {
            return v instanceof Map ? (Map<String, Object>) v : new HashMap<>();
        }

        private static Integer optInteger(Object v, Integer def) {
            try {
                return v == null ? def : Integer.valueOf(String.valueOf(v));
            } catch (Exception e) {
                return def;
            }
        }
    }
}
