'use client'

import { useState } from 'react';
import { CheckCircle2, Circle, Save, RotateCcw, History, ChevronDown, ChevronUp } from 'lucide-react';
import { useProgresoEstudiante } from '@/hooks/useProgresoEstudiante';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Progress } from '@/components/ui/progress';
import { Badge } from '@/components/ui/badge';
import { toast } from 'sonner';

interface Tarea {
  id: string;
  titulo: string;
  descripcion: string;
  modulo: string;
}

interface ProgressTrackerProps {
  estudianteId: number;
  cursoId: number;
  nombreCurso: string;
}

// Tareas de ejemplo - puedes cargarlas desde el backend
const TAREAS_EJEMPLO: Tarea[] = [
  { id: 'tarea-1', titulo: 'Introducción al curso', descripcion: 'Ver video de introducción', modulo: 'Módulo 1' },
  { id: 'tarea-2', titulo: 'Leer material básico', descripcion: 'Leer PDF de conceptos básicos', modulo: 'Módulo 1' },
  { id: 'tarea-3', titulo: 'Quiz Módulo 1', descripcion: 'Completar evaluación del módulo 1', modulo: 'Módulo 1' },
  { id: 'tarea-4', titulo: 'Patrones Creacionales', descripcion: 'Estudiar Factory, Singleton, Builder', modulo: 'Módulo 2' },
  { id: 'tarea-5', titulo: 'Implementar Factory Method', descripcion: 'Ejercicio práctico de Factory', modulo: 'Módulo 2' },
  { id: 'tarea-6', titulo: 'Quiz Módulo 2', descripcion: 'Completar evaluación del módulo 2', modulo: 'Módulo 2' },
  { id: 'tarea-7', titulo: 'Patrones Estructurales', descripcion: 'Estudiar Adapter, Facade, Composite', modulo: 'Módulo 3' },
  { id: 'tarea-8', titulo: 'Implementar Adapter', descripcion: 'Ejercicio práctico de Adapter', modulo: 'Módulo 3' },
  { id: 'tarea-9', titulo: 'Quiz Módulo 3', descripcion: 'Completar evaluación del módulo 3', modulo: 'Módulo 3' },
  { id: 'tarea-10', titulo: 'Patrones Comportamiento', descripcion: 'Estudiar Observer, Strategy, Memento', modulo: 'Módulo 4' },
  { id: 'tarea-11', titulo: 'Implementar Memento', descripcion: 'Ejercicio práctico de Memento', modulo: 'Módulo 4' },
  { id: 'tarea-12', titulo: 'Quiz Módulo 4', descripcion: 'Completar evaluación del módulo 4', modulo: 'Módulo 4' },
  { id: 'tarea-13', titulo: 'Proyecto integrador', descripcion: 'Diseñar sistema con patrones', modulo: 'Proyecto Final' },
  { id: 'tarea-14', titulo: 'Implementación proyecto', descripcion: 'Codificar el proyecto', modulo: 'Proyecto Final' },
  { id: 'tarea-15', titulo: 'Documentación', descripcion: 'Documentar patrones usados', modulo: 'Proyecto Final' },
  { id: 'tarea-16', titulo: 'Presentación', descripcion: 'Preparar y presentar proyecto', modulo: 'Proyecto Final' },
  { id: 'tarea-17', titulo: 'Evaluación final', descripcion: 'Examen final del curso', modulo: 'Evaluación Final' },
  { id: 'tarea-18', titulo: 'Retroalimentación', descripcion: 'Completar encuesta del curso', modulo: 'Cierre' },
  { id: 'tarea-19', titulo: 'Material adicional', descripcion: 'Revisar recursos extras', modulo: 'Extras' },
  { id: 'tarea-20', titulo: 'Certificado', descripcion: 'Descargar certificado de finalización', modulo: 'Cierre' },
];

