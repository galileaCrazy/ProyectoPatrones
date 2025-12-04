// Usar variable de entorno o localhost por defecto
export const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function getCursos() {
  const res = await fetch(`${API_URL}/cursos`);
  return res.json();
}

export async function getEstudiantes() {
  const res = await fetch(`${API_URL}/estudiantes`);
  return res.json();
}

export async function getInscripciones() {
  const res = await fetch(`${API_URL}/inscripciones`);
  return res.json();
}

export async function createCurso(curso: { nombre: string; descripcion: string; duracion: number }) {
  const res = await fetch(`${API_URL}/cursos`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(curso)
  });
  return res.json();
}

export async function createEstudiante(estudiante: { nombre: string; apellidos: string; email: string }) {
  const res = await fetch(`${API_URL}/estudiantes`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(estudiante)
  });
  return res.json();
}

export async function login(email: string, password: string) {
  const res = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  return res.json();
}

// ═══════════════════════════════════════════════════════════════
// PATRÓN PROXY - API DE MATERIALES CON LAZY LOADING
// ═══════════════════════════════════════════════════════════════

/**
 * Interfaz para la respuesta del Material Proxy
 */
export interface MaterialProxyResponse {
  id: number;
  titulo: string;
  descripcion: string;
  tipoMaterial: string;
  urlRecurso: string | null;
  tamanoBytes: number | null;
  duracionSegundos: number | null;
  esObligatorio: boolean;
  requiereVisualizacion: boolean;
  estado: string;
  orden: number;
  contenidoCargado: boolean;
  tieneAcceso: boolean;
  esMaterialPesado: boolean;
  urlCarga: string;
}

/**
 * Request para obtener material con Proxy
 */
export interface MaterialProxyRequest {
  materialId: number;
  usuarioId: number;
  rolUsuario: string;
  cargarContenido?: boolean;
}

/**
 * Request para obtener materiales de un módulo
 */
export interface ModuloMaterialesRequest {
  moduloId: number;
  usuarioId: number;
  rolUsuario: string;
}

/**
 * Obtiene un material con Proxy (sin cargar contenido pesado)
 * Endpoint: POST /api/materiales/proxy
 */
export async function obtenerMaterialConProxy(request: MaterialProxyRequest): Promise<MaterialProxyResponse> {
  const res = await fetch(`${API_URL}/materiales/proxy`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(request)
  });

  if (!res.ok) {
    throw new Error(`Error al obtener material: ${res.statusText}`);
  }

  return res.json();
}

/**
 * Obtiene todos los materiales de un módulo con Proxy (lazy loading)
 * Endpoint: POST /api/materiales/proxy/modulo
 */
export async function obtenerMaterialesModuloConProxy(
  request: ModuloMaterialesRequest
): Promise<MaterialProxyResponse[]> {
  const res = await fetch(`${API_URL}/materiales/proxy/modulo`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(request)
  });

  if (!res.ok) {
    throw new Error(`Error al obtener materiales del módulo: ${res.statusText}`);
  }

  return res.json();
}

/**
 * Carga el contenido real de un material (activa lazy loading)
 * Endpoint: POST /api/materiales/proxy/cargar
 */
export async function cargarContenidoMaterial(request: MaterialProxyRequest): Promise<Blob> {
  const res = await fetch(`${API_URL}/materiales/proxy/cargar`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(request)
  });

  if (!res.ok) {
    if (res.status === 403) {
      throw new Error('No tienes permisos para acceder a este material');
    }
    throw new Error(`Error al cargar contenido: ${res.statusText}`);
  }

  return res.blob();
}

/**
 * Verifica si un usuario tiene acceso a un material
 * Endpoint: GET /api/materiales/proxy/verificar-acceso/{materialId}
 */
