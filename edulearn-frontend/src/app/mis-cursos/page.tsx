'use client'

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import ProgressCardMini from '@/components/progress/progress-card-mini';
import { Button } from '@/components/ui/button';
import { ArrowLeft } from 'lucide-react';

export default function MisCursosPage() {
  const router = useRouter();
  const [estudiante, setEstudiante] = useState<any>(null);
  const [inscripciones, setInscripciones] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const usuarioStr = localStorage.getItem('usuario');

    if (!usuarioStr) {
      router.push('/');
      return;
    }

    try {
      const usuario = JSON.parse(usuarioStr);

      if (usuario.tipoUsuario !== 'estudiante') {
        router.push('/dashboard');
        return;
      }

      setEstudiante(usuario);
      cargarInscripciones(usuario.id);
    } catch (error) {
      console.error('Error al verificar usuario:', error);
      router.push('/');
    }
  }, [router]);

  const cargarInscripciones = async (estudianteId: number) => {
    try {
      // Cargar inscripciones del estudiante
      const response = await fetch(`http://localhost:8080/api/inscripciones/estudiante/${estudianteId}`);
      if (response.ok) {
        const data = await response.json();

        // Cargar información de cada curso
        const inscripcionesConCursos = await Promise.all(
          data.map(async (inscripcion: any) => {
            try {
              const cursoResponse = await fetch(`http://localhost:8080/api/cursos/${inscripcion.cursoId}`);
              if (cursoResponse.ok) {
                const curso = await cursoResponse.json();
                return { ...inscripcion, curso };
              }
            } catch (error) {
              console.error('Error al cargar curso:', error);
            }
            return inscripcion;
          })
        );

        setInscripciones(inscripcionesConCursos.filter(i => i.curso));
      }
    } catch (error) {
      console.error('Error al cargar inscripciones:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading || !estudiante) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Cargando cursos...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              size="icon"
              onClick={() => router.push('/dashboard/estudiante')}
            >
              <ArrowLeft className="w-5 h-5" />
            </Button>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Mis Cursos</h1>
              <p className="text-sm text-gray-500">
                {estudiante.nombre} {estudiante.apellidos}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Contenido */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {inscripciones.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-gray-500 mb-4">No estás inscrito en ningún curso</p>
            <Button onClick={() => router.push('/dashboard/estudiante')}>
              Explorar Cursos
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {inscripciones.map((inscripcion) => (
              <ProgressCardMini
                key={inscripcion.id}
                estudianteId={estudiante.id}
                cursoId={inscripcion.cursoId}
                nombreCurso={inscripcion.curso.titulo}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
