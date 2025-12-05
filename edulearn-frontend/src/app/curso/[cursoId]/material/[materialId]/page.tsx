'use client'

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { useMaterialNavegacion } from '@/hooks/useMaterialNavegacion';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Progress } from '@/components/ui/progress';
import { Badge } from '@/components/ui/badge';
import { ArrowLeft, ArrowRight, CheckCircle2, FileText, Video, FileImage, Link as LinkIcon } from 'lucide-react';
import { toast } from 'sonner';

export default function MaterialPage() {
  const params = useParams();
  const router = useRouter();
  const cursoId = parseInt(params.cursoId as string);
  const materialId = parseInt(params.materialId as string);

  const [estudiante, setEstudiante] = useState<any>(null);
  const [progreso, setProgreso] = useState<any>(null);
  const [avanzando, setAvanzando] = useState(false);

  const { navegacion, loading, completarYAvanzar, obtenerEstadisticas } = useMaterialNavegacion(
    materialId,
    estudiante?.id,
    cursoId
  );

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
      cargarProgreso();
    } catch (error) {
      console.error('Error al verificar usuario:', error);
      router.push('/');
    }
  }, [router]);

  const cargarProgreso = async () => {
    if (!estudiante) return;

    const stats = await obtenerEstadisticas();
    if (stats) {
      setProgreso(stats);
    }
  };

  const handleCompletarYAvanzar = async () => {
    setAvanzando(true);
    try {
      await completarYAvanzar((siguienteId) => {
        toast.success('¡Material completado! Avanzando al siguiente...');
        router.push(`/curso/${cursoId}/material/${siguienteId}`);
      });

      // Actualizar progreso
      await cargarProgreso();
    } catch (error) {
      toast.error('Error al completar el material');
    } finally {
      setAvanzando(false);
    }
  };

  const handleAnterior = () => {
    if (navegacion?.anterior) {
      router.push(`/curso/${cursoId}/material/${navegacion.anterior.id}`);
    }
  };

  const renderContenido = () => {
    if (!navegacion) return null;

    const { material } = navegacion;
    const tipo = material.tipoMaterial?.toLowerCase();

    // Video
    if (tipo === 'video') {
      return (
        <div className="aspect-video bg-black rounded-lg overflow-hidden">
          {material.urlRecurso ? (
            <iframe
              src={material.urlRecurso}
              className="w-full h-full"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          ) : (
            <div className="flex items-center justify-center h-full text-white">
              <Video className="w-12 h-12 mb-2" />
              <p>Video no disponible</p>
            </div>
          )}
        </div>
      );
    }

    // PDF
    if (tipo === 'pdf') {
      const pdfUrl = material.urlRecurso ||
        (material.archivoPath ? `http://localhost:8080/uploads/${material.archivoPath}` : null);

      return (
        <div className="bg-gray-50 rounded-lg p-4">
          {pdfUrl ? (
            <>
              <iframe
                src={`${pdfUrl}#toolbar=0`}
                className="w-full h-[600px] rounded border"
              />
              <div className="mt-4 text-center">
                <a
                  href={pdfUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-blue-600 hover:underline inline-flex items-center gap-2"
                >
                  <FileText className="w-4 h-4" />
                  Abrir PDF en nueva ventana
                </a>
              </div>
            </>
          ) : (
            <div className="text-center py-12">
              <FileText className="w-12 h-12 mx-auto mb-2 text-gray-400" />
              <p className="text-gray-500">PDF no disponible</p>
            </div>
          )}
        </div>
      );
    }

    // Documento de texto
    if (tipo === 'documento' || tipo === 'text') {
      return (
        <Card>
          <CardContent className="pt-6">
            <div className="prose max-w-none">
              <p className="whitespace-pre-wrap">{material.descripcion}</p>
            </div>
          </CardContent>
        </Card>
      );
    }

    // Enlace
    if (tipo === 'enlace' || tipo === 'link') {
      return (
        <Card>
          <CardContent className="pt-6">
            <div className="text-center">
              <LinkIcon className="w-12 h-12 mx-auto mb-4 text-blue-600" />
              <p className="mb-4">{material.descripcion}</p>
              <a
                href={material.urlRecurso}
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
              >
                Abrir enlace
                <ArrowRight className="w-4 h-4" />
              </a>
            </div>
          </CardContent>
        </Card>
      );
    }

    // Imagen
    if (tipo === 'imagen' || tipo === 'image') {
      const imgUrl = material.urlRecurso ||
        (material.archivoPath ? `http://localhost:8080/uploads/${material.archivoPath}` : null);

      return (
        <div className="text-center">
          {imgUrl ? (
            <img
              src={imgUrl}
              alt={material.titulo}
              className="max-w-full rounded-lg shadow-lg mx-auto"
            />
          ) : (
            <div className="bg-gray-50 rounded-lg p-12">
              <FileImage className="w-12 h-12 mx-auto mb-2 text-gray-400" />
              <p className="text-gray-500">Imagen no disponible</p>
            </div>
          )}
        </div>
      );
    }

    // Tipo no reconocido
    return (
      <Card>
        <CardContent className="pt-6">
          <p>{material.descripcion}</p>
        </CardContent>
      </Card>
    );
  };

  if (loading || !estudiante || !navegacion) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Cargando material...</p>
        </div>
      </div>
    );
  }

  const porcentajeProgreso = progreso?.porcentajeCompletado || 0;

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header con progreso */}
      <div className="bg-white shadow">
        <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex items-center justify-between mb-3">
            <div className="flex items-center gap-3">
              <Button
                variant="ghost"
                size="icon"
                onClick={() => router.push(`/mis-cursos`)}
              >
                <ArrowLeft className="w-5 h-5" />
              </Button>
              <div>
                <h1 className="text-lg font-bold text-gray-900">
                  {navegacion.material.titulo}
                </h1>
                <p className="text-sm text-gray-500">
                  Material {navegacion.indice} de {navegacion.total}
                </p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              {navegacion.completado && (
                <Badge variant="default" className="bg-green-600">
                  <CheckCircle2 className="w-3 h-3 mr-1" />
                  Completado
                </Badge>
              )}
              <Badge variant="outline">
                {porcentajeProgreso}% del curso
              </Badge>
            </div>
          </div>

          {/* Barra de progreso del curso */}
          <div>
            <Progress value={porcentajeProgreso} className="h-2" />
            <p className="text-xs text-gray-500 mt-1">
              {progreso?.materialesCompletados || 0} de {progreso?.totalMateriales || 0} materiales completados
            </p>
          </div>
        </div>
      </div>

      {/* Contenido del material */}
      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Información del material */}
        {navegacion.material.descripcion && navegacion.material.tipoMaterial !== 'documento' && (
          <Card className="mb-6">
            <CardHeader>
              <CardTitle className="text-lg">Descripción</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-gray-700">{navegacion.material.descripcion}</p>
            </CardContent>
          </Card>
        )}

        {/* Renderizar contenido según tipo */}
        <div className="mb-8">
          {renderContenido()}
        </div>

        {/* Botones de navegación */}
        <div className="flex items-center justify-between pt-6 border-t">
          <Button
            variant="outline"
            onClick={handleAnterior}
            disabled={!navegacion.anterior}
          >
            <ArrowLeft className="w-4 h-4 mr-2" />
            Anterior
          </Button>

          {navegacion.siguiente ? (
            <Button
              onClick={handleCompletarYAvanzar}
              disabled={avanzando}
              size="lg"
              className="bg-blue-600 hover:bg-blue-700"
            >
              {avanzando ? 'Avanzando...' : 'Completar y Continuar'}
              <ArrowRight className="w-4 h-4 ml-2" />
            </Button>
          ) : (
            <Button
              onClick={() => {
                toast.success('¡Felicidades! Has completado todos los materiales');
                router.push(`/progreso/${cursoId}`);
              }}
              size="lg"
              className="bg-green-600 hover:bg-green-700"
            >
              <CheckCircle2 className="w-4 h-4 mr-2" />
              ¡Curso Completado!
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