export async function verificarAccesoMaterial(
  materialId: number,
  usuarioId: number,
  rol: string
): Promise<{ tieneAcceso: boolean; mensaje: string }> {
  const res = await fetch(
    `${API_URL}/materiales/proxy/verificar-acceso/${materialId}?usuarioId=${usuarioId}&rol=${rol}`
  );

  if (!res.ok) {
    throw new Error(`Error al verificar acceso: ${res.statusText}`);
  }

  return res.json();
}

/**
 * Obtiene estadísticas del caché de proxies
 * Endpoint: GET /api/materiales/proxy/estadisticas
 */
export async function obtenerEstadisticasProxyCache(): Promise<{
  totalProxiesEnCache: number;
  proxiesConContenidoCargado: number;
  proxiesSinCargar: number;
}> {
  const res = await fetch(`${API_URL}/materiales/proxy/estadisticas`);

  if (!res.ok) {
    throw new Error(`Error al obtener estadísticas: ${res.statusText}`);
  }

  return res.json();
}

/**
 * Limpia el caché de materiales de un usuario
 * Endpoint: DELETE /api/materiales/proxy/cache/{usuarioId}
 */
export async function limpiarCacheMaterialesUsuario(usuarioId: number): Promise<{ mensaje: string }> {
  const res = await fetch(`${API_URL}/materiales/proxy/cache/${usuarioId}`, {
    method: 'DELETE'
  });

  if (!res.ok) {
    throw new Error(`Error al limpiar caché: ${res.statusText}`);
  }

  return res.json();
}

// ============================================================
// CRUD de Módulos del Curso
// ============================================================

export interface MaterialDTO {
  id?: string | number | null;
  moduloId?: number;
  cursoId?: number;
  nombre: string;
  titulo?: string;
  descripcion?: string;
  tipo: string;
  tipoMaterial?: string;
  file?: string | null;
  urlRecurso?: string | null;
  duration?: number;
  duracion?: number;
  duracionSegundos?: number;
  tamanoBytes?: number | null;
  size?: string;
  orden?: number;
  esObligatorio?: boolean;
  isCompleted?: boolean;
  estado?: string;
}

export interface ModuloCursoDTO {
  id?: number;
  cursoId: number;
  nombre: string;
  descripcion?: string;
  orden?: number;
  duracionHoras?: number;
  moduloPadreId?: number | null;
  esHoja?: boolean;
  nivel?: number;
  estado?: string;
  materiales?: MaterialDTO[];
}

/**
 * Obtiene todos los módulos de un curso
 */
export async function obtenerModulosPorCurso(cursoId: string | number): Promise<ModuloCursoDTO[]> {
  const res = await fetch(`${API_URL}/modulos-curso/curso/${cursoId}`);

  if (!res.ok) {
    throw new Error(`Error al obtener módulos: ${res.statusText}`);
  }

  return res.json();
}

/**
 * Obtiene un módulo específico por ID
 */
export async function obtenerModuloPorId(moduloId: number): Promise<ModuloCursoDTO> {
  const res = await fetch(`${API_URL}/modulos-curso/${moduloId}`);

  if (!res.ok) {
    throw new Error(`Error al obtener módulo: ${res.statusText}`);
  }

  return res.json();
}

/**
 * Crea un nuevo módulo
 */
export async function crearModulo(modulo: ModuloCursoDTO): Promise<ModuloCursoDTO> {
  const res = await fetch(`${API_URL}/modulos-curso`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(modulo),
  });

  if (!res.ok) {
    throw new Error(`Error al crear módulo: ${res.statusText}`);
  }

  return res.json();
}

/**
 * Actualiza un módulo existente (incluyendo materiales)
 */
