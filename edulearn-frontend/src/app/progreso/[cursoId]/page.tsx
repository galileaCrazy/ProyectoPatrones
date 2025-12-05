'use client'

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import ProgressTracker from '@/components/progress/progress-tracker';
import { Button } from '@/components/ui/button';
import { ArrowLeft } from 'lucide-react';

export default function ProgresoPage() {
  const params = useParams();
  const router = useRouter();
  const cursoId = parseInt(params.cursoId as string);

  const [estudiante, setEstudiante] = useState<any>(null);
  const [nombreCurso, setNombreCurso] = useState('');
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
      cargarNombreCurso(cursoId);
    } catch (error) {
      console.error('Error al verificar usuario:', error);
      router.push('/');
    }
  }, [cursoId, router]);

  const cargarNombreCurso = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/cursos/${id}`);
      if (response.ok) {
        const curso = await response.json();
        setNombreCurso(curso.titulo || 'Curso');
      }
    } catch (error) {
      console.error('Error al cargar curso:', error);
      setNombreCurso('Mi Curso');
    } finally {
      setLoading(false);
    }
  };

  if (loading || !estudiante) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Cargando progreso...</p>
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
              onClick={() => router.back()}
            >
              <ArrowLeft className="w-5 h-5" />
            </Button>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Mi Progreso</h1>
              <p className="text-sm text-gray-500">
                Estudiante: {estudiante.nombre} {estudiante.apellidos}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Contenido */}
      <div className="max-w-7xl mx-auto">
        <ProgressTracker
          estudianteId={estudiante.id}
          cursoId={cursoId}
          nombreCurso={nombreCurso}
        />
      </div>
    </div>
  );
}
