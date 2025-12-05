import { useState, useEffect } from 'react';
import { API_URL as BASE_API_URL } from '@/lib/api';

interface Material {
  id: number;
  titulo: string;
  descripcion: string;
  tipoMaterial: string;
  urlRecurso: string;
  archivoPath: string;
  orden: number;
  duracionSegundos: number;
}

interface NavegacionData {
  material: Material;
  anterior: Material | null;
  siguiente: Material | null;
  completado: boolean;
  indice: number;
  total: number;
}

const API_URL = `${BASE_API_URL}/materiales`.replace('/api/api', '/api');

export const useMaterialNavegacion = (
  materialId: number | null,
  estudianteId: number,
  cursoId: number
) => {
  const [navegacion, setNavegacion] = useState<NavegacionData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Cargar información del material con navegación
  const cargarMaterial = async () => {
    if (!materialId) {
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      const response = await fetch(
        `${API_URL}/${materialId}/navegacion?estudianteId=${estudianteId}&cursoId=${cursoId}`
      );

      if (response.ok) {
        const data = await response.json();
        setNavegacion(data);
      } else {
        setError('Error al cargar el material');
      }
    } catch (err) {
      setError('Error de conexión');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };

  // Completar material actual
  const completarMaterial = async () => {
    if (!materialId) return;

    try {
      const response = await fetch(`${API_URL}/${materialId}/completar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ estudianteId, cursoId })
      });

      if (response.ok) {
        const data = await response.json();
        // Actualizar estado de completado
        if (navegacion) {
          setNavegacion({
            ...navegacion,
            completado: true
          });
        }
        return data;
      }
    } catch (err) {
      console.error('Error al completar material:', err);
      throw err;
    }
  };

  // Completar y avanzar al siguiente
  const completarYAvanzar = async (
    onNavegar: (siguienteMaterialId: number) => void
  ) => {
    try {
      // Completar material actual
      await completarMaterial();

      // Navegar al siguiente si existe
      if (navegacion?.siguiente) {
        onNavegar(navegacion.siguiente.id);
      }
    } catch (err) {
      console.error('Error al completar y avanzar:', err);
    }
  };

  // Obtener estadísticas de progreso
  const obtenerEstadisticas = async () => {
    try {
      const response = await fetch(
        `${API_URL}/estadisticas?estudianteId=${estudianteId}&cursoId=${cursoId}`
      );
      if (response.ok) {
        return await response.json();
      }
    } catch (err) {
      console.error('Error al obtener estadísticas:', err);
    }
    return null;
  };

  useEffect(() => {
    cargarMaterial();
  }, [materialId, estudianteId, cursoId]);

  return {
    navegacion,
    loading,
    error,
    cargarMaterial,
    completarMaterial,
    completarYAvanzar,
    obtenerEstadisticas
  };
};