export async function actualizarModulo(moduloId: number, modulo: ModuloCursoDTO): Promise<ModuloCursoDTO> {
  console.log("📤 [API] Enviando actualización de módulo:", moduloId, modulo);

  const res = await fetch(`${API_URL}/modulos-curso/${moduloId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(modulo),
  });

  if (!res.ok) {
    // Intentar leer el cuerpo de la respuesta para obtener el error real
    let errorMsg = `Error al actualizar módulo (${res.status} ${res.statusText})`;
    try {
      const errorData = await res.json();
      if (errorData.message) {
        errorMsg += `: ${errorData.message}`;
      } else if (errorData.error) {
        errorMsg += `: ${errorData.error}`;
      }
      console.error("❌ [API] Error del backend:", errorData);
    } catch (e) {
      // Si no se puede parsear el error como JSON, usar el texto
      try {
        const errorText = await res.text();
        if (errorText) {
          errorMsg += `: ${errorText.substring(0, 200)}`;
        }
        console.error("❌ [API] Error del backend (texto):", errorText);
      } catch (e2) {
        console.error("❌ [API] No se pudo leer el error del backend");
      }
    }
    throw new Error(errorMsg);
  }

  const result = await res.json();
  console.log("✅ [API] Módulo actualizado exitosamente:", result);
  return result;
}

/**
 * Elimina un módulo
 */
export async function eliminarModulo(moduloId: number): Promise<{ message: string }> {
  const res = await fetch(`${API_URL}/modulos-curso/${moduloId}`, {
    method: 'DELETE',
  });

  if (!res.ok) {
    throw new Error(`Error al eliminar módulo: ${res.statusText}`);
  }

  return res.json();
}

// ============================================================
// FILE UPLOAD API
// ============================================================

/**
 * Sube un archivo al servidor
 */
export async function subirArchivo(
  file: File,
  materialId?: number,
  tipoMaterial?: string,
  onProgress?: (progress: number) => void
): Promise<{
  success: boolean;
  filename: string;
  originalFilename: string;
  size: number;
  url: string;
  urlRecurso: string;
  type: string;
  message: string;
}> {
  const formData = new FormData();
  formData.append('file', file);

  if (materialId) {
    formData.append('materialId', materialId.toString());
  }

  if (tipoMaterial) {
    formData.append('tipoMaterial', tipoMaterial);
  }

  const xhr = new XMLHttpRequest();

  return new Promise((resolve, reject) => {
    // Progreso de subida
    if (onProgress) {
      xhr.upload.addEventListener('progress', (e) => {
        if (e.lengthComputable) {
          const progress = (e.loaded / e.total) * 100;
          onProgress(progress);
        }
      });
    }

    // Completado
    xhr.addEventListener('load', () => {
      if (xhr.status >= 200 && xhr.status < 300) {
        try {
          const response = JSON.parse(xhr.responseText);
          resolve(response);
        } catch (error) {
          reject(new Error('Error al parsear respuesta del servidor'));
        }
      } else {
        reject(new Error(`Error al subir archivo: ${xhr.statusText}`));
      }
    });

    // Error
    xhr.addEventListener('error', () => {
      reject(new Error('Error de red al subir archivo'));
    });

    // Abrir conexión y enviar
    xhr.open('POST', `${API_URL}/archivos/subir`);
    xhr.send(formData);
  });
}

/**
 * Obtiene la URL completa para descargar/visualizar un archivo
 */
export function obtenerUrlArchivo(filename: string, inline: boolean = true): string {
  const baseUrl = API_URL.replace('/api', '');
  return `${baseUrl}/api/archivos/descargar/${filename}?inline=${inline}`;
}

/**
 * Obtiene información de un archivo
 */
export async function obtenerInfoArchivo(filename: string) {
  const res = await fetch(`${API_URL}/archivos/info/${filename}`);

  if (!res.ok) {
    throw new Error(`Error al obtener información del archivo: ${res.statusText}`);
  }

  return res.json();
}

/**
 * Elimina un archivo del servidor
 */
export async function eliminarArchivo(filename: string) {
  const res = await fetch(`${API_URL}/archivos/${filename}`, {
    method: 'DELETE',
  });

  if (!res.ok) {
    throw new Error(`Error al eliminar archivo: ${res.statusText}`);
  }

  return res.json();
}
