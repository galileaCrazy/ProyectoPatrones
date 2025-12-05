import { useState, useEffect } from 'react';
import { API_URL as BASE_API_URL } from '@/lib/api';

interface ProgresoEstudiante {
  id?: number;
  estudianteId: number;
  cursoId: number;
  moduloActualId: number;
  porcentajeCompletado: number;
  calificacionAcumulada: number;
  leccionesCompletadas: number;
  evaluacionesCompletadas: number;
  estadoCurso: string;
  notasEstudiante?: string;
  tareasCompletadas?: string[]; // IDs de tareas completadas
  fechaUltimaActualizacion?: string;
}

interface HistorialProgreso {
  id: number;
  estudianteId: number;
  cursoId: number;
  porcentajeCompletado: number;
  fechaGuardado: string;
  descripcion: string;
}

const API_URL = `${BASE_API_URL}/progreso`.replace('/api/api', '/api');

export const useProgresoEstudiante = (estudianteId: number, cursoId: number) => {
  const [progreso, setProgreso] = useState<ProgresoEstudiante | null>(null);
  const [historial, setHistorial] = useState<HistorialProgreso[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Obtener progreso actual
  const cargarProgreso = async () => {
    try {
      setLoading(true);
      console.log('📥 Cargando progreso para estudiante:', estudianteId, 'curso:', cursoId);
      const url = `${API_URL}/${estudianteId}/${cursoId}`;
      console.log('🌐 URL completa:', url);

      const response = await fetch(url);
      console.log('📡 Status de respuesta:', response.status);

      if (response.ok) {
        const data = await response.json();
        console.log('✅ Progreso cargado exitosamente:', data);

        // Asegurarse de que notasEstudiante sea un string válido
        if (!data.notasEstudiante) {
          data.notasEstudiante = '[]';
        }

        setProgreso(data);
        console.log('✅ Estado actualizado con progreso:', data);
      } else {
        const errorText = await response.text();
        console.error('❌ Error al cargar progreso:', response.status, errorText);
        setError(`Error ${response.status}: ${errorText}`);
      }
    } catch (err) {
      setError('Error al cargar el progreso');
      console.error('❌ Error de conexión:', err);
    } finally {
      setLoading(false);
      console.log('✅ Carga completada, loading = false');
    }
  };

  // Actualizar progreso
  const actualizarProgreso = async (progresoActualizado: ProgresoEstudiante) => {
    try {
      console.log('📡 Actualizando progreso en el servidor...');
      const response = await fetch(`${API_URL}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(progresoActualizado)
      });

      console.log('📥 Respuesta del servidor:', response.status);

      if (response.ok) {
        const data = await response.json();
        console.log('✅ Datos recibidos:', data);
        setProgreso(data);
        return data;
      } else {
        const errorText = await response.text();
        console.error('❌ Error en respuesta:', errorText);
        throw new Error(`Error ${response.status}: ${errorText}`);
      }
    } catch (err) {
      console.error('❌ Error al actualizar progreso:', err);
      throw err;
    }
  };

  // Marcar/desmarcar tarea completada
  const toggleTarea = async (tareaId: string, completada: boolean) => {
    console.log('🔄 toggleTarea llamado:', { tareaId, completada, progreso });

    if (!progreso) {
      console.error('❌ No hay progreso disponible');
      return;
    }

    let tareasCompletadas: string[] = [];
    try {
      if (progreso.notasEstudiante && progreso.notasEstudiante.trim() !== '') {
        tareasCompletadas = JSON.parse(progreso.notasEstudiante);
        console.log('📋 Tareas actuales:', tareasCompletadas);
      }
    } catch (error) {
      console.warn('⚠️ Error al parsear tareas, iniciando array vacío:', error);
      tareasCompletadas = [];
    }

    if (completada) {
      if (!tareasCompletadas.includes(tareaId)) {
        tareasCompletadas.push(tareaId);
        console.log('✅ Tarea agregada:', tareaId);
      }
    } else {
      tareasCompletadas = tareasCompletadas.filter((id: string) => id !== tareaId);
      console.log('❌ Tarea removida:', tareaId);
    }

    const totalTareas = 20;
    const porcentaje = Math.round((tareasCompletadas.length / totalTareas) * 100);

    const progresoActualizado = {
      ...progreso,
      notasEstudiante: JSON.stringify(tareasCompletadas),
      porcentajeCompletado: porcentaje,
      leccionesCompletadas: tareasCompletadas.length
    };

    console.log('📤 Enviando actualización:', progresoActualizado);

    try {
      await actualizarProgreso(progresoActualizado);
      console.log('✅ Progreso actualizado exitosamente');
    } catch (error) {
      console.error('❌ Error al actualizar progreso:', error);
      throw error;
    }
  };

  // Guardar estado (Memento)
  const guardarEstado = async (descripcion: string = 'Checkpoint manual') => {
    try {
      const response = await fetch(`${API_URL}/guardar/${estudianteId}/${cursoId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ descripcion })
      });

      if (response.ok) {
        const data = await response.json();
        await cargarHistorial();
        return data;
      }
    } catch (err) {
      console.error('Error al guardar estado:', err);
      throw err;
    }
  };

  // Restaurar último estado
  const restaurarUltimoEstado = async () => {
    try {
      const response = await fetch(`${API_URL}/restaurar/${estudianteId}/${cursoId}`, {
        method: 'POST'
      });

      if (response.ok) {
        const data = await response.json();
        await cargarProgreso();
        return data;
      }
    } catch (err) {
      console.error('Error al restaurar estado:', err);
      throw err;
    }
  };

  // Restaurar estado específico
  const restaurarEstado = async (historialId: number) => {
    try {
      const response = await fetch(`${API_URL}/restaurar/${estudianteId}/${cursoId}/${historialId}`, {
        method: 'POST'
      });

      if (response.ok) {
        const data = await response.json();
        await cargarProgreso();
        return data;
      }
    } catch (err) {
      console.error('Error al restaurar estado:', err);
      throw err;
    }
  };

  // Cargar historial
  const cargarHistorial = async () => {
    try {
      const response = await fetch(`${API_URL}/historial/${estudianteId}/${cursoId}`);
      if (response.ok) {
        const data = await response.json();
        setHistorial(data);
      }
    } catch (err) {
      console.error('Error al cargar historial:', err);
    }
  };

  // Obtener tareas completadas
  const getTareasCompletadas = (): string[] => {
    if (!progreso?.notasEstudiante) return [];
    try {
      return JSON.parse(progreso.notasEstudiante);
    } catch {
      return [];
    }
  };

  useEffect(() => {
    if (estudianteId && cursoId) {
      cargarProgreso();
      cargarHistorial();
    }
  }, [estudianteId, cursoId]);

  return {
    progreso,
    historial,
    loading,
    error,
    cargarProgreso,
    actualizarProgreso,
    toggleTarea,
    guardarEstado,
    restaurarUltimoEstado,
    restaurarEstado,
    cargarHistorial,
    getTareasCompletadas
  };
};
