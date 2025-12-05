'use client'

import { useEffect, useState } from 'react';
import ProgressTracker from './progress-tracker';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Progress } from '@/components/ui/progress';
import { BookOpen, ChevronRight, TrendingUp } from 'lucide-react';
import { API_URL } from '@/lib/api';

interface MyProgressViewProps {
  estudianteId: number;
}

interface CursoConProgreso {
  id: number;
  titulo: string;
  descripcion: string;
  progreso: any;
}

export default function MyProgressView({ estudianteId }: MyProgressViewProps) {
  const [cursos, setCursos] = useState<CursoConProgreso[]>([]);
  const [cursoSeleccionado, setCursoSeleccionado] = useState<CursoConProgreso | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    cargarCursosConProgreso();
  }, [estudianteId]);

  const cargarCursosConProgreso = async () => {
    try {
      // 1. Obtener inscripciones del estudiante
      const inscripcionesRes = await fetch(`${API_URL}/inscripciones/estudiante/${estudianteId}`);
      if (!inscripcionesRes.ok) {
        setLoading(false);
        return;
      }

      const inscripciones = await inscripcionesRes.json();

      // 2. Cargar datos de cada curso con su progreso
      const cursosPromises = inscripciones.map(async (inscripcion: any) => {
        try {
          // Obtener datos del curso
          const cursoRes = await fetch(`${API_URL}/cursos/${inscripcion.cursoId}`);
          const curso = cursoRes.ok ? await cursoRes.json() : null;

          // Obtener progreso
          const progresoRes = await fetch(`${API_URL}/progreso/${estudianteId}/${inscripcion.cursoId}`);
          const progreso = progresoRes.ok ? await progresoRes.json() : null;

          if (curso) {
            return {
              id: curso.id,
              titulo: curso.titulo,
              descripcion: curso.descripcion,
              progreso
            };
          }
          return null;
        } catch (error) {
          console.error('Error al cargar curso:', error);
          return null;
        }
      });

      const cursosData = await Promise.all(cursosPromises);
      setCursos(cursosData.filter(c => c !== null) as CursoConProgreso[]);
    } catch (error) {
      console.error('Error al cargar cursos con progreso:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="p-8 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Cargando tu progreso...</p>
        </div>
      </div>
    );
  }

  // Si hay un curso seleccionado, mostrar el tracker completo
  if (cursoSeleccionado) {
    return (
      <div className="h-full overflow-auto">
        <div className="p-6 bg-white border-b sticky top-0 z-10">
          <Button
            variant="ghost"
            onClick={() => setCursoSeleccionado(null)}
            className="mb-2"
          >
            ← Volver a mis cursos
          </Button>
          <h2 className="text-2xl font-bold">{cursoSeleccionado.titulo}</h2>
        </div>
        <ProgressTracker
          estudianteId={estudianteId}
          cursoId={cursoSeleccionado.id}
          nombreCurso={cursoSeleccionado.titulo}
        />
      </div>
    );
  }

  // Vista de lista de cursos
  return (
    <div className="p-8 max-w-7xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Mi Progreso</h1>
        <p className="text-gray-600">Selecciona un curso para ver y gestionar tu progreso</p>
      </div>

      {cursos.length === 0 ? (
        <Card>
          <CardContent className="p-12 text-center">
            <BookOpen className="w-16 h-16 text-gray-300 mx-auto mb-4" />
            <h3 className="text-xl font-semibold text-gray-700 mb-2">No tienes cursos inscritos</h3>
            <p className="text-gray-500">Inscríbete a un curso para comenzar a ver tu progreso</p>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {cursos.map((curso) => {
            const porcentaje = curso.progreso?.porcentajeCompletado || 0;
            const tareasCompletadas = curso.progreso?.leccionesCompletadas || 0;
            const estadoCurso = curso.progreso?.estadoCurso || 'EN_PROGRESO';

            return (
              <Card
                key={curso.id}
                className="hover:shadow-lg transition-all cursor-pointer border-2 hover:border-blue-300"
                onClick={() => setCursoSeleccionado(curso)}
              >
                <CardHeader>
                  <div className="flex items-start justify-between mb-2">
                    <BookOpen className="w-6 h-6 text-blue-600 flex-shrink-0" />
                    {porcentaje === 100 && (
                      <span className="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full font-semibold">
                        ✓ Completado
                      </span>
                    )}
                  </div>
                  <CardTitle className="text-lg line-clamp-2">{curso.titulo}</CardTitle>
                  <CardDescription className="line-clamp-2">
                    {curso.descripcion || 'Sin descripción'}
                  </CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  {/* Barra de progreso */}
                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium text-gray-700">Progreso</span>
                      <span className="text-lg font-bold text-blue-600">{porcentaje}%</span>
                    </div>
                    <Progress value={porcentaje} className="h-3" />
                  </div>

                  {/* Stats */}
                  <div className="flex items-center justify-between text-sm">
                    <div className="flex items-center gap-2 text-gray-600">
                      <TrendingUp className="w-4 h-4" />
                      <span>{tareasCompletadas} tareas completadas</span>
                    </div>
                  </div>

                  {/* Botón de ver detalles */}
                  <Button
                    className="w-full group"
                    onClick={(e) => {
                      e.stopPropagation();
                      setCursoSeleccionado(curso);
                    }}
                  >
                    Ver Progreso
                    <ChevronRight className="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" />
                  </Button>
                </CardContent>
              </Card>
            );
          })}
        </div>
      )}
    </div>
  );
}