export default function ProgressTracker({ estudianteId, cursoId, nombreCurso }: ProgressTrackerProps) {
  const {
    progreso,
    historial,
    loading,
    toggleTarea,
    guardarEstado,
    restaurarUltimoEstado,
    restaurarEstado,
    getTareasCompletadas
  } = useProgresoEstudiante(estudianteId, cursoId);

  const [mostrarHistorial, setMostrarHistorial] = useState(false);
  const [guardando, setGuardando] = useState(false);

  const tareasCompletadas = getTareasCompletadas();

  console.log('🎯 ProgressTracker - Estado actual:', {
    estudianteId,
    cursoId,
    progreso,
    tareasCompletadas
  });

  const handleToggleTarea = async (tareaId: string) => {
    const estaCompletada = tareasCompletadas.includes(tareaId);
    console.log('🖱️ Click en tarea:', { tareaId, estaCompletada, tareasCompletadas });

    try {
      await toggleTarea(tareaId, !estaCompletada);
      toast.success(estaCompletada ? 'Tarea desmarcada' : 'Tarea completada ✓');
      console.log('✅ Tarea actualizada correctamente');
    } catch (error) {
      console.error('❌ Error en handleToggleTarea:', error);
      toast.error('Error al actualizar la tarea');
    }
  };

  const handleGuardarEstado = async () => {
    setGuardando(true);
    try {
      await guardarEstado(`Checkpoint: ${tareasCompletadas.length} tareas completadas`);
      toast.success('Progreso guardado exitosamente');
    } catch (error) {
      toast.error('Error al guardar el progreso');
    } finally {
      setGuardando(false);
    }
  };

  const handleRestaurarUltimo = async () => {
    try {
      await restaurarUltimoEstado();
      toast.success('Progreso restaurado exitosamente');
    } catch (error) {
      toast.error('Error al restaurar el progreso');
    }
  };

  const handleRestaurarEspecifico = async (historialId: number) => {
    try {
      await restaurarEstado(historialId);
      toast.success('Progreso restaurado exitosamente');
    } catch (error) {
      toast.error('Error al restaurar el progreso');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center p-8">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto mb-2"></div>
          <p className="text-sm text-gray-500">Cargando progreso...</p>
        </div>
      </div>
    );
  }

  if (!progreso) {
    return (
      <div className="flex items-center justify-center p-8">
        <div className="text-center">
          <p className="text-red-600 font-semibold mb-2">⚠️ No se pudo cargar el progreso</p>
          <p className="text-sm text-gray-500 mb-4">
            Estudiante ID: {estudianteId}, Curso ID: {cursoId}
          </p>
          <Button onClick={() => window.location.reload()}>
            Recargar página
          </Button>
        </div>
      </div>
    );
  }

  const porcentaje = progreso?.porcentajeCompletado || 0;

  return (
    <div className="space-y-6 p-6">
      {/* Header con progreso */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle className="text-2xl">{nombreCurso}</CardTitle>
              <CardDescription>Progreso del curso</CardDescription>
            </div>
            <Badge variant={porcentaje === 100 ? 'default' : 'secondary'} className="text-lg px-4 py-2">
              {porcentaje}%
            </Badge>
          </div>
        </CardHeader>
        <CardContent>
          <Progress value={porcentaje} className="h-3 mb-4" />
          <div className="flex gap-2">
            <Button
              onClick={handleGuardarEstado}
              disabled={guardando}
              size="sm"
            >
              <Save className="w-4 h-4 mr-2" />
              Guardar Progreso
            </Button>
            <Button
              onClick={handleRestaurarUltimo}
              variant="outline"
              size="sm"
              disabled={historial.length === 0}
            >
              <RotateCcw className="w-4 h-4 mr-2" />
              Restaurar
            </Button>
            <Button
              onClick={() => setMostrarHistorial(!mostrarHistorial)}
              variant="outline"
              size="sm"
            >
              <History className="w-4 h-4 mr-2" />
              Historial ({historial.length})
              {mostrarHistorial ? <ChevronUp className="w-4 h-4 ml-2" /> : <ChevronDown className="w-4 h-4 ml-2" />}
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Historial de checkpoints */}
      {mostrarHistorial && historial.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Puntos de Restauración</CardTitle>
            <CardDescription>Puedes volver a cualquier punto anterior</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-2">
              {historial.map((item) => (
                <div key={item.id} className="flex items-center justify-between p-3 border rounded-lg hover:bg-gray-50">
                  <div>
                    <p className="font-medium">{item.descripcion}</p>
                    <p className="text-sm text-gray-500">
                      {new Date(item.fechaGuardado).toLocaleString('es-MX')} - {item.porcentajeCompletado}%
                    </p>
                  </div>
                  <Button
                    onClick={() => handleRestaurarEspecifico(item.id)}
                    variant="ghost"
                    size="sm"
                  >
                    <RotateCcw className="w-4 h-4 mr-1" />
                    Restaurar
                  </Button>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {/* Lista de tareas */}
      <Card>
        <CardHeader>
          <CardTitle>Tareas del Curso</CardTitle>
          <CardDescription>
            {tareasCompletadas.length} de {TAREAS_EJEMPLO.length} tareas completadas
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-1">
            {TAREAS_EJEMPLO.map((tarea) => {
              const completada = tareasCompletadas.includes(tarea.id);

              return (
                <div
                  key={tarea.id}
                  className={`flex items-start gap-3 p-3 rounded-lg border cursor-pointer transition-all ${
                    completada
                      ? 'bg-green-50 border-green-200 hover:bg-green-100'
                      : 'hover:bg-gray-50'
                  }`}
                  onClick={() => handleToggleTarea(tarea.id)}
                >
                  {completada ? (
                    <CheckCircle2 className="w-6 h-6 text-green-600 flex-shrink-0 mt-0.5" />
                  ) : (
                    <Circle className="w-6 h-6 text-gray-400 flex-shrink-0 mt-0.5" />
                  )}

                  <div className="flex-1">
                    <div className="flex items-center justify-between">
                      <h4 className={`font-medium ${completada ? 'line-through text-gray-500' : ''}`}>
                        {tarea.titulo}
                      </h4>
                      <Badge variant="outline" className="text-xs">
                        {tarea.modulo}
                      </Badge>
                    </div>
                    <p className={`text-sm ${completada ? 'text-gray-400' : 'text-gray-600'}`}>
                      {tarea.descripcion}
                    </p>
                  </div>
                </div>
              );
            })}
          </div>
        </CardContent>
      </Card>

      {/* Estadísticas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium text-gray-600">Tareas Completadas</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold">{tareasCompletadas.length}</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium text-gray-600">Progreso Total</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold">{porcentaje}%</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium text-gray-600">Checkpoints Guardados</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold">{historial.length}</p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
