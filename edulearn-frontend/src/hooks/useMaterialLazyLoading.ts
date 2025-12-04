import { useState, useCallback } from 'react';
import {
  MaterialProxyResponse,
  cargarContenidoMaterial,
  obtenerMaterialConProxy,
} from '@/lib/api';

/**
 * Estado del material con lazy loading
 */
interface MaterialLazyState {
  material: MaterialProxyResponse | null;
  contenidoCargado: boolean;
  cargando: boolean;
  error: string | null;
  urlBlob: string | null;
}

/**
 * Hook personalizado para Lazy Loading de Materiales
 *
 * Implementa el patrón Proxy en el frontend:
 * - Carga inicial solo de metadata (información básica)
 * - Carga diferida del contenido pesado cuando el usuario lo solicita
 * - Manejo de estados (loading, error, success)
 * - Gestión de URLs de blobs para visualización
 *
 * @param usuarioId ID del usuario actual
 * @param rolUsuario Rol del usuario (ESTUDIANTE, DOCENTE, ADMIN)
 */
export function useMaterialLazyLoading(usuarioId: number, rolUsuario: string) {
  const [state, setState] = useState<MaterialLazyState>({
    material: null,
    contenidoCargado: false,
    cargando: false,
    error: null,
    urlBlob: null,
  });

  /**
   * Obtiene la información básica del material (sin cargar contenido)
   */
  const obtenerInformacionBasica = useCallback(
    async (materialId: number) => {
      setState((prev) => ({ ...prev, cargando: true, error: null }));

      try {
        const material = await obtenerMaterialConProxy({
          materialId,
          usuarioId,
          rolUsuario,
          cargarContenido: false,
        });

        setState({
          material,
          contenidoCargado: false,
          cargando: false,
          error: null,
          urlBlob: null,
        });

        return material;
      } catch (error) {
        const errorMsg = error instanceof Error ? error.message : 'Error al obtener material';
        setState((prev) => ({
          ...prev,
          cargando: false,
          error: errorMsg,
        }));
        throw error;
      }
    },
    [usuarioId, rolUsuario]
  );

  /**
   * Carga el contenido pesado del material (lazy loading activado)
   */
  const cargarContenido = useCallback(
    async (materialId: number) => {
      setState((prev) => ({ ...prev, cargando: true, error: null }));

      try {
        // Cargar contenido desde el backend (Proxy activado)
        const blob = await cargarContenidoMaterial({
          materialId,
          usuarioId,
          rolUsuario,
        });

        // Crear URL del blob para visualización
        const urlBlob = URL.createObjectURL(blob);

        setState((prev) => ({
          ...prev,
          contenidoCargado: true,
          cargando: false,
          urlBlob,
        }));

        return urlBlob;
      } catch (error) {
        const errorMsg = error instanceof Error ? error.message : 'Error al cargar contenido';
        setState((prev) => ({
          ...prev,
          cargando: false,
          error: errorMsg,
        }));
        throw error;
      }
    },
    [usuarioId, rolUsuario]
  );

  /**
   * Libera el blob URL de memoria
   */
  const liberarContenido = useCallback(() => {
    if (state.urlBlob) {
      URL.revokeObjectURL(state.urlBlob);
      setState((prev) => ({
        ...prev,
        contenidoCargado: false,
        urlBlob: null,
      }));
    }
  }, [state.urlBlob]);

  /**
   * Reinicia el estado del hook
   */
  const reiniciar = useCallback(() => {
    if (state.urlBlob) {
      URL.revokeObjectURL(state.urlBlob);
    }
    setState({
      material: null,
      contenidoCargado: false,
      cargando: false,
      error: null,
      urlBlob: null,
    });
  }, [state.urlBlob]);

  return {
    // Estado
    material: state.material,
    contenidoCargado: state.contenidoCargado,
    cargando: state.cargando,
    error: state.error,
    urlBlob: state.urlBlob,

    // Acciones
    obtenerInformacionBasica,
    cargarContenido,
    liberarContenido,
    reiniciar,
  };
}
